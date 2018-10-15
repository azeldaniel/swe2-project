package swe2slayers.gpacalculationapplication.controllers;

import java.util.ArrayList;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;

public class YearController extends Observable {

    private Year year;

    /**
     * Constructor that requires year
     * @param year The year to control
     */
    public YearController(Year year) {
        this.year = year;
    }

    /**
     * Function that returns the year number e.g. 2018
     * @return Year number as an integer
     */
    public int getYearNum() {
        return this.year.yearNum;
    }

    /**
     * Function that sets the year number
     * @param yearNum Integer number representing the new year number to set
     */
    public void setYearNum(int yearNum) {
        this.year.yearNum = yearNum;
        this.notifyObservers();
    }

    /**
     * Function that returns the list of semesters in the year
     * @return ArrayList of Semester objects for the year
     */
    public ArrayList<Semester> getYearSemesters() {
        return this.year.semesters;
    }

    /**
     * Function that adds a semester to the year
     * @param semester The semester to add
     */
    public void addSemester(Semester semester){
        this.year.semesters.add(semester);
        this.notifyObservers();
    }

    /**
     * Function that removes a given semester from the year
     * @param semester The semester to remove
     */
    public void removeSemester(Semester semester){
        this.year.semesters.remove(semester);
        this.notifyObservers();
    }

    /**
     * Function that return the starting date of the academic year
     * @return The starting date of the academic year
     */
    public Date getYearStart() {
        return this.year.start;
    }

    /**
     * Function that sets the start date
     * @param start The new start date of the academic year
     */
    public void setYearStart(Date start) {
        if(start != null) {
            this.year.start = start;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the ending date of the academic year
     * @return The ending date of the academic year
     */
    public Date getYearEnd() {
        return this.year.end;
    }

    /**
     * Function that sets the end date
     * @param end The new end date of the academic year
     */
    public void setYearEnd(Date end){
        if(end != null) {
            this.year.end = end;
            this.notifyObservers();
        }
    }

    /**
     * TODO @Amanda
     * Function that calculates this year's GPA for the user
     * @return Double value representing the GPA for this year
     */
    public double calculateYearGPA(){
        double gpa = 0;

        for(Semester semester: this.getYearSemesters()){
            //TODO
        }

        return gpa;
    }





}
