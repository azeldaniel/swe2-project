package swe2slayers.gpacalculationapplication.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class createTimeTest {

    @Test
    public void getHour() {
    }

    @Test
    public void setHour() {
    }

    @Test
    public void getMinute() {
    }

    @Test
    public void setMinute() {
    }

    @Test
    public void testToString() {
        String testString = "3:14";
        Time time = new Time(3, 14);
        assertTrue(time.toString().equals(testString));
    }
}