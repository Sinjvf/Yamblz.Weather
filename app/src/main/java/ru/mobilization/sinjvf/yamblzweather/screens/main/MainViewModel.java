package ru.mobilization.sinjvf.yamblzweather.screens.main;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

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
public class MainViewModel extends BaseFragmentViewModel {
    ServiceHandler serviceHandler = ServiceHandler.getInstance(getApplication().getApplicationContext());

    public MainViewModel(Application application) {
        super(application);
    }

    protected MutableLiveData<WeatherResponse> weather;
    protected MutableLiveData<String> lastUpdateTime;

    public MutableLiveData<WeatherResponse> getWeatherData() {
        if (weather == null){
            weather = new MutableLiveData<>();
            sendWeatherRequest();
        }
        return weather;
    }


    public MutableLiveData<String> getLastUpdate() {
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

    public void sendWeatherRequest(){
        Timber.d("sendWeatherRequest:");
        serviceHandler.getWeather(getResponseCallback(response -> {
            weather.setValue(response);
            Preferenses.setPrefLastTimeUpdate(context);
            lastUpdateTime.setValue(Utils.lastUpdateString(context));
            handler.postDelayed(this::sendWeatherRequest, Preferenses.getIntervalTime(context));
        }, progressObserver()));
    }




}
