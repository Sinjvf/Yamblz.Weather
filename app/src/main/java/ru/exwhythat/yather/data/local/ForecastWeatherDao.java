package ru.exwhythat.yather.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;

/**
 * Created by exwhythat on 8/5/17.
 */

@Dao
public interface ForecastWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ForecastWeather forecastWeather);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ForecastWeather> forecastWeatherList);

    @Query("SELECT * FROM forecastweather WHERE apiCityId = :cityId ORDER BY date")
    Flowable<List<ForecastWeather>> getFlowForCity(int cityId);

    @Query("SELECT * FROM forecastweather WHERE apiCityId = :cityId ORDER BY date")
    Single<List<ForecastWeather>> getSingleForCity(int cityId);

    @Query("DELETE FROM forecastweather WHERE apiCityId = :cityId")
    int deleteForCity(int cityId);
}
