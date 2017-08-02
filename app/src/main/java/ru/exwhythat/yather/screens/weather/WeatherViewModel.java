package ru.exwhythat.yather.screens.weather;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.YatherApp;
import ru.exwhythat.yather.base_util.BaseFragmentViewModel;
import ru.exwhythat.yather.network.weather.WeatherItem;
import ru.exwhythat.yather.repository.RemoteWeatherRepository;
import ru.exwhythat.yather.utils.Preferenses;
import ru.exwhythat.yather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * ViewModel for main fragment
 */
@Keep
public class WeatherViewModel extends BaseFragmentViewModel {

    @Inject
    RemoteWeatherRepository remoteWeatherRepository;

    private Disposable weatherSubscription = Disposables.disposed();

    public WeatherViewModel(Application application) {
        super(application);
        YatherApp.get(application).plusDataComponent().inject(this);
        Timber.tag("wvmDebug").d("Data component injected!");
    }

    protected MutableLiveData<WeatherItem> weather;
    protected MutableLiveData<String> lastUpdateTime;

    public LiveData<WeatherItem> getWeatherDataByCityCoords(LatLng cityCoords) {
        if (weather == null) {
            weather = new MutableLiveData<>();
            sendWeatherRequestByCityCoords(cityCoords);
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

    public void sendWeatherRequestByCityCoords(LatLng cityCoords) {
        weatherSubscription.dispose();
        weatherSubscription = remoteWeatherRepository.getCurrentWeatherByLocation(cityCoords)
                .map(WeatherItem::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(WeatherItem weather) {
        updateWeather(weather);
        updateInterval();
    }

    private void onError(Throwable throwable) {
        // TODO: how to show an error?
    }

    private void updateWeather(WeatherItem newWeather) {
        if (weather == null) weather = new MutableLiveData<>();
        weather.setValue(newWeather);
    }

    private void updateInterval() {
        Preferenses.setPrefLastTimeUpdate(context);
        if (lastUpdateTime == null) lastUpdateTime = new MutableLiveData<>();
        lastUpdateTime.setValue(Utils.lastUpdateString(context));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        weatherSubscription.dispose();
        YatherApp.get(getApplication()).clearDataComponent();
        Timber.tag("wvmDebug").d("Data component cleared!");
    }
}
