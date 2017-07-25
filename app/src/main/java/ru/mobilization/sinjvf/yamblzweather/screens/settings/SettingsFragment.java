package ru.mobilization.sinjvf.yamblzweather.screens.settings;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import butterknife.BindView;
import butterknife.OnClick;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * Fragment for settings screen
 */

public class SettingsFragment extends BaseFragment {

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @BindView(R.id.interval_text)
    TextView intervalView;

    @BindView(R.id.city_name_title)
    TextView cityTitleView;
    @BindView(R.id.city_name_text)
    TextView cityNameView;
    @BindView(R.id.progress_city_selection)
    ProgressBar progressCitySelection;

    SettingsViewModel localModel;

    public static SettingsFragment getInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        localModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        baseModel = localModel;
        localModel.getInterval().observe(this, this::setInterval);
        localModel.getCityInfo().observe(this, this::setCityInfo);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fr_settings, container, false);
    }


    @OnClick(R.id.interval_selection)
    public void selectIntervalClicked(){
        localModel.selectIntervalClicked();
    }

    @OnClick(R.id.city_selection)
    public void selectCityClicked() {
        //localModel.selectCityClicked();
        setProgressStatus(Utils.PROGRESS_SHOW);
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            setProgressStatus(Utils.PROGRESS_FAIL);
            Timber.e(e);
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(), 0).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            setProgressStatus(Utils.PROGRESS_FAIL);
            Timber.e(e);
            String errorMsg = String.format(getString(R.string.error_gp_service_not_available),
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode));
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            setProgressStatus(Utils.PROGRESS_SUCCESS);
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    localModel.selectCityClicked(place);
                    break;
                case PlaceAutocomplete.RESULT_ERROR:
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    Timber.e(status.getStatusMessage());
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Timber.i("City selection cancelled!");
                    break;
                default:
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setInterval(Long interval){
        int minutes = (int)(interval/ Utils.MINUTE);
        intervalView.setText(String.format(getString(R.string.n_min), minutes));
    }

    public void setCityInfo(CityInfo cityInfo){
        cityNameView.setText(cityInfo.getCityName());
    }

    @Override
    protected void setProgressStatus(int status) {
        switch (status) {
            case Utils.PROGRESS_SHOW:
                cityTitleView.setVisibility(View.GONE);
                cityNameView.setVisibility(View.GONE);
                progressCitySelection.setVisibility(View.VISIBLE);
                break;
            case Utils.PROGRESS_FAIL:
            case Utils.PROGRESS_SUCCESS:
                cityTitleView.setVisibility(View.VISIBLE);
                cityNameView.setVisibility(View.VISIBLE);
                progressCitySelection.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void getArgs() {
        
    }
}