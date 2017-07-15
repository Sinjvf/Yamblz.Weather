package ru.mobilization.sinjvf.yamblzweather.fragments.about;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mobilization.sinjvf.yamblzweather.BuildConfig;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;

/**
 * Created by Sinjvf on 09.07.2017.
 */

public class AboutFragment extends BaseFragment {
    AboutViewModel localModel;
    @BindView(R.id.vers_name)
    TextView versionName;


    public static AboutFragment getInstance() {
        return new AboutFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        localModel = ViewModelProviders.of(getActivity()).get(AboutViewModel.class);
        baseModel = localModel;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fr_about, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setVers();
        return rootView;
    }

    private void setVers() {
        String name = getString(R.string.about_version) + " " + BuildConfig.VERSION_NAME;
        versionName.setText(name);
    }


}

