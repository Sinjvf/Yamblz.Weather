package ru.mobilization.sinjvf.yamblzweather.fragments.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;

/**
 * Created by Sinjvf on 09.07.2017.
 * Fragment for weather screen - main application screen
 */

public class MainFragment extends BaseFragment{
    MainViewModel localModel;
    @BindView(R.id.main_tempr)
    TextView mainTempr;
    @BindView(R.id.min_tempr)
    TextView minTempr;
    @BindView(R.id.max_tempr)
    TextView maxTempr;
    @BindView(R.id.last_updated)
    TextView lastUpdated;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fr_main, container, false);
    }

    public void setWeather(WeatherResponse resp){
        Integer mainInt = Utils.getDataWithoutException(() -> resp.getMain().getTemp().intValue());
        Integer minInt = Utils.getDataWithoutException(() -> resp.getMain().getTempMin().intValue());
        Integer maxInt = Utils.getDataWithoutException(() -> resp.getMain().getTempMax().intValue());
        mainTempr.setText(String.format(getString(R.string.main_tempr), mainInt));
        minTempr.setText(String.format(getString(R.string.min_tempr), minInt));
        maxTempr.setText(String.format(getString(R.string.max_tempr), maxInt));

        Preferenses.setPrefLastTimeUpdate(getContext());
        lastUpdated.setText(String.format(getString(R.string.last_update), Utils.lastUpdateString(getContext())));

    }


}

