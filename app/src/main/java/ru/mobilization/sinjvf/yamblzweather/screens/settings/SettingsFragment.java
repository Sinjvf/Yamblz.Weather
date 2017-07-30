package ru.mobilization.sinjvf.yamblzweather.screens.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;

/**
 * Created by Sinjvf on 09.07.2017.
 * Fragment for settings screen
 */

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.interval_text)
    TextView intervalView;

    @BindView(R.id.city_name_title)
    TextView cityTitleView;
    @BindView(R.id.city_name_text)
    TextView cityNameView;
    @BindView(R.id.progress_city_selection)
    ProgressBar progressCitySelection;

    SettingsViewModel localModel;

    public static SettingsFragment getInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        localModel = ViewModelProviders.of(getActivity()).get(SettingsViewModel.class);
        baseModel = localModel;
        localModel.getInterval().observe(this, this::setInterval);
        localModel.getCityInfo().observe(this, this::setCityInfo);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fr_settings, container, false);
    }


    @OnClick(R.id.interval_selection)
    public void selectIntervalClicked(){
        localModel.selectIntervalClicked();
    }

    @OnClick(R.id.city_selection)
    public void selectCityClicked() {
        localModel.selectCityClicked();
    }

    public void setInterval(Long interval){
        int minutes = (int)(interval/ Utils.MINUTE);
        intervalView.setText(String.format(getString(R.string.n_min), minutes));
    }

    public void setCityInfo(CityInfo cityInfo){
        cityNameView.setText(cityInfo.getCityName());
    }

    @Override
    protected void setProgressStatus(int status) {
        switch (status) {
            case Utils.PROGRESS_SHOW:
                cityTitleView.setVisibility(View.GONE);
                cityNameView.setVisibility(View.GONE);
                progressCitySelection.setVisibility(View.VISIBLE);
                break;
            case Utils.PROGRESS_FAIL:
            case Utils.PROGRESS_SUCCESS:
                cityTitleView.setVisibility(View.VISIBLE);
                cityNameView.setVisibility(View.VISIBLE);
                progressCitySelection.setVisibility(View.GONE);
                break;
        }
    }
}