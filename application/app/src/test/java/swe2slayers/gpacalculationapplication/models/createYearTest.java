package swe2slayers.gpacalculationapplication.models;

import org.junit.Test;

import swe2slayers.gpacalculationapplication.utils.Date;

import static org.junit.Assert.assertTrue;

public class createYearTest {

    // Year(String title, String userId)
    // Year(String title, String userId, Date start, Date end)

    @Test
    public void getYearId() {
        String id = "o1nf9Bdf82AnPWW";
        Year year = new Year();
        year.setYearId(id);
        assertTrue(year.getYearId().equals(id));
    }

    @Test
    public void getTitle() {
        String title = "Year 1";
        Year year = new Year(title, "bn12aboey45ucn12wn29Eas");
        assertTrue(year.getTitle().equals(title));
        title = "Year 2";
        year.setTitle(title);
        assertTrue(year.getTitle().equals(title));
    }

    @Test
    public void getUserId() {
        String userId = "bn12aboey45ucn12wn29Eas";
        Year year = new Year("Year 1", userId);
        assertTrue(year.getUserId().equals(userId));
        userId = "ThisIsATestID";
        year.setUserId(userId);
        assertTrue(year.getUserId().equals(userId));
    }

    @Test
    public void getStart() {
        Date start = new Date(15, 4, 2018);
        Year year = new Year("Year 1", "bn12aboey45ucn12wn29Eas", start, new Date());
        assertTrue(year.getStart().toString().equals(start.toString()));
        start.setDay(27);
        start.setMonth(8);
        year.setStart(start);
        assertTrue(year.getStart().toString().equals(start.toString()));
    }

    @Test
    public void getEnd() {
        Date end = new Date(15, 4, 2018);
        Year year = new Year("Year 1", "bn12aboey45ucn12wn29Eas", new Date(), end);
        assertTrue(year.getEnd().toString().equals(end.toString()));
        end.setDay(27);
        end.setMonth(8);
        year.setEnd(end);
        assertTrue(year.getEnd().toString().equals(end.toString()));
    }

}
