package ru.mobilization.sinjvf.yamblzweather.base_util;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

/**
 * Created by Sinjvf on 09.07.2017.
 */

abstract public class BaseFragmentViewModel extends AndroidViewModel {

    protected MutableLiveData<Integer> titleId;

    public BaseFragmentViewModel(Application application) {
        super(application);
    }

    public LiveData<Integer> getTitle() {
        if (titleId == null) {
            titleId = new MutableLiveData<>();
            setToolbarText();
        }
        return titleId;
    }

    protected void setToolbarText(){
        titleId.setValue(getTitleStringId());
    }


    protected abstract int getTitleStringId();
}
