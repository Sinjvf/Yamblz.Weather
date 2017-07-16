package ru.mobilization.sinjvf.yamblzweather.retrofit;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import ru.mobilization.sinjvf.yamblzweather.BuildConfig;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.BaseResponse;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;

/**
 * Created by Sinjvf on 15.07.2017.
 * Class for handling all service requests
 */

public class ServiceHandler {

    private final String TAG = "tag:" + getClass().getSimpleName();
    private static ServiceHandler instance;
    public static ServiceHandler getInstance(Context context) {
        if (instance == null)
            instance = new ServiceHandler(context);
        return instance;
    }

    private final ServiceContract.WeatherAPI service;
    private final Context context;

    private ServiceHandler(Context context) {
        this.service = ServiceContract.getService();
        this.context = context;
    }

    //for fast change of requests and responses types if need
    interface Invocation<T extends BaseResponse> {
        Call<T> invoke();
    }
    public <T extends BaseResponse> void callService(Callback<T> callback, Invocation<T> invocation){
        Call<T> call = invocation.invoke();
        call.enqueue(callback);
    }


    /*
    * requests
    * */
    public void getWeather(Callback<WeatherResponse> callback) {
        Map<String, String> map = new HashMap<>();
        if (BuildConfig.isDebug)
            Log.d(TAG, "getWeatherData: ");
        map.put(ServiceContract.FIELD_APP_KEY, context.getString(R.string.api_key));
        map.put(ServiceContract.FIELD_CITY_ID, context.getString(R.string.moscow_id));
        map.put(ServiceContract.FIELD_UNITS, ServiceContract.UNITS_METRIC);
        callService(callback, () -> service.getWeather(map));
    }


}
