package ru.exwhythat.yather.data.repository;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.exwhythat.yather.data.local.entities.City;
import ru.exwhythat.yather.data.local.entities.CityWithWeather;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.utils.Prefs;

/**
 * Created by exwhythat on 8/6/17.
 */

public class WeatherCachingRepository {

    private Context appContext;
    private LocalWeatherRepository localRepo;
    private RemoteWeatherRepository remoteRepo;

    @Inject
    public WeatherCachingRepository(Context appContext, LocalWeatherRepository localRepo, RemoteWeatherRepository remoteRepo) {
        this.appContext = appContext;
        this.localRepo = localRepo;
        this.remoteRepo = remoteRepo;
    }

    /**
     * 1) Try to get weather from database
     * 2) If there is no weather for out city in DB, fetch it from network
     * 3) Merge Single with endless Flowable to watch for database changes
     */
    public Flowable<CurrentWeather> getWeather() {
        int cityId = getSelectedCityIdFromPrefs();
        return localRepo.getSingleWeatherForCity(cityId)
                .onErrorResumeNext(getFreshWeather())
                .toFlowable()
                .mergeWith(localRepo.getFlowingWeatherForCity(cityId));
    }

    public Single<CurrentWeather> getFreshWeather() {
        return remoteRepo.getCurrentWeatherForCity(getSelectedCityIdFromPrefs())
                .doOnSuccess(currentWeather -> localRepo.updateCurrentWeather(currentWeather));
    }

    public Flowable<List<ForecastWeather>> getForecast() {
        int cityId = getSelectedCityIdFromPrefs();
        return localRepo.getSingleForecastForCity(cityId)
                .onErrorResumeNext(getFreshForecast())
                .toFlowable()
                .mergeWith(localRepo.getFlowingForecastForCity(cityId));
    }

    public Single<List<ForecastWeather>> getFreshForecast() {
        return remoteRepo.getForecastForCity(getSelectedCityIdFromPrefs())
                .doOnSuccess(forecast -> localRepo.updateForecast(forecast));
    }

    private int getSelectedCityIdFromPrefs() {
        return Prefs.getSelectedCity(appContext).getCityId();
    }

    public City getCityById(int cityId) {
        return localRepo.getCityById(cityId);
    }

    public Flowable<List<CityWithWeather>> getCitiesWithWeather() {
        return localRepo.getFlowingCitiesWithWeather();
    }
}
