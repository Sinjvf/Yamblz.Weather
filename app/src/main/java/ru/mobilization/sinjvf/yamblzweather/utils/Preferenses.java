package ru.mobilization.sinjvf.yamblzweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sinjvf on 16.07.2017.
 * works with shared preference
 */

public class Preferenses {
    private static final String PACKAGE = "ru.mobilization.sinjvf.yamblzweather";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
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
    public static long getIntervalTime(Context context) {
        return getPrefs(context).getLong(PREF_INTERVAL_TIME, 0);
    }

    public static void setIntervalTime(Context context, long interval) {
        getPrefs(context).edit().putLong(PREF_INTERVAL_TIME, interval).apply();
    }
}
