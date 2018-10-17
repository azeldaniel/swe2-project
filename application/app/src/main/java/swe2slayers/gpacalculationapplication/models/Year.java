package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.Serializable;
import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.utils.Date;

public class Year implements Serializable {

    public String title;

    public ArrayList<Semester> semesters;

    public Date start;

    public Date end;

    /**
     * Constructor that requires title
     * @param title Which year e.g. Academic Year 2018-2019
     */
    public Year(String title) {
        super();
        this.title = title;
        this.semesters = new ArrayList<>();
        this.start = new Date();
        this.end = new Date();
    }

    /**
     * Constructor that requires title, start and end
     * @param title Which year e.g. Academic Year 2018-2019
     * @param start Start date of the academic year
     * @param end End date of the academic year
     */
    public Year(String title, Date start, Date end) {
        this(title);
        this.start = start;
        this.end = end;
    }
}
