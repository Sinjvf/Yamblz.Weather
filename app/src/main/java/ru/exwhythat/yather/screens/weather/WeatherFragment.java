package ru.exwhythat.yather.screens.weather;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.di.Injectable;
import ru.exwhythat.yather.utils.Prefs;
import ru.exwhythat.yather.base_util.BaseFragment;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * Fragment for weather screen - main application screen
 */

public class WeatherFragment extends BaseFragment implements Injectable, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.city_name)
    TextView cityNameView;
    @BindView(R.id.main_tempr)
    TextView mainTempr;
    @BindView(R.id.min_tempr)
    TextView minTempr;
    @BindView(R.id.max_tempr)
    TextView maxTempr;
    @BindView(R.id.last_updated)
    TextView lastUpdatedView;
    @BindView(R.id.image_weather)
    ImageView imageWeather;
    @BindView(R.id.wind_text)
    TextView windView;
    @BindView(R.id.humidity_text)
    TextView humidityView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.main_layout)

    View mainLayout;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private WeatherViewModel localModel;

    private int cityId;

    public static WeatherFragment getInstance(){
        return new WeatherFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        localModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherViewModel.class);
        baseModel = localModel;
        super.onActivityCreated(savedInstanceState);
        //cityId = Prefs.getSelectedCity(getContext()).getCityId();
        localModel.getWeatherForSelectedCity().observe(this, this::setWeather);
        localModel.getLastUpdate().observe(this, this::setLastUpdate);
    }

    @Override
    protected void getArgs() {
        cityId = Prefs.getSelectedCity(getContext()).getCityId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fr_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setWeather(CurrentWeather weather){
        if (weather == null) {
            setIsLoading(true);
            return;
        }
        setWeatherDataToViews(weather);
        loadWeatherIcon(weather.getBaseWeather().getIconId());
        setIsLoading(false);
        Timber.d("setWeather: " + weather);
    }

    private void setWeatherDataToViews(CurrentWeather weather) {
        String cityName = Prefs.getSelectedCity(getContext()).getCityName();
        cityNameView.setText(cityName);
        mainTempr.setText(getString(R.string.main_tempr, (int)weather.getBaseWeather().getTemp()));
        windView.setText(getString(R.string.wind, (int)weather.getWindSpeed()));
        humidityView.setText(getString(R.string.percent, (int)weather.getHumidity()));
    }

    private void loadWeatherIcon(String imageName) {
        if (imageName != null) {
            String imageFullPath = String.format(getString(R.string.image_path), imageName);
            Picasso.with(getContext())
                    .load(imageFullPath)
                    .fit()
                    .into(imageWeather);
        }
    }

    private void setLastUpdate(String lastUpdate){
        lastUpdatedView.setText(String.format(getString(R.string.last_update), lastUpdate));
    }

    @Override
    public void onRefresh() {
        localModel.forceUpdateCurrentWeather();
    }

    private void setIsLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
        mainLayout.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}

