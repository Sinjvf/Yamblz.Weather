package ru.mobilization.sinjvf.yamblzweather.base_util;

import android.app.Application;

import ru.mobilization.sinjvf.yamblzweather.retrofit.ServiceHandler;

/**
 * Created by Sinjvf on 16.07.2017.
 * Application
 * Init some libraries' instances if need
 */

public class WeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ServiceHandler.getInstance(getApplicationContext());
    }
}
