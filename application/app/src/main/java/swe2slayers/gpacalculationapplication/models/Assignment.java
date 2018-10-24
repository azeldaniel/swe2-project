package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.File;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import swe2slayers.gpacalculationapplication.utils.Date;

public class Assignment extends Gradable implements Serializable {

    public Assignment() {
    }

    /**
     * Constructor that requires title
     * @param id The id of the assignment
     * @param title Title of the assignment e.g. 'Assignment 2'
     */
    public Assignment(String id, String title) {
        super(id, title);

    }

    /**
     * Constructor that requires title and date
     * @param id The id of the assignment
     * @param title Title of the assignment e.g. 'Assignment 2'
     * @param date Due date of the assignment
     */
    public Assignment(String id, String title, Date date) {
        super(id, title, date);
    }

    /**
     * Constructor that requires title, date and weight
     * @param id The id of the assignment
     * @param title Title of the assignment e.g. 'Assignment 2'
     * @param date Due date of the assignment
     * @param weight Weight of the assignment as a percentage e.g. 7.5%
     */
    public Assignment(String id, String title, Date date, double weight) {
        super(id, title, date, weight);
    }

    /**
     * Constructor that requires title, date, weight and mark. This should be used
     * in cases where the mark is already known to the user.
     * @param id The id of the assignment
     * @param title Title of the assignment e.g. 'Assignment 2'
     * @param date Due date of the assignment
     * @param weight Weight of the assignment as a percentage e.g. 7.5%
     * @param mark The mark attained for the assignment
     * @param total The total marks that could be achieved for this activity
     */
    public Assignment(String id, String title, Date date, double weight, double mark, double total) {
        super(id, title, date, weight, mark, total);
    }

    /**
     * Constructor that requires title, date, weight and handout. This should be used
     * in cases where the there is a handout for the assignment.
     * @param id The id of the assignment
     * @param title Title of the assignment e.g. 'Assignment 2'
     * @param date Due date of the assignment
     * @param weight Weight of the assignment as a percentage e.g. 7.5%
     * @param handout Handout file for the assignment
     */
    public Assignment(String id, String title, Date date, double weight, File handout) {
        super(id, title, date, weight);
    }
}