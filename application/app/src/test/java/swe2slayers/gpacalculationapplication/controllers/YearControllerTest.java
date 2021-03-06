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


import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;

import static org.junit.Assert.*;

/**
 * The levels are not really used int this testing but i test them to 3 for completeness
 */
public class YearControllerTest {
    private swe2slayers.gpacalculationapplication.models.User user;
    Year originalYear;
    Year secondYear;
    Semester semester;
    Semester semester2;
    Semester semesterOne;

    Course course1, course2, course3, course4;
    Course course11, course22, course33, course44;
    Course courseOne, courseTwo, courseThree;
    private static boolean alreadySetUp = false;//To Mitigate Duplication of courses

    @Before
    public void before() {
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.enableTestingMode();//Open testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.load(user, null);//This user really would never be used and one can actual enter null

        user = new swe2slayers.gpacalculationapplication.models.User("S9oThHsvlAX8OVSBA0Xp09mNKMr3", "test@test.com", "First", "Last");//Test user created by maya

        // Initialization of years, semesters and courses
        originalYear = new Year("Year 1", user.getUserId());
        originalYear.setYearId("tempyear1"); //Must set the year id this is what we use to comapare years because the object would not be the same
        secondYear = new Year("Year 2", user.getUserId());
        secondYear.setYearId("tempyear2");

        semester = new Semester("Semester 1", originalYear.getYearId(), originalYear.getUserId());
        semester.setSemesterId("tempsemester1");
        // Second semester
        semester2 = new Semester("Semester 2", originalYear.getYearId(), originalYear.getUserId());
        semester2.setSemesterId("tempsemester2");
        semesterOne = new Semester("Semester One", secondYear.getYearId(), secondYear.getUserId());
        semesterOne.setSemesterId("tempsemesterOne");

        // Add courses to originalYear, semester1 by specifying semester id and add courses to user
        course1 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), user.getUserId(), 3, 3, 75);
        course1.setCourseId("tempcourse1");
        course2 = new Course("COMP3603", "Human Computer Interaction", semester.getSemesterId(), user.getUserId(), 3, 3, 75);
        course2.setCourseId("tempcourse2");
        course3 = new Course("COMP3607", "Object Oriented Program 2", semester.getSemesterId(), user.getUserId(), 3, 3, 75);
        course3.setCourseId("tempcourse3");
        course4 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), user.getUserId(), 3, 3, 75);
        course4.setCourseId("tempcourse4");
        // Second set for semester2
        course11 = new Course("COMP9000", "Over The Top II", semester2.getSemesterId(), user.getUserId(), 3, 3, 20);
        course11.setCourseId("tempcourse11");
        course22 = new Course("COMP9001", "Over The Top I", semester2.getSemesterId(), user.getUserId(), 3, 3, 10);
        course22.setCourseId("tempcourse22");
        course33 = new Course("PSYC101", "Intro to Psychology", semester2.getSemesterId(), user.getUserId(), 3, 3, 10);
        course33.setCourseId("tempcourse33");
        course44 = new Course("TOWN8000", "Town and County Planning", semester2.getSemesterId(), user.getUserId(), 3, 3, 10);
        course44.setCourseId("tempcourse44");
        // Third set for secondYear, semesterOne
        courseOne = new Course("COMP2700", "Computer Engineering", semesterOne.getSemesterId(), user.getUserId(), 3,2, 87);
        courseOne.setCourseId("tempcourseOne");
        courseTwo = new Course("COMP2300", "Data Analytics", semesterOne.getSemesterId(), user.getUserId(), 3,2, 55);
        courseTwo.setCourseId("tempcourseTwo");
        courseThree = new Course("INFO2975", "Business Information Systems", semesterOne.getSemesterId(), user.getUserId(), 3,2);
        courseThree.setCourseId("tempcourseThree");


        if (alreadySetUp) return; //Avoidance of duplication
        //Attach Year and Semester to user
        UserController.addYearForUser(user, originalYear, null);
        UserController.addYearForUser(user, secondYear, null);
        UserController.addSemesterForUser(user, semester, null);
        //Attach semester 2
        UserController.addSemesterForUser(user, semester2, null);

        UserController.addCourseForUser(user, course1, null);
        UserController.addCourseForUser(user, course2, null);
        UserController.addCourseForUser(user, course3, null);
        UserController.addCourseForUser(user, course4, null);
        //Second set
        UserController.addCourseForUser(user, course11, null);
        UserController.addCourseForUser(user, course22, null);
        UserController.addCourseForUser(user, course33, null);
        UserController.addCourseForUser(user, course44, null);

        alreadySetUp = true;

    }

    @Test
    public void getYearTitleWithYears() {//SEt the course to the user years to the user

        String temp = YearController.getYearTitleWithYears(originalYear);

        if (originalYear.getStart() == null || originalYear.getEnd() == null || originalYear.getStart().getYear() == -1 ||
                originalYear.getEnd().getYear() == -1) {
            originalYear.getTitle();
            assertTrue(temp.equals(originalYear.getTitle()));//What is returned must be compared to what we have currently

        } else if (originalYear.getStart().getYear() == originalYear.getEnd().getYear()) {//This muse be an else if because in the actual code it would jhave return meaning that we wouldnt have gone into that next if statement.
            assertTrue(temp.equals(originalYear.getTitle() + " (" + originalYear.getStart().getYear() + ")"));
        } else {
            assertTrue(temp.equals(originalYear.getTitle() + " (" + originalYear.getStart().getYear() + " - " + originalYear.getEnd().getYear() + ")"));

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

        // ASSERTION 1

        // Semester has four courses, which should amount to GPA of 1.85
        double temp = YearController.calculateGpaForYear(originalYear);
        java.math.BigDecimal bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(2, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue(); //rounded up
        assertTrue(1.85==temp);

        // ASSERTION 2

        // Checks that the year has no semesters and GPA is 0.0
        assertTrue((YearController.getSemestersForYear(secondYear).isEmpty()));
        temp = YearController.calculateGpaForYear(secondYear);
        bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(2, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();
        assertTrue(temp==0.0);

        // ASSERTION 3

        // Adds a semester and course with a final grade of 87, GPA of 4.0
        UserController.addSemesterForUser(user, semesterOne, null);
        UserController.addCourseForUser(user, courseOne, null);

        temp = YearController.calculateGpaForYear(secondYear);
        bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(2, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();
        assertTrue(temp==4.0);

        // ASSERTION 4

        // Adds another course to the semester to recalculate GPA of 3.15
        UserController.addCourseForUser(user, courseTwo, null);
        temp = YearController.calculateGpaForYear(secondYear);
        bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(2, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();
        assertTrue(temp==3.15);

        // ASSERTION 5

        // Adds another course to semester with in-course calculations with weight 100 to recalculate GPA of 3.43
        // Attach course assessments
        Assignment assignmentOne = new Assignment("A1", "Assignment 1", new Date(10, 11, 2018), 40.00, 17.00, 20.00);
        assignmentOne.setCourseId(courseThree.getCourseId());
        UserController.addAssignmentForUser(user, assignmentOne, null);
        Exam examOne = new Exam("CW1", "Coursework 1", new Date(21, 12, 2018), 60.00, 88.00, 100.00);
        examOne.setCourseId(courseThree.getCourseId());
        UserController.addExamForUser(user, examOne, null);
        UserController.addCourseForUser(user, courseThree, null);

        temp = YearController.calculateGpaForYear(secondYear);
        bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(2, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();
        assertTrue(temp==3.43);

    }

    @Test
    public void attachSemesterListenerForYear() { //Firebase

    }

    @Test
    public void attachYearListener() { //Firebase
    }
    @After
    public void after() {
        // Disable Testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.disableTestingMode();//Close testing
    }
}