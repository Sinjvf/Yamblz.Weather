package ru.mobilization.sinjvf.yamblzweather.screens.settings;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

import com.google.android.gms.location.places.Place;

import java.util.concurrent.TimeUnit;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragmentViewModel;
import ru.mobilization.sinjvf.yamblzweather.ui.SelectCityDialogFragment;
import ru.mobilization.sinjvf.yamblzweather.ui.SelectIntervalDialogFragment;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;
import timber.log.Timber;

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

    public LiveData<Long> getInterval() {
        if (interval == null) {
            long savedInterval = Preferenses.getIntervalTime(context);
            interval = new MutableLiveData<>();
            interval.setValue(savedInterval);
        }
        return interval;
    }

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

    public void updateCityInfo(Place place) {
        CityInfo newCityInfo = new CityInfo(place.getName().toString(), place.getLatLng());
        Preferenses.setCityInfo(context, newCityInfo);
        cityInfo.setValue(newCityInfo);

        Timber.d("New cityInfo has been set! " + newCityInfo.toString());
    }

    public void updateInterval(Long newInterval) {
        Preferenses.setIntervalTime(getApplication(), newInterval);
        interval.setValue(newInterval);
    }
}
