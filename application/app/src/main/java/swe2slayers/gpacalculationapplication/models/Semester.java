package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.Globals;


public class Semester implements Serializable {

    private String semesterId;

    private String yearId;

    private String userId;

	private String title;

	private Date start;

	private Date end;

    /**
     * Default Constructor for Firebase
     */
    public Semester() {
    }

    /**
	 * Constructor that requires title
	 * @param title Which semester e.g Semester 1
	 * @param yearId Which year the semester belongs to
     * @param userId Which user
	 */
	public Semester(String title, String yearId, String userId){
		this.title = title;
		this.userId = userId;
		this.yearId = yearId;
		this.start = new Date();
		this.end = new Date();
	}

    /**
     * Constructor that requires title, start and end
     * @param title Which semester e.g Semester 1
	 * @param yearId Which year the semester belongs to
     *  @param userId Which user
     * @param start Start date of the semester
     * @param end End date of the semester
     */
	public Semester(String title, String yearId, String userId, Date start, Date end){
		this(title, yearId, userId);
		this.start = start;
		this.end = end;
	}

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getYearId() {
        return yearId;
    }

    public void setYearId(String yearId) {
        this.yearId = yearId;
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