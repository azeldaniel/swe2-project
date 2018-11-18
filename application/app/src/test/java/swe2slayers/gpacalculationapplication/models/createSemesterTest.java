package swe2slayers.gpacalculationapplication.models;

import org.junit.Test;

import swe2slayers.gpacalculationapplication.utils.Date;

import static org.junit.Assert.assertTrue;

public class createSemesterTest {

    // Semester(String title, String yearId, String userId)
    // Semester(String title, String yearId, String userId, Date start, Date end)

    @Test
    public void getSemesterId() {
        String id = "o1nf9Bdf82AnPWW";
        Semester sem = new Semester();
        sem.setSemesterId(id);
        assertTrue(sem.getSemesterId().equals(id));
    }

    @Test
    public void getYearId() {
        String yearId = "o1nf9Bdf82AnPWW";
        Semester sem = new Semester("Semester 2", yearId, "bn12aboey45ucn12wn29Eas");
        assertTrue(sem.getYearId().equals(yearId));
        yearId = "ThisIsATestID";
        sem.setYearId(yearId);
        assertTrue(sem.getYearId().equals(yearId));
    }

    @Test
    public void getUserId() {
        String userId = "o1nf9Bdf82AnPWW";
        Semester sem = new Semester("Semester 2", "o1nf9Bdf82AnPWW", userId);
        assertTrue(sem.getUserId().equals(userId));
        userId = "ThisIsATestID";
        sem.setUserId(userId);
        assertTrue(sem.getUserId().equals(userId));
    }

    @Test
    public void getTitle() {
        String title = "Semester 2";
        Semester sem = new Semester(title, "o1nf9Bdf82AnPWW", "bn12aboey45ucn12wn29Eas");
        assertTrue(sem.getTitle().equals(title));
        title = "ThisIsATestID";
        sem.setTitle(title);
        assertTrue(sem.getTitle().equals(title));
    }

    @Test
    public void getStart() {
        Date start = new Date(15, 4, 2018);
        Semester sem = new Semester("Semester 2", "o1nf9Bdf82AnPWW", "bn12aboey45ucn12wn29Eas", start, new Date());
        assertTrue(sem.getStart().toString().equals(start.toString()));
        start.setDay(27);
        start.setMonth(8);
        sem.setStart(start);
        assertTrue(sem.getStart().toString().equals(start.toString()));
    }

    @Test
    public void getEnd() {
        Date end = new Date(15, 4, 2018);
        Semester sem = new Semester("Semester 2", "o1nf9Bdf82AnPWW", "bn12aboey45ucn12wn29Eas", new Date(), end);
        assertTrue(sem.getEnd().toString().equals(end.toString()));
        end.setDay(27);
        end.setMonth(8);
        sem.setStart(end);
        assertTrue(sem.getEnd().toString().equals(end.toString()));
    }

}
