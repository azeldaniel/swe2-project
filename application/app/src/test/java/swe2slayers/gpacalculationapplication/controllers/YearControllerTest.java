/*
 * Copyright (c) 2018. Software Engineering Slayers
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Christopher Joseph (814000605)
 * Michael Bristol (816003612)
 * Maya Bannis (816000144)
 *
 * COMP 3613
 * Software Engineering II
 *
 * GPA Calculator Project
 */

package swe2slayers.gpacalculationapplication.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;

import static org.junit.Assert.*;

/**
 * The levels are not really used int this testing but i test them to 3 for completeness
 */
public class YearControllerTest {
    private swe2slayers.gpacalculationapplication.models.User user;
    Year originalYear;
    Semester semester;
    Semester semester2;

    Course course1, course2, course3, course4;
    private static boolean alreadySetUp = false;//To Mitigate Duplication of courses

    @Before
    public void before() {
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.enableTestingMode();//Open testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.load(user, null);//This user really would never be used and one can actual enter null


        user = new swe2slayers.gpacalculationapplication.models.User("S9oThHsvlAX8OVSBA0Xp09mNKMr2", "test@test.com", "First", "Last");//Test user created by maya

        originalYear = new Year("Year 1", "S9oThHsvlAX8OVSBA0Xp09mNKMr2");
        originalYear.setYearId("tempyear1");//Must set the year id this is what we use to comapare years because the object would not be the same
        semester = new Semester("Semester 1", originalYear.getYearId(), originalYear.getUserId());
        semester.setSemesterId("tempsemester1");
        //Second semester
        semester2 = new Semester("Semester 2", originalYear.getYearId(), originalYear.getUserId());
        semester2.setSemesterId("tempsemester2");
        //Attach Year and Semester to user
        UserController.addYearForUser(user, originalYear, null);
        UserController.addSemesterForUser(user, semester, null);
        //Attach semester 2
        UserController.addSemesterForUser(user, semester2, null);

        // Add courses to semester by specifying semester id and add courses to user
        course1 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, 3, 75);
        course1.setCourseId("tempcourse1");
        course2 = new Course("COMP3603", "Human Computer Interaction", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, 3, 75);
        course2.setCourseId("tempcourse2");
        course3 = new Course("COMP3607", "Object Oriented Program 2", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, 3, 75);
        course3.setCourseId("tempcourse3");
        course4 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, 3, 75);
        course4.setCourseId("tempcourse4");

        if (alreadySetUp) return;//Avoidance of duplication
        UserController.addCourseForUser(user, course1, null);
        UserController.addCourseForUser(user, course2, null);
        UserController.addCourseForUser(user, course3, null);
        UserController.addCourseForUser(user, course4, null);
        alreadySetUp = true;


    }

    @Test
    public void getYearTitleWithYears() {//SEt the course to the user years to the user

        String temp = YearController.getYearTitleWithYears(originalYear);

        if(originalYear.getStart() == null || originalYear.getEnd() == null || originalYear.getStart().getYear() == -1 ||
                originalYear.getEnd().getYear() == -1){
             originalYear.getTitle();
            assertTrue(temp.equals(originalYear.getTitle()));//What is returned must be compared to what we have currently

        }

       else if (originalYear.getStart().getYear() == originalYear.getEnd().getYear()) {//This muse be an else if because in the actual code it would jhave return meaning that we wouldnt have gone into that next if statement.
            assertTrue(temp.equals(originalYear.getTitle() + " (" + originalYear.getStart().getYear() + ")")) ;
        }



    }

    @Test
    public void getSemestersForYear() {
        java.util.ArrayList<Semester> semesters = new java.util.ArrayList<>();
        semesters.add(semester);
        semesters.add(semester2);
        java.util.ArrayList<Semester> SemesterComparison =YearController.getSemestersForYear(originalYear);
        for (int i = 0; i < 2; i++) {
            assertTrue(semesters.get(i).getSemesterId()==SemesterComparison.get(i).getSemesterId());
        }
    }


    @Test
    public void calculateGpaForYear() {
    }

    @Test
    public void attachSemesterListenerForYear() {
    }

    @Test
    public void attachYearListener() {
    }
    @After
    public void after() {
        // Disable Testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.disableTestingMode();//Close testing
    }
}