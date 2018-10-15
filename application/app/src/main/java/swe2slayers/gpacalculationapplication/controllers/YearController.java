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
     * @param year The year to manipulate
     */
    public YearController(Year year) {
        this.year = year;
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


    public void addYearSemester(Semester semester){
        this.year.getSemesters().add(semester);
        this.notifyObservers();
    }


    public int getYearNum() {
        return this.year.getYearNum();
    }

    public void setYearNum(int yearNum) {
        this.year.setYearNum(yearNum);
        this.notifyObservers();
    }


    public ArrayList<Semester> getYearSemesters() {
        return this.year.getSemesters();
    }

    public void setYearSemesters(ArrayList<Semester> semesters) {
        this.year.setSemesters(semesters);
        this.notifyObservers();
    }


    public Date getYearStart() {
        return this.year.getStart();
    }

    public void setYearStart(Date start) {
        this.year.setStart(start);
        this.notifyObservers();
    }


    public Date getYearEnd() {
        return this.year.getEnd();
    }

    public void setYearEnd(Date end){
        this.year.setEnd(end);
        this.notifyObservers();
    }
}
