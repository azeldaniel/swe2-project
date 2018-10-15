package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.File;

import swe2slayers.gpacalculationapplication.utils.Date;

public class Assignment extends Gradable {

    private File handout;

    /**
     * Constructor that requires title
     * @param title Title of the assignment e.g. 'Assignment 2'
     */
    public Assignment(String title) {
        super(title);
        this.handout = null;//git test

    }

    /**
     * Constructor that requires title and date
     * @param title Title of the assignment e.g. 'Assignment 2'
     * @param date Due date of the assignment
     */
    public Assignment(String title, Date date) {
        super(title, date);
        this.handout = null;
    }

    /**
     * Constructor that requires title, date and weight
     * @param title Title of the assignment e.g. 'Assignment 2'
     * @param date Due date of the assignment
     * @param weight Weight of the assignment as a percentage e.g. 7.5%
     */
    public Assignment(String title, Date date, double weight) {
        super(title, date, weight);
        this.handout = null;
    }

    /**
     * Constructor that requires title, date, weight and grade. This should be used
     * in cases where the grade is already known to the user.
     * @param title Title of the assignment e.g. 'Assignment 2'
     * @param date Due date of the assignment
     * @param weight Weight of the assignment as a percentage e.g. 7.5%
     * @param grade The grade attained for the assignment as a percentage e.g. 80%
     */
    public Assignment(String title, Date date, double weight, double grade) {
        super(title, date, weight, grade);
        this.handout = null;
    }

    /**
     * Constructor that requires title, date, weight and handout. This should be used
     * in cases where the there is a handout for the assignment.
     * @param title Title of the assignment e.g. 'Assignment 2'
     * @param date Due date of the assignment
     * @param weight Weight of the assignment as a percentage e.g. 7.5%
     * @param handout Handout file for the assignment
     */
    public Assignment(String title, Date date, double weight, File handout) {
        super(title, date, weight);
        this.handout = handout;
    }

    public File getHandout() {
        return handout;
    }

    public void setHandout(File handout) {
        this.handout = handout;
    }
}