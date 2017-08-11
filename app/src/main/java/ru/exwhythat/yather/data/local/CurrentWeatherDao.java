package ru.exwhythat.yather.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;

/**
 * Created by exwhythat on 8/4/17.
 */

@Dao
public interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CurrentWeather currentWeather);

    @Query("SELECT * from currentweather WHERE apiCityId = :cityId")
    Flowable<CurrentWeather> getFlowForCity(int cityId);

    @Query("SELECT * from currentweather WHERE apiCityId = :cityId")
    Single<CurrentWeather> getSingleForCity(int cityId);
}
