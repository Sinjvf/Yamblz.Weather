package ru.exwhythat.yather.repository;

import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;
import ru.exwhythat.yather.network.weather.parts.WeatherResponse;

/**
 * Created by exwhythat on 01.08.17.
 */

public interface WeatherRepository {

    Single<WeatherResponse> getCurrentWeatherByLocation(LatLng latLng);
}
