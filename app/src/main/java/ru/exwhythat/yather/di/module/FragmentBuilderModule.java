package ru.exwhythat.yather.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.exwhythat.yather.screens.settings.SettingsFragment;
import ru.exwhythat.yather.screens.weather.WeatherFragment;

/**
 * Created by exwhythat on 03.08.17.
 */

@Module
public abstract class FragmentBuilderModule {
    @ContributesAndroidInjector
    abstract WeatherFragment contributeWeatherFragment();

    @ContributesAndroidInjector
    abstract SettingsFragment contributeSettingsFragment();
}