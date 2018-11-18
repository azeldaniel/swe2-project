package swe2slayers.gpacalculationapplication.models;

import org.junit.Test;

import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.Time;

import static org.junit.Assert.assertTrue;

public class createExamTest {

    @Test
    public void getRoom() {
        String room = "JFK";
        Exam exam = new Exam("COMP3607F2018", "Object Oriented Programming Final Exam 2018", new Date(), 1, room, 120);
        assertTrue(exam.getRoom().equals(room));
        room = "MD3";
        exam.setRoom(room);
        assertTrue(exam.getRoom().equals(room));
    }

    @Test
    public void getDuration() {
        int duration = 120;
        Exam exam = new Exam("COMP3607F2018", "Object Oriented Programming Final Exam 2018", new Date(), 1, "JFK", duration);
        assertTrue(exam.getDuration() == duration);
        duration = 150;
        exam.setDuration(duration);
        assertTrue(exam.getDuration() == duration);
    }

    @Test
    public void getTime() {
        Time time = new Time(2, 30);
        Exam exam = new Exam("COMP3607F2018", "Object Oriented Programming Final Exam 2018", new Date(), time);
        assertTrue(exam.getTime() == time);
        time = new Time(2);
        exam.setTime(time);
        assertTrue(exam.getTime() == time);
    }

}
