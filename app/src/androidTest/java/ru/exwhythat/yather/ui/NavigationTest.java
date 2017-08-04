package ru.exwhythat.yather.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.exwhythat.yather.R;
import ru.exwhythat.yather.activity.MainActivity;


@RunWith(AndroidJUnit4.class)
public class NavigationTest {

    private Context targetContext = InstrumentationRegistry.getTargetContext();

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testStartScreen() {
        UiUtils.verifyToolbarText(targetContext, R.string.menu_weather);
    }

    @Test
    public void testNavigateFromStartScreenToWeather() {
        UiUtils.navigateToWeatherFragment();
        UiUtils.verifyToolbarText(targetContext, R.string.menu_weather);
    }

    @Test
    public void testNavigateFromWeatherToWeather() {
        UiUtils.navigateToWeatherFragment();
        UiUtils.navigateToWeatherFragment();
        UiUtils.verifyToolbarText(targetContext, R.string.menu_weather);
    }

    @Test
    public void testNavigateFromAboutToWeather() {
        UiUtils.navigateToAboutFragment();
        UiUtils.navigateToWeatherFragment();
        UiUtils.verifyToolbarText(targetContext, R.string.menu_weather);
    }

    @Test
    public void testNavigateFromSettingsToWeather() {
        UiUtils.navigateToSettingsFragment();
        UiUtils.navigateToWeatherFragment();
        UiUtils.verifyToolbarText(targetContext, R.string.menu_weather);
    }

    @Test
    public void testNavigateToAbout() {
        UiUtils.navigateToAboutFragment();
        UiUtils.verifyToolbarText(targetContext, R.string.menu_about);
        AboutFragmentTest.verifyContentVisibility();
    }

    @Test
    public void testNavigateToSettings() {
        UiUtils.navigateToSettingsFragment();
        UiUtils.verifyToolbarText(targetContext, R.string.menu_tools);
        SettingsFragmentTest.verifyContentVisibility();
    }
}
