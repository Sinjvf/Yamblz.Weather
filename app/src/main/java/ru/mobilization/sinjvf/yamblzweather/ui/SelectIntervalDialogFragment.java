package ru.mobilization.sinjvf.yamblzweather.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;
import timber.log.Timber;

/**
 * Created by Sinjvf on 16.07.2017.
 * Class for showing dialogs to user
 */

public class SelectIntervalDialogFragment extends DialogFragment {

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.radio_10)
    RadioButton radio10;
    @BindView(R.id.radio_15)
    RadioButton radio15;
    @BindView(R.id.radio_30)
    RadioButton radio30;
    @BindView(R.id.radio_60)
    RadioButton radio60;

    Unbinder unbinder;
    private SingleObserver<Long> action;
    public void setAction(SingleObserver<Long> action) {
        this.action = action;
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.el_dialog_select_interval, container, false);
        unbinder = ButterKnife.bind(this, v);
        try {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }catch (NullPointerException e){
            Timber.e(e.getMessage());
        }
        setData();
        return v;
    }


    private void setData(){

        radio10.setText(getString(R.string.n_min, Utils.TIME_10));
        radio15.setText(getString(R.string.n_min, Utils.TIME_15));
        radio30.setText(getString(R.string.n_min, Utils.TIME_30));
        radio60.setText(getString(R.string.n_min, Utils.TIME_60));
        switch ((int)(Preferenses.getIntervalTime(getContext())/Utils.MINUTE)){
            case Utils.TIME_15:
                radio15.setChecked(true);
                break;
            case Utils.TIME_30:
                radio30.setChecked(true);
                break;
            case Utils.TIME_60:
                radio60.setChecked(true);
                break;
            default:
                radio10.setChecked(true);
        }
    }

    @OnClick(R.id.ok)
    public void onClickOk() {
        long time = TimeUnit.MINUTES.toMillis(Utils.TIME_10);
        int id = radioGroup.getCheckedRadioButtonId();
        switch (id){
            case R.id.radio_10:
                time = TimeUnit.MINUTES.toMillis(Utils.TIME_10);
                break;
            case R.id.radio_15:
                time = TimeUnit.MINUTES.toMillis(Utils.TIME_15);
                break;
            case R.id.radio_30:
                time = TimeUnit.MINUTES.toMillis(Utils.TIME_30);
                break;
            case R.id.radio_60:
                time = TimeUnit.MINUTES.toMillis(Utils.TIME_60);
                break;
        }
        if (action != null) {
            Single.fromObservable(Observable.fromArray(time)).subscribe(action);
        }
        dismiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
