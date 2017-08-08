package ru.exwhythat.yather.screens.weather;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import ru.exwhythat.yather.data.local.entities.City;

/**
 * Created by exwhythat on 8/8/17.
 */

public class CitiesDiffUtilCallback extends DiffUtil.Callback{

    List<City> oldCities;
    List<City> newCities;

    public CitiesDiffUtilCallback(List<City> oldCities, List<City> newCities) {
        this.oldCities = oldCities;
        this.newCities = newCities;
    }

    @Override
    public int getOldListSize() {
        return oldCities.size();
    }

    @Override
    public int getNewListSize() {
        return newCities.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCities.get(oldItemPosition).getApiCityId() == newCities.get(newItemPosition).getApiCityId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCities.get(oldItemPosition).equals(newCities.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
