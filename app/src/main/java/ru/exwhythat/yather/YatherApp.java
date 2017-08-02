package ru.exwhythat.yather;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import ru.exwhythat.yather.di.component.AppComponent;
import ru.exwhythat.yather.di.component.DaggerAppComponent;
import ru.exwhythat.yather.di.component.DataComponent;
import ru.exwhythat.yather.di.module.ApplicationModule;
import ru.exwhythat.yather.di.module.NetworkModule;
import timber.log.Timber;

/**
 * Created by Sinjvf on 16.07.2017.
 * Application
 * Init some libraries' instances if need
 */

public class YatherApp extends Application {

    private AppComponent appComponent;
    private DataComponent dataComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Init LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new YatherDebugTree());
            Stetho.initializeWithDefaults(this);
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    public DataComponent plusDataComponent() {
        if (dataComponent == null) {
            dataComponent = appComponent.plus(new NetworkModule());
        }
        return dataComponent;
    }

    public void clearDataComponent() {
        dataComponent = null;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static YatherApp get(Context context) {
        return ((YatherApp)context.getApplicationContext());
    }

    /** Customized Timber.DebugTree */
    private static class YatherDebugTree extends Timber.DebugTree {
        @Override
        protected String createStackElementTag(StackTraceElement element) {
            // Add line number to every log message
            return super.createStackElementTag(element) + ":" + element.getLineNumber();
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
