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

    /**
     * TODO @Amanda
     * Function that calculates this semester's GPA for the user
     * @return Double value representing the GPA for this semester
     */
	public double calculateSemesterGPA(){

	    double gpa = 0;

	    for(Course course: this.getCourses()){
	        //TODO
        }

		return 0;
	}

	public void addCourse(Course course){
		this.courses.add(course);
	}

	public void removeCourse(Course course){
		this.courses.remove(course);
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