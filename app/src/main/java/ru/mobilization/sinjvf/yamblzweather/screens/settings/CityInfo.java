package ru.mobilization.sinjvf.yamblzweather.screens.settings;

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
}
