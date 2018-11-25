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

public class YearController {

    /**
     * Function that returns a year's title with the start year and end year appended
     * @param year The year to return the appended title for
     * @return The year's title with the start year and end year appended
     */
    public static String getYearTitleWithYears(Year year){
        if(year.getStart() == null || year.getEnd() == null || year.getStart().getYear() == -1 ||
                year.getEnd().getYear() == -1){
            return year.getTitle();
        }

        if (year.getStart().getYear() == year.getEnd().getYear()) {
            return  year.getTitle() + " (" + year.getStart().getYear() + ")";
        } else {
            return year.getTitle() + " (" + year.getStart().getYear() + " - " + year.getEnd().getYear() + ")";
        }
    }

    /**
     * Function that returns the semesters associated with a year
     * @param year The year to get semesters for
     * @return ArrayList of semesters for the year
     */
    public static ArrayList<Semester> getSemestersForYear(Year year){

        final ArrayList<Semester> semesters = new ArrayList<>();

        for(Semester semester : FirebaseDatabaseHelper.getSemesters()){
            if(semester.getYearId().equals(year.getYearId())){
                semesters.add(semester);
            }
        }

        return semesters;
    }

    /**
     * Function that calculates a year's GPA
     * @param year The year to calculate the GPA for
     * @return Double value representing the GPA for the year
     */
    public static double calculateGpaForYear(Year year){
        double qualityPoints = 0;
        int creditHours = 0;
        for(Semester semester: getSemestersForYear(year)){

            for(Course course: SemesterController.getCoursesForSemester(semester)){
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
        }

        if(creditHours==0){
            return 0;
        }

        return qualityPoints/creditHours;
    }

    /**
     * Function that adds a event listener to courses for a year
     * @param year The year attach semester listener for
     * @param listener The listener to attach
     */
    public static void attachSemesterListenerForYear(Year year, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("semesters").orderByChild("yearId").equalTo(year.getYearId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to a year
     * @param year The year to associate the listener with
     * @param listener The listener to attack
     */
    public static void attachYearListener(Year year, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("years").orderByChild("yearId")
                .equalTo(year.getYearId()).addValueEventListener(listener);
    }
}
