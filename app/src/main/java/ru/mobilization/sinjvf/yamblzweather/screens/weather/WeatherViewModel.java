package ru.mobilization.sinjvf.yamblzweather.screens.weather;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

import com.google.android.gms.maps.model.LatLng;

import io.reactivex.SingleObserver;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragmentViewModel;
import ru.mobilization.sinjvf.yamblzweather.retrofit.ServiceHandler;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * ViewModel for main fragment
 */
@Keep
public class WeatherViewModel extends BaseFragmentViewModel {
    ServiceHandler serviceHandler = ServiceHandler.getInstance(getApplication().getApplicationContext());

    public WeatherViewModel(Application application) {
        super(application);
    }

    protected MutableLiveData<WeatherResponse> weather;
    protected MutableLiveData<String> lastUpdateTime;

    public LiveData<WeatherResponse> getWeatherDataByCityId(String cityId) {
        if (weather == null) {
            weather = new MutableLiveData<>();
            sendWeatherRequestByCityId(cityId);
        }
        return weather;
    }

    public LiveData<WeatherResponse> getWeatherDataByCityCoords(LatLng cityCoords) {
        if (weather == null) {
            weather = new MutableLiveData<>();
            sendWeatherRequestByCityCoords(cityCoords);
        }
        return weather;
    }

    public LiveData<String> getLastUpdate() {
        if (lastUpdateTime == null){
            lastUpdateTime = new MutableLiveData<>();
            lastUpdateTime.setValue(Utils.lastUpdateString(context));
        }
        return lastUpdateTime;
    }

    @Override
    protected int getTitleStringId() {
        return R.string.menu_main;
    }

    public void sendWeatherRequestByCityId(String cityId){
        Timber.d("sendWeatherRequestByCityId:");
        SingleObserver<WeatherResponse> observer = getObserver(response -> {
            weather.setValue(response);
            Preferenses.setPrefLastTimeUpdate(context);
            lastUpdateTime.setValue(Utils.lastUpdateString(context));
            handler.postDelayed(() -> sendWeatherRequestByCityId(cityId),
                    Preferenses.getIntervalTime(context));
        });
        serviceHandler.getWeatherByCityId(cityId, observer);
    }

    public void sendWeatherRequestByCityCoords(LatLng cityCoords){
        Timber.d("sendWeatherRequestByCityCoords:");
        SingleObserver<WeatherResponse> observer = getObserver(response -> {
            weather.setValue(response);
            Preferenses.setPrefLastTimeUpdate(context);
            lastUpdateTime.setValue(Utils.lastUpdateString(context));
            handler.postDelayed(() -> sendWeatherRequestByCityCoords(cityCoords),
                    Preferenses.getIntervalTime(context));
        });
        serviceHandler.getWeatherByCityCoords(cityCoords, observer);
    }
}
