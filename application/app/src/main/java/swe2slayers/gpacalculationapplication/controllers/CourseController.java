package swe2slayers.gpacalculationapplication.controllers;

import java.util.ArrayList;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;

public class CourseController extends Observable {

    private Course course;

    /**
     * Constructor that requires course
     * @param course The course to control
     */
    public CourseController(Course course) {
        this.course = course;
    }

    /**
     * Function that returns the course code for the course
     * @return String value of the course code
     */
    public String getCourseCode(){
        return this.course.code;
    }

    /**
     * Function that sets the course code
     * @param code The new course code to be set
     */
    public void setCourseCode(String code){
        if(code != null) {
            this.course.code = code;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the course name for the course
     * @return String value of the course name
     */
    public String getCourseName(){
        return this.course.name;
    }

    /**
     * Function that sets the course name
     * @param name The new course name
     */
    public void setCourseName(String name){
        if(name != null) {
            this.course.name = name;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the credit hours of a course
     * @return Integer value for the credit hours
     */
    public int getCourseCredits(){
        return this.course.credits;
    }

    /**
     * Function that sets the course credits
     * @param credits The new credit hours
     */
    public void setCourseCredits(int credits){
        this.course.credits = credits;
        this.notifyObservers();
    }

    /**
     * Function that returns the final grade for the course
     * @return Double value for the final grade
     */
    public double getFinalGrade(){
        if(this.course.finalGrade == 0){
            return this.calculateFinalGrade();
        }

        return this.course.finalGrade;
    }

    /**
     * Function that returns the Assignments of a course
     * @return ArrayList of Assignments for a course
     */
    public ArrayList<Gradable> getAssignments(){
        ArrayList<Gradable> assignments = new ArrayList<>();

        for(Gradable gradable : this.course.gradables){
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
    public ArrayList<Gradable> getExams(){
        ArrayList<Gradable> assignments = new ArrayList<>();

        for(Gradable gradable : this.course.gradables){
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
    public void addGradable(Gradable gradable){
        this.course.gradables.add(gradable);
        this.notifyObservers();
    }

    /**
     * Function that removes an assignment or exam from a course
     * @param gradable The assignment or exam to remove
     */
    public void removeGradable(Gradable gradable){
        this.course.gradables.remove(gradable);
        this.notifyObservers();
    }

    /**
     * Function that returns the level of the course
     * @return Integer value of the level
     */
    public int getCourseLevel(){
        return this.course.level;
    }

    /**
     * Function that sets the course level
     * @param level The new course level
     */
    public void setCourseLevel(int level){
        this.course.level = level;
        this.notifyObservers();
    }

    /**
     * Function that returns the target grade for the course
     * @return Double value for the target grade
     */
    public double getCourseTargetGrade(){
        return this.course.targetGrade;
    }

    /**
     * Function that sets the target grade
     * @param targetGrade The new target grade
     */
    public void setCourseTargetGrade(double targetGrade){
        this.course.targetGrade = targetGrade;
        this.notifyObservers();
    }

    /**
     * Function that returns calculates the final grade
     * @return Double value for the final grade
     */
    public double calculateFinalGrade(){

        double finalGrade = 0;

        for(Gradable gradable : this.course.gradables){
            finalGrade += gradable.calculateWeightedGrade();
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
    public double calculateMinimumGrade(){

        double finalGrade = this.calculateFinalGrade();

        double minimum = 0;

        if(finalGrade < 50){
            minimum = 50 - finalGrade;
        }

        return minimum;
    }
}
