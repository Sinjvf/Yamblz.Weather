package ru.mobilization.sinjvf.yamblzweather.screens.settings;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

import java.util.concurrent.TimeUnit;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragmentViewModel;
import ru.mobilization.sinjvf.yamblzweather.ui.SelectIntervalDialogFragment;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;

/**
 * Created by Sinjvf on 09.07.2017.
 * View model sor settings fragment
 */
@Keep
public class SettingsViewModel extends BaseFragmentViewModel {
    public SettingsViewModel(Application application) {
        super(application);
    }
    protected MutableLiveData<Long> interval;
    private final long DEFAULT_INTERVAL = TimeUnit.MINUTES.toMillis(Utils.TIME_10);

    public MutableLiveData<Long> getInterval() {
        if (interval == null){
            interval = new MutableLiveData<>();
            interval.setValue(DEFAULT_INTERVAL);
        }
        return interval;
    }



    @Override
    protected int getTitleStringId() {
        return R.string.menu_tools;
    }

    public void selectClicked(){
        SelectIntervalDialogFragment dialog = new SelectIntervalDialogFragment();
        dialog.setAction(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onSuccess(@NonNull Long aLong) {
                Preferenses.setIntervalTime(context, aLong);
                interval.setValue(aLong);
            }

            @Override
            public void onError(@NonNull Throwable e) {}
        });
        dialog.show(fragmentManager, null);
    }
}
