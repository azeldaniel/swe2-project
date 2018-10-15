package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.utils.Date;


public class Semester implements Serializable {

	public int semesterNum;

	public ArrayList<Course> courses;

	public Date start;

	public Date end;

	/**
	 * Constructor that requires semesterNum
	 * @param semesterNum Which semester e.g 1
	 */
	public Semester(int semesterNum){
		this.semesterNum = semesterNum;
		this.courses = new ArrayList<>();
		this.start = new Date();
		this.end = new Date();
	}

    /**
     * Constructor that requires semesterNum, start and end
     * @param semesterNum Which semester e.g 1
     * @param start Start date of the semester
     * @param end End date of the semester
     */
	public Semester(int semesterNum, Date start, Date end){
		this(semesterNum);
		this.start = start;
		this.end = end;
	}
}