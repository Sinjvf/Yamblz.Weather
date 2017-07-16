package ru.mobilization.sinjvf.yamblzweather.retrofit;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mobilization.sinjvf.yamblzweather.BuildConfig;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;
import ru.mobilization.sinjvf.yamblzweather.ui.Dialogs;

/**
 * Created by Sinjvf on 16.07.2017.
 * Base handler
 */

public  class BaseResponseCallback<T> implements Callback<T> {
    private  final Context context;
    private  final FragmentManager fragmentManager;
    private  final NonNullResp responseHandler;

    public BaseResponseCallback(Context context, FragmentManager fragmentManager, NonNullResp<T> responseHandler) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.responseHandler =responseHandler;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response == null || response.body() == null) {
            return;
        }
        responseHandler.onResponse(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        String message;
        t.printStackTrace();
        //don't show strange errors text in release
        if (t instanceof UnknownHostException || t instanceof SocketTimeoutException) {
            message = context.getString(R.string.network_error);
        }else
            if(BuildConfig.isDebug)
                message = t.getMessage();
            else
                message = context.getString(R.string.oops_error);
        Dialogs dialog = Dialogs.getInstance(context.getString(R.string.error), message, false);
        dialog.show(fragmentManager, null);
    }
}
