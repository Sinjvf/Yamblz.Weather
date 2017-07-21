package ru.mobilization.sinjvf.yamblzweather.screens.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * Fragment for weather screen - main application screen
 */

public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    MainViewModel localModel;
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


    public static MainFragment getInstance(){
        return new MainFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        localModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        baseModel = localModel;
        super.onActivityCreated(savedInstanceState);
        //before supermethod calling we have null fragment manager
        localModel.getWeatherData().observe(this, this::setWeather);
        localModel.getLastUpdate().observe(this, this::setLastUpdate);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fr_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void setWeather(WeatherResponse resp){
        Integer mainInt = Utils.getDataWithoutException(() -> resp.getMain().getTemp().intValue());
        Integer minInt = Utils.getDataWithoutException(() -> resp.getMain().getTempMin().intValue());
        Integer maxInt = Utils.getDataWithoutException(() -> resp.getMain().getTempMax().intValue());
        Integer humidity = Utils.getDataWithoutException(() -> resp.getMain().getHumidity());
        Integer wind = Utils.getDataWithoutException(() -> resp.getWind().getSpeed().intValue());
        mainTempr.setText(String.format(getString(R.string.main_tempr), mainInt));
        minTempr.setText(String.format(getString(R.string.min_tempr), minInt));
        maxTempr.setText(String.format(getString(R.string.max_tempr), maxInt));
        windView.setText(String.format(getString(R.string.wind), wind));
        humidityView.setText(String.format(getString(R.string.percent), humidity));



        String imageName = Utils.getDataWithoutException(() -> resp.getWeather().get(0).getIcon());
        if (imageName!=null) {
            String imageFullPath = String.format(getString(R.string.image_path), imageName);
            Picasso.with(getContext())
                    .load(imageFullPath)
                    .fit()
                    .into(imageWeather);
        }

        Timber.d(TAG, "setWeather: ");

    }
    private void setLastUpdate(String lastUpdate){
        lastUpdatedView.setText(String.format(getString(R.string.last_update), lastUpdate));
    }


    @Override
    public void onRefresh() {
        localModel.sendWeatherRequest();
    }

    protected void setProgressStatus(Integer status){
        switch (status){
            case Utils.PROGRESS_SUCCESS:
                swipeRefreshLayout.setRefreshing(false);
                mainLayout.setVisibility(View.VISIBLE);
                break;
            case Utils.PROGRESS_FAIL:
                swipeRefreshLayout.setRefreshing(false);
                mainLayout.setVisibility(View.GONE);
                break;

        }
    }
}

