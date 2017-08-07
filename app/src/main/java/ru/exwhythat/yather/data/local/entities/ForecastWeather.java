package ru.exwhythat.yather.data.local.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by exwhythat on 8/5/17.
 */

@Entity (foreignKeys =
            @ForeignKey(entity = City.class,
            parentColumns = "apiCityId",
            childColumns = "apiCityId",
            onDelete = ForeignKey.CASCADE))
public class ForecastWeather {

    @PrimaryKey(autoGenerate = true)
    private int forecastId;

    @Embedded
    private BaseWeather baseWeather;

    private int apiCityId;

    public ForecastWeather(BaseWeather baseWeather, int apiCityId) {
        this.baseWeather = baseWeather;
        this.apiCityId = apiCityId;
    }

    public int getForecastId() {
        return forecastId;
    }

    public void setForecastId(int forecastId) {
        this.forecastId = forecastId;
    }

    public BaseWeather getBaseWeather() {
        return baseWeather;
    }

    public void setBaseWeather(BaseWeather baseWeather) {
        this.baseWeather = baseWeather;
    }

    public int getApiCityId() {
        return apiCityId;
    }

    public void setApiCityId(int apiCityId) {
        this.apiCityId = apiCityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForecastWeather)) return false;

        ForecastWeather that = (ForecastWeather) o;

        if (forecastId != that.forecastId) return false;
        if (apiCityId != that.apiCityId) return false;
        return baseWeather.equals(that.baseWeather);
    }

    @Override
    public int hashCode() {
        int result;
        result = forecastId;
        result = 31 * result + baseWeather.hashCode();
        result = 31 * result + apiCityId;
        return result;
    }
}
