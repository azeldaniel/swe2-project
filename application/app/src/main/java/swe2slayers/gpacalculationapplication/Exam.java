package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.ArrayList;
import java.util.Date;

public class Exam extends Gradable{

    private String room;

    private int duration;

    private ArrayList<String> topics;

    public Exam(String title) {
        super(title);
        this.room = "";
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    public Exam(String title, Date date) {
        super(title, date);
        this.room = "";
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    public Exam(String title, Date date, double weight) {
        super(title, date, weight);
        this.room = "";
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    public Exam(String title, Date date, double weight, double grade) {
        super(title, date, weight, grade);
        this.room = "";
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    public Exam(String title, Date date, double weight, String room) {
        super(title, date, weight);
        this.room = room;
        this.duration = 0;
        this.topics = new ArrayList<>();
    }

    public Exam(String title, Date date, double weight, String room, int duration) {
        super(title, date, weight);
        this.room = room;
        this.duration = duration;
    }

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