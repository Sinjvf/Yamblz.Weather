package ru.mobilization.sinjvf.yamblzweather.retrofit;

import android.support.annotation.NonNull;

import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;

/**
 * Created by Sinjvf on 16.07.2017.
 * Describes what we do when get non null response from server
 */

public interface NonNullResp<T> {
    void onResponse(@NonNull T responseBody);
}
