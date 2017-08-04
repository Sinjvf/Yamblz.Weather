package ru.exwhythat.yather.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by exwhythat on 01.08.17.
 */

@Module(includes = {ViewModelModule.class, NetworkModule.class})
public class AppModule {

    @Singleton
    @Provides
    Context provideApplicationContext(Application application) {
        return application.getApplicationContext();
    }
}