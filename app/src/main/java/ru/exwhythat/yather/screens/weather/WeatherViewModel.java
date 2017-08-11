package ru.exwhythat.yather.screens.weather;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Keep;

import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.base_util.BaseFragmentViewModel;
import ru.exwhythat.yather.base_util.livedata.Resource;
import ru.exwhythat.yather.data.local.entities.City;
import ru.exwhythat.yather.data.local.entities.CityWithWeather;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.data.repository.WeatherCachingRepository;
import ru.exwhythat.yather.screens.settings.CityInfo;
import ru.exwhythat.yather.utils.Prefs;
import ru.exwhythat.yather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * ViewModel for main fragment
 */
@Keep
public class WeatherViewModel extends BaseFragmentViewModel {

    private WeatherCachingRepository weatherRepo;

    private Disposable weatherSubscription = Disposables.disposed();
    private Disposable forecastSubscription = Disposables.disposed();
    private Disposable citiesSubscription = Disposables.disposed();

    protected MutableLiveData<Resource<List<CityWithWeather>>> citiesWithWeather = new MutableLiveData<>();
    protected MutableLiveData<Resource<CurrentWeather>> weather = new MutableLiveData<>();
    protected MutableLiveData<Resource<List<ForecastWeather>>> forecast = new MutableLiveData<>();
    protected MutableLiveData<String> lastUpdateTime = new MutableLiveData<>();

    @Inject
    public WeatherViewModel(Application application, WeatherCachingRepository weatherRepo) {
        super(application);
        this.weatherRepo = weatherRepo;
    }

    public LiveData<Resource<CurrentWeather>> getWeather() {
        setLiveLoading(weather);
        loadWeatherFromRepo();
        return weather;
    }

    public LiveData<Resource<List<ForecastWeather>>> getForecast() {
        setLiveLoading(forecast);
        loadForecastFromRepo();
        return forecast;
    }

    public LiveData<Resource<List<CityWithWeather>>> getCitiesWithWeather() {
        setLiveLoading(citiesWithWeather);
        loadCitiesWithWeatherFromRepo();
        return citiesWithWeather;
    }

    public LiveData<String> getLastUpdate() {
        lastUpdateTime.setValue(Utils.lastUpdateString(context));
        return lastUpdateTime;
    }

    public void onCitySelected(int cityId) {
        Single.just(cityId)
                .map(id -> weatherRepo.getCityById(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateCity, this::onError);
    }

    private void updateCity(City city) {
        Prefs.setSelectedCity(context, new CityInfo(city.getName(), city.getCityId()));
        loadWeatherFromRepo();
        loadForecastFromRepo();
    }

    @Override
    protected int getTitleStringId() {
        return R.string.menu_weather;
    }

    public void loadWeatherFromRepo() {
        weatherSubscription = weatherRepo.getWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newWeather -> setLiveSuccess(weather, newWeather),
                        err -> setLiveError(weather, err));
    }

    public void loadForecastFromRepo() {
        forecastSubscription = weatherRepo.getForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newForecast -> setLiveSuccess(forecast, newForecast),
                        err -> setLiveError(forecast, err));
    }

    private void loadCitiesWithWeatherFromRepo() {
        citiesSubscription = weatherRepo.getCitiesWithWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cities -> setLiveSuccess(citiesWithWeather, cities),
                        err -> setLiveError(citiesWithWeather, err));
    }

    public void forceUpdateWeatherAndForecast() {
        forceUpdateWeather();
        forceUpdateForecast();
    }

    private void forceUpdateWeather() {
        weatherSubscription.dispose();
        weatherSubscription = weatherRepo.getFreshWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateWeatherAndLastUpdateTime,
                        err -> setLiveError(weather, err));
    }

    private void forceUpdateForecast() {
        forecastSubscription.dispose();
        forecastSubscription = weatherRepo.getFreshForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newForecast -> setLiveSuccess(forecast, newForecast),
                        err -> setLiveError(forecast, err));
    }

    private void updateWeatherAndLastUpdateTime(CurrentWeather newWeather) {
        setLiveSuccess(weather, newWeather);
        updateLastUpdateTime();
    }

    private void updateLastUpdateTime() {
        Prefs.setPrefLastTimeUpdate(context);
        lastUpdateTime.setValue(Utils.lastUpdateString(context));
    }

    private void onError(Throwable throwable) {
        Timber.e(throwable);
    }

    private <T> void setLiveSuccess(MutableLiveData<Resource<T>> mutableLiveData, T value) {
        mutableLiveData.setValue(Resource.success(value));
    }

    private <T> void setLiveError(MutableLiveData<Resource<T>> mutableLiveData, Throwable e) {
        String msg;
        if (e instanceof UnknownHostException) {
            msg = context.getString(R.string.error_network);
        } else {
            msg = context.getString(R.string.error_oops);
        }
        mutableLiveData.setValue(Resource.error(msg));
    }

    private <T> void setLiveLoading(MutableLiveData<Resource<T>> mutableLiveData) {
        mutableLiveData.setValue(Resource.loading());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        weatherSubscription.dispose();
        forecastSubscription.dispose();
        citiesSubscription.dispose();
    }
}
