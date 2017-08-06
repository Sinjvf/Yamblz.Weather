package ru.exwhythat.yather.ui.db;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ru.exwhythat.yather.db.CurrentWeatherDao;
import ru.exwhythat.yather.db.entities.City;
import ru.exwhythat.yather.db.entities.CurrentWeather;
import ru.exwhythat.yather.ui.util.LiveDataTestUtil;
import ru.exwhythat.yather.ui.util.TestData;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by exwhythat on 8/4/17.
 */

@RunWith(AndroidJUnit4.class)
public class CurrentWeatherDaoTest extends WeatherTest {

    private CurrentWeatherDao currentWeatherDao;

    @Before
    public void prepareCurrentWeather() {
        currentWeatherDao = db.currentWeatherDao();
    }

    @Test
    public void insertOneAndLoad() throws InterruptedException {
        CurrentWeather expectedWeather = TestData.TestCurrentWeather.testCurrentWeather1;
        long insertedId = currentWeatherDao.insert(expectedWeather);

        CurrentWeather loadedWeather = LiveDataTestUtil.getValue(
                currentWeatherDao.getCurrentWeatherForCity(TestData.TestCity.testCityId1));

        assertEquals(TestData.TestCity.testCityId1, insertedId);
        assertEquals(expectedWeather, loadedWeather);
    }

    @Test
    public void insertSecondWeatherForSameCityOverwrites() throws InterruptedException {
        CurrentWeather initial = TestData.TestCurrentWeather.testCurrentWeather1;
        long insertedId1 = currentWeatherDao.insert(initial);
        int cityId = initial.getApiCityId();

        // replace current weather for city #1 with new weather
        CurrentWeather replacement = TestData.TestCurrentWeather.testCurrentWeather2;
        replacement.setApiCityId(cityId);
        long insertedId2 = currentWeatherDao.insert(replacement);

        CurrentWeather loadedWeather = LiveDataTestUtil.getValue(currentWeatherDao.getCurrentWeatherForCity(cityId));
        List<CurrentWeather> allCurrentWeathers = LiveDataTestUtil.getValue(currentWeatherDao.getAll());

        assertEquals(1, allCurrentWeathers.size());
        assertThat(allCurrentWeathers, hasItem(replacement));
        assertThat(allCurrentWeathers, not(hasItem(initial)));
        assertEquals(insertedId1, insertedId2);
        assertEquals(replacement, loadedWeather);
    }

    @Test
    public void insertTwoWeathersToDifferentCities() throws InterruptedException {
        CurrentWeather weatherForCity1 = TestData.TestCurrentWeather.testCurrentWeather1;
        long insertedId1 = currentWeatherDao.insert(weatherForCity1);

        CurrentWeather weatherForCity2 = TestData.TestCurrentWeather.testCurrentWeather2;
        long insertedId2 = currentWeatherDao.insert(weatherForCity2);

        List<CurrentWeather> allCurrentWeathers = LiveDataTestUtil.getValue(currentWeatherDao.getAll());

        assertNotEquals(insertedId1, insertedId2);
        assertEquals(2, allCurrentWeathers.size());
        assertThat(allCurrentWeathers, hasItem(weatherForCity1));
        assertThat(allCurrentWeathers, hasItem(weatherForCity2));
    }

    @Test
    public void loadTestCities() throws InterruptedException {
        List<City> loadedCities = LiveDataTestUtil.getValue(cityDao.getAll());
        assertEquals(3, loadedCities.size());
    }
}
