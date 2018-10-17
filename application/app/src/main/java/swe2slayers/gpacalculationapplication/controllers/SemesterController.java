package swe2slayers.gpacalculationapplication.controllers;

import java.util.ArrayList;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.utils.Date;

public class SemesterController extends Observable {

    private static final SemesterController instance = new SemesterController();

    /**
     * Private default constructor
     */
    private SemesterController() {}

    /**
     * Function that returns singleton instance
     * @return Singleton instance
     */
    public static SemesterController getInstance(){
        return instance;
    }

    /**
     * Function that returns the semester  e.g. Semester 1
     * @return Which semester e.g Semester 1
     */
    public String getSemesterTitle(Semester semester) {
        return semester.title;
    }

    /**
     * Function that sets the semester number
     * @param title Which semester e.g Semester 1
     */
    public void setSemesterTitle(Semester semester, String title) {
        semester.title = title;
        this.setChanged();
        this.notifyObservers(semester);
    }

    /**
     * Fucntion that returns the courses for the semester
     * @return ArrayList of Courses for the semester
     */
    public ArrayList<Course> getSemesterCourses(Semester semester) {
        return semester.courses;
    }

    /**
     * Function that adds a course to the semester
     * @param course The new course to add
     */
    public void addCourse(Semester semester, Course course){
        if(course != null) {
            semester.courses.add(course);
            this.setChanged();
            this.notifyObservers(semester);
        }
    }

    /**
     * Function to remove a course from the semester
     * @param course The course to remove
     */
    public void removeCourse(Semester semester, Course course){
        if(course != null) {
            semester.courses.remove(course);
            this.setChanged();
            this.notifyObservers(semester);
        }
    }

    /**
     * Function that returns the starting date of the semester
     * @return The starting date of the semester
     */
    public Date getSemesterStart(Semester semester) {
        return semester.start;
    }

    /**
     * Function that sets the starting date of the semester
     * @param start The new starting date of the semester
     */
    public void setSemesterStart(Semester semester, Date start) {
        if(start != null) {
            semester.start = start;
            this.setChanged();
            this.notifyObservers(semester);
        }
    }

    /**
     * Function that returns the ending date of the semester
     * @return The ending date of the semester
     */
    public Date getSemesterEnd(Semester semester) {
        return semester.end;
    }

    /**
     * Function that sets the ending date of the semester
     * @param end The new ending date of the semester
     */
    public void setSemesterEnd(Semester semester, Date end) {
        if(end != null) {
            semester.end = end;
            this.setChanged();
            this.notifyObservers(semester);
        }
    }

    /**
     * TODO @Amanda
     * Function that calculates this semester's GPA for the user
     * @return Double value representing the GPA for this semester
     */
    public double calculateSemesterGPA(Semester semester){

        double gpa = 0;

        for(Course course: this.getSemesterCourses(semester)){
            //TODO
        }

        return gpa;
    }
}
