package ru.exwhythat.yather.screens.weather;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.data.local.entities.City;

/**
 * Created by exwhythat on 8/8/17.
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> implements LifecycleObserver {

    private Context context;
    private List<City> plainCities = new ArrayList<>();
    private OnCitySelectionListener selectionListener;

    private CitiesDiffUtilCallback diffUtilCallback;

    public CitiesAdapter(Context context, LifecycleOwner lifecycleOwner, OnCitySelectionListener listener) {
        this.context = context;
        this.selectionListener = listener;
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_list_item_small, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        final City city = plainCities.get(position);
        holder.cityName.setText(city.getName());
        holder.itemView.setOnClickListener(view -> selectionListener.onCitySelected(city.getApiCityId()));
    }

    @Override
    public int getItemCount() {
        return plainCities.size();
    }

    public void updateCities(List<City> newCities) {
        diffUtilCallback = new CitiesDiffUtilCallback(plainCities, newCities);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        plainCities = newCities;
        diffResult.dispatchUpdatesTo(CitiesAdapter.this);
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_list_item_name)
        TextView cityName;

        public CityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnCitySelectionListener {
        void onCitySelected(int cityId);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void clear() {
        context = null;
        selectionListener = null;
        diffUtilCallback = null;
    }
}
