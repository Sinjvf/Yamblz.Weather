package ru.exwhythat.yather.ui.db;

import org.junit.After;
import org.junit.Before;

import ru.exwhythat.yather.db.CityDao;
import ru.exwhythat.yather.ui.util.TestData;

/**
 * Created by exwhythat on 8/5/17.
 */

public abstract class WeatherTest extends DbTest {

    protected CityDao cityDao;

    @Before
    public void prepareWeather() {
        cityDao = db.cityDao();

        // fill cities table to satisfy foreign key constraint
        cityDao.insert(TestData.TestCity.testCity1);
        cityDao.insert(TestData.TestCity.testCity2);
        cityDao.insert(TestData.TestCity.testCity3);
    }

    @After
    public void tearDownWeather() {
        cityDao.clearTable();
    }
}
