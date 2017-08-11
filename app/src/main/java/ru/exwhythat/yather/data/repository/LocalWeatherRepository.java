package ru.exwhythat.yather.data.repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.exwhythat.yather.data.local.CityDao;
import ru.exwhythat.yather.data.local.CurrentWeatherDao;
import ru.exwhythat.yather.data.local.ForecastWeatherDao;
import ru.exwhythat.yather.data.local.entities.City;
import ru.exwhythat.yather.data.local.entities.CityWithWeather;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;

/**
 * Created by exwhythat on 8/6/17.
 */

public class LocalWeatherRepository {

    private final CityDao cityDao;
    private final CurrentWeatherDao currentWeatherDao;
    private final ForecastWeatherDao forecastWeatherDao;

    @Inject
    public LocalWeatherRepository(CityDao cityDao, CurrentWeatherDao currentWeatherDao,
                                  ForecastWeatherDao forecastWeatherDao) {
        this.cityDao = cityDao;
        this.currentWeatherDao = currentWeatherDao;
        this.forecastWeatherDao = forecastWeatherDao;
    }

    public Flowable<CurrentWeather> getFlowingWeatherForCity(int cityId) {
        // For some reason Room triggers updates for every city in database, not the only one which I'm asked for.
        // So I have to filter it manually
        return currentWeatherDao.getFlowForCity(cityId)
                .filter(weather -> weather.getApiCityId() == cityId);
    }

    public Single<CurrentWeather> getSingleWeatherForCity(int cityId) {
        return currentWeatherDao.getSingleForCity(cityId);
    }

    public Flowable<List<ForecastWeather>> getFlowingForecastForCity(int cityId) {
        return forecastWeatherDao.getFlowForCity(cityId)
                .filter(forecast -> {
                    if (forecast.size() > 0) {
                        return forecast.get(0).getApiCityId() == cityId;
                    } else {
                        return true;
                    }
                });
    }

    public Single<List<ForecastWeather>> getSingleForecastForCity(int cityId) {
        return forecastWeatherDao.getSingleForCity(cityId)
                .flatMap(forecast -> {
                    // Room does not return error on empty list as a result of Single, surprise!
                    if (forecast.size() == 0) {
                        return Single.error(new Throwable("Forecast list has 0 elements"));
                    } else {
                        return Single.just(forecast);
                    }
                });
    }

    public void updateCurrentWeather(CurrentWeather currentWeather) {
        currentWeatherDao.insert(currentWeather);
    }

    public void updateForecast(List<ForecastWeather> forecast) {
        if (forecast != null && forecast.size() > 0) {
            forecastWeatherDao.deleteForCity(forecast.get(0).getApiCityId());
            forecastWeatherDao.insertAll(forecast);
        }
    }

    public void addNewCity(City city) {
        cityDao.insert(city);
    }

    public City getCityById(int cityId) {
        return cityDao.getById(cityId);
    }

    public Flowable<List<CityWithWeather>> getFlowingCitiesWithWeather() {
        return cityDao.getCitiesWithWeather();
    }
}
