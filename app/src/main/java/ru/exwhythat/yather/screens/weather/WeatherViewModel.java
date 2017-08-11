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

    protected MutableLiveData<List<CityWithWeather>> citiesWithWeather;
    protected MutableLiveData<CurrentWeather> weather;
    protected MutableLiveData<List<ForecastWeather>> forecast;
    protected MutableLiveData<String> lastUpdateTime;

    @Inject
    public WeatherViewModel(Application application, WeatherRepo weatherRepo) {
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

    public LiveData<String> getLastUpdate() {
        if (lastUpdateTime == null) {
            lastUpdateTime = new MutableLiveData<>();
            lastUpdateTime.setValue(Utils.lastUpdateString(context));
        }
        return lastUpdateTime;
    }

    public LiveData<List<CityWithWeather>> getCitiesWithWeather() {
        if (citiesWithWeather == null) {
            citiesWithWeather = new MutableLiveData<>();
            getCitiesWithWeatherFromDb();
        }
        return citiesWithWeather;
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
        weatherSubscription.dispose();
        weatherSubscription = weatherRepo.getWeatherForSelectedCityFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextWeather, this::onError);
    }

    public void getForecastFromDb() {
        weatherSubscription.dispose();
        weatherSubscription = weatherRepo.getForecastForSelectedCityFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextForecast, this::onError);
    }

    private void getCitiesWithWeatherFromDb() {
        weatherRepo.getCitiesWithWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(citiesWeather -> citiesWithWeather.setValue(citiesWeather));
    }

    public void forceUpdateWeatherAndForecast() {
        forceUpdateWeather();
        forceUpdateForecast();
    }

    private void forceUpdateWeather() {
        weatherSubscription.dispose();
        weatherSubscription = weatherRepo.getFreshWeatherForSelectedCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextWeather, this::onError);
    }

    private void forceUpdateForecast() {
        forecastSubscription.dispose();
        forecastSubscription = weatherRepo.getFreshForecastForSelectedCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextForecast, this::onError);
    }

    private void onNextWeather(CurrentWeather weather) {
        updateWeather(weather);
        updateInterval();
    }

    private void onNextForecast(List<ForecastWeather> forecast) {
        updateForecast(forecast);
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
        forecastSubscription.dispose();
    }
}
