package ru.mobilization.sinjvf.yamblzweather.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.activity.MainActivity;
import ru.mobilization.sinjvf.yamblzweather.retrofit.data.WeatherResponse;
import ru.mobilization.sinjvf.yamblzweather.screens.weather.WeatherFragment;

import static android.R.attr.fragment;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by exwhythat on 28.07.17.
 */

@RunWith(AndroidJUnit4.class)
public class WeatherFragmentTest {

    private Context targetContext = InstrumentationRegistry.getTargetContext();

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void prepare() {
        UiUtils.navigateToWeatherFragment();
    }

    @Test
    public void testToolbar() {
        UiUtils.verifyToolbarText(targetContext, R.string.menu_weather);
    }
}
