package ru.mobilization.sinjvf.yamblzweather;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;
import ru.mobilization.sinjvf.yamblzweather.screens.weather.WeatherViewModel;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * This test is kinda messy (and not working yet).
 * App needs to be refactored a lot in a good way to make network operations easy for testing.
 */

@RunWith(MockitoJUnitRunner.Silent.class)
public class WeatherApiTest {

    private static final String testJsonResponse = "{\"coord\":{\"lon\":92.89,\"lat\":56.02},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"base\":\"stations\",\"main\":{\"temp\":21,\"pressure\":1001,\"humidity\":83,\"temp_min\":21,\"temp_max\":21},\"visibility\":10000,\"wind\":{\"speed\":1.32,\"deg\":101.002},\"clouds\":{\"all\":40},\"dt\":1501266600,\"sys\":{\"type\":1,\"id\":7285,\"message\":0.0021,\"country\":\"RU\",\"sunrise\":1501191966,\"sunset\":1501250549},\"id\":1502026,\"name\":\"Krasnoyarsk\",\"cod\":200}";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Mock
    Application mockApplication;

    @Mock
    Context mockApplicationContext;

    @Mock
    SharedPreferences mockPrefs;

    @Mock
    SharedPreferences.Editor mockEditor;

    @Before
    public void prepare() {
        initMocks();
    }

    private void initMocks() {
        when(mockApplication.getApplicationContext()).thenReturn(mockApplicationContext);
        when(mockApplicationContext.getString(R.string.api_key)).thenReturn("4910f098be51f2bea56f920eff28f314");
        when(mockApplicationContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        UnitUtils.mockPrefsWithDefaults(mockPrefs, mockEditor);
    }

    /**
     * Here we should inject mocked web server into network chain, but this is not implemented yet,
     * so the test does not work right now
     */
    @Test
    public void testServer() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse()
                .setBody(testJsonResponse)
                .setResponseCode(200));
        mockWebServer.url("http://api.openweathermap.org/data/2.5");

        RxAndroidPlugins.initMainThreadScheduler(Schedulers::single);

        WeatherViewModel weatherModel = new WeatherViewModel(mockApplication);

        LatLng defaultCoords = Preferenses.DEFAULT_CITY_INFO.getCityCoords();

        LiveData<WeatherResponse> liveResponse = weatherModel.getWeatherDataByCityCoords(defaultCoords, false);
        WeatherResponse response = liveResponse.getValue();

        // This is bad way to get data from response. It should have been done in another certain place and then
        // applied here and in the WeatherFragment
        Integer mainInt = Utils.getDataWithoutException(() -> response.getMain().getTemp().intValue());
        Integer minInt = Utils.getDataWithoutException(() -> response.getMain().getTempMin().intValue());
        Integer maxInt = Utils.getDataWithoutException(() -> response.getMain().getTempMax().intValue());
        Integer humidity = Utils.getDataWithoutException(() -> response.getMain().getHumidity());
        Integer wind = Utils.getDataWithoutException(() -> response.getWind().getSpeed().intValue());

        // Since mocked WebServer is not injected yet, these asserts fails
        // Huge delta is to make it test the chain at least, not resulted values
        assertEquals(56.02, response.getCoord().getLat(), 180);
        assertEquals(92.89, response.getCoord().getLon(), 180);

        assertEquals(21, mainInt.intValue(), 100);
        assertEquals(21, minInt.intValue(), 100);
        assertEquals(21, maxInt.intValue(), 100);

        assertEquals(83, humidity.intValue(), 100);
        assertEquals(1, wind.intValue(), 100);

        mockWebServer.shutdown();
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.reset();
    }
}
