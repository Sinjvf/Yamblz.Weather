package ru.mobilization.sinjvf.yamblzweather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import ru.mobilization.sinjvf.yamblzweather.R;
import timber.log.Timber;

/**
 * Created by Sinjvf on 16.07.2017.
 * Class for showing dialogs to user
 */

public class SelectCityDialogFragment extends DialogFragment {

    @BindView(R.id.autocomplete_cities)
    AutoCompleteTextView autoCompleteTextView;

    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter adapter;

    Unbinder unbinder;
    private SingleObserver<Place> action;

    public void setAction(SingleObserver<Place> action) {
        this.action = action;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.el_dialog_select_city, container, false);
        unbinder = ButterKnife.bind(this, v);
        getDialog().setTitle(R.string.settings_city_selection_title);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareAutocomplete();
        showKeyboard();
    }

    private void showKeyboard() {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private void prepareAutocomplete(){
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .build();

        AutocompleteFilter cityFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        adapter = new PlaceAutocompleteAdapter(getContext(), googleApiClient, null, cityFilter);

        autoCompleteTextView.setOnItemClickListener(autocompleteClickListener);
        autoCompleteTextView.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener autocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final AutocompletePrediction item = adapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(updatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> updatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                String errorMsg = String.format(getString(R.string.error_with_explanation), places.getStatus().toString());
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                Timber.e("Place query did not complete. Error: " + places.getStatus().toString());
                releaseAndDismiss(places);
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            Timber.i("Place details received: " + "Name=" + place.getName() +
                    ", coords=[" + place.getLatLng().latitude + ", " + place.getLatLng().longitude + "]");
            if (action != null) {
                Single.just(place)
                        .doFinally(() -> releaseAndDismiss(places))
                        .subscribe(action);
            } else {
                releaseAndDismiss(places);
            }
        }

        private void releaseAndDismiss(PlaceBuffer places) {
            places.release();
            dismiss();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    public void onPause() {
        googleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
