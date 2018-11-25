/*
 * Copyright (c) 2018. Software Engineering Slayers
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Christopher Joseph (814000605)
 * Michael Bristol (816003612)
 * Maya Bannis (816000144)
 *
 * COMP 3613
 * Software Engineering II
 *
 * GPA Calculator Project
 */

package swe2slayers.gpacalculationapplication.models;

import java.io.Serializable;

public class Grade implements Serializable {

    private String grade;

    private double max;

    private double min;

    private double GPA;

    public Grade() {
    }

    /**
     * Constructor that requires the grade, max, min and GPA associated with a grade
     * @param grade The letter grade
     * @param max The maximum percentage corresponding to this grade
     * @param min The minimum percentage corresponding to this grade
     * @param GPA The GPA associated with the grade
     */
    public Grade(String grade, double max, double min, double GPA) {
        this.grade = grade;
        this.max = max;
        this.min = min;
        this.GPA = GPA;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }
}
