package ru.exwhythat.yather.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.exwhythat.yather.data.local.CityDao;
import ru.exwhythat.yather.data.local.CurrentWeatherDao;
import ru.exwhythat.yather.data.local.ForecastWeatherDao;
import ru.exwhythat.yather.data.local.YatherDb;

/**
 * Created by exwhythat on 8/6/17.
 */

@Module
public class DbModule {

    @Singleton
    @Provides
    YatherDb provideDb(Application application) {
        return Room.databaseBuilder(application, YatherDb.class, "yather.db").build();
    }

    @Singleton
    @Provides
    CityDao provideCityDao(YatherDb db) {
        return db.cityDao();
    }

    @Singleton
    @Provides
    CurrentWeatherDao provideCurrentWeatherDao(YatherDb db) {
        return db.currentWeatherDao();
    }

    @Singleton
    @Provides
    ForecastWeatherDao provideForecastWeatherDao(YatherDb db) {
        return db.forecastWeatherDao();
    }
}
