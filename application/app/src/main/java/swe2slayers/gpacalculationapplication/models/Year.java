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

import swe2slayers.gpacalculationapplication.utils.Date;

public class Year implements Serializable {

    private String yearId;

    private String title;

    private String userId;

    private Date start;

    private Date end;

    public Year() {}

    /**
     * Constructor that requires title
     * @param title Which year e.g. Year One
     * @param userId Which user
     */
    public Year(String title, String userId) {
        this();
        this.title = title;
        this.userId = userId;
        this.start = new Date();
        this.end = new Date();
    }

    /**
     * Constructor that requires title, start and end
     * @param title Which year e.g. Year One
     * @param userId Which user
     * @param start Start date of the academic year
     * @param end End date of the academic year
     */
    public Year(String title, String userId, Date start, Date end) {
        this(title, userId);
        this.start = start;
        this.end = end;
    }

    public String getYearId() {
        return yearId;
    }

    public void setYearId(String yearId) {
        this.yearId = yearId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    @Override
    public int hashCode() {
        return this.getYearId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.yearId.equals(((Year)obj).getYearId());
    }
}
