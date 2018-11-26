package swe2slayers.gpacalculationapplication.controllers;

import org.junit.Test;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;

import static org.junit.Assert.*;

public class SemesterGPACalculationTest {

    private swe2slayers.gpacalculationapplication.models.User user = new swe2slayers.gpacalculationapplication.models.User("S9oThHsvlAX8OVSBA0Xp09mNKMr2", "test@test.com", "First", "Last");

    @Test
    public void getSemesterTitleWithYear() {
        // Enable testing mode and load so that grading scheme is set up
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.enableTestingMode();
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.load(user, null);

        Year originalYear = new Year("Year 1", "S9oThHsvlAX8OVSBA0Xp09mNKMr2");
        originalYear.setYearId("tempval");
        UserController.addYearForUser(user, originalYear, null);
        Semester semester = new Semester("Semester 0", originalYear.getYearId(), originalYear.getUserId());
        UserController.addSemesterForUser(user, semester, null);

        String temp = SemesterController.getSemesterTitleWithYear(semester);

        assertTrue(temp.equals(originalYear.getTitle() + " " + semester.getTitle()));

        // Disable Testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.disableTestingMode();
    }

    @Test
    public void getCoursesForSemester() {
        // Enable testing mode and load so that grading scheme is set up
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.enableTestingMode();
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.load(user, null);

        // Set up semester for testing. Important to add to user
        Semester semester = new Semester("Semester 1", null, "S9oThHsvlAX8OVSBA0Xp09mNKMr2");
        semester.setSemesterId("tempval2");
        UserController.addSemesterForUser(user, semester, null);

        // Add courses to semester by specifying semester id and add courses to user
        Course course1 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        UserController.addCourseForUser(user, course1, null);
        Course course2 = new Course("COMP3603", "Human Computer Interaction", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        UserController.addCourseForUser(user, course2, null);
        Course course3 = new Course("COMP3607", "Object Oriented Program 2", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        UserController.addCourseForUser(user, course3, null);
        Course course4 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        UserController.addCourseForUser(user, course4, null);

        java.util.ArrayList<Course> courses = new java.util.ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courses.add(course4);

        java.util.ArrayList<Course> courseComparison = SemesterController.getCoursesForSemester(semester);
        for (int i = 0; i < 4; i++) {
            assertTrue(courses.get(i) == courseComparison.get(i));
        }

        // Disable Testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.disableTestingMode();
    }

    @Test
    public void calculateGpaForSemester() {
        // Enable testing mode and load so that grading scheme is set up
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.enableTestingMode();
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.load(user, null);

        // Set up semester for testing. Important to add to user
        Semester semester = new Semester("Semester 1", null, "S9oThHsvlAX8OVSBA0Xp09mNKMr2");
        semester.setSemesterId("tempval1");
        UserController.addSemesterForUser(user, semester, null);



        // ASSERTION 1

        // Add courses to semester by specifying semester id and add courses to user
        Course course1 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        UserController.addCourseForUser(user, course1, null);
        Course course2 = new Course("COMP3603", "Human Computer Interaction", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        UserController.addCourseForUser(user, course2, null);
        Course course3 = new Course("COMP3607", "Object Oriented Program 2", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        UserController.addCourseForUser(user, course3, null);
        Course course4 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 75);
        UserController.addCourseForUser(user, course4, null);

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



        // ASSERTION 5

        // Add a fifth course with a final mark of 0
        Course course5 = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), "S9oThHsvlAX8OVSBA0Xp09mNKMr2", 3, -1, 0);
        UserController.addCourseForUser(user, course5, null);

        // Recalculate GPA
        temp = swe2slayers.gpacalculationapplication.controllers.SemesterController.calculateGpaForSemester(semester);

        bd = new java.math.BigDecimal(Double.toString(temp));
        bd = bd.setScale(1, java.math.RoundingMode.HALF_UP);
        temp = bd.doubleValue();

        // Assert that the GPA is what it should be
        assertTrue(2.0 == temp);

        // Disable Testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.disableTestingMode();

    }

    @Test
    public void getYearForSemester() {
        // Enable testing mode and load so that grading scheme is set up
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.enableTestingMode();
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.load(user, null);

        Year originalYear = new Year("Year 2", "S9oThHsvlAX8OVSBA0Xp09mNKMr2");
        originalYear.setYearId("tempval3");
        UserController.addYearForUser(user, originalYear, null);
        Semester semester = new Semester("Semester 0.1", originalYear.getYearId(), originalYear.getUserId());
        UserController.addSemesterForUser(user, semester, null);

        Year tempYear = SemesterController.getYearForSemester(semester);

        assertTrue(originalYear == tempYear);

        // Disable Testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.disableTestingMode();
    }
}