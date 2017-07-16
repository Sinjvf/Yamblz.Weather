package ru.mobilization.sinjvf.yamblzweather.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import ru.mobilization.sinjvf.yamblzweather.R;

/**
 * Created by Sinjvf on 16.07.2017.
 * Class for showing dialogs to user
 */

public class Dialogs extends DialogFragment {

    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.cancel)
    TextView cancel;

    Unbinder unbinder;

    private String title, text;
    private boolean haveCancelButton;
    private Observer<Boolean> action;
    private static final String TITLE_KEY = "title_key";
    private static final String TEXT_KEY = "text_key";
    private static final String HAVE_CANCEL_KEY = "have_cancel_key";


    public static Dialogs getInstance(String title, String text, boolean haveCancelButton) {
        Dialogs dialog = new Dialogs();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, title);
        bundle.putString(TEXT_KEY, text);
        bundle.putBoolean(HAVE_CANCEL_KEY, haveCancelButton);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setAction(Observer<Boolean> action) {
        this.action = action;
    }

    private void getData() {
        Bundle bundle = getArguments();
        if (bundle == null) return;
        title = bundle.getString(TITLE_KEY);
        text = bundle.getString(TEXT_KEY);
        haveCancelButton = bundle.getBoolean(HAVE_CANCEL_KEY);
    }

    public void setData() {
        if (!TextUtils.isEmpty(title))
            titleView.setText(title);
        else
            titleView.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(text))
            textView.setText(text);
        else
            textView.setVisibility(View.GONE);
        if (!haveCancelButton)
            cancel.setVisibility(View.GONE);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.el_dialog, container, false);
        unbinder = ButterKnife.bind(this, v);
        try {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        getData();
        setData();
        return v;
    }

    @OnClick(R.id.ok)
    public void onClickOk() {
        if (action != null)
            Observable.fromArray(true).subscribe(action);
        dismiss();
    }

    @OnClick(R.id.cancel)
    public void onClickCancel() {
        if (action != null)
            Observable.fromArray(false).subscribe(action);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
