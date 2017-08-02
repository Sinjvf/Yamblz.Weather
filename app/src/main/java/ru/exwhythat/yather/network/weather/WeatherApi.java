package ru.exwhythat.yather.network.weather;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.exwhythat.yather.network.weather.parts.WeatherResponse;

/**
 * Created by exwhythat on 15.07.17.
 */

public interface WeatherApi {

    @GET("weather")
    Observable<WeatherResponse> getWeatherByCityId(@Query("id") int cityId,
                                                   @Nullable @Query("units") String tempUnits);

    @GET("weather")
    Single<WeatherResponse> getWeatherByLocation(@Query("lat") double lat,
                                              @Query("lon") double lon,
                                              @Nullable @Query("units") String tempUnits);
}
