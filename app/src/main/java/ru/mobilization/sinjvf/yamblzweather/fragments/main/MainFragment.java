package ru.mobilization.sinjvf.yamblzweather.fragments.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.fragments.settings.SettingsViewModel;

/**
 * Created by Sinjvf on 09.07.2017.
 */

public class MainFragment extends BaseFragment{
    MainViewModel localModel;

    public static MainFragment getInstance(){
        return new MainFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        localModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        baseModel = localModel;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fr_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


}

