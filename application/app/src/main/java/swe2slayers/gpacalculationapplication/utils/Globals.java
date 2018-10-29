package swe2slayers.gpacalculationapplication.utils;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class Globals {

    public interface Closable{
        public abstract void close(User user);
    }

    private static List<Year> years = new ArrayList<>();

    private static List<Semester> semesters = new ArrayList<>();

    private static List<Course> courses = new ArrayList<>();

    private static List<Assignment> assignments = new ArrayList<>();

    private static List<Exam> exams = new ArrayList<>();

    private static GradingSchema gradingSchema = new GradingSchema();

    public static void loadGlobals(final User user, final Closable closable){

        FirebaseDatabase.getInstance().getReference().child("years").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        years.clear();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            years.add(snapshot.getValue(Year.class));
                        }

                        Collections.sort(years, new Comparator<Year>() {
                            @Override
                            public int compare(Year y1, Year y2) {
                                return y1.getTitle().compareTo(y2.getTitle());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("semesters").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        semesters.clear();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            semesters.add(snapshot.getValue(Semester.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("courses").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        courses.clear();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            courses.add(snapshot.getValue(Course.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("assignments").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        assignments.clear();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            assignments.add(snapshot.getValue(Assignment.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("gradingSchemas").orderByChild("gradingSchemaId").equalTo(user.getGradingSchemaId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            gradingSchema = snapshot.getValue(GradingSchema.class);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("exams").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        exams.clear();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            exams.add(snapshot.getValue(Exam.class));
                        }

                        closable.close(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public static List<Year> getYears() {
        return years;
    }

    public static List<Semester> getSemesters() {
        return semesters;
    }

    public static Semester getSemester(String semesterId){
        for(Semester semester: semesters){
            if(semester.getSemesterId().equals(semesterId)){
                return semester;
            }
        }
        return null;
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static Course getCourse(String courseId){
        for(Course course: courses){
            if(course.getCourseId().equals(courseId)){
                return course;
            }
        }
        return null;
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

    public static Grade getGrade(double percent){
        for(Grade grade: Globals.getGradingSchema().getScheme().values()){
            if(percent <= grade.getMax() && percent >= grade.getMin()){
                return grade;
            }
        }
        return null;
    }
}
