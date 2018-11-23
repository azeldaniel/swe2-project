package swe2slayers.gpacalculationapplication.controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import org.junit.Test;

import java.util.HashMap;

import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import static org.junit.Assert.*;

public class UserControllerTest {
Date dateS= new Date(19,12,2018);
Date dateE= new Date(19,12,2019);
    @Test
    public void updateYearForUser() {
        // Year(String title, String userId, Date start, Date end)
        final Year yr=new Year("Year3","S9oThHsvlAX8OVSBA0Xp09mNKMr2",dateS,dateE);
        yr.setYearId("123456");
        yr.setUserId("S9oThHsvlAX8OVSBA0Xp09mNKMr2");
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(yr.getYearId(), yr);
        assertFalse(yr.getYearId()!="123456");
        assertFalse(yr.getUserId()!="S9oThHsvlAX8OVSBA0Xp09mNKMr2");
        assertTrue(yr.getYearId()=="123456");
        assertTrue(yr.getUserId()=="S9oThHsvlAX8OVSBA0Xp09mNKMr2");


    }

    @Test
    public void updateSemesterForUser() {
    }

    @Test
    public void updateCourseForUser() {
    }

    @Test
    public void updateAssignmentForUser() {
    }

    @Test
    public void updateExamForUser() {
    }

    @Test
    public void calculateDegreeGPA() {
    }

    @Test
    public void calculateCumulativeGPA() {
    }

    @Test
    public void save() {
    }
}