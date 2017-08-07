package ru.exwhythat.yather.data.repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.data.remote.model.WeatherResponse;

/**
 * Created by exwhythat on 8/6/17.
 */

public class WeatherRepo {

    private LocalWeatherRepository localRepo;
    private RemoteWeatherRepository remoteRepo;

    @Inject
    public WeatherRepo (LocalWeatherRepository localRepo, RemoteWeatherRepository remoteRepo) {
        this.localRepo = localRepo;
        this.remoteRepo = remoteRepo;
    }

    public Flowable<CurrentWeather> getCurrentWeatherForCity(int cityId) {

        Flowable<CurrentWeather> loadFromDb = localRepo.getCurrentWeatherForCity(cityId);
        Flowable<CurrentWeather> fetchFromNetwork = remoteRepo.getCurrentWeatherForCity(cityId);

        Flowable<CurrentWeather> concattedWeather = Flowable.merge(loadFromDb, fetchFromNetwork)
                .filter(weather -> weather != null);
        return concattedWeather;
    }

    public Flowable<CurrentWeather> getFreshCurrentWeatherForCity(int cityId) {
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
