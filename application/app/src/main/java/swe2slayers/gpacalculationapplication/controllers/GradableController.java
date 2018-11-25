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

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class GradableController {

    /**
     * Function that returns the percentage grade for this gradable
     * @param gradable The gradable to return the percentage for
     * @return Double value of the percentage
     */
    public static double calculatePercentageGrade(Gradable gradable){
        if(gradable.getTotal() > 0) {
            return (gradable.getMark() / gradable.getTotal() * 100);
        }else {
            return 0;
        }
    }

    /**
     * Function that calculates the letter grade for a gradable
     * @param gradable The gradable to calculate the percentage for
     * @return The letter corresponding to the percentage attained for the gradable
     */
    public static String calculateLetterGrade(Gradable gradable){
        if(gradable.getTotal() <= 0 || gradable.getMark() == -1) {
            return "N/G";
        }

        int percent = (int) calculatePercentageGrade(gradable);

        return FirebaseDatabaseHelper.getGrade(percent).getGrade();
    }

    /**
     * Function that returns the course associated with a gradable
     * @param gradable The gradable to get the course for
     * @return The course associated with the gradable
     */
    public static Course getCourseForGradable(Gradable gradable){

        for(Course course: FirebaseDatabaseHelper.getCourses()){
            if(course.getCourseId().equals(gradable.getCourseId())){
                return course;
            }
        }

        return null;
    }

    /**
     * Function that attaches a listener for a gradable
     * @param gradable The gradable to attach the listener to
     * @param listener The listener to attach
     */
    public static void attachGradableListener(Gradable gradable, ValueEventListener listener){
        if(gradable instanceof Exam){
            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("exams").orderByChild("id").equalTo(gradable.getId())
                    .addValueEventListener(listener);
        }else{
            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("assignments").orderByChild("id").equalTo(gradable.getId())
                    .addValueEventListener(listener);
        }
    }
}
