package swe2slayers.gpacalculationapplication.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;

import static org.junit.Assert.*;

public class SemesterGPACalculationTest {

    private swe2slayers.gpacalculationapplication.models.User user;
    Year originalYear;
    Semester semester;
    Course course1, course2, course3, course4;
    private static boolean alreadySetUp = false;

    @Before
    public void before() {
        // Enable testing mode and load so that grading scheme is set up
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.enableTestingMode();
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.load(user, null);

        user = new swe2slayers.gpacalculationapplication.models.User("S9oThHsvlAX8OVSBA0Xp09mNKMr2", "test@test.com", "First", "Last");

        originalYear = new Year("Year 1", "S9oThHsvlAX8OVSBA0Xp09mNKMr2");
        originalYear.setYearId("tempyear1");
        semester = new Semester("Semester 1", originalYear.getYearId(), originalYear.getUserId());
        semester.setSemesterId("tempsemester1");
        UserController.addYearForUser(user, originalYear, null);
        UserController.addSemesterForUser(user, semester, null);

        // Add courses to semester by specifying semester id and add courses to user
        course1 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        course1.setCourseId("tempcourse1");
        course2 = new Course("COMP3603", "Human Computer Interaction", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        course2.setCourseId("tempcourse2");
        course3 = new Course("COMP3607", "Object Oriented Program 2", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        course3.setCourseId("tempcourse3");
        course4 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        course4.setCourseId("tempcourse4");
        if (alreadySetUp) return;
        UserController.addCourseForUser(user, course1, null);
        UserController.addCourseForUser(user, course2, null);
        UserController.addCourseForUser(user, course3, null);
        UserController.addCourseForUser(user, course4, null);
        alreadySetUp = true;

    }

    @Test
    public void getSemesterTitleWithYear() {
        String temp = SemesterController.getSemesterTitleWithYear(semester);
        assertTrue(temp.equals(originalYear.getTitle() + " " + semester.getTitle()));
    }

    @Test
    public void getCoursesForSemester() {

        java.util.ArrayList<Course> courses = new java.util.ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courses.add(course4);

        java.util.ArrayList<Course> courseComparison = SemesterController.getCoursesForSemester(semester);
        for (int i = 0; i < 4; i++) {
            assertTrue(courses.get(i).getCourseId() == courseComparison.get(i).getCourseId());
        }
    }

    @Test
    public void calculateGpaForSemester() {

        // ASSERTION 1

        // Semester has four courses, each with a grade of 75 which should amount to GPA of 3.7
        double temp = swe2slayers.gpacalculationapplication.controllers.SemesterController.calculateGpaForSemester(semester);

        java.math.BigDecimal bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(1, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();

        // Assert that the GPA is what it should be
        assertTrue(3.7 == temp);



        // ASSERTION 2

        // Semester has four courses, each with a grade of 50 which should amount to GPA of 2.0
        course1.setFinalGrade(50);
        course2.setFinalGrade(50);
        course3.setFinalGrade(50);
        course4.setFinalGrade(50);

        // Recalculate GPA
        temp = swe2slayers.gpacalculationapplication.controllers.SemesterController.calculateGpaForSemester(semester);

        bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(1, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();

        // Assert that the GPA is what it should be
        assertTrue(2.0 == temp);



        // ASSERTION 3

        // Semester has four courses, each with a grade of 30 which should amount to GPA of 1.3
        course1.setFinalGrade(30);
        course2.setFinalGrade(30);
        course3.setFinalGrade(30);
        course4.setFinalGrade(30);

        // Recalculate GPA
        temp = swe2slayers.gpacalculationapplication.controllers.SemesterController.calculateGpaForSemester(semester);

        bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(1, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();

        // Assert that the GPA is what it should be
        assertTrue(1.3 == temp);


        // ASSERTION 4

        // Courses are mixed and result in a GPA of 2.5
        course1.setFinalGrade(100); // 4.3
        course2.setFinalGrade(75);  // 3.7
        course3.setFinalGrade(50);  // 2.0
        course4.setFinalGrade(25);  // 0

        // Recalculate GPA
        temp = swe2slayers.gpacalculationapplication.controllers.SemesterController.calculateGpaForSemester(semester);

        bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(1, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();

        // Assert that the GPA is what it should be
        assertTrue(2.5 == temp);

        // Reset final grades
        course1.setFinalGrade(75);
        course2.setFinalGrade(75);
        course3.setFinalGrade(75);
        course4.setFinalGrade(75);

    }

    @Test
    public void getYearForSemester() {
        Year tempYear = SemesterController.getYearForSemester(semester);

        assertTrue(originalYear.getYearId() == tempYear.getYearId());
    }

    @After
    public void after() {
        // Disable Testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.disableTestingMode();
    }
}