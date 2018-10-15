package swe2slayers.gpacalculationapplication.controllers;

import java.util.ArrayList;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.utils.Date;

public class SemesterController extends Observable {

    private Semester semester;

    /**
     * Constructor that requires semester
     * @param semester The semester to control
     */
    public SemesterController(Semester semester) {
        this.semester = semester;
    }

    /**
     * Function that returns the semester number e.g. 1
     * @return Integer value of the semester number
     */
    public int getSemesterNum() {
        return this.semester.semesterNum;
    }

    /**
     * Function that sets the semester number
     * @param semesterNum The new semester number to be set
     */
    public void setSemesterNum(int semesterNum) {
        this.semester.semesterNum = semesterNum;
        this.notifyObservers();
    }

    /**
     * Fucntion that returns the courses for the semester
     * @return ArrayList of Courses for the semester
     */
    public ArrayList<Course> getSemesterCourses() {
        return this.semester.courses;
    }

    /**
     * Function that adds a course to the semester
     * @param course The new course to add
     */
    public void addCourse(Course course){
        if(course != null) {
            this.semester.courses.add(course);
            this.notifyObservers();
        }
    }

    /**
     * Function to remove a course from the semester
     * @param course The course to remove
     */
    public void removeCourse(Course course){
        if(course != null) {
            this.semester.courses.remove(course);
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the starting date of the semester
     * @return The starting date of the semester
     */
    public Date getSemesterStart() {
        return this.semester.start;
    }

    /**
     * Function that sets the starting date of the semester
     * @param start The new starting date of the semester
     */
    public void setSemesterStart(Date start) {
        if(start != null) {
            this.semester.start = start;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the ending date of the semester
     * @return The ending date of the semester
     */
    public Date getSemesterEnd() {
        return this.semester.end;
    }

    /**
     * Function that sets the ending date of the semester
     * @param end The new ending date of the semester
     */
    public void setSemesterEnd(Date end) {
        if(end != null) {
            this.semester.end = end;
            this.notifyObservers();
        }
    }



    /**
     * TODO @Amanda
     * Function that calculates this semester's GPA for the user
     * @return Double value representing the GPA for this semester
     */
    public double calculateSemesterGPA(){

        double gpa = 0;

        for(Course course: this.getSemesterCourses()){
            //TODO
        }

        return gpa;
    }
}
