package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.ArrayList;
import java.util.Date;

public class Year {

    private int year;

    private ArrayList<Semester> semesters;

    private Date start;

    private Date end;

    public Year(int year) {
        this.year = year;
        this.semesters = new ArrayList<>();
        this.start = new Date();
        this.end = new Date();
    }

    public Year(int year, Date start, Date end) {
        this(year);
        this.start = start;
        this.end = end;
    }

    public void addSemester(Semester semester){
        this.semesters.add(semester);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
