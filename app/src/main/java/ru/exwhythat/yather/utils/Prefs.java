package ru.exwhythat.yather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import ru.exwhythat.yather.screens.settings.CityInfo;


/**
 * Created by Sinjvf on 16.07.2017.
 * works with shared preference
 */

public class Prefs {
    private static final String PACKAGE = "ru.exwhythat.yather";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);
    }

    public static final String PREF_UNITS = "pref_units";
    public static void setUnits(Context context, String units) {
        getPrefs(context).edit().putString(PREF_UNITS, units).apply();
    }

    public static String getUnits(Context context, String defaultUnits) {
        return getPrefs(context).getString(PREF_UNITS, defaultUnits);
    }


    private static String PREF_LAST_TIME_UPDATE = "pref_last_time";

    public static long getPrefLastTimeUpdateInterval(Context context) {
        long last = getPrefs(context).getLong(PREF_LAST_TIME_UPDATE, 0);
        long now = Calendar.getInstance().getTime().getTime();
        return now - last;
    }

    public static long getPrefLastTimeUpdateDate(Context context) {
        return getPrefs(context).getLong(PREF_LAST_TIME_UPDATE, 0);
    }

    public static void setPrefLastTimeUpdate(Context context) {
        long date = Calendar.getInstance().getTime().getTime();
        getPrefs(context).edit().putLong(PREF_LAST_TIME_UPDATE, date).apply();
    }


    public static String PREF_INTERVAL_TIME = "pref_interval_time";
    public static final long DEFAULT_INTERVAL = TimeUnit.MINUTES.toMillis(Utils.TIME_10);
    public static long getIntervalTime(Context context) {
        return getPrefs(context).getLong(PREF_INTERVAL_TIME, DEFAULT_INTERVAL);
    }

    public static void setIntervalTime(Context context, long interval) {
        getPrefs(context).edit().putLong(PREF_INTERVAL_TIME, interval).apply();
    }

    //region CityInfo Preferences
    public static final CityInfo DEFAULT_CITY_INFO = new CityInfo("Moscow", 524901);

    public static void setSelectedCity(Context context, CityInfo cityInfo) {
        setCityName(context, cityInfo.getCityName());
        setCityId(context, cityInfo.getCityId());
    }

    /*public static void setSelectedCity(Context context, CityInfo cityInfo) {
        setCityName(context, cityInfo.getCityName());
        setCityCoords(context, cityInfo.getCityCoords());
    }*/

    public static CityInfo getSelectedCity(Context context) {
        int cityId = getCityId(context);
        String cityName = getCityName(context);
        return new CityInfo(cityName, cityId);
        //LatLng cityCoords = getCityCoords(context);
        //return new CityInfo(cityName, cityCoords);
    }

    public static final String PREF_CITY_ID = "pref_city_id";
    private static int getCityId(Context context) {
        return getPrefs(context).getInt(PREF_CITY_ID, DEFAULT_CITY_INFO.getCityId());
    }

    private static void setCityId(Context context, int cityId) {
        getPrefs(context).edit().putInt(PREF_CITY_ID, cityId).apply();
    }

    public static final String PREF_CITY_NAME = "pref_city_name";
    private static String getCityName(Context context) {
        return getPrefs(context).getString(PREF_CITY_NAME, DEFAULT_CITY_INFO.getCityName());
    }

    private static void setCityName(Context context, String cityName) {
        getPrefs(context).edit().putString(PREF_CITY_NAME, cityName).apply();
    }

    /*public static final String PREF_CITY_COORDS = "pref_city_coords";
    private static LatLng getCityCoords(Context context) {
        String defaultCoords = getDefaultCoordsAsString();
        String strCoords = getPrefs(context).getString(PREF_CITY_COORDS, defaultCoords);
        return CoordsConverter.fromStringToCoords(strCoords);
    }

    private static void setCityCoords(Context context, LatLng cityCoords) {
        String strCoords = CoordsConverter.fromCoordsToString(cityCoords);
        getPrefs(context).edit().putString(PREF_CITY_COORDS, strCoords).apply();
    }

    private static String getDefaultCoordsAsString() {
        LatLng defaultCoords = DEFAULT_CITY_INFO.getCityCoords();
        return CoordsConverter.fromCoordsToString(defaultCoords);
    }*/
    //endregion
}
