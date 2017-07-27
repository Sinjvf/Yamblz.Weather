package ru.mobilization.sinjvf.yamblzweather.ui;

/**
 * Created by exwhythat on 26.07.17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ru.mobilization.sinjvf.yamblzweather.R;
import timber.log.Timber;

public class PlaceAutocompleteAdapter
        extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);

    private ArrayList<AutocompletePrediction> resultList;

    private GoogleApiClient googleApiClient;
    private LatLngBounds bounds;
    private AutocompleteFilter placeFilter;
    private AutocompleteCallbackListener callbackListener;

    public PlaceAutocompleteAdapter(@NonNull Context context,
                                    @NonNull GoogleApiClient googleApiClient,
                                    @Nullable LatLngBounds bounds,
                                    @Nullable AutocompleteFilter filter,
                                    @NonNull AutocompleteCallbackListener callbackListener) {
        super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        this.googleApiClient = googleApiClient;
        this.bounds = bounds;
        this.placeFilter = filter;
        this.callbackListener = callbackListener;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public AutocompletePrediction getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = super.getView(position, convertView, parent);

        AutocompletePrediction item = getItem(position);
        TextView textView1 = row.findViewById(android.R.id.text1);
        TextView textView2 = row.findViewById(android.R.id.text2);
        textView1.setText(item.getPrimaryText(STYLE_BOLD));
        textView2.setText(item.getSecondaryText(STYLE_BOLD));

        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                ArrayList<AutocompletePrediction> filterData = new ArrayList<>();
                if (constraint != null) {
                    filterData = getAutocomplete(constraint);
                }

                results.values = filterData;
                if (filterData != null) {
                    results.count = filterData.size();
                } else {
                    results.count = 0;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (ArrayList<AutocompletePrediction>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView when clicked.
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getPrimaryText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        callbackListener.onHideError();
        callbackListener.onShowLoading();
        if (googleApiClient.isConnected()) {
            Timber.d("Starting autocomplete query for: " + constraint);

            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi.getAutocompletePredictions(googleApiClient, constraint.toString(), bounds, placeFilter);

            // This method should have been called off the main UI thread
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(5, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                callbackListener.onError(R.string.error_connection);
                Timber.e("Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            int predictionsCount = autocompletePredictions.getCount();
            if (predictionsCount == 0) {
                callbackListener.onError(R.string.city_autocomplete_zero_predictions);
            }
            Timber.i("Query completed. Received " + predictionsCount + " predictions.");

            callbackListener.onHideLoading();
            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        } else {
            callbackListener.onError(R.string.error);
            Timber.e("Google API client is not connected for autocomplete query.");
            return null;
        }
    }

    public interface AutocompleteCallbackListener {
        void onError(String errorMsg);
        void onError(@StringRes int stringResId);
        void onHideError();
        void onShowLoading();
        void onHideLoading();
    }
}
