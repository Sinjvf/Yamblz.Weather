package ru.exwhythat.yather;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Single;
import ru.exwhythat.yather.network.weather.WeatherItem;
import ru.exwhythat.yather.network.weather.parts.WeatherResponse;
import ru.exwhythat.yather.repository.RemoteWeatherRepository;
import ru.exwhythat.yather.screens.weather.WeatherViewModel;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by exwhythat on 02.08.17.
 */

@RunWith(MockitoJUnitRunner.class)
public class WeatherViewModelTest {

    private static final String testJsonResponse = "{\"coord\":{\"lon\":92.89,\"lat\":56.02},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"base\":\"stations\",\"main\":{\"temp\":21,\"pressure\":1001,\"humidity\":83,\"temp_min\":21,\"temp_max\":21},\"visibility\":10000,\"wind\":{\"speed\":1.32,\"deg\":101.002},\"clouds\":{\"all\":40},\"dt\":1501266600,\"sys\":{\"type\":1,\"id\":7285,\"message\":0.0021,\"country\":\"RU\",\"sunrise\":1501191966,\"sunset\":1501250549},\"id\":1502026,\"name\":\"Krasnoyarsk\",\"cod\":200}";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    YatherApp yatherApp;

    @Mock
    RemoteWeatherRepository remoteWeatherRepository;

    @Before
    public void prepare() {
    }

    @Test
    public void testWeatherModelCreation() {
        when(yatherApp.getSharedPreferences(anyString(), anyInt()).getString(anyString(), anyString()))
                .thenReturn("AHAHHAHAH IT WORKS");

        WeatherResponse expectedResponse = new GsonBuilder().create().fromJson(testJsonResponse, WeatherResponse.class);
        when(remoteWeatherRepository.getCurrentWeatherByLocation(any())).thenReturn(Single.just(expectedResponse));
        WeatherItem expectedWeatherItem = new WeatherItem(expectedResponse);

        WeatherViewModel weatherModel = new WeatherViewModel(yatherApp, remoteWeatherRepository);
        LiveData<WeatherItem> actualWeatherItemLive = weatherModel.getWeatherDataByCityCoords(new LatLng(30.0, 40.0));
        WeatherItem actualWeatherItem = actualWeatherItemLive.getValue();
        assertEquals(expectedWeatherItem, actualWeatherItem);
        assertEquals(21, actualWeatherItem.getMainTemp());
    }
}
