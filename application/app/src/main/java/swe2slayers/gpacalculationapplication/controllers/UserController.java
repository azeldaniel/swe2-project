package swe2slayers.gpacalculationapplication.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;

public class UserController extends Observable {

    private User user;

    /**
     * Constructor that requires user
     * @param user The user to control
     */
    public UserController(User user) {
        this.user = user;
    }

    /**
     * Function that returns the username
     * @return String value of the username
     */
    public String getUserUsername() {
        return this.user.username;
    }

    /**
     * Function that returns the email
     * @return String value of the email
     */
    public String getUserEmail() {
        return this.user.email;
    }

    /**
     * Function the sets the email
     * @param email The new email
     */
    public void setUserEmail(String email) {
        if(email != null) {
            this.user.email = email;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the password
     * @return String value of the hashed password
     */
    public String getUserPassHash() {
        return this.user.passHash;
    }

    /**
     * Function that sets the password
     * @param passHash The new hashed password
     */
    public void setUserPassHash(String passHash) {
        if(passHash != null) {
            this.user.passHash = passHash;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the name of the user
     * @return String value of the user name
     */
    public String getUserFullName() {
        return this.user.name;
    }

    /**
     * Function that sets the user's name
     * @param name The new user name
     */
    public void setUserFullName(String name) {
        if(name != null) {
            this.user.name = name;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the user's id
     * @return The student ID of the user
     */
    public long getUserId() {
        return this.user.id;
    }

    /**
     * Function that sets user id
     * @param id The new student id of the user
     */
    public void setUserId(long id) {
        if(id >= 0) {
            this.user.id = id;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the years
     * @return ArrayList of years for the user
     */
    public ArrayList<Year> getYears() {
        return this.user.years;
    }

    /**
     * Function to add a year
     * @param year The new year to add
     */
    public void addYear(Year year){
        if(year != null) {
            this.user.years.add(year);
            this.notifyObservers();
        }
    }

    /**
     * Function that removes a given year
     * @param year The year to remove
     */
    public void removeYear(Year year){
        if(year != null) {
            this.user.years.remove(year);
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the degree of the user
     * @return String value of the degree of the user
     */
    public String getUserDegree() {
        return this.user.degree;
    }

    /**
     * Function that sets the degree
     * @param degree The new degree of the user
     */
    public void setUserDegree(String degree) {
        if(degree != null) {
            this.user.degree = degree;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the target GPA of a user
     * @return Double value of the target GPA
     */
    public double getUserTargetGPA() {
        return this.user.targetGPA;
    }

    /**
     * Function that sets the target GPA
     * @param targetGPA The new target GPA
     */
    public void setUserTargetGPA(double targetGPA) {
        if(targetGPA >= 0) {
            this.user.targetGPA = targetGPA;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the grading schema of the user
     * @return HashMap containing the grading schema of the user
     */
    public HashMap<String, Double> getGradingSchema() {
        return this.user.gradingSchema;
    }

    /**
     * Function that sets the grading schema
     * @param gradingSchema The new grading schema
     */
    public void setGradingSchema(HashMap<String, Double> gradingSchema) {
        if(gradingSchema != null) {
            this.user.gradingSchema = gradingSchema;
            this.notifyObservers();
        }
    }

    /**
     * Function that authenticates the user with the application
     * @return True if user was successfully authenticated, false otherwise
     */
    public boolean login(){

        return false;
    }

    /**
     * TODO @Amanda
     * Function that calculates the degree GPA for a user
     * @return Double value for the degree GPA of a user
     */
    public double calculateDegreeGPA(){
        double gpa = 0;

        for(Year year: this.getYears()){
            //TODO
        }

        return gpa;
    }

    /**
     * TODO @Amanda
     * Function that calculates the cumulative GPA for a user
     * @return Double value for the cumulative GPA of a user
     */
    public double calculateCumulativeGPA(){
        double gpa = 0;

        for(Year year: this.getYears()){
            //TODO
        }

        return gpa;
    }
}
