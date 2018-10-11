package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.Date;

public abstract class GradedActivity {

	private String title;

	private Date date;

	private double weight;

	private double grade;

	private String notes;

	public GradedActivity(String title){
		this.title = title;
		this.date = new Date();
		this.grade = 0;
		this.weight = 0;
		this.notes = "";
	}

	public GradedActivity(String title, Date date){
		this(title);
		this.date = date;
	}

	public GradedActivity(String title, Date date, double weight){
		this(title, date);
		this.weight = weight;
	}

	public GradedActivity(String title, Date date, double weight, double grade){
		this(title, date, weight);
		this.grade = grade;
	}

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