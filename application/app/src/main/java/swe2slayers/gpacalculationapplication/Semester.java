package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.ArrayList;
import java.util.Date;


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

	// TODO @Amanda testing
	public double calculateSemesterGPA(){
		return 0;
	}


    public int getSemesterNum() {
        return semesterNum;
    }

    public void setSemesterNum(int semesterNum) {
        this.semesterNum = semesterNum;
    }


    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
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
}