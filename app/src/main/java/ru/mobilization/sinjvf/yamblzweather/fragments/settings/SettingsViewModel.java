package ru.mobilization.sinjvf.yamblzweather.fragments.settings;

import android.app.Application;
import android.support.annotation.Keep;

import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragmentViewModel;

/**
 * Created by Sinjvf on 09.07.2017.
 */
@Keep
public class SettingsViewModel extends BaseFragmentViewModel {
    public SettingsViewModel(Application application) {
        super(application);
    }

    @Override
    protected int getTitleStringId() {
        return R.string.menu_tools;
    }
}
