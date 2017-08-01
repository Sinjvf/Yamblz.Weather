package ru.exwhythat.yather;

import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import ru.exwhythat.yather.screens.settings.CityInfo;
import ru.exwhythat.yather.utils.CoordsConverter;
import ru.exwhythat.yather.utils.Preferenses;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by exwhythat on 30.07.17.
 */

class UnitUtils {

    static void mockPrefsWithDefaults(SharedPreferences preferences, SharedPreferences.Editor editor) {
        when(preferences.edit()).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);

        // Mock default city name and coordinates
        CityInfo defaultCityInfo = Preferenses.DEFAULT_CITY_INFO;
        String defaultCityName = defaultCityInfo.getCityName();
        when(preferences.getString(eq(Preferenses.PREF_CITY_NAME), anyString())).thenReturn(defaultCityName);
        LatLng defaultCoords = defaultCityInfo.getCityCoords();
        String defaultStringCoords = CoordsConverter.fromCoordsToString(defaultCoords);
        when(preferences.getString(eq(Preferenses.PREF_CITY_COORDS), anyString())).thenReturn(defaultStringCoords);

        long defaultInterval = Preferenses.DEFAULT_INTERVAL;
        when(preferences.getLong(eq(Preferenses.PREF_INTERVAL_TIME), anyLong())).thenReturn(defaultInterval);
    }
}
