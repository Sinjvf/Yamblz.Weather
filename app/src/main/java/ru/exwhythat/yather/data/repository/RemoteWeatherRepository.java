package ru.exwhythat.yather.data.repository;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.data.remote.WeatherApi;
import ru.exwhythat.yather.data.remote.model.ForecastResponse;
import ru.exwhythat.yather.data.remote.model.WeatherResponse;

/**
 * Created by exwhythat on 02.08.17.
 */

public class RemoteWeatherRepository implements WeatherRepository {

    private WeatherApi weatherApi;

    @Inject
    public RemoteWeatherRepository(WeatherApi weatherApi) {
        this.weatherApi = weatherApi;
    }

    @Override
    public Flowable<CurrentWeather> getCurrentWeatherForCity(int cityId) {
        return weatherApi.getWeatherByCityId(cityId)
                .map(WeatherResponse.Mapper::toCurrentWeather).toFlowable();
    }

    @Override
    public Flowable<List<ForecastWeather>> getForecastForCity(int cityId) {
        return weatherApi.getForecastByCityId(cityId)
                .map(ForecastResponse.Mapper::toForecast).toFlowable();
    }

    public Single<Integer> getCityIdByLatLng(LatLng latLng) {
        return weatherApi.getWeatherByLocation(latLng.latitude, latLng.longitude)
                .map(WeatherResponse::getCityId);
    }
}
