package ru.mobilization.sinjvf.yamblzweather.retrofit;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.BaseResponse;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;
import timber.log.Timber;

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
        Observable<T> invoke();
    }
    public <T extends BaseResponse> void callService(SingleObserver<T> observer, Invocation<T> invocation){
        Single.fromObservable(invocation.invoke())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /*
    * requests
    * */
    public void getWeatherByCityId(String cityId, SingleObserver<WeatherResponse> observer) {
        Map<String, String> map = buildBasicQueryMap();
        map.put(ServiceContract.FIELD_CITY_ID, cityId);
        Timber.d(TAG, "getWeatherDataByCityId by city id: ");
        callService(observer, () -> service.getWeather(map));
    }

    public void getWeatherByCityCoords(LatLng cityCoords, SingleObserver<WeatherResponse> observer) {
        Map<String, String> map = buildBasicQueryMap();
        map.put(ServiceContract.FIELD_LAT, String.valueOf(cityCoords.latitude));
        map.put(ServiceContract.FIELD_LON, String.valueOf(cityCoords.longitude));
        Timber.d(TAG, "getWeatherDataByCityId by city coords: ");
        callService(observer, () -> service.getWeather(map));
    }

    private Map<String, String> buildBasicQueryMap() {
        Map<String, String> map = new HashMap<>();
        map.put(ServiceContract.FIELD_APP_KEY, context.getString(R.string.api_key));
        map.put(ServiceContract.FIELD_UNITS, ServiceContract.UNITS_METRIC);
        return map;
    }
}
