package ru.exwhythat.yather.di.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.exwhythat.yather.BuildConfig;
import ru.exwhythat.yather.di.DataScope;
import ru.exwhythat.yather.network.weather.WeatherApi;
import ru.exwhythat.yather.network.weather.WeatherApiKeyInsertInterceptor;

/**
 * Created by exwhythat on 01.08.17.
 */

@Module
public class NetworkModule {

    private static final String WEATHER_API_END_POINT = "http://api.openweathermap.org/data/2.5/";

    @Provides
    @DataScope
    public Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @DataScope
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }

    @Provides
    @DataScope
    WeatherApi provideWeatherApi(OkHttpClient baseHttpClient) {
        OkHttpClient weatherHttpClient = baseHttpClient.newBuilder()
                .addInterceptor(new WeatherApiKeyInsertInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_API_END_POINT)
                .client(weatherHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WeatherApi.class);
    }
}
