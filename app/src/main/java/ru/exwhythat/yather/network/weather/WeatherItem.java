package ru.exwhythat.yather.network.weather;

import ru.exwhythat.yather.network.weather.parts.WeatherResponse;

/**
 * Created by exwhythat on 02.08.17.
 */

public class WeatherItem {

    private int mainTemp;
    private int minTemp;
    private int maxTemp;
    private double humidity;
    private int windSpeed;
    private String imageUrlName;

    public WeatherItem(WeatherResponse response) {
        mainTemp = (int) response.getMain().getTemp();
        minTemp = (int) response.getMain().getTempMin();
        maxTemp = (int) response.getMain().getTempMax();
        humidity = response.getMain().getHumidity();
        windSpeed = (int) response.getWind().getSpeed();
        imageUrlName = response.getWeather().get(0).getIcon();
    }

    public int getMainTemp() {
        return mainTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public double getHumidity() {
        return humidity;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public String getImageUrlName() {
        return imageUrlName;
    }

    @Override
    public String toString() {
        return "WeatherItem{" +
                "mainTemp=" + mainTemp +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", imageUrlName='" + imageUrlName + '\'' +
                '}';
    }
}
