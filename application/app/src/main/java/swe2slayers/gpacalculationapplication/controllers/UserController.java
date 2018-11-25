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

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class UserController {

    /**
     * Function that returns the years associated with a user
     * @param user The user to get the years for
     * @return ArrayList of years for the user
     */
    public static ArrayList<Year> getYearsForUser(User user){

        ArrayList<Year> years = new ArrayList<>();

        for(Year year: FirebaseDatabaseHelper.getYears()){
            if(year.getUserId().equals(user.getUserId())){
                years.add(year);
            }
        }

        return years;
    }

    /**
     * Function that returns the semesters associated with a user
     * @param user The user to get the years for
     * @return ArrayList of semesters for the user
     */
    public static ArrayList<Semester> getSemestersForUser(User user){

        ArrayList<Semester> semesters = new ArrayList<>();

        for(Semester semester: FirebaseDatabaseHelper.getSemesters()){
            if(semester.getUserId().equals(user.getUserId())){
                semesters.add(semester);
            }
        }

        return semesters;
    }

    /**
     * Function that associates a year with a user
     * @param user The user to associate year with
     * @param year The year to associate with the user
     * @param closable The activity to close on completion (can be null)
     */
    public static void addYearForUser(final User user, Year year, final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            FirebaseDatabaseHelper.getYears().add(year);
        }else {
            if (year != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                DatabaseReference yearRef = myRef.child("years").push();

                year.setYearId(yearRef.getKey());

                yearRef.setValue(year).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if(!FirebaseDatabaseHelper.isOnline()){
                    if(closable != null){
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that updates a year already associated with a user
     * @param user The user to update the year for
     * @param year The year to update
     * @param closable The activity to close on completion (can be null)
     */
    public static void updateYearForUser(final User user, Year year, final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getYears().contains(year)){
                FirebaseDatabaseHelper.getYears()
                        .set(FirebaseDatabaseHelper.getYears().indexOf(year), year);
            }
        }else {
            if (year != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                HashMap<String, Object> updates = new HashMap<>();

                updates.put(year.getYearId(), year);

                myRef.child("years").updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if(!FirebaseDatabaseHelper.isOnline()){
                    if(closable != null){
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that disassociates and removes a year from a user
     * @param user The user to disassociate year from
     * @param year The year to disassociate and remove
     * @param closable The activity to close on completion (can be null)
     */
    public static void removeYearForUser(final User user, Year year,
                                         final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getYears().contains(year)){
                FirebaseDatabaseHelper.getYears().remove(year);
            }
        }else {
            if (year != null) {
                FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("years").child(year.getYearId()).setValue(null);

                YearController.attachSemesterListenerForYear(year, new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot sem : dataSnapshot.getChildren()) {
                            removeSemesterForUser(user, sem.getValue(Semester.class), null);
                        }

                        if (closable != null) {
                            closable.close(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(!FirebaseDatabaseHelper.isOnline()){
                    if(closable != null){
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that associates a semester with a year
     * @param user The user to associate semester with
     * @param semester The semester to associate with the year
     * @param closable The activity to close on completion (can be null)
     */
    public static void addSemesterForUser(final User user, Semester semester,
                                          final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            FirebaseDatabaseHelper.getSemesters().add(semester);
        }else {
            if (semester != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                DatabaseReference semRef = myRef.child("semesters").push();

                semester.setSemesterId(semRef.getKey());

                semRef.setValue(semester).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that updates a semester already associated with a year
     * @param user The user to update the semester for
     * @param semester The semester to update
     * @param closable The activity to close on completion (can be null)
     */
    public static void updateSemesterForUser(final User user, Semester semester,
                                             final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getSemesters().contains(semester)){
                FirebaseDatabaseHelper.getSemesters()
                        .set(FirebaseDatabaseHelper.getSemesters().indexOf(semester), semester);
            }
        }else {
            if (semester != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                HashMap<String, Object> updates = new HashMap<>();

                updates.put(semester.getSemesterId(), semester);

                myRef.child("semesters").updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that disassociates and removes a semester from a user
     * @param user The user to disassociate semester from
     * @param semester The year to remove
     * @param closable The activity to close on completion (can be null)
     */
    public static void removeSemesterForUser(final User user, Semester semester,
                                             final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getSemesters().contains(semester)){
                FirebaseDatabaseHelper.getSemesters().remove(semester);
            }
        }else {
            if (semester != null) {
                FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("semesters").child(semester.getSemesterId()).setValue(null);

                SemesterController.attachCoursesListenerForSemester(semester, new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot sem : dataSnapshot.getChildren()) {
                            removeCourseForUser(user, sem.getValue(Course.class), null);
                        }

                        if (closable != null) {
                            closable.close(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that associates a course with a user
     * @param user The user to associate the course with
     * @param course The course to associate with the user
     * @param closable The activity to close on completion (can be null)
     */
    public static void addCourseForUser(final User user, Course course,
                                        final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            FirebaseDatabaseHelper.getCourses().add(course);
        }else {
            if (course != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                DatabaseReference courRef = myRef.child("courses").push();

                course.setCourseId(courRef.getKey());

                courRef.setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that updates a course already associated with a semester
     * @param user The user to update the course for
     * @param course The course to update
     * @param closable The activity to close on completion (can be null)
     */
    public static void updateCourseForUser(final User user, Course course,
                                           final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getCourses().contains(course)){
                FirebaseDatabaseHelper.getCourses()
                        .set(FirebaseDatabaseHelper.getCourses().indexOf(course), course);
            }
        }else {
            if (course != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                HashMap<String, Object> updates = new HashMap<>();

                updates.put(course.getCourseId(), course);

                myRef.child("courses").updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that disassociates and removes a course from a user
     * @param user The user to disassociate course from
     * @param course The course to disassociate and remove
     * @param closable The activity to close on completion (can be null)
     */
    public static void removeCourseForUser(final User user, Course course,
                                           final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getCourses().contains(course)){
                FirebaseDatabaseHelper.getCourses().remove(course);
            }
        }else {
            if (course != null) {
                FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("courses").child(course.getCourseId()).setValue(null);

                final boolean done[] = {false, false};

                if (CourseController.getExamsForCourse(course).size() > 0) {
                    CourseController.attachExamsListenerForCourse(course, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot exam : dataSnapshot.getChildren()) {
                                exam.getRef().setValue(null);
                            }

                            if (done[1]) {
                                if (closable != null) {
                                    closable.close(user);
                                }
                            } else {
                                done[0] = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    done[0] = true;
                }

                if (CourseController.getAssignmentsForCourse(course).size() > 0) {
                    CourseController.attachAssignmentsListenerForCourse(course, new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot assignment : dataSnapshot.getChildren()) {
                                assignment.getRef().setValue(null);
                            }

                            if (done[0]) {
                                if (closable != null) {
                                    closable.close(user);
                                }
                            } else {
                                done[1] = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    done[1] = true;
                }

                if (done[0] && done[1] && closable != null) {
                    closable.close(user);
                }

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that associates an assignment with a user
     * @param user The to associate the course with
     * @param assignment The assignment to associate with the user
     * @param closable The activity to close on completion (can be null)
     */
    public static void addAssignmentForUser(final User user, Assignment assignment,
                                            final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            FirebaseDatabaseHelper.getAssignments().add(assignment);
        }else {
            if (assignment != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                DatabaseReference courRef = myRef.child("assignments").push();

                assignment.setId(courRef.getKey());

                courRef.setValue(assignment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that updates an assignment already associated with a user
     * @param user The user to update the assignment for
     * @param assignment The assignment to update
     * @param closable The activity to close on completion (can be null)
     */
    public static void updateAssignmentForUser(final User user, Assignment assignment,
                                               final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getAssignments().contains(assignment)){
                FirebaseDatabaseHelper.getAssignments()
                        .set(FirebaseDatabaseHelper.getAssignments().indexOf(assignment), assignment);
            }
        }else {
            if (assignment != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                HashMap<String, Object> updates = new HashMap<>();

                updates.put(assignment.getId(), assignment);

                myRef.child("assignments").updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that disassociates and removes an assignment from a user
     * @param user The user to disassociate the assignment from
     * @param assignment The assignment to remove and disassociate
     * @param closable The activity to close on completion (can be null)
     */
    public static void removeAssignmentForUser(final User user, Assignment assignment,
                                               final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getAssignments().contains(assignment)){
                FirebaseDatabaseHelper.getAssignments().remove(assignment);
            }
        }else {
            if (assignment != null) {
                FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("assignments")
                        .child(assignment.getId()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }



    /**
     * Function that associates an exam with a user
     * @param user The user to associate the exam with
     * @param exam The exam to associate with the user
     * @param closable The activity to close on completion (can be null)
     */
    public static void addExamForUser(final User user, Exam exam,
                                      final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            FirebaseDatabaseHelper.getExams().add(exam);
        }else {
            if (exam != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                DatabaseReference courRef = myRef.child("exams").push();

                exam.setId(courRef.getKey());

                courRef.setValue(exam).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that updates an exam already associated with a user
     * @param user The user to update the exam for
     * @param exam The exam to update
     * @param closable The activity to close on completion (can be null)
     */
    public static void updateExamForUser(final User user, Exam exam,
                                         final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getExams().contains(exam)){
                FirebaseDatabaseHelper.getExams()
                        .set(FirebaseDatabaseHelper.getExams().indexOf(exam), exam);
            }
        }else {
            if (exam != null) {
                DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();

                HashMap<String, Object> updates = new HashMap<>();

                updates.put(exam.getId(), exam);

                myRef.child("exams").updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that disassociates anf removes an exam from a course
     * @param user The user to disassociate the exam from
     * @param exam The exam to remove and disassociate
     * @param closable The activity to close on completion (can be null)
     */
    public static void removeExamForUser(final User user, Exam exam,
                                         final Closable closable){
        if(FirebaseDatabaseHelper.testingModeEnabled()){
            if(FirebaseDatabaseHelper.getExams().contains(exam)){
                FirebaseDatabaseHelper.getExams().remove(exam);
            }
        }else {
            if (exam != null) {
                FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("exams").child(exam.getId())
                        .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(closable != null) {
                            closable.close(user);
                        }
                    }
                });

                if (!FirebaseDatabaseHelper.isOnline()) {
                    if (closable != null) {
                        closable.close(user);
                    }
                }
            }
        }
    }

    /**
     * Function that adds a event listener to a user
     * @param listener The listener to attack
     */
    public static void attachUserListener(User user, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("users").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }


    /**
     * Function that adds a event listener to years of a user
     * @param listener The listener to attack
     */
    public static void attachYearsListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("years").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to semester of a user
     * @param listener The listener to attack
     */
    public static void attachSemestersListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("semesters").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to courses of a user
     * @param listener The listener to attack
     */
    public static void attachCoursesListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("courses").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to assignments of a user
     * @param listener The listener to attack
     */
    public static void attachAssignmentsListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("assignments").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to exams of a user
     * @param listener The listener to attack
     */
    public static void attachExamsListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("exams").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that calculates the degree GPA for a user
     * @return Double value for the degree GPA of a user
     */
    public static double calculateDegreeGPA(User user){
        double qualityPoints = 0;
        int creditHours = 0;

        for(Year year: getYearsForUser(user)){
            for(Semester semester : YearController.getSemestersForYear(year)){
                for(Course course : SemesterController.getCoursesForSemester(semester)){
                    if(course.getLevel() > 1){
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
            }
        }

        if(creditHours == 0){
            return 0;
        }

        return qualityPoints/creditHours;
    }

    /**
     * Function that calculates the cumulative GPA for a user
     * @return Double value for the cumulative GPA of a user
     */
    public static double calculateCumulativeGPA(User user){
        double qualityPoints = 0;
        int creditHours = 0;

        for(Year year: getYearsForUser(user)){
            for(Semester semester : YearController.getSemestersForYear(year)){
                for(Course course : SemesterController.getCoursesForSemester(semester)){
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
        }

        if(creditHours == 0){
            return 0;
        }

        return qualityPoints/creditHours;
    }

    /**
     * Function that saves a user's data to the database
     * @param user The user to save data for
     * @param closable The activity to close on completion (can be null)
     */
    public static void save(final User user, final Closable closable){
        DatabaseReference myRef = FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference();
        myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(closable != null){
                    closable.close(user);
                }
            }
        });
    }
}