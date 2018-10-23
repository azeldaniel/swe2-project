package swe2slayers.gpacalculationapplication.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;

public class YearController extends Observable implements Serializable {

    private static final YearController instance = new YearController();

    private static final SemesterController semesterC = SemesterController.getInstance();

    /**
     * Private default constructor
     */
    private YearController() {}

    /**
     * Function that returns singleton instance
     * @return Singleton instance
     */
    public static YearController getInstance(){
        return instance;
    }

    /**
     * Function that returns the year number e.g. 2018
     * @return Year number as an integer
     */
    public String getYearTitle(Year year) {
        return year.title;
    }

    /**
     * Function that sets the year title
     * @param title Which year e.g. Academic Year 2018-2019
     */
    public void setYearTitle(Year year, String title) {
        if(title != null) {
            year.title = title;
            this.setChanged();
            this.notifyObservers(year);
        }
    }

    /**
     * Function that returns the list of semesters in the year
     * @return ArrayList of Semester objects for the year
     */
    public ArrayList<Semester> getYearSemesters(Year year) {
        return year.semesters;
    }

    /**
     * Function that adds a semester to the year
     * @param semester The semester to add
     */
    public void addSemester(Year year, Semester semester){
        if(semester != null) {
            year.semesters.add(semester);
            this.setChanged();
            this.notifyObservers(year);
        }
    }

    /**
     * Function that removes a given semester from the year
     * @param semester The semester to remove
     */
    public void removeSemester(Year year, Semester semester){
        if(semester != null) {
            year.semesters.remove(semester);
            this.setChanged();
            this.notifyObservers(year);
        }
    }

    /**
     * Function that return the starting date of the academic year
     * @return The starting date of the academic year
     */
    public Date getYearStart(Year year) {
        return year.start;
    }

    /**
     * Function that sets the start date
     * @param start The new start date of the academic year
     */
    public void setYearStart(Year year, Date start) {
        if(start != null) {
            year.start = start;
            this.setChanged();
            this.notifyObservers(year);
        }
    }

    /**
     * Function that returns the ending date of the academic year
     * @return The ending date of the academic year
     */
    public Date getYearEnd(Year year) {
        return year.end;
    }

    /**
     * Function that sets the end date
     * @param end The new end date of the academic year
     */
    public void setYearEnd(Year year, Date end){
        if(end != null) {
            year.end = end;
            this.setChanged();
            this.notifyObservers(year);
        }
    }

    /**
     * TODO Test
     * Function that calculates this year's GPA for the user
     * @return Double value representing the GPA for this year
     */
    public double calculateYearGPA(Year year){
        double gpa = 0;

        for(Semester semester: this.getYearSemesters(year)){
            gpa += semesterC.calculateSemesterGPA(semester);
        }

        return gpa;
    }
}
