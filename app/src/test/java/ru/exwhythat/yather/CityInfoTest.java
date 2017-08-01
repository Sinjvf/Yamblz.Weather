package ru.exwhythat.yather;

import org.junit.Test;

import ru.exwhythat.yather.screens.settings.CityInfo;

import static org.junit.Assert.*;

/**
 * Created by exwhythat on 30.07.17.
 */

public class CityInfoTest {

    @Test
    public void testEquals() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow", 55, 33);
        assertEquals(cityInfo1, cityInfo2);
    }

    @Test
    public void testEqualsReflexive() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        assertEquals(cityInfo1, cityInfo1);
    }

    @Test
    public void testEqualsSymmetric() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow", 55, 33);
        assertEquals(cityInfo1, cityInfo2);
        assertEquals(cityInfo2, cityInfo1);
    }

    @Test
    public void testEqualsTransitive() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo3 = new CityInfo("Moscow", 55, 33);
        assertEquals(cityInfo1, cityInfo2);
        assertEquals(cityInfo2, cityInfo3);
        assertEquals(cityInfo3, cityInfo1);
    }

    @Test
    public void testNotEqualsNull() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        assertNotEquals(cityInfo1, null);
    }

    @Test
    public void testNotEquals_1() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow2", 55, 33);
        assertNotEquals(cityInfo1, cityInfo2);
    }

    @Test
    public void testNotEquals_2() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow", 51, 33);
        assertNotEquals(cityInfo1, cityInfo2);
    }

    @Test
    public void testNotEquals_3() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow", 55, 31);
        assertNotEquals(cityInfo1, cityInfo2);
    }

    @Test
    public void testHashCode_Equals() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow", 55, 33);
        assertTrue(cityInfo1.hashCode() == cityInfo2.hashCode());
    }

    @Test
    public void testHashCode_Not_Equals_1() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow2", 55, 33);
        assertTrue(cityInfo1.hashCode() != cityInfo2.hashCode());
    }

    @Test
    public void testHashCode_Not_Equals_2() {
        CityInfo cityInfo1 = new CityInfo("Moscow", 55, 33);
        CityInfo cityInfo2 = new CityInfo("Moscow", 55, 31);
        assertTrue(cityInfo1.hashCode() != cityInfo2.hashCode());
    }
}
