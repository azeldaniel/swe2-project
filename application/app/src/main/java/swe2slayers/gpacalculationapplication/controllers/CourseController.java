package swe2slayers.gpacalculationapplication.controllers;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.models.Grade;
import swe2slayers.gpacalculationapplication.utils.Globals;

public class CourseController {

    /**
     * Function that returns the assignments associated with a course
     * @param course The course to get assignments for
     * @return ArrayList of assignments for the  course
     */
    public static ArrayList<Assignment> getAssignmentsForCourse(Course course){

        final ArrayList<Assignment> assignments = new ArrayList<>();

        for(Assignment assignment: Globals.getAssignments()){
            if(assignment.getCourseId().equals(course.getCourseId())){
                assignments.add(assignment);
            }
        }

        return assignments;
    }

    /**
     * Function that returns the exams associated with a course
     * @param course The course to get exams for
     * @return ArrayList of exams for the  course
     */
    public static ArrayList<Exam> getExamsForCourse(Course course){

        final ArrayList<Exam> exams = new ArrayList<>();

        for(Exam exam: Globals.getExams()){
            if(exam.getCourseId().equals(course.getCourseId())){
                exams.add(exam);
            }
        }

        return exams;
    }

    /**
     * Function that attaches a listener for the assignments of a course
     * @param course The course to attach the listener to
     * @param listener The listener to attach
     */
    public static void attachAssignmentsListenerForCourse(Course course, ValueEventListener listener){
        Globals.getFirebaseDatabaseInstance().getReference().child("assignments").orderByChild("courseId").equalTo(course.getCourseId())
                .addValueEventListener(listener);
    }

    /**
     * Function that attaches a listener for the exams of a course
     * @param course The course to attach the listener to
     * @param listener The listener to attach
     */
    public static void attachExamsListenerForCourse(Course course, ValueEventListener listener){
        Globals.getFirebaseDatabaseInstance().getReference().child("exams").orderByChild("courseId").equalTo(course.getCourseId())
                .addValueEventListener(listener);
    }

    /**
     * Function that returns the combined weights of the assignments and exams for a course
     * @param course The course to calculate total weights for
     * @return Double value for total weight
     */
    public static double calculateTotalWeights(Course course){
        double totalWeight = 0;

        List<Gradable> gradables = new ArrayList<>();

        gradables.addAll(getAssignmentsForCourse(course));
        gradables.addAll(getExamsForCourse(course));

        for(Gradable gradable : gradables){
            totalWeight += gradable.getWeight();
        }

        return totalWeight;
    }

    /**
     * Function that returns calculates the final mark for a course
     * @param course The course to calculate the final grade for
     * @return Double value for the final mark
     */
    public static double calculatePercentageFinalGrade(Course course){

        double finalGrade = 0;

        List<Gradable> gradables = new ArrayList<>();

        gradables.addAll(getAssignmentsForCourse(course));
        gradables.addAll(getExamsForCourse(course));

        for(Gradable gradable : gradables){
            finalGrade += (gradable.getMark()/gradable.getTotal()) * gradable.getWeight();
        }

        return finalGrade;
    }

    /**
     * Function that calculates the letter grade for a couse
     * @param course The course to calculate the final grade for
     * @return The letter grade associated with the final grade for the course
     */
    public static String calculateLetterFinalGrade(Course course){
        int percent = 0;

        if(course.getFinalGrade() == -1){
            percent = (int) calculatePercentageFinalGrade(course);
        }else{
            percent = (int) course.getFinalGrade();
        }

        for(Grade grade: Globals.getGradingSchema().getScheme().values()){
            if(percent <= grade.getMax() && percent >= grade.getMin()){
                return grade.getGrade();
            }
        }
        return String.valueOf(percent);
    }

    /**
     * Function that calculates the minimum mark needed to pass the course
     * @param course The course to get the minimum grade need for
     * @return double value of the minimum mark needed to pass the course or achieve their target mark
     */
    public static double calculateMinimumGrade(Course course){

        double finalGrade = calculatePercentageFinalGrade(course);

        double minimum = 0;

        if(finalGrade < 50){
            minimum = 50 - finalGrade;
        }

        return minimum;
    }
}
