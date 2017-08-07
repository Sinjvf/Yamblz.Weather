package ru.exwhythat.yather.data.local.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by exwhythat on 8/4/17.
 */

@Entity
public class City {

    @PrimaryKey private int apiCityId;
    private String name;

    public City(int apiCityId, String name) {
        this.apiCityId = apiCityId;
        this.name = name;
    }

    public int getApiCityId() {
        return apiCityId;
    }

    public void setApiCityId(int apiCityId) {
        this.apiCityId = apiCityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (apiCityId != city.apiCityId) return false;
        return name.equals(city.name);
    }

    @Override
    public int hashCode() {
        int result;
        result = apiCityId;
        result = 31 * result + name.hashCode();
        return result;
    }
}
