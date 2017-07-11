package ru.mobilization.sinjvf.yamblzweather.activity;

import android.app.Application;
import android.app.Fragment;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.app.FragmentManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.events.OpenNewFragment;
import ru.mobilization.sinjvf.yamblzweather.fragments.main.MainFragment;

/**
 * Created by Sinjvf on 11.07.2017.
 */

public class MainActivityViewModel extends AndroidViewModel {
    public MainActivityViewModel(Application application) {
        super(application);
        EventBus.getDefault().register(this);
    }


    private MutableLiveData<OpenNewFragment> currentFragment;

    public LiveData<OpenNewFragment> getCurrentFragment() {
        if (currentFragment == null) {
            currentFragment = new MutableLiveData<>();
            currentFragment.setValue(new OpenNewFragment(MainFragment.getInstance(), false));
        }
        return currentFragment;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OpenNewFragment event) {
        currentFragment.setValue(event);
    }
}
