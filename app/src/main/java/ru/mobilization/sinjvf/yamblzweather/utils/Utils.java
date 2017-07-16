package ru.mobilization.sinjvf.yamblzweather.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mobilization.sinjvf.yamblzweather.R;

/**
 * Created by Sinjvf on 16.07.2017.
 * Some utils
 */

public class Utils {
    public static long MINUTE = 1000 * 60;

    public static String lastUpdateString(Context context) {
        Date date = new Date(Preferenses.getPrefLastTimeUpdateDate(context));
        SimpleDateFormat sf = new SimpleDateFormat(" HH:mm, dd.MM.yy ", Locale.getDefault());
        return sf.format(date);
    }

    //if we have many inner classes and can catch the NullPointer Exception while do many get calls
    public static <T> T getDataWithoutException(GetData<T> getData) {
        try {
            return getData.getData();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface GetData<T> {
        T getData();
    }
}
