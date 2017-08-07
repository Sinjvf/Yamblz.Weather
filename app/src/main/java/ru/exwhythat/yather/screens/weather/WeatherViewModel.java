package ru.exwhythat.yather.screens.weather;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.base_util.BaseFragmentViewModel;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.repository.WeatherRepo;
import ru.exwhythat.yather.utils.Prefs;
import ru.exwhythat.yather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * ViewModel for main fragment
 */
@Keep
public class WeatherViewModel extends BaseFragmentViewModel {

    private WeatherRepo weatherRepo;

    private Disposable weatherSubscription = Disposables.disposed();

    protected MutableLiveData<CurrentWeather> weather;
    protected MutableLiveData<String> lastUpdateTime;

    @Inject
    public WeatherViewModel(Application application, WeatherRepo weatherRepo) {
        super(application);
        this.weatherRepo = weatherRepo;
    }

    public LiveData<CurrentWeather> getWeatherForSelectedCity() {
        if (weather == null) {
            weather = new MutableLiveData<>();
            getWeatherFromDbOrNetwork();
        }
        return weather;
    }

    public LiveData<String> getLastUpdate() {
        if (lastUpdateTime == null) {
            lastUpdateTime = new MutableLiveData<>();
            lastUpdateTime.setValue(Utils.lastUpdateString(context));
        }
        return lastUpdateTime;
    }

    @Override
    protected int getTitleStringId() {
        return R.string.menu_weather;
    }

    public void getWeatherFromDbOrNetwork() {
        weatherSubscription.dispose();
        weatherSubscription = weatherRepo.getWeatherForSelectedCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNext, this::onError);
    }

    public void forceUpdateCurrentWeather() {
        weatherSubscription.dispose();
        weatherSubscription = weatherRepo.getFreshWeatherForSelectedCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNext, this::onError);
    }

    private void onNext(CurrentWeather weather) {
        updateWeather(weather);
        updateInterval();
    }

    private void onError(Throwable throwable) {
        // TODO: how to show an error?
        Timber.e(throwable);
    }

    private void updateWeather(CurrentWeather newWeather) {
        if (weather == null) weather = new MutableLiveData<>();
        weather.setValue(newWeather);
    }

    private void updateInterval() {
        Prefs.setPrefLastTimeUpdate(context);
        if (lastUpdateTime == null) lastUpdateTime = new MutableLiveData<>();
        lastUpdateTime.setValue(Utils.lastUpdateString(context));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        weatherSubscription.dispose();
    }
}
