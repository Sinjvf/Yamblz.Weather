package ru.exwhythat.yather.data.repository;

import java.util.List;

import io.reactivex.Flowable;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;

/**
 * Created by exwhythat on 01.08.17.
 */

public interface WeatherRepository {

    Flowable<CurrentWeather> getCurrentWeatherForCity(int cityId);

    Flowable<List<ForecastWeather>> getForecastForCity(int cityId);
}
