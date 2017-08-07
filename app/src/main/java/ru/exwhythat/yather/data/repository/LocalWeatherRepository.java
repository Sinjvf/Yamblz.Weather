package ru.exwhythat.yather.data.repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import ru.exwhythat.yather.data.local.CurrentWeatherDao;
import ru.exwhythat.yather.data.local.ForecastWeatherDao;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;

/**
 * Created by exwhythat on 8/6/17.
 */

public class LocalWeatherRepository implements WeatherRepository {

    private final CurrentWeatherDao currentWeatherDao;
    private final ForecastWeatherDao forecastWeatherDao;

    @Inject
    public LocalWeatherRepository(CurrentWeatherDao currentWeatherDao, ForecastWeatherDao forecastWeatherDao) {
        this.currentWeatherDao = currentWeatherDao;
        this.forecastWeatherDao = forecastWeatherDao;
    }

    @Override
    public Flowable<CurrentWeather> getCurrentWeatherForCity(int cityId) {
        return currentWeatherDao.getForCity(cityId);
    }

    @Override
    public Flowable<List<ForecastWeather>> getForecastForCity(int cityId) {
        return forecastWeatherDao.getForCity(cityId);
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
}
