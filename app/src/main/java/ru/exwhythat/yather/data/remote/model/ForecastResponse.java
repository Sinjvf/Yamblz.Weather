package ru.exwhythat.yather.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.exwhythat.yather.data.local.entities.BaseWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;

/**
 * Created by exwhythat on 8/3/17.
 */

public class ForecastResponse extends BaseResponse {

    @SerializedName("cod") @Expose
    private String cod;
    @SerializedName("message") @Expose
    private double message;
    @SerializedName("cnt") @Expose
    private int responseLinesCount;
    @SerializedName("list") @Expose
    private List<ForecastList> list = null;
    @SerializedName("city") @Expose
    private City city;
    
    public static class Mapper {
        public static List<ForecastWeather> toForecast(ForecastResponse response) {
            List<ForecastWeather> result = new ArrayList<>();

            for (ForecastList forecast: response.getList()) {
                String main = forecast.getWeather().get(0).getMain();
                String descr = forecast.getWeather().get(0).getDescription();
                String icon = forecast.getWeather().get(0).getIcon();
                long date = forecast.getDt();
                double temp = forecast.getMain().getTemp();
                BaseWeather baseWeather = new BaseWeather(main, descr, icon, new Date(date), temp);

                int apiCityId = response.getCity().getId();
                result.add(new ForecastWeather(baseWeather, apiCityId));
            }

            return result;
        }
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public int getResponseLinesCount() {
        return responseLinesCount;
    }

    public void setResponseLinesCount(int responseLinesCount) {
        this.responseLinesCount = responseLinesCount;
    }

    public List<ForecastList> getList() {
        return list;
    }

    public void setList(List<ForecastList> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
