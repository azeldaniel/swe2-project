package swe2slayers.gpacalculationapplication.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;

public class UserController extends Observable {

    private static final UserController instance = new UserController();

    /**
     * Private default constructor
     */
    private UserController() {}

    /**
     * Function that returns singleton instance
     * @return Singleton instance
     */
    public static UserController getInstance(){
        return instance;
    }

    /**
     * Function that returns the username
     * @return String value of the username
     */
    public String getUserUsername(User user) {
        return user.username;
    }

    /**
     * Function that returns the username
     *
     */
    public void setUserUsername(User user, String username) {
        if(username != null) {
            user.username = username;
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that returns the email
     * @return String value of the email
     */
    public String getUserEmail(User user) {
        return user.email;
    }

    /**
     * Function the sets the email
     * @param email The new email
     */
    public void setUserEmail(User user, String email) {
        if(email != null) {
            user.email = email;
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that returns the password
     * @return String value of the hashed password
     */
    public String getUserPassHash(User user) {
        return user.passHash;
    }

    /**
     * Function that sets the password
     * @param passHash The new hashed password
     */
    public void setUserPassHash(User user, String passHash) {
        if(passHash != null) {
            user.passHash = passHash;
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that returns the name of the user
     * @return String value of the user name
     */
    public String getUserFullName(User user) {
        return user.name;
    }

    /**
     * Function that sets the user's name
     * @param name The new user name
     */
    public void setUserFullName(User user, String name) {
        if(name != null) {
            user.name = name;
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that returns the user's id
     * @return The student ID of the user
     */
    public long getUserId(User user) {
        return user.id;
    }

    /**
     * Function that sets user id
     * @param id The new student id of the user
     */
    public void setUserId(User user, long id) {
        if(id >= 0) {
            user.id = id;
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that returns the years
     * @return ArrayList of years for the user
     */
    public ArrayList<Year> getYears(User user) {
        return user.years;
    }

    /**
     * Function to add a year
     * @param year The new year to add
     */
    public void addYear(User user, Year year){
        if(year != null) {
            user.years.add(year);
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that removes a given year
     * @param year The year to remove
     */
    public void removeYear(User user, Year year){
        if(year != null) {
            user.years.remove(year);
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that returns the degree of the user
     * @return String value of the degree of the user
     */
    public String getUserDegree(User user) {
        return user.degree;
    }

    /**
     * Function that sets the degree
     * @param degree The new degree of the user
     */
    public void setUserDegree(User user, String degree) {
        if(degree != null) {
            user.degree = degree;
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that returns the target GPA of a user
     * @return Double value of the target GPA
     */
    public double getUserTargetGPA(User user) {
        return user.targetGPA;
    }

    /**
     * Function that sets the target GPA
     * @param targetGPA The new target GPA
     */
    public void setUserTargetGPA(User user, double targetGPA) {
        if(targetGPA >= 0) {
            user.targetGPA = targetGPA;
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that returns the grading schema of the user
     * @return HashMap containing the grading schema of the user
     */
    public HashMap<String, Double> getGradingSchema(User user) {
        return user.gradingSchema;
    }

    /**
     * Function that sets the grading schema
     * @param gradingSchema The new grading schema
     */
    public void setGradingSchema(User user, HashMap<String, Double> gradingSchema) {
        if(gradingSchema != null) {
            user.gradingSchema = gradingSchema;
            this.setChanged();
            this.notifyObservers(user);
        }
    }

    /**
     * Function that authenticates the user with the application
     * @return True if user was successfully authenticated, false otherwise
     */
    public boolean login(User user){

        return false;
    }

    /**
     * TODO @Amanda
     * Function that calculates the degree GPA for a user
     * @return Double value for the degree GPA of a user
     */
    public double calculateDegreeGPA(User user){
        double gpa = 0;

        for(Year year: this.getYears(user)){
            //TODO
        }

        return gpa;
    }

    /**
     * TODO @Amanda
     * Function that calculates the cumulative GPA for a user
     * @return Double value for the cumulative GPA of a user
     */
    public double calculateCumulativeGPA(User user){
        double gpa = 0;

        for(Year year: this.getYears(user)){
            // TODO
        }

        return gpa;
    }
}
