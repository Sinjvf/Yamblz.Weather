package ru.mobilization.sinjvf.yamblzweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ru.mobilization.sinjvf.yamblzweather.screens.settings.CityInfo;


/**
 * Created by Sinjvf on 16.07.2017.
 * works with shared preference
 */

public class Preferenses {
    private static final String PACKAGE = "ru.mobilization.sinjvf.yamblzweather";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);
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


    private static String PREF_INTERVAL_TIME = "pref_interval_time";
    private static final long DEFAULT_INTERVAL = TimeUnit.MINUTES.toMillis(Utils.TIME_10);
    public static long getIntervalTime(Context context) {
        return getPrefs(context).getLong(PREF_INTERVAL_TIME, DEFAULT_INTERVAL);
    }

    public static void setIntervalTime(Context context, long interval) {
        getPrefs(context).edit().putLong(PREF_INTERVAL_TIME, interval).apply();
    }

    //region CityInfo Preferences
    private static final CityInfo DEFAULT_CITY_INFO = new CityInfo("Moscow", 55, 37);

    public static void setCityInfo(Context context, CityInfo cityInfo) {
        setCityName(context, cityInfo.getCityName());
        setCityCoords(context, cityInfo.getCityCoords());
    }

    public static CityInfo getCityInfo(Context context) {
        String cityName = getCityName(context);
        LatLng cityCoords = getCityCoords(context);
        return new CityInfo(cityName, cityCoords);
    }

    private static String PREF_CITY_NAME = "pref_city_name";
    private static String getCityName(Context context) {
        return getPrefs(context).getString(PREF_CITY_NAME, DEFAULT_CITY_INFO.getCityName());
    }

    private static void setCityName(Context context, String cityName) {
        getPrefs(context).edit().putString(PREF_CITY_NAME, cityName).apply();
    }

    private static String PREF_CITY_COORDS = "pref_city_coords";

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
    }

    private static class CoordsConverter {
        private static final Character COORDS_SPLIT_SYMBOL = ' ';
        private static final String COORDS_FORMAT_PATTERN = "%1$f" + COORDS_SPLIT_SYMBOL + "%2$f";

        private static LatLng fromStringToCoords(String strCoords) {
            String[] splittedCoords = strCoords.split(COORDS_SPLIT_SYMBOL.toString());
            double latitude = Double.valueOf(splittedCoords[0].replace(',', '.'));
            double longitude = Double.valueOf(splittedCoords[1].replace(',', '.'));
            return new LatLng(latitude, longitude);
        }

        private static String fromCoordsToString(LatLng coords) {
            return String.format(Locale.getDefault(), COORDS_FORMAT_PATTERN, coords.latitude, coords.longitude);
        }
    }
    //endregion
}
