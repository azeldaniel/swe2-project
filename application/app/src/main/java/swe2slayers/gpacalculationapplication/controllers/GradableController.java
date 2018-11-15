package swe2slayers.gpacalculationapplication.controllers;

import com.google.firebase.database.ValueEventListener;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class GradableController {

    /**
     *
     * @param gradable
     * @return
     */
    public static double calculatePercentageGrade(Gradable gradable){
        if(gradable.getTotal() != 0) {
            return (gradable.getMark() / gradable.getTotal() * 100);
        }else {
            return 0;
        }
    }

    /**
     *
     * @param gradable
     * @return
     */
    public static String calculateLetterGrade(Gradable gradable){
        if(gradable.getMark() == 0 || gradable.getTotal() == 0) {
            return "N/A";
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
