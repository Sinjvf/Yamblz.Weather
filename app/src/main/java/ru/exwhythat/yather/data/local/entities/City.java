package ru.exwhythat.yather.data.local.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by exwhythat on 8/4/17.
 */

@Entity
public class City {

    @PrimaryKey private int cityId;
    private String name;

    public City(int cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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

        if (cityId != city.cityId) return false;
        return name.equals(city.name);
    }

    @Override
    public int hashCode() {
        int result;
        result = cityId;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityId=" + cityId +
                ", name='" + name + '\'' +
                '}';
    }
}
