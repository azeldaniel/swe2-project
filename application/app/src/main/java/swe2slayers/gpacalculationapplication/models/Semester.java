package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.Serializable;
import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.utils.Date;


public class Semester implements Serializable {

	public String title;

	public ArrayList<Course> courses;

	public Date start;

	public Date end;

	/**
	 * Constructor that requires title
	 * @param title Which semester e.g Semester 1
	 */
	public Semester(String title){
		this.title = title;
		this.courses = new ArrayList<>();
		this.start = new Date();
		this.end = new Date();
	}

    /**
     * Constructor that requires title, start and end
     * @param title Which semester e.g Semester 1
     * @param start Start date of the semester
     * @param end End date of the semester
     */
	public Semester(String title, Date start, Date end){
		this(title);
		this.start = start;
		this.end = end;
	}
}