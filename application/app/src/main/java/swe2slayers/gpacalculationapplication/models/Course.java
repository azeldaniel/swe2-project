/*
 * Copyright (c) 2018. Software Engineering Slayers
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Christopher Joseph (814000605)
 * Michael Bristol (816003612)
 * Maya Bannis (816000144)
 *
 * COMP 3613
 * Software Engineering II
 *
 * GPA Calculator Project
 */

package swe2slayers.gpacalculationapplication.models;

import java.io.Serializable;

public class Course implements Serializable {

    private String courseId;

    private String semesterId;

    private String userId;

	private String code;

	private String name;

	private int credits;

	private double finalGrade;

	private int level;

	private double targetGrade;

	/**
	 * Default Constructor for Firebase
	 */
	public Course(){}

    /**
     * Constructor that requires course code, fullName, credits and level
     * @param code The course code e.g. COMP 3613
     * @param name The fullName of the course e.g. Software Engineering I
     * @param semesterId Which semester
     * @param userId Which user
     * @param credits The number of credit hours e.g. 3
     * @param level The level of the course e.g. 1, 2, 3 etc.
     */
	public Course(String code, String name,  String semesterId, String userId, int credits, int level){
		this.code = code;
		this.name = name;
		this.semesterId = semesterId;
		this.userId = userId;
		this.credits = credits;
		this.level = level;
		this.finalGrade = -1;
		this.targetGrade = -1;
	}

    /**
     * Constructor that requires course code, fullName, credits, level and final mark. This should
     * be used in cases where the final mark is already known to the user
     * @param code The course code e.g. COMP 3613
     * @param name The fullName of the course e.g. Software Engineering II
     * @param semesterId Which semester
     * @param userId Which user
     * @param credits The number of credit hours e.g. 3
     * @param level The level of the course e.g. 1, 2, 3 etc.
     * @param finalGrade The final mark attained for the course e.g. 80%
     *
     */
	public Course(String code, String name,  String semesterId, String userId, int credits, int level, double finalGrade){
		this(code, name, semesterId, userId, credits, level);
		this.finalGrade = finalGrade;
	}

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(double targetGrade) {
        this.targetGrade = targetGrade;
    }

    @Override
    public int hashCode() {
        return this.getCourseId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getCourseId().equals(((Course)obj).getCourseId());
    }
}