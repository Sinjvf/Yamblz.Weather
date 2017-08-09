package ru.exwhythat.yather.data.repository;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import ru.exwhythat.yather.data.local.entities.City;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.utils.Prefs;

/**
 * Created by exwhythat on 8/6/17.
 */

public class WeatherRepo {

    private Context appContext;
    private LocalWeatherRepository localRepo;
    private RemoteWeatherRepository remoteRepo;

    @Inject
    public WeatherRepo (Context appContext, LocalWeatherRepository localRepo, RemoteWeatherRepository remoteRepo) {
        this.appContext = appContext;
        this.localRepo = localRepo;
        this.remoteRepo = remoteRepo;
    }

    private Flowable<CurrentWeather> getWeatherForCity(int cityId) {
        Flowable<CurrentWeather> loadFromDb = localRepo.getCurrentWeatherForCity(cityId);
        Flowable<CurrentWeather> fetchFromNetwork = remoteRepo.getCurrentWeatherForCity(cityId);

        return Flowable.merge(loadFromDb, fetchFromNetwork)
                .filter(weather -> weather != null);
    }

    public Flowable<CurrentWeather> getWeatherForSelectedCity() {
        int selectedCityId = Prefs.getSelectedCity(appContext).getCityId();
        return getWeatherForCity(selectedCityId);
    }

    public Flowable<CurrentWeather> getFreshWeatherForSelectedCity() {
        int selectedCityId = Prefs.getSelectedCity(appContext).getCityId();
        return remoteRepo.getCurrentWeatherForCity(selectedCityId)
                .doOnNext(currentWeather -> localRepo.updateCurrentWeather(currentWeather));
    }
    public Flowable<CurrentWeather> getFreshWeatherForCity(int cityId) {
        return remoteRepo.getCurrentWeatherForCity(cityId)
                .doOnNext(currentWeather -> localRepo.updateCurrentWeather(currentWeather));
    }

    public Flowable<List<ForecastWeather>> getFreshForecastForCity(int cityId) {
        return remoteRepo.getForecastForCity(cityId)
                .doOnNext(forecast -> localRepo.updateForecast(forecast));
    }

    public Flowable<List<ForecastWeather>> getFreshForecastForSelectedCity() {
        int selectedCityId = Prefs.getSelectedCity(appContext).getCityId();
        return remoteRepo.getForecastForCity(selectedCityId)
                .doOnNext(forecast -> localRepo.updateForecast(forecast));
    }

    public Flowable<List<City>> getCities() {
        return localRepo.getCities();
    }

    public City getCityById(int cityId) {
        return localRepo.getCityById(cityId);
    }
}
