package ru.exwhythat.yather.di.component;

import dagger.Subcomponent;
import ru.exwhythat.yather.di.DataScope;
import ru.exwhythat.yather.di.module.NetworkModule;
import ru.exwhythat.yather.screens.weather.WeatherViewModel;

/**
 * Created by exwhythat on 01.08.17.
 */

/**
 * Component which interacts with remote and local repositories
 */
@DataScope
@Subcomponent(modules = {NetworkModule.class})
public interface DataComponent {
    void inject(WeatherViewModel weatherModel);
}