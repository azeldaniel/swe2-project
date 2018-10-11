package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.ArrayList;
import java.util.Date;

import swe2slayers.gpacalculationapplication.Course;

public class Semester{

	private int semesterNum;

	private ArrayList<Course> courses;

	private Date start;

	private Date end;

	public Semester(int semesterNum){
		this.semesterNum = semesterNum;
		this.courses = new ArrayList<>();
		this.start = new Date();
		this.end = new Date();
	}

	public Semester(int semesterNum, Date start, Date end){
		this(semesterNum);
		this.start = start;
		this.end = end;
	}

	public void addCourse(Course course){
		this.courses.add(course);
	}

	public void removeCourse(Course course){
		this.courses.remove(course);
	}

	// TODO: @Amanda
	public double calculateSemesterGPA(){
		return 0;
	}
}