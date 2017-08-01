package ru.exwhythat.yather.screens.settings;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by exwhythat on 24.07.17.
 */

public class CityInfo {

    private String cityName;
    private LatLng cityCoords;

    public CityInfo(String cityName, LatLng cityCoords) {
        this.cityName = cityName;
        this.cityCoords = cityCoords;
    }

    public CityInfo(String cityName, double latitude, double longitude) {
        this.cityName = cityName;
        this.cityCoords = new LatLng(latitude, longitude);
    }

    public CityInfo(Place place) {
        this.cityName = place.getName().toString();
        this.cityCoords = place.getLatLng();
    }

    public String getCityName() {
        return cityName;
    }

    public LatLng getCityCoords() {
        return cityCoords;
    }

    @Override
    public String toString() {
        return "Place name=" + cityName +
                ", coords=[" + cityCoords.latitude + ", " + cityCoords.longitude + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityInfo cityInfo = (CityInfo) o;

        if (!cityName.equals(cityInfo.cityName)) return false;
        return cityCoords.equals(cityInfo.cityCoords);

    }

    @Override
    public int hashCode() {
        int result = cityName.hashCode();
        result = 31 * result + cityCoords.hashCode();
        return result;
    }
}
