package swe2slayers.gpacalculationapplication.controllers;

import java.util.ArrayList;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;

public class CourseController extends Observable {

    private static final CourseController instance = new CourseController();

    /**
     * Private default constructor
     */
    private CourseController() {}

    /**
     * Function that returns singleton instance
     * @return Singleton instance
     */
    public static CourseController getInstance(){
        return instance;
    }

    /**
     * Function that returns the course code for the course
     * @return String value of the course code
     */
    public String getCourseCode(Course course){
        return course.code;
    }

    /**
     * Function that sets the course code
     * @param code The new course code to be set
     */
    public void setCourseCode(Course course, String code){
        if(code != null) {
            course.code = code;
            this.setChanged();
            this.notifyObservers(course);
        }
    }

    /**
     * Function that returns the course name for the course
     * @return String value of the course name
     */
    public String getCourseName(Course course){
        return course.name;
    }

    /**
     * Function that sets the course name
     * @param name The new course name
     */
    public void setCourseName(Course course, String name){
        if(name != null) {
            course.name = name;
            this.setChanged();
            this.notifyObservers(course);
        }
    }

    /**
     * Function that returns the credit hours of a course
     * @return Integer value for the credit hours
     */
    public int getCourseCredits(Course course){
        return course.credits;
    }

    /**
     * Function that sets the course credits
     * @param credits The new credit hours
     */
    public void setCourseCredits(Course course, int credits){
        if(credits >= 0) {
            course.credits = credits;
            this.setChanged();
            this.notifyObservers(course);
        }
    }

    /**
     * Function that returns the final grade for the course
     * @return Double value for the final grade
     */
    public double getFinalGrade(Course course){
        if(course.finalGrade == 0){
            return this.calculateFinalGrade(course);
        }

        return course.finalGrade;
    }

    /**
     * Function that returns the Assignments of a course
     * @return ArrayList of Assignments for a course
     */
    public ArrayList<Gradable> getAssignments(Course course){
        ArrayList<Gradable> assignments = new ArrayList<>();

        for(Gradable gradable : course.gradables){
            if(gradable instanceof Assignment){
                assignments.add(gradable);
            }
        }

        return assignments;
    }

    /**
     * Function that returns the Exams of a course
     * @return ArrayList of Exams for a course
     */
    public ArrayList<Gradable> getExams(Course course){
        ArrayList<Gradable> assignments = new ArrayList<>();

        for(Gradable gradable : course.gradables){
            if(gradable instanceof Exam){
                assignments.add(gradable);
            }
        }

        return assignments;
    }

    /**
     * Function that adds an Assignment or Exam to a course
     * @param gradable New assignment or exam to add to the course
     */
    public void addGradable(Course course, Gradable gradable){
        if(gradable != null) {
            course.gradables.add(gradable);
            this.setChanged();
            this.notifyObservers(course);
        }
    }

    /**
     * Function that removes an assignment or exam from a course
     * @param gradable The assignment or exam to remove
     */
    public void removeGradable(Course course, Gradable gradable){
        if(gradable != null) {
            course.gradables.remove(gradable);
            this.setChanged();
            this.notifyObservers(course);
        }
    }

    /**
     * Function that returns the level of the course
     * @return Integer value of the level
     */
    public int getCourseLevel(Course course){
        return course.level;
    }

    /**
     * Function that sets the course level
     * @param level The new course level
     */
    public void setCourseLevel(Course course, int level){
        if(level >= 1) {
            course.level = level;
            this.setChanged();
            this.notifyObservers(course);
        }
    }

    /**
     * Function that returns the target grade for the course
     * @return Double value for the target grade
     */
    public double getCourseTargetGrade(Course course){
        return course.targetGrade;
    }

    /**
     * Function that sets the target grade
     * @param targetGrade The new target grade
     */
    public void setCourseTargetGrade(Course course, double targetGrade){
        if(targetGrade >= 0) {
            course.targetGrade = targetGrade;
            this.setChanged();
            this.notifyObservers(course);
        }
    }

    /**
     * Function that returns calculates the final grade
     *
     * TODO Refine this code
     *
     * @return Double value for the final grade
     */
    public double calculateFinalGrade(Course course){

        double finalGrade = 0;

        for(Gradable gradable : course.gradables){
            // TODO
        }

        return finalGrade;
    }

    /**
     * Function that calculates the minimum grade needed to pass the course
     *
     * TODO Refine this function
     *
     * @return double value of the minimum grade needed to pass the course or achieve their
     * target grade
     */
    public double calculateMinimumGrade(Course course){

        double finalGrade = this.calculateFinalGrade(course);

        double minimum = 0;

        if(finalGrade < 50){
            minimum = 50 - finalGrade;
        }

        return minimum;
    }
}
