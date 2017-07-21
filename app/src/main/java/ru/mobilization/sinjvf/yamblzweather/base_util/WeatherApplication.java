package ru.mobilization.sinjvf.yamblzweather.base_util;

import android.app.Application;
import android.util.Log;

import ru.mobilization.sinjvf.yamblzweather.BuildConfig;
import ru.mobilization.sinjvf.yamblzweather.retrofit.ServiceHandler;
import timber.log.Timber;

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


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
            log(priority, tag, message);
        }
    }
}
