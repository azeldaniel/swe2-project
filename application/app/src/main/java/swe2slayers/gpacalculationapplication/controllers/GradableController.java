package swe2slayers.gpacalculationapplication.controllers;

import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.utils.Date;

public class GradableController extends Observable {

    protected Gradable gradable;

    /**
     * Contructor that requires a gradable
     * @param gradable The gradable to control
     */
    public GradableController(Gradable gradable) {
        this.gradable = gradable;
    }

    /**
     * Function that returns the title of the gradable
     * @return String value of the title of the gradable
     */
    public String getGradableTitle(){
        return this.gradable.title;
    }

    /**
     * Function that sets the title of the gradable
     * @param title The new title
     */
    public void setGradableTitle(String title){
        if(title != null) {
            this.gradable.title = title;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the date of the gradable
     * @return Date of the gradable
     */
    public Date getGradableDate(){
        return this.gradable.date;
    }

    /**
     * Function that sets the date
     * @param date The new date
     */
    public void setGradableDate(Date date){
        if(date != null) {
            this.gradable.date = date;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the weight
     * @return Double value of the weight
     */
    public double getGradableWeight(){
        return this.gradable.weight;
    }

    /**
     * Function that sets the weight
     * @param weight The new weight to be set
     */
    public void setGradableWeight(double weight){
        if(weight >= 0) {
            this.gradable.weight = weight;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the grade
     * @return Double value of the grade
     */
    public double getGradableGrade(){
        return this.gradable.grade;
    }

    /**
     * Function that sets the grade
     * @param grade The new grade to be set
     */
    public void setGradableGrade(double grade){
        if(grade >= 0) {
            this.gradable.grade = grade;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the optional note for the gradable
     * @return String value of the note
     */
    public String getNote(){
        return this.gradable.note;
    }

    /**
     * Function that sets the note
     * @param note The new note to be set
     */
    public void setNote(String note){
        if(note != null) {
            this.gradable.note = note;
            this.notifyObservers();
        }
    }

    /**
     * Function that calculates the grade for this activity. E.g. if grade is 80% and weight is 50%,
     * the weighted grade will be 40%
     * @return The weighted grade of this activity
     */
    public double calculateWeightedGrade(){
        return this.gradable.grade * this.gradable.weight;
    }
}
