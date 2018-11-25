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

import swe2slayers.gpacalculationapplication.utils.Date;

public abstract class Gradable implements Serializable {

	private String id;

	private String courseId;

	private String userId;

	private String title;

	private Date date;

	private double weight;

	private double mark;

	private double total;

	private String note;

    public Gradable() {
    }

    /**
	 * Constructor that requires title
	 * @param id The id for the gradable
	 * @param title Title of the activity e.g. Course Work Exam 1
	 */
	public Gradable(String id, String title){
		this.id = id;
		this.title = title;
		this.date = new Date();
		this.mark = -1;
		this.total = -1;
		this.weight = -1;
		this.note = "";
	}

    /**
     * Constructor that requires title and date
	 * @param id The id for the gradable
     * @param title Title of the activity e.g. Course Work Exam 1
     * @param date Date of the activity
     */
	public Gradable(String id, String title, Date date){
		this(id, title);
		this.date = date;
	}

    /**
     * Constructor that requires title, date and weight
	 * @param id The id for the gradable
     * @param title Title of the activity e.g. Course Work Exam 1
     * @param date Date of the activity
     * @param weight Weight of the activity as a percentage e.g. 15%
     */
	public Gradable(String id, String title, Date date, double weight){
		this(id, title, date);
		this.weight = weight;
	}

    /**
     * Constructor that requires title, date, weight and mark
	 * @param id The id for the gradable
     * @param title Title of the activity e.g. Course Work Exam 1
     * @param date Date of the activity
     * @param weight Weight of the activity as a percentage e.g. 15%
     * @param mark The mark attained for the activity
     * @param total The total marks that could be achieved for this activity
     */
	public Gradable(String id, String title, Date date, double weight, double mark, double total){
		this(id, title, date, weight);
		this.mark = mark;
		this.total = total;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getMark() {
		return mark;
	}

	public void setMark(double mark) {
		this.mark = mark;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((Gradable)obj).getId());
	}
}