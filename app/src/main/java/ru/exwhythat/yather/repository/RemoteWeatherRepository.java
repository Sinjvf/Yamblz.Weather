package ru.exwhythat.yather.repository;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.exwhythat.yather.network.weather.WeatherApi;
import ru.exwhythat.yather.network.weather.parts.WeatherResponse;

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
    public Single<WeatherResponse> getCurrentWeatherByLocation(LatLng latLng) {
        return weatherApi.getWeatherByLocation(latLng.latitude, latLng.longitude, null);
    }
}
