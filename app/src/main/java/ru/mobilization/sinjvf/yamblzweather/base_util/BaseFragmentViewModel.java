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

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Response;
import ru.mobilization.sinjvf.yamblzweather.BuildConfig;
import ru.mobilization.sinjvf.yamblzweather.retrofit.BaseResponseCallback;
import ru.mobilization.sinjvf.yamblzweather.retrofit.NonNullResp;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;

/**
 * Created by Sinjvf on 09.07.2017.
 * Parent class for fragment's ViewModels
 */

public abstract class BaseFragmentViewModel extends AndroidViewModel {

    protected final String TAG = "tag:" + getClass().getSimpleName();
    protected MutableLiveData<Integer> titleId;
    protected final Context context;
    protected FragmentManager fragmentManager;

    protected MutableLiveData<Integer> progres;
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

    public MutableLiveData<Integer> getProgress() {
        if (progres == null){
            progres = new MutableLiveData<>();
            progres.setValue(Utils.PROGRESS_SUCCESS);
        }
        return progres;
    }

    protected void setToolbarText() {
        titleId.setValue(getTitleStringId());
    }


    protected abstract int getTitleStringId();

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    protected <T> BaseResponseCallback<T> getResponseCallback(NonNullResp<T> nonNullResp, SingleObserver<Integer> action) {
        return new BaseResponseCallback<T>(context, fragmentManager, nonNullResp, action);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacksAndMessages(null);
    }

    protected SingleObserver<Integer> progressObserver(){
        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {}

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Integer aInt) {
                progres.setValue(aInt);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {}
        };
    }
}
