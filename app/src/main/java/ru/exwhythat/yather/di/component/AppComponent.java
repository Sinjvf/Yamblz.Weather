package ru.exwhythat.yather.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.exwhythat.yather.di.module.ApplicationModule;
import ru.exwhythat.yather.di.module.NetworkModule;

/**
 * Created by exwhythat on 01.08.17.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {
    DataComponent plus(NetworkModule networkModule);
}
