package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.ArrayList;
import java.util.Date;

public class Exam extends Gradable {

    private String room;

    private int duration;

    private ArrayList<String> topics;

    /**
     * Constructor that requires title
     * @param title Title of the exam e.g. Course Work Exam 1
     */
    public Exam(String title) {
        super(title);
        this.room = "";
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    /**
     * Constructor that requires title and date
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     */
    public Exam(String title, Date date) {
        super(title, date);
        this.room = "";
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    /**
     * Constructor that requires title, date and weight
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     */
    public Exam(String title, Date date, double weight) {
        super(title, date, weight);
        this.room = "";
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    /**
     * Constructor that requires title, date, weight and grade
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     * @param grade The grade attained for the exam as a percentage e.g. 80%
     */
    public Exam(String title, Date date, double weight, double grade) {
        super(title, date, weight, grade);
        this.room = "";
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    /**
     * Constructor that requires title, date, weight and room
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     * @param room The room of the exam e.g. TCB 3
     */
    public Exam(String title, Date date, double weight, String room) {
        super(title, date, weight);
        this.room = room;
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    /**
     * Constructor that requires title, date, weight, room and duration
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     * @param room The room of the exam e.g. TCB 3
     * @param duration The length of the exam in minutes e.g. 120 minutes
     */
    public Exam(String title, Date date, double weight, String room, int duration) {
        super(title, date, weight);
        this.room = room;
        this.duration = duration;
    }

    /**
     * Constructor that requires title, date, weight, room, duration and topics
     * @param title Title of the exam e.g. Course Work Exam 1
     * @param date Date of the exam
     * @param weight Weight of the exam as a percentage e.g. 15%
     * @param room The room of the exam e.g. TCB 3
     * @param duration The length of the exam in minutes e.g. 120 minutes
     * @param topics List of topics that the exam will cover
     */
    public Exam(String title, Date date, double weight, String room, int duration, ArrayList<String> topics) {
        super(title, date, weight);
        this.room = room;
        this.duration = duration;
        this.topics = topics;
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


    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }
}