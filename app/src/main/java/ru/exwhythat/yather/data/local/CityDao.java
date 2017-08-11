package ru.exwhythat.yather.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import ru.exwhythat.yather.data.local.entities.City;
import ru.exwhythat.yather.data.local.entities.CityWithWeather;

/**
 * Created by exwhythat on 8/3/17.
 */

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(City city);

    @Query("SELECT * FROM city WHERE cityId = :cityId")
    City getById(int cityId);

    @Query("SELECT * FROM city LEFT JOIN currentweather ON currentweather.apiCityId = city.cityId")
    Flowable<List<CityWithWeather>> getCitiesWithWeather();

    @Query("DELETE FROM city")
    int clearTable();
}
