package ru.exwhythat.yather.data.local.entities;

import java.util.Date;

/**
 * Created by exwhythat on 8/5/17.
 */

public class BaseWeather {

    private String main;
    private String descr;
    private String iconId;
    private Date date;
    private double temp;

    public BaseWeather(String main, String descr, String iconId, Date date, double temp) {
        this.main = main;
        this.descr = descr;
        this.iconId = iconId;
        this.date = date;
        this.temp = temp;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseWeather)) return false;

        BaseWeather that = (BaseWeather) o;

        if (Double.compare(that.temp, temp) != 0) return false;
        if (!main.equals(that.main)) return false;
        if (!descr.equals(that.descr)) return false;
        if (!iconId.equals(that.iconId)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        result = main.hashCode();
        result = 31 * result + descr.hashCode();
        result = 31 * result + iconId.hashCode();
        result = 31 * result + date.hashCode();
        temp1 = Double.doubleToLongBits(temp);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        return result;
    }
}
