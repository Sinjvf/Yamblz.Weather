package ru.mobilization.sinjvf.yamblzweather.fragments.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;

/**
 * Created by Sinjvf on 09.07.2017.
 * Fragment for settings screen
 */

public class SettingsFragment extends BaseFragment{
    SettingsViewModel localModel;
    @BindView(R.id.interval_text)
    TextView intervalView;


    public static SettingsFragment getInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        localModel = ViewModelProviders.of(getActivity()).get(SettingsViewModel.class);
        baseModel = localModel;
        localModel.getInterval().observe(this, this::setInterval);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fr_settings, container, false);
    }


    @OnClick(R.id.select_interval)
    public void selectIntervalClicked(){
        localModel.selectClicked();
    }

    @OnClick(R.id.interval_text)
    public void selectIntervalTextClicked(){
        localModel.selectClicked();
    }

    public void setInterval(Long interval){
        int minutes = (int)(interval/ Utils.MINUTE);
        intervalView.setText(String.format(getString(R.string.n_min), minutes));
    }



}

