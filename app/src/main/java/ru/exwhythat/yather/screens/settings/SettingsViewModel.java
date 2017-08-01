package ru.exwhythat.yather.screens.settings;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import ru.exwhythat.yather.R;
import ru.exwhythat.yather.base_util.BaseFragmentViewModel;
import ru.exwhythat.yather.ui.SelectCityDialogFragment;
import ru.exwhythat.yather.ui.SelectIntervalDialogFragment;
import ru.exwhythat.yather.utils.Preferenses;

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
    protected MutableLiveData<CityInfo> cityInfo;

    @NonNull
    public LiveData<Long> getInterval() {
        if (interval == null) {
            long savedInterval = Preferenses.getIntervalTime(context);
            interval = new MutableLiveData<>();
            interval.setValue(savedInterval);
        }
        return interval;
    }

    @NonNull
    public LiveData<CityInfo> getCityInfo() {
        if (cityInfo == null) {
            CityInfo savedCityInfo = Preferenses.getCityInfo(context);
            cityInfo = new MutableLiveData<>();
            cityInfo.setValue(savedCityInfo);
        }
        return cityInfo;
    }

    @Override
    protected int getTitleStringId() {
        return R.string.menu_tools;
    }

    public void selectIntervalClicked(){
        new SelectIntervalDialogFragment().show(fragmentManager, null);
    }

    public void selectCityClicked() {
        new SelectCityDialogFragment().show(fragmentManager, null);
    }

    public void updateCityInfo(CityInfo newCityInfo) {
        Preferenses.setCityInfo(context, newCityInfo);
        if (cityInfo == null) cityInfo = new MutableLiveData<>();
        cityInfo.setValue(newCityInfo);
    }

    public void updateInterval(Long newInterval) {
        Preferenses.setIntervalTime(context, newInterval);
        if (interval == null) interval = new MutableLiveData<>();
        interval.setValue(newInterval);
    }
}
