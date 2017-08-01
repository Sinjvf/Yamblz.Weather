package ru.exwhythat.yather.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.Toolbar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import ru.exwhythat.yather.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;

/**
 * Created by exwhythat on 28.07.17.
 */

class UiUtils {

    static void navigateToWeatherFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_weather));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    static void navigateToSettingsFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_tools));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    static void navigateToAboutFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_about));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    static void verifyToolbarText(Context targetContext, @StringRes int resId) {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        CharSequence toolbarTitle = targetContext.getResources().getString(resId);
        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(toolbarTitle))));
    }

    private static Matcher<Object> withToolbarTitle(
            final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }
}
