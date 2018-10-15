package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class Course extends Observable implements Serializable {

	private String code;

	private String name;

	private int credits;

	private ArrayList<Gradable> gradables;

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

	public double calculateFinalGrade(){

		double finalGrade = 0;

		for(Gradable gradable : this.gradables){
			finalGrade += gradable.calculateWeightedGrade();
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
		this.notifyObservers();
	}


	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
		this.notifyObservers();
	}


	public int getCredits(){
		return this.credits;
	}

	public void setCredits(int credits){
		this.credits = credits;
		this.notifyObservers();
	}


	public double getFinalGrade(){
	    if(finalGrade == 0){
            return this.calculateFinalGrade();
        }

	    return this.finalGrade;
	}

	public ArrayList<Gradable> getAssignments(){
		ArrayList<Gradable> assignments = new ArrayList<>();

		for(Gradable gradable : this.gradables){
			if(gradable instanceof Assignment){
				assignments.add(gradable);
			}
		}

		return assignments;
	}

	public ArrayList<Gradable> getExams(){
		ArrayList<Gradable> assignments = new ArrayList<>();

		for(Gradable gradable : this.gradables){
			if(gradable instanceof Exam){
				assignments.add(gradable);
			}
		}

		return assignments;
	}

	public void addGradable(Gradable gradable){
		this.gradables.add(gradable);
        this.notifyObservers();
	}

	public void removeGradable(Gradable gradable){
		this.gradables.remove(gradable);
        this.notifyObservers();
	}


	public int getLevel(){
		return this.level;
	}

	public void setLevel(int level){
		this.level = level;
        this.notifyObservers();
	}


	public double getTargetGrade(){
		return this.targetGrade;
	}

	public void setTargetGrade(double targetGrade){
		this.targetGrade = targetGrade;
        this.notifyObservers();
	}
}