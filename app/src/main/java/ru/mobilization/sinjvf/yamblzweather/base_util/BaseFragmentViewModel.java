package ru.mobilization.sinjvf.yamblzweather.base_util;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ru.mobilization.sinjvf.yamblzweather.retrofit.BaseResponseCallback;
import ru.mobilization.sinjvf.yamblzweather.retrofit.NonNullResp;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * Parent class for fragment's ViewModels
 */

public abstract class BaseFragmentViewModel extends AndroidViewModel {

    protected final String TAG = "tag:" + getClass().getSimpleName();
    protected MutableLiveData<Integer> titleId;
    protected final Context context;
    protected FragmentManager fragmentManager;

    protected MutableLiveData<Integer> progress;
    protected final Handler handler = new Handler();

    public BaseFragmentViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        Timber.tag(TAG);
    }

    public LiveData<Integer> getTitle() {
        if (titleId == null) {
            titleId = new MutableLiveData<>();
            setToolbarText();
        }
        return titleId;
    }

    public MutableLiveData<Integer> getProgress() {
        if (progress == null){
            progress = new MutableLiveData<>();
            progress.setValue(Utils.PROGRESS_SUCCESS);
        }
        return progress;
    }

    protected void setToolbarText() {
        titleId.setValue(getTitleStringId());
    }


    protected abstract @StringRes int getTitleStringId();

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
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onSuccess(@NonNull Integer aInt) {
                progress.setValue(aInt);
            }

            @Override
            public void onError(@NonNull Throwable e) {}
        };
    }
}
