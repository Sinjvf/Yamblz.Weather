package ru.mobilization.sinjvf.yamblzweather.events;

import android.os.Bundle;

import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;

/**
 * Created by Sinjvf on 09.07.2017.
 */

public class OpenNewFragment {
    BaseFragment fgm;
    boolean addToBackStack;

    public OpenNewFragment(BaseFragment fgm, boolean addToBackStack) {
        this.fgm = fgm;
        this.addToBackStack = addToBackStack;
    }

    public BaseFragment getFgm() {
        return fgm;
    }


    public boolean isAddToBackStack() {
        return addToBackStack;
    }


}
