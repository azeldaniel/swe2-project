package swe2slayers.gpacalculationapplication.controllers;

import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.utils.Date;

public class GradableController extends Observable {

    /**
     * Default Constructor
     */
    public GradableController() {}

    /**
     * Function that returns the title of the gradable
     * @return String value of the title of the gradable
     */
    public String getGradableTitle(Gradable gradable){
        return gradable.title;
    }

    /**
     * Function that sets the title of the gradable
     * @param title The new title
     */
    public void setGradableTitle(Gradable gradable, String title){
        if(title != null) {
            gradable.title = title;
            this.setChanged();
            this.notifyObservers(gradable);
        }
    }

    /**
     * Function that returns the date of the gradable
     * @return Date of the gradable
     */
    public Date getGradableDate(Gradable gradable){
        return gradable.date;
    }

    /**
     * Function that sets the date
     * @param date The new date
     */
    public void setGradableDate(Gradable gradable, Date date){
        if(date != null) {
            gradable.date = date;
            this.setChanged();
            this.notifyObservers(gradable);
        }
    }

    /**
     * Function that returns the weight
     * @return Double value of the weight
     */
    public double getGradableWeight(Gradable gradable){
        return gradable.weight;
    }

    /**
     * Function that sets the weight
     * @param weight The new weight to be set
     */
    public void setGradableWeight(Gradable gradable, double weight){
        if(weight >= 0) {
            gradable.weight = weight;
            this.setChanged();
            this.notifyObservers(gradable);
        }
    }

    /**
     * Function that returns the mark
     * @return Double value of the mark
     */
    public double getGradableGrade(Gradable gradable){
        return gradable.mark;
    }

    /**
     * Function that sets the mark
     * @param grade The new mark to be set
     */
    public void setGradableGrade(Gradable gradable, double grade){
        if(grade >= 0) {
            gradable.mark = grade;
            this.setChanged();
            this.notifyObservers(gradable);
        }
    }

    /**
     * Function that returns the optional note for the gradable
     * @return String value of the note
     */
    public String getNote(Gradable gradable){
        return gradable.note;
    }

    /**
     * Function that sets the note
     * @param note The new note to be set
     */
    public void setNote(Gradable gradable, String note){
        if(note != null) {
            gradable.note = note;
            this.setChanged();
            this.notifyObservers(gradable);
        }
    }

    /**
     * Function that calculates the mark for this activity. E.g. if mark is 80% and weight is 50%,
     * the weighted mark will be 40%
     * @return The weighted mark of this activity
     */
    public double calculateWeightedGrade(Gradable gradable){
        return gradable.mark * gradable.weight;
    }
}
