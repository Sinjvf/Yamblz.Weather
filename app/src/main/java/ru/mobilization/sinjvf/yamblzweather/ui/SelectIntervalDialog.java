package ru.mobilization.sinjvf.yamblzweather.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;

/**
 * Created by Sinjvf on 16.07.2017.
 * Class for showing dialogs to user
 */

public class SelectIntervalDialog extends DialogFragment {

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
            e.printStackTrace();
        }
        setData();
        return v;
    }


    private void setData(){

        radio10.setText(String.format(getString(R.string.n_min), 10));
        radio15.setText(String.format(getString(R.string.n_min), 15));
        radio30.setText(String.format(getString(R.string.n_min), 30));
        radio60.setText(String.format(getString(R.string.n_min), 60));
        switch ((int)(Preferenses.getIntervalTime(getContext())/Utils.MINUTE)){
            case 15:
                radio15.setChecked(true);
                break;
            case 30:
                radio30.setChecked(true);
                break;
            case 60:
                radio60.setChecked(true);
                break;
            default:
                radio10.setChecked(true);
        }
    }

    @OnClick(R.id.ok)
    public void onClickOk() {
        long time = 10* Utils.MINUTE;
        int id = radioGroup.getCheckedRadioButtonId();
        switch (id){
            case R.id.radio_10:
                time = 10* Utils.MINUTE;
                break;
            case R.id.radio_15:
                time = 15* Utils.MINUTE;
                break;
            case R.id.radio_30:
                time = 30* Utils.MINUTE;
                break;
            case R.id.radio_60:
                time = 60* Utils.MINUTE;
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
