package ru.exwhythat.yather.screens.weather;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.exwhythat.yather.R;
import ru.exwhythat.yather.data.local.entities.ForecastWeather;
import ru.exwhythat.yather.utils.DateUtils;

/**
 * Created by exwhythat on 8/8/17.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> implements LifecycleObserver {

    private Context context;
    private List<ForecastWeather> forecast = new ArrayList<>();
    private OnForecastClickListener clickListener;

    private ForecastDiffUtilCallback diffUtilCallback;

    public ForecastAdapter(Context context, LifecycleOwner lifecycleOwner, OnForecastClickListener listener) {
        this.context = context;
        this.clickListener = listener;
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_list_item_small, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        final ForecastWeather forecast = this.forecast.get(position);
        String dateString = DateUtils.dateToString(forecast.getBaseWeather().getDate());
        holder.forecastDate.setText(capitalizeFirstLetter(dateString));
        holder.forecastInfoMain.setText(forecast.getBaseWeather().getMain());
        holder.forecastInfoDescr.setText(forecast.getBaseWeather().getDescr());
        holder.forecastDayTemp.setText(String.format(Locale.getDefault(), context.getString(R.string.tempr_float),
                forecast.getDayTemp()));
        holder.forecastNightTemp.setText(String.format(Locale.getDefault(), context.getString(R.string.tempr_float),
                forecast.getNightTemp()));
        holder.itemView.setOnClickListener(view -> clickListener.onForecastClicked(forecast.getForecastId()));
    }

    private String capitalizeFirstLetter(String word) {
        if (!TextUtils.isEmpty(word)) {
            StringBuilder sb = new StringBuilder(word);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        } else {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return forecast.size();
    }

    public void updateForecast(List<ForecastWeather> newForecast) {
        diffUtilCallback = new ForecastDiffUtilCallback(forecast, newForecast);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        forecast = newForecast;
        diffResult.dispatchUpdatesTo(this);
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.forecastDate)
        TextView forecastDate;
        @BindView(R.id.forecastInfoMain)
        TextView forecastInfoMain;
        @BindView(R.id.forecastInfoDescr)
        TextView forecastInfoDescr;
        @BindView(R.id.forecastDayTemp)
        TextView forecastDayTemp;
        @BindView(R.id.forecastNightTemp)
        TextView forecastNightTemp;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnForecastClickListener {
        void onForecastClicked(int forecastItemId);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void clear() {
        context = null;
        clickListener = null;
        diffUtilCallback = null;
    }
}
