package ru.exwhythat.yather.utils;

import android.support.annotation.NonNull;

/**
 * Created by exwhythat on 8/10/17.
 */

public class StringUtils {

    @NonNull
    public static String makeSignedTempr(@NonNull String temperature) {
        if (temperature.charAt(0) != '-') {
            return '+' + temperature;
        } else {
            return temperature;
        }
    }
}