package swe2slayers.gpacalculationapplication.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;

import static org.junit.Assert.*;

public class CourseGradeCalculationTest {

    private swe2slayers.gpacalculationapplication.models.User user;
    Semester semester;
    Course course;
    Assignment assn1, assn2, assn3;
    Exam exam;
    private static boolean setup = false;

    @Before
    public void before() {
        // Enable testing mode and load so that grading scheme is set up
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.enableTestingMode();
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.load(user, null);

        user = new swe2slayers.gpacalculationapplication.models.User("S9oThHsvlAX8OVSBA0Xp09mNKMr2", "test@test.com", "First", "Last");

        semester = new Semester("Semester 3", null, user.getUserId());
        semester.setSemesterId("tempsemester3");

        course = new Course("COMP3613", "Software Engineering II", semester.getSemesterId(), user.getUserId(), 3, 3, -1);
        course.setCourseId("coursecontrollercourse");


        assn1 = new Assignment("testassignment1", "Assignment 1");
        assn1.setCourseId(course.getCourseId());
        assn1.setUserId(user.getUserId());
        assn1.setMark(20);
        assn1.setTotal(40);
        assn1.setWeight(20);

        assn2 = new Assignment("testassignment2", "Assignment 2");
        assn2.setCourseId(course.getCourseId());
        assn2.setUserId(user.getUserId());
        assn2.setMark(20);
        assn2.setTotal(40);
        assn2.setWeight(20);

        assn3 = new Assignment("testassignment3", "Assignment 3");
        assn3.setCourseId(course.getCourseId());
        assn3.setUserId(user.getUserId());
        assn3.setMark(20);
        assn3.setTotal(40);
        assn3.setWeight(20);

        exam = new Exam("testexam1", "Final Exam");
        exam.setCourseId(course.getCourseId());
        exam.setUserId(user.getUserId());
        exam.setMark(20);
        exam.setTotal(40);
        exam.setWeight(40);

        if (setup) return;
        UserController.addSemesterForUser(user, semester, null);
        UserController.addCourseForUser(user, course, null);
        UserController.addAssignmentForUser(user, assn1, null);
        UserController.addAssignmentForUser(user, assn2, null);
        UserController.addAssignmentForUser(user, assn3, null);
        UserController.addExamForUser(user, exam, null);
        setup = true;

    }

    @Test
    public void getAssignmentsForCourse() {
        java.util.ArrayList<Assignment> myAssns = new java.util.ArrayList<>();
        myAssns.add(assn1);
        myAssns.add(assn2);
        myAssns.add(assn3);

        java.util.ArrayList<Assignment> compareAssns = CourseController.getAssignmentsForCourse(course);

        for (int i = 0; i < 3; i++) {
            assertTrue(myAssns.get(i).getId().equals(compareAssns.get(i).getId()));
//            assertTrue(myAssns.get(i).getCourseId().equals(compareAssns.get(i).getCourseId()));
//            assertTrue(myAssns.get(i).getUserId().equals(compareAssns.get(i).getUserId()));
//            assertTrue(myAssns.get(i).getMark() == compareAssns.get(i).getMark());
//            assertTrue(myAssns.get(i).getTotal() == compareAssns.get(i).getTotal());
//            assertTrue(myAssns.get(i).getTitle().equals(compareAssns.get(i).getTitle()));
        }
    }

    @Test
    public void getExamsForCourse() {
        java.util.ArrayList<Exam> myExams = new java.util.ArrayList<>();
        myExams.add(exam);

        java.util.ArrayList<Exam> compareExams = CourseController.getExamsForCourse(course);

        for (int i = 0; i < 1; i++) {
            assertTrue(myExams.get(i).getId().equals(compareExams.get(i).getId()));
//            assertTrue(myExams.get(i).getCourseId().equals(compareExams.get(i).getCourseId()));
//            assertTrue(myExams.get(i).getUserId().equals(compareExams.get(i).getUserId()));
//            assertTrue(myExams.get(i).getMark() == compareExams.get(i).getMark());
//            assertTrue(myExams.get(i).getTotal() == compareExams.get(i).getTotal());
//            assertTrue(myExams.get(i).getTitle().equals(compareExams.get(i).getTitle()));
        }

    }

    @Test
    public void getSemesterForCourse() {
        Semester tempSemester = CourseController.getSemesterForCourse(course);
        assertTrue(tempSemester.getSemesterId().equals(semester.getSemesterId()));
    }

    @Test
    public void calculateTotalWeights() {
        double total = assn1.getWeight() + assn2.getWeight() + assn3.getWeight() + exam.getWeight();
        assertTrue(total == 100);
        assertTrue(total == CourseController.calculateTotalWeights(course));
    }

    @Test
    public void calculatePercentageFinalGrade() {
        assertTrue(50 == CourseController.calculatePercentageFinalGrade(course));
    }

    @Test
    public void calculateAverage() {
        assertTrue(50 == CourseController.calculateAverage(course));
    }

    @Test
    public void calculateLetterAverage() {
        assertTrue("C" == CourseController.calculateLetterAverage(course));
    }

    @Test
    public void calculateLetterFinalGrade() {
        assertTrue("C" == CourseController.calculateLetterFinalGrade(course));
    }

    @Test
    public void calculateMinimumGrade() {
        assn1.setWeight(10); // Instead of 20. Now 10% is up in the air
        UserController.updateAssignmentForUser(user, assn1, null);
        course.setTargetGrade(50);
        course.setFinalGrade(-1); // Depending on previous calculations, I need to set this to -1. (The course may have been assigned a final grade through other tests)

        double minimumGrade = CourseController.calculateMinimumGrade(course);
        assertTrue(50 == minimumGrade);

        assn1.setWeight(20); // Return to previous value
        UserController.updateAssignmentForUser(user, assn1, null);
    }

    @After
    public void after() {
        // Disable Testing Mode
        swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper.disableTestingMode();
    }
}