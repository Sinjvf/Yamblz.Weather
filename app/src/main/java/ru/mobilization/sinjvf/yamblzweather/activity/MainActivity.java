package ru.mobilization.sinjvf.yamblzweather.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mobilization.sinjvf.yamblzweather.R;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseActivity;
import ru.mobilization.sinjvf.yamblzweather.base_util.BaseFragment;
import ru.mobilization.sinjvf.yamblzweather.events.OpenNewFragment;
import ru.mobilization.sinjvf.yamblzweather.fragments.about.AboutFragment;
import ru.mobilization.sinjvf.yamblzweather.fragments.main.MainFragment;
import ru.mobilization.sinjvf.yamblzweather.fragments.settings.SettingsFragment;

/**
 * Created by Sinjvf on 09.07.2017.
 */


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityInterface {
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
    }


    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
        EventBus.getDefault().post(new OpenNewFragment(fgm, false));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void changeFragment(OpenNewFragment event) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, event.getFgm()).commit();

    }

    @Override
    public void setTitleText(int titleResId) {
        if (titleResId != -1) {
            String title = getString(titleResId);
            ActionBar actionbar = getSupportActionBar();
            if (actionbar == null) return;
            actionbar.setTitle(title);
        }
    }
}
