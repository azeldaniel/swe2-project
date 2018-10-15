package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.Serializable;
import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.utils.Date;

public class Year implements Serializable {

    public int yearNum;

    public ArrayList<Semester> semesters;

    public Date start;

    public Date end;

    /**
     * Constructor that requires yearNum
     * @param yearNum Which yearNum e.g. 2018
     */
    public Year(int yearNum) {
        super();
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
}
