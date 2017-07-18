package ru.mobilization.sinjvf.yamblzweather.activity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;


import ru.mobilization.sinjvf.yamblzweather.events.OpenNewFragmentEvent;
import ru.mobilization.sinjvf.yamblzweather.fragments.main.MainFragment;

/**
 * Created by Sinjvf on 11.07.2017.
 * ViewModel for Main Activity. Contains current fragment
 */
@Keep
public class MainActivityViewModel extends AndroidViewModel {
    public MainActivityViewModel(Application application) {
        super(application);
    }


    private MutableLiveData<OpenNewFragmentEvent> currentFragment;

    public LiveData<OpenNewFragmentEvent> getCurrentFragment() {
        if (currentFragment == null) {
            currentFragment = new MutableLiveData<>();
            currentFragment.setValue(new OpenNewFragmentEvent(MainFragment.getInstance(), false));
        }
        return currentFragment;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }


    public void changeFragmentEvent(OpenNewFragmentEvent event) {
        currentFragment.setValue(event);
    }
}