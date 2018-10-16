package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

	public String code;

	public String name;

	public int credits;

	public ArrayList<Gradable> gradables;

	public double finalGrade;

	public int level;

	public double targetGrade;

    /**
     * Constructor that requires course code, name, credits and level
     * @param code The course code e.g. COMP 3613
     * @param name The name of the course e.g. Software Engineering II
     * @param credits The number of credit hours e.g. 3
     * @param level The level of the course e.g. 1, 2, 3 etc.
     */
	public Course(String code, String name, int credits, int level){
		this.code = code;
		this.name = name;
		this.credits = credits;
		this.level = level;		
		this.gradables = new ArrayList<>();
		this.finalGrade = 0;
		this.targetGrade = 0;
	}

    /**
     * Constructor that requires course code, name, credits, level and final grade. This should
     * be used in cases where the final grade is already known to the user
     * @param code The course code e.g. COMP 3613
     * @param name The name of the course e.g. Software Engineering II
     * @param credits The number of credit hours e.g. 3
     * @param level The level of the course e.g. 1, 2, 3 etc.
     * @param finalGrade The final grade attained for the course e.g. 80%
     *
     */
	public Course(String code, String name, int credits, int level, double finalGrade){
		this(code, name, credits, level);
		this.finalGrade = finalGrade;
	}
}