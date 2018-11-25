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
import java.util.List;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.models.Grade;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class CourseController {

    /**
     * Function that returns the assignments associated with a course
     * @param course The course to get assignments for
     * @return ArrayList of assignments for the  course
     */
    public static ArrayList<Assignment> getAssignmentsForCourse(Course course){

        final ArrayList<Assignment> assignments = new ArrayList<>();

        for(Assignment assignment: FirebaseDatabaseHelper.getAssignments()){
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

        for(Exam exam: FirebaseDatabaseHelper.getExams()){
            if(exam.getCourseId().equals(course.getCourseId())){
                exams.add(exam);
            }
        }

        return exams;
    }

    /**
     * Function that attaches a listener for the a course
     * @param course The course to attach the listener to
     * @param listener The listener to attach
     */
    public static void attachCourseListener(Course course, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("courses").orderByChild("courseId").equalTo(course.getCourseId())
                .addValueEventListener(listener);
    }

    /**
     * Function that attaches a listener for the assignments of a course
     * @param course The course to attach the listener to
     * @param listener The listener to attach
     */
    public static void attachAssignmentsListenerForCourse(Course course, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("assignments").orderByChild("courseId").equalTo(course.getCourseId())
                .addValueEventListener(listener);
    }

    /**
     * Function that attaches a listener for the exams of a course
     * @param course The course to attach the listener to
     * @param listener The listener to attach
     */
    public static void attachExamsListenerForCourse(Course course, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("exams").orderByChild("courseId").equalTo(course.getCourseId())
                .addValueEventListener(listener);
    }

    /**
     * Function that returns the semester associated with a course
     * @param course The course to get the semester for
     * @return The semester associated with the course
     */
    public static Semester getSemesterForCourse(Course course){

        for(Semester semester: FirebaseDatabaseHelper.getSemesters()){
            if(semester.getSemesterId().equals(course.getSemesterId())){
                return semester;
            }
        }

        return null;
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
            if(gradable.getTotal() != 0 && gradable.getMark() != -1 && gradable.getWeight() != -1) {
                totalWeight += gradable.getWeight();
            }
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
            if(gradable.getTotal() != 0 && gradable.getMark() != -1 && gradable.getWeight() != -1) {
                finalGrade += (gradable.getMark() / gradable.getTotal()) * gradable.getWeight();
            }
        }

        return finalGrade;
    }

    /**
     * Function that calculates the average grade for a course
     * @param course The course to calculate the average grade for
     * @return Double value for the average grade for the course
     */
    public static double calculateAverage(Course course){
        if(course.getFinalGrade() == -1) {

            if(calculateTotalWeights(course) == 0){
                return -1;
            }

            List<Gradable> gradables = new ArrayList<>();

            gradables.addAll(getAssignmentsForCourse(course));
            gradables.addAll(getExamsForCourse(course));

            double courseGrade = 0;

            for (Gradable gradable : gradables) {
                if(gradable.getTotal() != 0 && gradable.getMark() != -1 && gradable.getWeight() != -1) {
                    courseGrade += ((gradable.getMark() / gradable.getTotal()) * gradable.getWeight());
                }
            }

            return courseGrade / calculateTotalWeights(course) * 100;
        }else{
            return course.getFinalGrade();
        }
    }

    /**
     * Function that calculates the letter grade for a couse
     * @param course The course to calculate the final grade for
     * @return The letter grade associated with the final grade for the course
     */
    public static String calculateLetterAverage(Course course){
        int avg = 0;

        if(course.getFinalGrade() == -1){
            if(calculateTotalWeights(course) == 0){
                return "N/G";
            }else {
                avg = (int) calculateAverage(course);
            }
        }else{
            avg = (int) course.getFinalGrade();
        }

        for(Grade grade: FirebaseDatabaseHelper.getGradingSchema().getScheme().values()){
            if(avg <= grade.getMax() && avg >= grade.getMin()){
                return grade.getGrade();
            }
        }
        return String.valueOf(avg);
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

        for(Grade grade: FirebaseDatabaseHelper.getGradingSchema().getScheme().values()){
            if(percent <= grade.getMax() && percent >= grade.getMin()){
                return grade.getGrade();
            }
        }
        return String.valueOf(percent);
    }

    /**
     * Function that calculates the minimum average grade needed to achieve a user's target grade
     * @param course The course to get the minimum average grade needed
     * @return Double variable holding the average minimum grade needed to achieve the course's
     * target grade if it is possible to achieve. -1 if it is has already been achieved. -2 if it is
     * not possible to achieve.
     */
    public static double calculateMinimumGrade(Course course){

        if(course.getFinalGrade() != -1){

            if(course.getFinalGrade() >= course.getTargetGrade()){
                return -1;
            }else {
                return -2;
            }
        }

        double courseGrade = 0;

        List<Gradable> gradables = new ArrayList<>();

        gradables.addAll(getAssignmentsForCourse(course));
        gradables.addAll(getExamsForCourse(course));

        for(Gradable gradable : gradables){
            if(gradable.getTotal() != 0 && gradable.getMark() != -1 && gradable.getWeight() != -1) {
                courseGrade += ((gradable.getMark() / gradable.getTotal()) * gradable.getWeight());
            }
        }

        if(courseGrade >= course.getTargetGrade()){
            return -1;
        }else{

            double weightLeft = 100 - calculateTotalWeights(course);

            double maxGradePossible = courseGrade + weightLeft;

            if(maxGradePossible < course.getTargetGrade()){
                return -2;
            }else{
                return (weightLeft - (maxGradePossible - course.getTargetGrade())) / weightLeft * 100;
            }
        }
    }
}
