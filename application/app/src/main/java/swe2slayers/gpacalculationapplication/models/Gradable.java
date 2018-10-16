package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */


import java.io.Serializable;

import swe2slayers.gpacalculationapplication.utils.Date;

public abstract class Gradable implements Serializable {

	public String title;

	public Date date;

	public double weight;

	public double grade;

	public String note;

	/**
	 * Constructor that requires title
	 * @param title Title of the activity e.g. Course Work Exam 1
	 */
	public Gradable(String title){
		this.title = title;
		this.date = new Date();
		this.grade = 0;
		this.weight = 0;
		this.note = "";
	}

    /**
     * Constructor that requires title and date
     * @param title Title of the activity e.g. Course Work Exam 1
     * @param date Date of the activity
     */
	public Gradable(String title, Date date){
		this(title);
		this.date = date;
	}

    /**
     * Constructor that requires title, date and weight
     * @param title Title of the activity e.g. Course Work Exam 1
     * @param date Date of the activity
     * @param weight Weight of the activity as a percentage e.g. 15%
     */
	public Gradable(String title, Date date, double weight){
		this(title, date);
		this.weight = weight;
	}

    /**
     * Constructor that requires title, date, weight and grade
     * @param title Title of the activity e.g. Course Work Exam 1
     * @param date Date of the activity
     * @param weight Weight of the activity as a percentage e.g. 15%
     * @param grade The grade attained for the activity as a percentage e.g. 80%
     */
	public Gradable(String title, Date date, double weight, double grade){
		this(title, date, weight);
		this.grade = grade;
	}
}