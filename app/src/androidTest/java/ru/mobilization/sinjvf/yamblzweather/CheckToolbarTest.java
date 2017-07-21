package ru.mobilization.sinjvf.yamblzweather;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobilization.sinjvf.yamblzweather.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class CheckToolbarTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkToolBarText() throws Exception {
        String toolsTitle = getResourceString(R.string.menu_tools);
        String aboutTitle = getResourceString(R.string.menu_about);
        String mainTitle = getResourceString(R.string.menu_main);



        //check main fragment name
        checkTitle(mainTitle);

        //check  about fragment name
        //open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        //click on menu
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_about));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
        //check name
        checkTitle(aboutTitle);

        //check  settings fragment name
        //open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        //click on menu
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_tools));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
        //check name
        checkTitle(toolsTitle);

        //another check main fragment name
        //open drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        //click on menu
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_main));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
        //check name
        checkTitle(mainTitle);
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

    private void checkTitle(String title){
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(title)));

    }
}
