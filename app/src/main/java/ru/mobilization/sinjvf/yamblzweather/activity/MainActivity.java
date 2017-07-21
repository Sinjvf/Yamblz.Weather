package ru.mobilization.sinjvf.yamblzweather.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseActivity;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.events.OpenNewFragmentEvent;
import ru.mobilization.sinjvf.yamblzweather.screens.about.AboutFragment;
import ru.mobilization.sinjvf.yamblzweather.screens.main.MainFragment;
import ru.mobilization.sinjvf.yamblzweather.screens.settings.SettingsFragment;
import timber.log.Timber;

/**
 * Created by Sinjvf on 09.07.2017.
 * Main activity class.
 * Contains and handles  the navigation drawer, the toolbar and the fragment content
 */

@Keep
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityInterface {

    protected final String TAG = "tag:" + getClass().getSimpleName();
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    MainActivityViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        model = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        model.getCurrentFragment().observe(this, this::changeFragment);
        setSupportActionBar(toolbar);
        initDrawer();

        Timber.tag(TAG);
    }


    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        BaseFragment fgm;
        switch (id) {
            case R.id.nav_tools:
                fgm = SettingsFragment.getInstance();
                break;
            case R.id.nav_about:
                fgm = AboutFragment.getInstance();
                break;
            case R.id.nav_main:
                fgm = MainFragment.getInstance();
                break;
            default:
                fgm = MainFragment.getInstance();
        }
        model.changeFragmentEvent(new OpenNewFragmentEvent(fgm, true));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void changeFragment(@NonNull OpenNewFragmentEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        if (event.isAddToBackStack()) {
            fm.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, event.getFgm()).commit();
        }else {
            fm.beginTransaction().replace(R.id.fragment_container, event.getFgm()).commit();
        }
    }

    @Override
    public void setTitleText(@StringRes int titleResId) {
        if (titleResId != -1) {
            String title = getString(titleResId);
            ActionBar actionbar = getSupportActionBar();
            if (actionbar == null) return;
            actionbar.setTitle(title);
        }
    }
}
