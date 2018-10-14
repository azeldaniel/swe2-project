package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.Date;

public abstract class Gradable {

	private String title;

	private Date date;

	private double weight;

	private double grade;

	private String notes;

	/**
	 * Constructor that requires title
	 * @param title Title of the activity e.g. Course Work Exam 1
	 */
	public Gradable(String title){
		this.title = title;
		this.date = new Date();
		this.grade = 0;
		this.weight = 0;
		this.notes = "";
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

    /**
     * Function that calculates the grade for this activity. E.g. if grade is 80% and weight is 50%,
     * the weighted grade will be 40%
     * @return The weighted grade of this activity
     */
	public double calculateWeightedGrade(){
		return grade * weight;
	}


	public String getTitle(){
		return this.title;
	}

	public void setTitle(String title){
		this.title = title;
	}


	public Date getDate(){
		return this.date;
	}

	public void setDate(Date date){
		this.date = date;
	}


	public double getWeight(){
		return this.weight;
	}

	public void setWeight(double weight){
		this.weight = weight;
	}


	public double getGrade(){
		return this.grade;
	}

	public void setGrade(double grade){
		this.grade = grade;
	}


	public String getNotes(){
		return this.notes;
	}

	public void setNotes(String notes){
		this.notes = notes;
	}

}