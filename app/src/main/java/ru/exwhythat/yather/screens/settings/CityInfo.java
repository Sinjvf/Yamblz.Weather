package ru.exwhythat.yather.screens.settings;


import ru.exwhythat.yather.data.local.entities.City;

/**
 * Created by exwhythat on 24.07.17.
 */

public class CityInfo {

    private int cityId;
    private String cityName;

    public CityInfo(String cityName, int cityId) {
        this.cityName = cityName;
        this.cityId = cityId;
    }

    public static class Mapper {
        public static City toCity(CityInfo cityInfo) {
            return new City(cityInfo.getCityId(), cityInfo.getCityName());
        }
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityInfo)) return false;

        CityInfo cityInfo = (CityInfo) o;

        if (cityId != cityInfo.cityId) return false;
        return cityName.equals(cityInfo.cityName);
    }

    @Override
    public int hashCode() {
        int result = cityId;
        result = 31 * result + cityName.hashCode();
        return result;
    }
}
