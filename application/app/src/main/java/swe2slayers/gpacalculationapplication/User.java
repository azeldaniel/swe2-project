package swe2slayers.gpacalculationapplication;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private String username;

    private String email;

    private String passHash;

    private String name;

    private long id;

    private ArrayList<Year> years;

    private String degree;

    private double targetGPA;

    private HashMap<String, Double> gradingSchema;

    public User(String username, String email, String passHash, String name, long id) {
        this.username = username;
        this.email = email;
        this.passHash = passHash;
        this.name = name;
        this.id = id;

        this.years = new ArrayList<>();
        this.degree = "";
        this.targetGPA = 0;
        this.gradingSchema = new HashMap<>();
    }

    public User(String username, String email, String passHash, String name, long id, String degree) {
        this(username, email, passHash, name, id);
        this.degree = degree;
    }

    public User(String username, String email, String passHash, String name, long id, HashMap<String, Double> gradingSchema) {
        this(username, email, passHash, name, id);
        this.gradingSchema = gradingSchema;
    }

    public User(String username, String email, String passHash, String name, long id, String degree, double targetGPA) {
        this(username, email, passHash, name, id, degree);
        this.targetGPA = targetGPA;
    }

    public User(String username, String email, String passHash, String name, long id, String degree, double targetGPA, HashMap<String, Double> gradingSchema) {
        this(username, email, passHash, name, id, degree, targetGPA);
        this.gradingSchema = gradingSchema;
    }

    // TODO
    public void login(){

    }

    // TODO @Amanda
    public double calculateDegreeGPA(){
        return 0;
    }

    // TODO @Amanda
    public double calculateCumulativeGPA(){
        return 0;
    }

    public double calculateSemesterGPA(Semester semester){
        return semester.calculateSemesterGPA();
    }

    public void addYear(Year year){
        this.years.add(year);
    }


    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public ArrayList<Year> getYears() {
        return years;
    }

    public void setYears(ArrayList<Year> years) {
        this.years = years;
    }


    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }


    public double getTargetGPA() {
        return targetGPA;
    }

    public void setTargetGPA(double targetGPA) {
        this.targetGPA = targetGPA;
    }
    

    public HashMap<String, Double> getGradingSchema() {
        return gradingSchema;
    }

    public void setGradingSchema(HashMap<String, Double> gradingSchema) {
        this.gradingSchema = gradingSchema;
    }
}
