package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.ArrayList;

public class Course{

	private String code;

	private String name;

	private int credits;

	private ArrayList<GradedActivity> gradedActivities;

	private double finalGrade;

	private int level;

	private double targetGrade;

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
		this.gradedActivities = new ArrayList<>();
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

	public double calculateFinalGrade(){

		double finalGrade = 0;

		for(GradedActivity gradedActivity : this.gradedActivities){
			finalGrade += gradedActivity.calculateWeightedGrade();
		}

		return finalGrade;
	}

	public double calculateMinimumGrade(){

		double finalGrade = this.calculateFinalGrade();

		double minimum = 0;

		if(finalGrade < 50){
			minimum = 50 - finalGrade;
		}

		return minimum;
	}

	public String getCode(){
		return this.code;
	}

	public void setCode(String code){
		this.code = code;
	}


	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}


	public int getCredits(){
		return this.credits;
	}

	public void setCredits(int credits){
		this.credits = credits;
	}


	public double getFinalGrade(){
	    if(finalGrade == 0){
            return this.calculateFinalGrade();
        }

	    return this.finalGrade;
	}

	public ArrayList<GradedActivity> getAssignments(){
		ArrayList<GradedActivity> assignments = new ArrayList<>();

		for(GradedActivity gradedActivity : this.gradedActivities){
			if(gradedActivity instanceof Assignment){
				assignments.add(gradedActivity);
			}
		}

		return assignments;
	}

	public ArrayList<GradedActivity> getExams(){
		ArrayList<GradedActivity> assignments = new ArrayList<>();

		for(GradedActivity gradedActivity : this.gradedActivities){
			if(gradedActivity instanceof Exam){
				assignments.add(gradedActivity);
			}
		}

		return assignments;
	}

	public void addGradable(GradedActivity gradedActivity){
		this.gradedActivities.add(gradedActivity);
	}

	public void removeGradable(GradedActivity gradedActivity){
		this.gradedActivities.remove(gradedActivity);
	}


	public int getLevel(){
		return this.level;
	}

	public void setLevel(int level){
		this.level = level;
	}


	public double getTargetGrade(){
		return this.targetGrade;
	}

	public void setTargetGrade(double targetGrade){
		this.targetGrade = targetGrade;
	}
}