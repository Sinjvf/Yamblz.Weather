package ru.exwhythat.yather.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.exwhythat.yather.db.entities.City;

/**
 * Created by exwhythat on 8/3/17.
 */

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(City city);

    @Query("SELECT * FROM city")
    LiveData<List<City>> getAll();

    @Query("SELECT * FROM city WHERE apiCityId = :cityId")
    LiveData<City> getById(int cityId);

    @Delete
    int deleteCity(City city);

    @Query("DELETE FROM city")
    int clearTable();
}
