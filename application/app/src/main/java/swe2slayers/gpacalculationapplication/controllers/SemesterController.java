package swe2slayers.gpacalculationapplication.controllers;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class SemesterController {

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
}
