package ru.mobilization.sinjvf.yamblzweather.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.activity.MainActivity;
import ru.mobilization.sinjvf.yamblzweather.utils.Preferenses;
import ru.mobilization.sinjvf.yamblzweather.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import static ru.mobilization.sinjvf.yamblzweather.ui.UiUtils.navigateToSettingsFragment;

/**
 * Created by exwhythat on 28.07.17.
 */

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    private Context targetContext = InstrumentationRegistry.getTargetContext();

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void prepare() {
        navigateToSettingsFragment();
    }

    @Test
    public void testToolbar() {
        UiUtils.verifyToolbarText(targetContext, R.string.menu_tools);
    }

    @Test
    public void testContent() {
        verifyContentVisibility();
    }

    @Test
    public void testIntervalSelectionDialog() {
        // Test all selection options
        setAndCheckRadioButtonResult(R.id.radio_10, Utils.TIME_10);
        setAndCheckRadioButtonResult(R.id.radio_15, Utils.TIME_15);
        setAndCheckRadioButtonResult(R.id.radio_30, Utils.TIME_30);
        setAndCheckRadioButtonResult(R.id.radio_60, Utils.TIME_60);
        setAndCheckRadioButtonResult(R.id.radio_10, Utils.TIME_10);
    }

    @Test
    public void testCitySelection() {
        onView(withId(R.id.city_selection)).perform(ViewActions.click());
        onView(withId(R.id.autocomplete_cities)).check(matches(isDisplayed()));
    }

    private void setAndCheckRadioButtonResult(int radioButtonResId, int expectedInterval) {
        // Open interval selection dialog and check it is displayed
        onView(withId(R.id.interval_selection)).perform(ViewActions.click());
        onView(withId(R.id.radio_group)).check(matches(isDisplayed()));

        // Click the certain radio button
        onView(withId(radioButtonResId)).perform(ViewActions.click());
        onView(withId(R.id.ok)).perform(ViewActions.click());

        // Check that the resulting textView contains chosen interval
        String expectedIntervalValue = targetContext.getResources().getString(R.string.n_min, expectedInterval);
        onView(withId(R.id.interval_text)).check(matches(withText(expectedIntervalValue)));

        // Check that chosen interval has been written into shared preferences
        assertEquals(Preferenses.getIntervalTime(targetContext), expectedInterval * Utils.MINUTE);
    }

    static void verifyContentVisibility() {
        onView(withText(R.string.updating_interval)).check(matches(isDisplayed()));
        onView(withText(R.string.settings_city_selection)).check(matches(isDisplayed()));
    }
}
