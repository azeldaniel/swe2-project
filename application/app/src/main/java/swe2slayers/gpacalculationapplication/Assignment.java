package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.File;
import java.util.Date;

public class Assignment extends GradedActivity {

    private File handout;

    public Assignment(String title) {
        super(title);
        this.handout = null;
    }

    public Assignment(String title, Date date) {
        super(title, date);
        this.handout = null;
    }

    public Assignment(String title, Date date, double weight) {
        super(title, date, weight);
        this.handout = null;
    }

    public Assignment(String title, Date date, double weight, double grade) {
        super(title, date, weight, grade);
        this.handout = null;
    }

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