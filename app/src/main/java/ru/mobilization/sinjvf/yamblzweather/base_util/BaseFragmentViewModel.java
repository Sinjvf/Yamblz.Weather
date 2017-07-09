package ru.mobilization.sinjvf.yamblzweather.base_util;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by Sinjvf on 09.07.2017.
 */

abstract public class BaseFragmentViewModel extends AndroidViewModel {

    protected MutableLiveData<String> titleName;

    public BaseFragmentViewModel(Application application) {
        super(application);
    }

    public LiveData<String> getTitle() {
        if (titleName == null) {
            titleName = new MutableLiveData<>();
            setToolbarText();
        }
        return titleName;
    }

    protected void setToolbarText(){
        String text = getTitleText();
        titleName.setValue(text);
    }


    protected abstract String getTitleText();
}
