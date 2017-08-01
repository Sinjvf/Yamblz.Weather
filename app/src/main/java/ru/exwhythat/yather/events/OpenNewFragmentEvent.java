package ru.exwhythat.yather.events;

import ru.exwhythat.yather.base_util.BaseFragment;

/**
 * Created by Sinjvf on 09.07.2017.
 * Data class for changing fragment in main activity
 * If addToBackStack we add this fragment to backstack (It's unexpectedly, isn't it?=))
 */

public class OpenNewFragmentEvent {
    private final BaseFragment fgm;
    private final boolean addToBackStack;

    public OpenNewFragmentEvent(BaseFragment fgm, boolean addToBackStack) {
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
