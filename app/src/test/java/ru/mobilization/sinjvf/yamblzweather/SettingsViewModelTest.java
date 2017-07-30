package ru.mobilization.sinjvf.yamblzweather;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import ru.mobilization.sinjvf.yamblzweather.screens.settings.CityInfo;
import ru.mobilization.sinjvf.yamblzweather.screens.settings.SettingsViewModel;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SettingsViewModelTest {

    private SettingsViewModel settingsViewModel;

    // Mock main looper
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Application mockApplication;

    @Mock
    Context mockApplicationContext;

    @Mock
    SharedPreferences mockPrefs;

    @Mock
    SharedPreferences.Editor mockEditor;

    @Before
    public void prepare() {
        initMocks();
        settingsViewModel = new SettingsViewModel(mockApplication);
    }

    private void initMocks() {
        when(mockApplication.getApplicationContext()).thenReturn(mockApplicationContext);
        when(mockApplication.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        when(mockApplicationContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);

        // Mock prefs
        UnitUtils.mockPrefsWithDefaults(mockPrefs, mockEditor);
    }

    @Test
    public void readEmptyCityInfoReturnsDefault() {
        CityInfo actualCityInfo = settingsViewModel.getCityInfo().getValue();
        assertNotNull(actualCityInfo);

        CityInfo expectedCityInfo = Preferenses.DEFAULT_CITY_INFO;
        assertEquals(expectedCityInfo, actualCityInfo);

        CityInfo wrongCityInfo = new CityInfo("wrongCityName", 77, 77);
        assertNotEquals(wrongCityInfo, actualCityInfo);
    }

    @Test
    public void readNotEmptyCityInfoReturnsWritten() {
        CityInfo expectedCityInfo = Preferenses.DEFAULT_CITY_INFO;
        settingsViewModel.updateCityInfo(expectedCityInfo);

        CityInfo actualCityInfo = settingsViewModel.getCityInfo().getValue();
        assertNotNull(actualCityInfo);
        assertEquals(expectedCityInfo, actualCityInfo);

        CityInfo wrongCityInfo = new CityInfo("wrongCityName", 77, 77);
        assertNotEquals(wrongCityInfo, actualCityInfo);
    }

    @Test
    public void readEmptyIntervalReturnsDefault() {
        Long actualInterval = settingsViewModel.getInterval().getValue();
        assertNotNull(actualInterval);
        Long expectedInterval = Preferenses.DEFAULT_INTERVAL;
        assertEquals(expectedInterval, actualInterval);
    }

    @Test
    public void readNotEmptyIntervalReturnsWritten() {
        Long expectedInterval = TimeUnit.MINUTES.toMillis(Utils.TIME_10);
        settingsViewModel.updateInterval(expectedInterval);
        Long actualInterval = settingsViewModel.getInterval().getValue();
        assertNotNull(actualInterval);
        assertEquals(expectedInterval, actualInterval);
    }
}