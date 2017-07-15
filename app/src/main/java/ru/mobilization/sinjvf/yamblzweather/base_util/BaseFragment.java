package ru.mobilization.sinjvf.yamblzweather.base_util;

import android.arch.lifecycle.LifecycleFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.activity.MainActivity;
import ru.mobilization.sinjvf.yamblzweather.activity.MainActivityInterface;

/**
 * Created by Sinjvf on 09.07.2017.
 * Parent class for fragments
 */

public class BaseFragment extends LifecycleFragment {
    protected final String TAG = "tag:" + getClass().getSimpleName();
    protected BaseFragmentViewModel baseModel;
    protected Unbinder unbinder;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        baseModel.getTitle().observe(this, this::setTitleText);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    protected void setTitleText(int titleResId) {
        try {
            ((MainActivityInterface) getActivity()).setTitleText(titleResId);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
