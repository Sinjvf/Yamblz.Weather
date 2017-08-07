package ru.exwhythat.yather.data.repository;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
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

        Flowable<CurrentWeather> concattedWeather = Flowable.merge(loadFromDb, fetchFromNetwork)
                .filter(weather -> weather != null);
        return concattedWeather;
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

    public Flowable<List<ForecastWeather>> getForecastForCity(int cityId) {

        Flowable<List<ForecastWeather>> loadFromDb = localRepo.getForecastForCity(cityId);
        Flowable<List<ForecastWeather>> fetchFromNetwork = remoteRepo.getForecastForCity(cityId);

        Flowable<List<ForecastWeather>> concattedWeather = Flowable.concat(loadFromDb, fetchFromNetwork);

        return concattedWeather;
    }

    public Flowable<List<ForecastWeather>> getFreshForecastForCity(int cityId) {
        return remoteRepo.getForecastForCity(cityId)
                .doOnNext(forecast -> localRepo.updateForecast(forecast));
    }
}
