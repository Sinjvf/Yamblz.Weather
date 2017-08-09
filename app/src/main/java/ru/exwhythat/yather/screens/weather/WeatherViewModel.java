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
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.data.repository.WeatherRepo;
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

    private WeatherRepo weatherRepo;

    private Disposable weatherSubscription = Disposables.disposed();
    private Disposable forecastSubscription = Disposables.disposed();

    protected MutableLiveData<List<City>> cities;
    protected MutableLiveData<CurrentWeather> weather;
    protected MutableLiveData<List<ForecastWeather>> forecast;
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

    public LiveData<List<City>> getCities() {
        if (cities == null) {
            cities = new MutableLiveData<>();
            getAllCities();
        }
        return cities;
    }

    public LiveData<List<ForecastWeather>> getForecast() {
        if (forecast == null) {
            forecast = new MutableLiveData<>();
            forceUpdateForecast();
        }
        return forecast;
    }

    public void onCitySelected(int cityId) {
        Single.just(cityId)
                .map(id -> weatherRepo.getCityById(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateCity, this::onError);
    }

    private void updateCity(City city) {
        Prefs.setSelectedCity(context, new CityInfo(city.getName(), city.getApiCityId()));
        forceUpdateCurrentWeather();
        forceUpdateForecast();
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
                .subscribe(this::onNextWeather, this::onError);
    }

    private void getAllCities() {
        weatherRepo.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allCities -> cities.setValue(allCities));
    }

    public void forceUpdateCurrentWeather() {
        weatherSubscription.dispose();
        weatherSubscription = weatherRepo.getFreshWeatherForSelectedCity()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextWeather, this::onError);
    }

    public void forceUpdateForecast() {
        forecastSubscription.dispose();
        forecastSubscription = weatherRepo.getFreshForecastForSelectedCity()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextForecast, this::onError);
    }

    private void onNextForecast(List<ForecastWeather> forecast) {
        updateForecast(forecast);
    }

    private void onNextWeather(CurrentWeather weather) {
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

    private void updateForecast(List<ForecastWeather> newForecast) {
        if (forecast == null) forecast = new MutableLiveData<>();
        forecast.setValue(newForecast);
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
