package swe2slayers.gpacalculationapplication.controllers;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Globals;

public class YearController {

    /**
     * Function that returns the semesters associated with a year
     * @param year The year to get semesters for
     * @return ArrayList of semesters for the year
     */
    public static ArrayList<Semester> getSemestersForYear(Year year){

        final ArrayList<Semester> semesters = new ArrayList<>();

        for(Semester semester : Globals.getSemesters()){
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


                qualityPoints += course.getCredits() * Globals.getGrade(percent).getGPA();
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
        FirebaseDatabase.getInstance().getReference().child("semesters").orderByChild("yearId").equalTo(year.getYearId())
                .addValueEventListener(listener);
    }
}
