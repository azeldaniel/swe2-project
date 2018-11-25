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

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class SemesterController {

    /**
     * Function that returns a semester's title with the start year and end year appended
     * @param semester The semester to return the appended title for
     * @return The semester's title with the start year and end year appended
     */
    public static String getSemesterTitleWithYear(Semester semester){
        Year year = SemesterController.getYearForSemester(semester);
        if(year != null){
            return year.getTitle() + " " + semester.getTitle();
        }

        return semester.getTitle();
    }

    /**
     * Function that returns the courses associated with a semester
     * @param semester The semester to get courses for
     * @return ArrayList of courses for the semester
     */
    public static ArrayList<Course> getCoursesForSemester(Semester semester){

        final ArrayList<Course> courses = new ArrayList<>();

        for(Course course: FirebaseDatabaseHelper.getCourses()){
            if(course.getSemesterId().equals(semester.getSemesterId())){
                courses.add(course);
            }
        }

        return courses;
    }

    /**
     * Function that calculates this semester's GPA for the user
     * @return Double value representing the GPA for this semester
     */
    public static double calculateGpaForSemester(Semester semester){

        double qualityPoints = 0;
        int creditHours = 0;

        for(Course course: getCoursesForSemester(semester)){
            int percent = 0;

            if(course.getFinalGrade() == -1){
                if(CourseController.calculateTotalWeights(course)!= 100){
                    continue;
                }
                percent = (int) CourseController.calculatePercentageFinalGrade(course);
            }else{
                percent = (int) course.getFinalGrade();
            }

            qualityPoints += course.getCredits() * FirebaseDatabaseHelper.getGrade(percent).getGPA();
            creditHours += course.getCredits();
        }

        if(creditHours == 0){
            return 0;
        }

        return qualityPoints/creditHours;
    }

    /**
     * Function that returns the year associated with a semester
     * @param semester The semester to get the year for
     * @return The year associated with the semester
     */
    public static Year getYearForSemester(Semester semester){

        for(Year year: FirebaseDatabaseHelper.getYears()){
            if(year.getYearId().equals(semester.getYearId())){
                return year;
            }
        }

        return null;
    }

    /**
     * Function that adds a event listener to courses of a semester
     * @param semester The semester to associate the listener with
     * @param listener The listener to attack
     */
    public static void attachCoursesListenerForSemester(Semester semester, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("courses").orderByChild("semesterId")
                .equalTo(semester.getSemesterId()).addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to a semester
     * @param semester The semester to associate the listener with
     * @param listener The listener to attack
     */
    public static void attachSemesterListener(Semester semester, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("semesters").orderByChild("semesterId")
                .equalTo(semester.getSemesterId()).addValueEventListener(listener);
    }
}
