package ru.exwhythat.yather.screens.settings;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.base_util.BaseFragmentViewModel;
import ru.exwhythat.yather.data.local.entities.City;
import ru.exwhythat.yather.data.repository.LocalWeatherRepository;
import ru.exwhythat.yather.ui.SelectCityDialogFragment;
import ru.exwhythat.yather.ui.SelectIntervalDialogFragment;
import ru.exwhythat.yather.utils.Prefs;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * View model sor settings fragment
 */
@Keep
public class SettingsViewModel extends BaseFragmentViewModel {

    private LocalWeatherRepository localRepo;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public SettingsViewModel(Application application, LocalWeatherRepository localRepo) {
        super(application);
        this.localRepo = localRepo;
    }

    protected MutableLiveData<Long> interval;
    protected MutableLiveData<CityInfo> cityInfo;

    @NonNull
    public LiveData<Long> getInterval() {
        if (interval == null) {
            long savedInterval = Prefs.getIntervalTime(context);
            interval = new MutableLiveData<>();
            interval.setValue(savedInterval);
        }
        return interval;
    }

    @NonNull
    public LiveData<CityInfo> getCityInfo() {
        if (cityInfo == null) {
            CityInfo savedCityInfo = Prefs.getSelectedCity(context);
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

    public void updateSelectedCity(Single<CityInfo> singleCityInfo) {
        singleCityInfo
                .doOnSuccess(this::writeCityToStorage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCityUpdated, this::onCityError);
    }

    private void onCityUpdated(CityInfo newCityInfo) {
        if (cityInfo == null) cityInfo = new MutableLiveData<>();
        cityInfo.setValue(newCityInfo);
    }

    private void onCityError(Throwable throwable) {
        Timber.e(throwable);
    }

    private void writeCityToStorage(CityInfo cityInfo) {
        Prefs.setSelectedCity(context, cityInfo);
        City city = CityInfo.Mapper.toCity(cityInfo);
        localRepo.addNewCity(city);
    }

    public void updateInterval(Long newInterval) {
        Prefs.setIntervalTime(context, newInterval);
        if (interval == null) interval = new MutableLiveData<>();
        interval.setValue(newInterval);
    }

    @Override
    protected void onCleared() {

        super.onCleared();
    }
}
