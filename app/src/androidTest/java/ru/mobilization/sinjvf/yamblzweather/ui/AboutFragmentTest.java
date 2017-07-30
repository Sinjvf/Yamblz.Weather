package ru.mobilization.sinjvf.yamblzweather.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.activity.MainActivity;
import ru.mobilization.sinjvf.yamblzweather.debug.test.BuildConfig;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ru.mobilization.sinjvf.yamblzweather.ui.UiUtils.navigateToAboutFragment;

/**
 * Created by exwhythat on 28.07.17.
 */

@RunWith(AndroidJUnit4.class)
public class AboutFragmentTest {

    private Context targetContext = InstrumentationRegistry.getTargetContext();

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void prepare() {
        navigateToAboutFragment();
    }

    @Test
    public void testToolbar() {
        UiUtils.verifyToolbarText(targetContext, R.string.menu_about);
    }

    @Test
    public void testContentVisibility() {
        verifyContentVisibility();
    }

    @Test
    public void testContentText() {
        Resources res = targetContext.getResources();
        onView(withId(R.id.about_app_title)).check(matches(withText(res.getString(R.string.about_title))));
        onView(withId(R.id.about_app_descr)).check(matches(withText(res.getString(R.string.about_descr))));
        onView(withId(R.id.about_app_author)).check(matches(withText(res.getString(R.string.about_author))));
        onView(withId(R.id.about_app_version)).check(matches(withText(res.getString(R.string.about_version,
                BuildConfig.VERSION_NAME))));
    }

    static void verifyContentVisibility() {
        onView(withText(R.string.about_title)).check(matches(isDisplayed()));
        onView(withText(R.string.about_descr)).check(matches(isDisplayed()));
        onView(withText(R.string.about_author)).check(matches(isDisplayed()));
        String versionText = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.about_version,
                BuildConfig.VERSION_NAME);
        onView(withText(versionText)).check(matches(isDisplayed()));
    }
}
