package swe2slayers.gpacalculationapplication.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Globals;

public class UserController {

    /**
     * Function that returns the years associated with a user
     * @param user The user to get the years for
     * @return ArrayList of years for the user
     */
    public static ArrayList<Year> getYearsForUser(User user){

        ArrayList<Year> years = new ArrayList<>();

        for(Year year: Globals.getYears()){
            if(year.getUserId().equals(user.getUserId())){
                years.add(year);
            }
        }

        return years;
    }


    /**
     * Function that associates a year with a user
     * @param user The user to associate year with
     * @param year The year to associate with the user
     */
    public static void addYearForUser(User user, Year year){
        if(year != null){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            DatabaseReference yearRef = myRef.child("years").push();

            year.setYearId(yearRef.getKey());

            yearRef.setValue(year);
        }
    }

    /**
     * Function that updates a year already associated with a user
     * @param user The user to update the year for
     * @param year The year to update
     */
    public static void updateYearForUser(User user, Year year){
        if(year != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> updates = new HashMap<>();

            updates.put(year.getYearId(), year);

            myRef.child("years").updateChildren(updates);
        }
    }

    /**
     * Function that disassociates and removes a year from a user
     * @param user The user to disassociate year from
     * @param year The year to disassociate and remove
     */
    public static void removeYearForUser(User user, Year year){
        if(year != null){
            FirebaseDatabase.getInstance().getReference().child("years").child(year.getYearId()).setValue(null);
        }
    }


    /**
     * Function that associates a semester with a year
     * @param user The user to associate semester with
     * @param semester The semester to associate with the year
     */
    public static void addSemesterForUser(User user, Semester semester){
        if(semester != null){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            DatabaseReference semRef = myRef.child("semesters").push();

            semester.setSemesterId(semRef.getKey());

            semRef.setValue(semester);
        }
    }

    /**
     * Function that updates a semester already associated with a year
     * @param user The user to update the semester for
     * @param semester The semester to update
     */
    public static void updateSemesterForUser(User user, Semester semester){
        if(semester != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> updates = new HashMap<>();

            updates.put(semester.getSemesterId(), semester);

            myRef.child("semesters").updateChildren(updates);
        }
    }

    /**
     * Function that disassociates and removes a semester from a user
     * @param user The user to disassociate semester from
     * @param semester The year to remove
     */
    public static void removeSemesterForUser(User user, Semester semester){
        if(semester != null){
            FirebaseDatabase.getInstance().getReference().child("semesters").child(semester.getSemesterId()).setValue(null);
        }
    }


    /**
     * Function that associates a course with a user
     * @param user The user to associate the course with
     * @param course The course to associate with the user
     */
    public static void addCourseForUser(User user, Course course){
        if(course != null){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            DatabaseReference courRef = myRef.child("courses").push();

            course.setCourseId(courRef.getKey());

            courRef.setValue(course);
        }
    }

    /**
     * Function that updates a course already associated with a semester
     * @param user The user to update the course for
     * @param course The course to update
     */
    public static void updateCourseForUser(User user, Course course){
        if(course != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> updates = new HashMap<>();

            updates.put(course.getCourseId(), course);

            myRef.child("courses").updateChildren(updates);
        }
    }

    /**
     * Function that disassociates and removes a course from a user
     * @param user The user to disassociate course from
     * @param course The course to disassociate and remove
     */
    public static void removeCourseForUser(User user, Course course){
        if(course != null){
            FirebaseDatabase.getInstance().getReference().child("courses").child(course.getCourseId()).setValue(null);
        }
    }



    /**
     * Function that associates an assignment with a user
     * @param user The to associate the course with
     * @param assignment The assignment to associate with the user
     */
    public static void addAssignmentForUser(User user, Assignment assignment){
        if(assignment != null){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            DatabaseReference courRef = myRef.child("assignments").push();

            assignment.setId(courRef.getKey());

            courRef.setValue(assignment);
        }
    }

    /**
     * Function that updates an assignment already associated with a user
     * @param user The user to update the assignment for
     * @param assignment The assignment to update
     */
    public static void updateAssignmentForUser(User user, Assignment assignment){
        if(assignment != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> updates = new HashMap<>();

            updates.put(assignment.getId(), assignment);

            myRef.child("assignments").updateChildren(updates);
        }
    }

    /**
     * Function that disassociates and removes an assignment from a user
     * @param user The user to disassociate the assignment from
     * @param assignment The assignment to remove and disassociate
     */
    public static void removeAssignmentForUser(User user, Assignment assignment){
        if(assignment != null){
            FirebaseDatabase.getInstance().getReference().child("assignments").child(assignment.getId()).setValue(null);
        }
    }



    /**
     * Function that associates an exam with a user
     * @param user The user to associate the exam with
     * @param exam The exam to associate with the user
     */
    public static void addExamForUser(User user, Exam exam){
        if(exam != null){
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            DatabaseReference courRef = myRef.child("exams").push();

            exam.setId(courRef.getKey());

            courRef.setValue(exam);
        }
    }

    /**
     * Function that updates an exam already associated with a user
     * @param user The user to update the exam for
     * @param exam The exam to update
     */
    public static void updateExamForUser(User user, Exam exam){
        if(exam != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> updates = new HashMap<>();

            updates.put(exam.getId(), exam);

            myRef.child("exams").updateChildren(updates);
        }
    }

    /**
     * Function that disassociates anf removes an exam from a course
     * @param user The user to disassociate the exam from
     * @param exam The exam to remove and disassociate
     */
    public static void removeExamForUser(User user, Exam exam){
        if(exam != null){
            FirebaseDatabase.getInstance().getReference().child("exams").child(exam.getId()).setValue(null);
        }
    }


    /**
     * Function that adds a event listener to years of a user
     * @param listener The listener to attack
     */
    public static void attachYearsListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabase.getInstance().getReference().child("years").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to semester of a user
     * @param listener The listener to attack
     */
    public static void attachSemestersListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabase.getInstance().getReference().child("semesters").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to courses of a user
     * @param listener The listener to attack
     */
    public static void attachCoursesListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabase.getInstance().getReference().child("courses").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to assignments of a user
     * @param listener The listener to attack
     */
    public static void attachAssignmentsListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabase.getInstance().getReference().child("assignments").orderByChild("userId").equalTo(user.getUserId())
                .addValueEventListener(listener);
    }

    /**
     * Function that adds a event listener to exams of a user
     * @param listener The listener to attack
     */
    public static void attachExamsListenerForUser(User user, ValueEventListener listener){
        FirebaseDatabase.getInstance().getReference().child("exams").orderByChild("userId").equalTo(user.getUserId())
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
                            percent = (int) CourseController.calculatePercentageFinalGrade(course);
                        }else{
                            percent = (int) course.getFinalGrade();
                        }


                        qualityPoints += course.getCredits() * Globals.getGrade(percent).getGPA();
                        creditHours += course.getCredits();
                    }
                }
            }
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
                        percent = (int) CourseController.calculatePercentageFinalGrade(course);
                    }else{
                        percent = (int) course.getFinalGrade();
                    }


                    qualityPoints += course.getCredits() * Globals.getGrade(percent).getGPA();
                    creditHours += course.getCredits();
                }
            }
        }

        return qualityPoints/creditHours;
    }


    public static void save(User user){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
    }
}
