package ru.exwhythat.yather.screens.weather;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.base_util.BaseFragmentViewModel;
import ru.exwhythat.yather.data.local.entities.City;
import ru.exwhythat.yather.data.local.entities.CityWithWeather;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.data.repository.WeatherCachingRepository;
import ru.exwhythat.yather.screens.settings.CityInfo;
import ru.exwhythat.yather.utils.Prefs;
import ru.exwhythat.yather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * ViewModel for main fragment
 */
@Keep
public class WeatherViewModel extends BaseFragmentViewModel {

    private WeatherCachingRepository weatherRepo;

    private Disposable weatherSubscription = Disposables.disposed();
    private Disposable forecastSubscription = Disposables.disposed();
    private Disposable citiesSubscription = Disposables.disposed();

    protected MutableLiveData<List<CityWithWeather>> citiesWithWeather;
    protected MutableLiveData<CurrentWeather> weather;
    protected MutableLiveData<List<ForecastWeather>> forecast;
    protected MutableLiveData<String> lastUpdateTime;

    @Inject
    public WeatherViewModel(Application application, WeatherCachingRepository weatherRepo) {
        super(application);
        this.weatherRepo = weatherRepo;
    }

    public LiveData<CurrentWeather> getWeather() {
        if (weather == null) {
            weather = new MutableLiveData<>();
            getWeatherFromDb();
        }
        return weather;
    }

    public LiveData<List<ForecastWeather>> getForecast() {
        if (forecast == null) {
            forecast = new MutableLiveData<>();
            getForecastFromDb();
        }
        return forecast;
    }

    public LiveData<List<CityWithWeather>> getCitiesWithWeather() {
        if (citiesWithWeather == null) {
            citiesWithWeather = new MutableLiveData<>();
            getCitiesWithWeatherFromDb();
        }
        return citiesWithWeather;
    }

    public LiveData<String> getLastUpdate() {
        if (lastUpdateTime == null) {
            lastUpdateTime = new MutableLiveData<>();
            lastUpdateTime.setValue(Utils.lastUpdateString(context));
        }
        return lastUpdateTime;
    }

    public void onCitySelected(int cityId) {
        Single.just(cityId)
                .map(id -> weatherRepo.getCityById(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateCity, this::onError);
    }

    private void updateCity(City city) {
        Prefs.setSelectedCity(context, new CityInfo(city.getName(), city.getCityId()));
        getWeatherFromDb();
        getForecastFromDb();
    }

    @Override
    protected int getTitleStringId() {
        return R.string.menu_weather;
    }

    public void getWeatherFromDb() {
        weatherSubscription = weatherRepo.getWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newWeather -> safeUpdateLiveData(weather, newWeather), this::onError);
    }

    public void getForecastFromDb() {
        forecastSubscription = weatherRepo.getForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newForecast -> safeUpdateLiveData(forecast, newForecast), this::onError);
    }

    private void getCitiesWithWeatherFromDb() {
        citiesSubscription = weatherRepo.getCitiesWithWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cities -> safeUpdateLiveData(citiesWithWeather, cities), this::onError);
    }

    public void forceUpdateWeatherAndForecast() {
        forceUpdateWeather();
        forceUpdateForecast();
    }

    private void forceUpdateWeather() {
        weatherSubscription.dispose();
        weatherSubscription = weatherRepo.getFreshWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateWeatherAndLastUpdateTime, this::onError);
    }

    private void forceUpdateForecast() {
        forecastSubscription.dispose();
        forecastSubscription = weatherRepo.getFreshForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newForecast -> safeUpdateLiveData(forecast, newForecast), this::onError);
    }

    private void updateWeatherAndLastUpdateTime(CurrentWeather newWeather) {
        safeUpdateLiveData(weather, newWeather);
        updateLastUpdateTime();
    }

    private void updateLastUpdateTime() {
        Prefs.setPrefLastTimeUpdate(context);
        safeUpdateLiveData(lastUpdateTime, Utils.lastUpdateString(context));
    }

    private void onError(Throwable throwable) {
        // TODO: how to show an error?
        Timber.e(throwable);
    }

    private <T> void safeUpdateLiveData(MutableLiveData<T> mutableLiveData, T value) {
        if (mutableLiveData == null) mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(value);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        weatherSubscription.dispose();
        forecastSubscription.dispose();
        citiesSubscription.dispose();
    }
}
