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
import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.Time;

public class Exam extends Gradable implements Serializable {

    private String room;

    private int duration;

    private Time time;

    public Exam() {
    }

    /**
     * Constructor that requires title
     * @param id The id of the exam
     * @param title Title of the exam e.g. Course Work Exam 1
     */
    public Exam(String id, String title) {
        super(id, title);
        this.room = "";
        this.duration = -1;
        this.time = new Time();
    }

    /**
     * Constructor that requires title and date
     * @param id The id of the exam
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     */
    public Exam(String id, String title, Date date) {
        super(id, title, date);
        this.room = "";
        this.duration = -1;
        this.time = new Time();
    }

    /**
     * Constructor that requires title and date
     * @param id The id of the exam
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param time Time of the exam
     */
    public Exam(String id, String title, Date date, Time time) {
        super(id, title, date);
        this.room = "";
        this.duration = -1;
        this.time = time;
    }

    /**
     * Constructor that requires title, date and weight
     * @param id The id of the exam
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     */
    public Exam(String id, String title, Date date, double weight) {
        super(id, title, date, weight);
        this.room = "";
        this.duration = -1;
        this.time = new Time();
    }

    /**
     * Constructor that requires title, date, weight and mark
     * @param id The id of the exam
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     * @param mark The mark attained for the exam as a percentage e.g. 80%
     * @param total The total marks that could be achieved for this activity
     */
    public Exam(String id, String title, Date date, double weight, double mark, double total) {
        super(id, title, date, weight, mark, total);
        this.room = "";
        this.time = new Time();
        this.duration = -1;
    }

    /**
     * Constructor that requires title, date, weight and room
     * @param id The id of the exam
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     * @param room The room of the exam e.g. TCB 3
     */
    public Exam(String id, String title, Date date, double weight, String room) {
        super(id, title, date, weight);
        this.room = room;
        this.duration = -1;
        this.time = new Time();
    }

    /**
     * Constructor that requires title, date, weight, room and duration
     * @param id The id of the exam
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     * @param room The room of the exam e.g. TCB 3
     * @param duration The length of the exam in minutes e.g. 120 minutes
     */
    public Exam(String id, String title, Date date, double weight, String room, int duration) {
        super(id, title, date, weight);
        this.room = room;
        this.duration = duration;
        this.time = new Time();
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}