package swe2slayers.gpacalculationapplication.utils;

import org.junit.Test;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class createDateTest {

    @Test
    public void getDay() {
    }

    @Test
    public void setDay() {
    }

    @Test
    public void getMonth() {
    }

    @Test
    public void setMonth() {
    }

    @Test
    public void getYear() {
    }

    @Test
    public void setYear() {
    }

    @Test
    public void testToString() {

        String testString = "30/3/2018";
        Date date = new Date(30, 3, 2018);
        assertTrue(date.toString().equals(testString));

    }

    @Test
    public void toStringFancy() {

        String testString = "October 23, 2018";
        Date date = new Date(23, 10, 2018);
        assertTrue(date.toStringFancy().equals(testString));

    }

    @Test
    public void daysUntil() {

        String testString = "Today";
        LocalDate currentLocalDate = LocalDate.now();
        int day = currentLocalDate.getDayOfMonth();
        int month = currentLocalDate.getMonthValue();
        int year = currentLocalDate.getYear();
        Date currentDate = new Date(day, month, year);
        assertTrue(currentDate.daysUntil().equals(testString));

        testString = "Tomorrow";
        LocalDate tomorrowLocalDate = currentLocalDate.plusDays(1);
        day = tomorrowLocalDate.getDayOfMonth();
        month = tomorrowLocalDate.getMonthValue();
        year = tomorrowLocalDate.getYear();
        Date tomorrowDate = new Date(day, month, year);
        assertTrue(tomorrowDate.daysUntil().equals(testString));

        testString = "12 days from now";
        LocalDate addedLocalDate = currentLocalDate.plusDays(12);
        day = addedLocalDate.getDayOfMonth();
        month = addedLocalDate.getMonthValue();
        year = addedLocalDate.getYear();
        Date addedDate = new Date(day, month, year);
        assertTrue(addedDate.daysUntil().equals(testString));

    }
}