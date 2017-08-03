package ru.exwhythat.yather.network.weather.parts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
