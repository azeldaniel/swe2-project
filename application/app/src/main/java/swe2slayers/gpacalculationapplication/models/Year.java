package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.Serializable;
import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.utils.Date;

public class Year implements Serializable {

    private int yearNum;

    private ArrayList<Semester> semesters;

    private Date start;

    private Date end;

    /**
     * Constructor that requires yearNum
     * @param yearNum Which yearNum e.g. 2018
     */
    public Year(int yearNum) {
        this.yearNum = yearNum;
        this.semesters = new ArrayList<>();
        this.start = new Date();
        this.end = new Date();
    }

    /**
     * Constructor that requires yearNum, start and end
     * @param yearNum Which yearNum e.g. 2018
     * @param start Start date of the academic year
     * @param end End date of the academic year
     */
    public Year(int yearNum, Date start, Date end) {
        this(yearNum);
        this.start = start;
        this.end = end;
    }

    /**
     * TODO @Amanda
     * Function that calculates this year's GPA for the user
     * @return Double value representing the GPA for this year
     */
    public double calculateYearGPA(){
        double gpa = 0;

        for(Semester semester: this.getSemesters()){
            //TODO
        }

        return gpa;
    }


    public void addSemester(Semester semester){
        this.semesters.add(semester);
    }


    public int getYearNum() {
        return yearNum;
    }

    public void setYearNum(int yearNum) {
        this.yearNum = yearNum;
    }


    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }


    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }


    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
