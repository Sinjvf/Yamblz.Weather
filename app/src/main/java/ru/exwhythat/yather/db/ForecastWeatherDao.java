package ru.exwhythat.yather.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.exwhythat.yather.db.entities.ForecastWeather;

/**
 * Created by exwhythat on 8/5/17.
 */

@Dao
public interface ForecastWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ForecastWeather forecastWeather);

    @Query("SELECT * FROM forecastweather")
    LiveData<List<ForecastWeather>> getAll();

    @Query("SELECT * FROM forecastweather WHERE apiCityId = :cityId")
    LiveData<List<ForecastWeather>> getForCity(int cityId);

    @Query("DELETE FROM forecastweather WHERE apiCityId = :cityId")
    int deleteForCity(int cityId);
}
