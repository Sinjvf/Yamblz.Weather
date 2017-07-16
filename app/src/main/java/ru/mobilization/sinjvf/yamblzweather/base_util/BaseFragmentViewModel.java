package ru.mobilization.sinjvf.yamblzweather.base_util;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;
import ru.mobilization.sinjvf.yamblzweather.BuildConfig;
import ru.mobilization.sinjvf.yamblzweather.retrofit.BaseResponseCallback;
import ru.mobilization.sinjvf.yamblzweather.retrofit.NonNullResp;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;

/**
 * Created by Sinjvf on 09.07.2017.
 * Parent class for fragment's ViewModels
 */

public abstract class BaseFragmentViewModel extends AndroidViewModel {

    protected final String TAG = "tag:" + getClass().getSimpleName();
    protected MutableLiveData<Integer> titleId;
    protected final Context context;
    protected FragmentManager fragmentManager;
    protected final Handler handler = new Handler();

    public BaseFragmentViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<Integer> getTitle() {
        if (titleId == null) {
            titleId = new MutableLiveData<>();
            setToolbarText();
        }
        return titleId;
    }

    protected void setToolbarText() {
        titleId.setValue(getTitleStringId());
    }


    protected abstract int getTitleStringId();

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    protected <T> BaseResponseCallback<T> getResponseCallback(NonNullResp<T> nonNullResp) {
        return new BaseResponseCallback<>(context, fragmentManager, nonNullResp);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacksAndMessages(null);
    }
}
