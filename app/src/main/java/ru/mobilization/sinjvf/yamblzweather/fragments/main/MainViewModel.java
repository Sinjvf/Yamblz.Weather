package ru.mobilization.sinjvf.yamblzweather.fragments.main;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;
import ru.mobilization.sinjvf.yamblzweather.BuildConfig;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragmentViewModel;
import ru.mobilization.sinjvf.yamblzweather.retrofit.BaseResponseCallback;
import ru.mobilization.sinjvf.yamblzweather.retrofit.ServiceHandler;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.BaseResponse;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;

/**
 * Created by Sinjvf on 09.07.2017.
 * ViewModel for main fragment
 */
@Keep
public class MainViewModel extends BaseFragmentViewModel {
    ServiceHandler handler = ServiceHandler.getInstance(getApplication().getApplicationContext());
    public MainViewModel(Application application) {
        super(application);


    }

    protected MutableLiveData<WeatherResponse> weather;

    public MutableLiveData<WeatherResponse> getWeatherData() {
        if (weather == null){
            weather = new MutableLiveData<>();
            sendWeatherRequest();
        }
        return weather;
    }

    @Override
    protected int getTitleStringId() {
        return R.string.menu_main;
    }

    public void sendWeatherRequest(){
        handler.getWeather(getResponseCallback(response -> weather.setValue(response)));
        if (BuildConfig.isDebug)
            Log.d(TAG, "MainViewModel: ");
    }


}
