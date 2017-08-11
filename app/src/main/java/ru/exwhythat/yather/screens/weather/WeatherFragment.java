package ru.exwhythat.yather.screens.weather;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.activity.MainActivity;
import ru.exwhythat.yather.base_util.livedata.Resource;
import ru.exwhythat.yather.base_util.livedata.Status;
import ru.exwhythat.yather.data.local.entities.CityWithWeather;
import ru.exwhythat.yather.data.local.entities.CurrentWeather;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.di.Injectable;
import ru.exwhythat.yather.utils.Prefs;
import ru.exwhythat.yather.base_util.BaseFragment;
import ru.exwhythat.yather.utils.StringUtils;
import timber.log.Timber;

import static ru.exwhythat.yather.data.remote.model.DailyForecastResponse.*;

/**
 * Created by Sinjvf on 09.07.2017.
 * Fragment for weather screen - main application screen
 */

public class WeatherFragment extends BaseFragment implements Injectable, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "WeatherFragment";

    @BindView(R.id.city_name)
    TextView cityNameView;
    @BindView(R.id.main_tempr)
    TextView mainTempr;
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
    @BindView(R.id.citiesRecycler)
    RecyclerView citiesRecycler;
    @BindView(R.id.forecastRecycler)
    RecyclerView forecastRecycler;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private CitiesAdapter citiesAdapter;
    private ForecastAdapter forecastAdapter;

    private WeatherViewModel localModel;

    private int cityId;

    private boolean isTwoPane;

    public static WeatherFragment getInstance(){
        return new WeatherFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        localModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherViewModel.class);
        baseModel = localModel;
        super.onActivityCreated(savedInstanceState);
        isTwoPane = ((MainActivity)getActivity()).getIsTwoPane();

        localModel.getWeather().observe(this, this::setWeather);
        localModel.getLastUpdate().observe(this, this::setLastUpdate);

        initRecyclers();
        localModel.getCitiesWithWeather().observe(this, this::setCities);
        localModel.getForecast().observe(this, this::setForecast);
    }

    private void initRecyclers() {
        citiesAdapter = new CitiesAdapter(getContext(), this, this::onCitySelected);
        int citiesOrientation = isTwoPane ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL;
        initRecycler(citiesRecycler, citiesAdapter, citiesOrientation);

        forecastAdapter = new ForecastAdapter(getContext(), this, this::onForecastClicked);
        initRecycler(forecastRecycler, forecastAdapter, LinearLayoutManager.VERTICAL);
        forecastRecycler.setNestedScrollingEnabled(false);
    }

    private void onCitySelected(int cityId) {
        localModel.onCitySelected(cityId);
    }

    private void onForecastClicked(int forecastItemId) {
        Toast.makeText(getContext(), "Forecast clicked: "  + forecastItemId, Toast.LENGTH_SHORT).show();
    }

    private void initRecycler(RecyclerView rv, RecyclerView.Adapter adapter, int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), orientation, false);
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                layoutManager.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        rv.setAdapter(adapter);
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

    private void setWeather(Resource<CurrentWeather> weatherRes) {
        if (weatherRes.status == Status.SUCCESS) {
            setWeatherDataToViews(weatherRes.data);
            setIsLoading(false);
        } else if (weatherRes.status == Status.LOADING) {
            setIsLoading(true);
            return;
        } else if (weatherRes.status == Status.ERROR) {
            Toast.makeText(getContext(), weatherRes.message, Toast.LENGTH_SHORT).show();
            setIsLoading(false);
        }

        Timber.d("setWeather: " + weatherRes);
    }

    private void setForecast(Resource<List<ForecastWeather>> forecastRes) {
        if (forecastRes.status == Status.SUCCESS) {
            forecastAdapter.updateForecast(forecastRes.data);
            setIsLoading(false);
        } else if (forecastRes.status == Status.LOADING) {
            setIsLoading(true);
            return;
        } else if (forecastRes.status == Status.ERROR) {
            Toast.makeText(getContext(), forecastRes.message, Toast.LENGTH_SHORT).show();
            setIsLoading(false);
        }

        Timber.d("setForecast: " + forecastRes);
    }

    private void setCities(Resource<List<CityWithWeather>> citiesRes) {
        if (citiesRes.status == Status.SUCCESS) {
            citiesAdapter.updateCities(citiesRes.data);
            setIsLoading(false);
        } else if (citiesRes.status == Status.LOADING) {
            setIsLoading(true);
            return;
        } else if (citiesRes.status == Status.ERROR) {
            Toast.makeText(getContext(), citiesRes.message, Toast.LENGTH_SHORT).show();
            setIsLoading(false);
        }

        Timber.d("setCities: " + citiesRes);
    }

    private void setWeatherDataToViews(CurrentWeather weather) {
        String cityName = Prefs.getSelectedCity(getContext()).getCityName();
        cityNameView.setText(cityName);

        @WeatherState String weatherState = weather.getBaseWeather().getMain();
        imageWeather.setImageDrawable(ContextCompat.getDrawable(getContext(), getDrawableResIdForWeatherState(weatherState)));

        mainTempr.setText(StringUtils.getFormattedTemperature(getContext(), weather.getTemp()));
        windView.setText(StringUtils.getFormattedWind(getContext(), weather.getWindSpeed()));
        humidityView.setText(StringUtils.getFormattedHumidity(getContext(), weather.getHumidity()));
    }

    public static @DrawableRes int getDrawableResIdForWeatherState(@WeatherState String weatherState) {
        switch (weatherState) {
            case WeatherState.clear:
                return R.drawable.ic_sun_big;
            case WeatherState.rain:
                return R.drawable.ic_rain_big;
            case WeatherState.clouds:
                return R.drawable.ic_cloud_big;
            case WeatherState.snow:
                return R.drawable.ic_snow_big;
            case WeatherState.storm:
                return R.drawable.ic_storm_big;
            case WeatherState.fog:
                return R.drawable.ic_fog;
            default:
                Timber.e("Unknown weather state: " + weatherState);
                return R.drawable.ic_help_outline;
        }
    }

    private void setLastUpdate(String lastUpdate){
        lastUpdatedView.setText(String.format(getString(R.string.last_update), lastUpdate));
    }

    @Override
    public void onRefresh() {
        localModel.forceUpdateWeatherAndForecast();
    }

    private void setIsLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }
}

