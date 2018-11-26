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

package swe2slayers.gpacalculationapplication.utils;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Grade;
import swe2slayers.gpacalculationapplication.models.GradingSchema;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;

public class FirebaseDatabaseHelper {

    // Variable to hold whether in testing mode or not
    private static boolean testingMode = false;

    // Variable to hold whether the connection to the database is love or offline
    private static boolean online = false;

    // Singleton instance of firebase database
    private static FirebaseDatabase instance;

    // Lists to hold the years, semesters, courses, assignments, exams and grading schemes for a user
    private static List<Year> years = new ArrayList<>();
    private static List<Semester> semesters = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private static List<Assignment> assignments = new ArrayList<>();
    private static List<Exam> exams = new ArrayList<>();
    private static GradingSchema gradingSchema = new GradingSchema();

    /**
     * Function that returns the singleton instance of the firebase database
     * @return Singleton instance of the firebase database
     */
    public static FirebaseDatabase getFirebaseDatabaseInstance(){
        if(instance == null){
            instance = FirebaseDatabase.getInstance();
            instance.setPersistenceEnabled(true);
        }

        return instance;
    }

    /**
     * Function that returns whether or not testing mode is enabled
     * @return Whether or not the testing mode is enabled
     */
    public static boolean testingModeEnabled(){
        return testingMode;
    }

    /**
     * Function that enables testing mode
     */
    public static void enableTestingMode(){
        testingMode = true;
    }

    /**
     * Function that disables testing mode
     */
    public static void disableTestingMode(){
        testingMode = false;
    }

    /**
     * Function that returns whether the connection to the database is live
     * @return Whether the connection to the database is live
     */
    public static boolean isOnline() {
        return online;
    }

    /**
     * Function that loads the data for a user
     * @param user The user to load data for
     * @param closable The activity to close on completion (can be null)
     */
    public static void load(final User user, final Closable closable){

        if(!testingMode) {

            DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    online = snapshot.getValue(Boolean.class);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });

            final boolean complete[] = {false, false, false, false, false, false};

            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("years").orderByChild("userId").equalTo(user.getUserId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            years.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                years.add(snapshot.getValue(Year.class));
                            }

                            Sorter.sortYears(years);

                            complete[0] = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("semesters").orderByChild("userId").equalTo(user.getUserId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            semesters.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                semesters.add(snapshot.getValue(Semester.class));
                            }

                            Sorter.sortSemesters(semesters);

                            complete[1] = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("courses").orderByChild("userId").equalTo(user.getUserId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            courses.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                courses.add(snapshot.getValue(Course.class));
                            }

                            Sorter.sortCourses(courses);

                            complete[2] = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("assignments").orderByChild("userId").equalTo(user.getUserId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            assignments.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                assignments.add(snapshot.getValue(Assignment.class));
                            }

                            complete[3] = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("gradingSchemas").orderByChild("gradingSchemaId").equalTo(user.getGradingSchemaId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                gradingSchema = snapshot.getValue(GradingSchema.class);
                            }

                            complete[4] = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("exams").orderByChild("userId").equalTo(user.getUserId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            exams.clear();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                exams.add(snapshot.getValue(Exam.class));
                            }

                            complete[5] = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    boolean allComplete = true;

                    for (boolean b : complete) {
                        if (!b) {
                            allComplete = false;
                        }
                    }

                    if (allComplete) {
                        closable.close(user);
                    } else {
                        handler.postDelayed(this, 200);
                    }
                }
            }, 200);
        }else{

            gradingSchema = new GradingSchema();
            gradingSchema.setGradingSchemaId("default");

            HashMap<String, Grade> scheme = new HashMap<>();
            scheme.put("A+", new Grade("A+", 100, 90, 4.3));
            scheme.put("A", new Grade("A", 89.99, 80, 4.0));
            scheme.put("A-", new Grade("A-", 79.99, 75, 3.7));
            scheme.put("B+", new Grade("B+", 74.99, 70, 3.3));
            scheme.put("B", new Grade("B", 69.99, 65, 3.0));
            scheme.put("B-", new Grade("B-", 64.99, 60, 2.7));
            scheme.put("C+", new Grade("C+", 59.99, 55, 2.3));
            scheme.put("C", new Grade("C", 54.99, 50, 2.0));
            scheme.put("F1", new Grade("F1", 49.99, 40, 1.7));
            scheme.put("F2", new Grade("F2", 39.99, 30, 1.3));
            scheme.put("F3", new Grade("F3", 29.99, 0, 0.0));

            gradingSchema.setScheme(scheme);
        }
    }

    public static List<Year> getYears() {
        return years;
    }

    public static List<Semester> getSemesters() {
        return semesters;
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static List<Assignment> getAssignments() {
        return assignments;
    }

    public static List<Exam> getExams() {
        return exams;
    }

    public static GradingSchema getGradingSchema() {
        return gradingSchema;
    }

    public static Year getYear(String yearId){
        for(Year year: years){
            if(year.getYearId().equals(yearId)){
                return year;
            }
        }
        return null;
    }

    public static Semester getSemester(String semesterId){
        for(Semester semester: semesters){
            if(semester.getSemesterId().equals(semesterId)){
                return semester;
            }
        }
        return null;
    }

    public static Course getCourse(String courseId){
        for(Course course: courses){
            if(course.getCourseId().equals(courseId)){
                return course;
            }
        }
        return null;
    }

    public static Grade getGrade(double percent){
        for(Grade grade: FirebaseDatabaseHelper.getGradingSchema().getScheme().values()){
            if(percent <= grade.getMax() && percent >= grade.getMin()){
                return grade;
            }
        }
        return null;
    }

    /**
     * Function that adds a event listener to the online status
     * @param listener The listener to attach
     */
    public static void attachIsOnlineListener(ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference(".info/connected").addValueEventListener(listener);
    }
}
