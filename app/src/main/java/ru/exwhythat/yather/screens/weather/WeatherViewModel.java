package ru.exwhythat.yather.screens.weather;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

import com.google.android.gms.maps.model.LatLng;

import io.reactivex.SingleObserver;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.base_util.BaseFragmentViewModel;
import ru.exwhythat.yather.retrofit.ServiceHandler;
import ru.exwhythat.yather.retrofit.data.WeatherResponse;
import ru.exwhythat.yather.utils.Preferenses;
import ru.exwhythat.yather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * ViewModel for main fragment
 */
@Keep
public class WeatherViewModel extends BaseFragmentViewModel {

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
        return getWeatherDataByCityCoords(cityCoords, true);
    }

    public LiveData<WeatherResponse> getWeatherDataByCityCoords(LatLng cityCoords, boolean repeat) {
        if (weather == null) {
            weather = new MutableLiveData<>();
            sendWeatherRequestByCityCoords(cityCoords, repeat);
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
        return R.string.menu_weather;
    }

    public void sendWeatherRequestByCityId(String cityId){
        Timber.d("sendWeatherRequestByCityId:");
        SingleObserver<WeatherResponse> observer = getObserver(response -> {
            updateWeather(response);
            updateInterval();
            handler.postDelayed(() -> sendWeatherRequestByCityId(cityId),
                    Preferenses.getIntervalTime(context));
        });
        ServiceHandler.getInstance(context).getWeatherByCityId(cityId, observer);
    }

    public void sendWeatherRequestByCityCoords(LatLng cityCoords){
        sendWeatherRequestByCityCoords(cityCoords, true);
    }

    public void sendWeatherRequestByCityCoords(LatLng cityCoords, boolean repeat){
        Timber.d("sendWeatherRequestByCityCoords:");
        SingleObserver<WeatherResponse> observer = getObserver(response -> {
            updateWeather(response);
            updateInterval();
            if (repeat) {
                handler.postDelayed(() -> sendWeatherRequestByCityCoords(cityCoords, repeat),
                        Preferenses.getIntervalTime(context));
            }
        });
        ServiceHandler.getInstance(context).getWeatherByCityCoords(cityCoords, observer);
    }

    private void updateWeather(WeatherResponse response) {
        if (weather == null) weather = new MutableLiveData<>();
        weather.setValue(response);
    }

    private void updateInterval() {
        Preferenses.setPrefLastTimeUpdate(context);
        if (lastUpdateTime == null) lastUpdateTime = new MutableLiveData<>();
        lastUpdateTime.setValue(Utils.lastUpdateString(context));
    }
}
