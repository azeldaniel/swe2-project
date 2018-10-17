package swe2slayers.gpacalculationapplication.models;

/*
 * Copyright (c) Software Engineering Slayers, 2018
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {

    public String username;

    public String email;

    public String passHash;

    public String name;

    public long id;

    public ArrayList<Year> years;

    public String degree;

    public double targetGPA;

    public HashMap<String, Double> gradingSchema;

    /**
     * Constructor that requires username, email, passHas, name and id
     * @param username The username of the user, used for identification purposes
     * @param email The email of the user
     * @param passHash The hashed password of the user
     * @param name The full name of the user e.g. Azel Daniel
     * @param id The student identification number of the user e.g. 81600XXXX
     */
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

    /**
     * Constructor that requires username, email, passHas, name, id and degree
     * @param username The username of the user, used for identification purposes
     * @param email The email of the user
     * @param passHash The hashed password of the user
     * @param name The full name of the user e.g. Azel Daniel
     * @param id The student identification number of the user e.g. 81600XXXX
     * @param degree The name of the degree the user is undertaking e.g. BSc. Biology
     */
    public User(String username, String email, String passHash, String name, long id, String degree) {
        this(username, email, passHash, name, id);
        this.degree = degree;
    }

    /**
     * Constructor that requires username, email, passHas, name, id and grading schema
     * @param username The username of the user, used for identification purposes
     * @param email The email of the user
     * @param passHash The hashed password of the user
     * @param name The full name of the user e.g. Azel Daniel
     * @param id The student identification number of the user e.g. 81600XXXX
     * @param gradingSchema The mapping of grades to percentage points for that user's institution
     */
    public User(String username, String email, String passHash, String name, long id, HashMap<String, Double> gradingSchema) {
        this(username, email, passHash, name, id);
        this.gradingSchema = gradingSchema;
    }

    /**
     * Constructor that requires username, email, passHas, name, id, degree and target GPA
     * @param username The username of the user, used for identification purposes
     * @param email The email of the user
     * @param passHash The hashed password of the user
     * @param name The full name of the user e.g. Azel Daniel
     * @param id The student identification number of the user e.g. 81600XXXX
     * @param degree The name of the degree the user is undertaking e.g. BSc. Biology
     * @param targetGPA The user's target GPA e.g. 3.6
     */
    public User(String username, String email, String passHash, String name, long id, String degree, double targetGPA) {
        this(username, email, passHash, name, id, degree);
        this.targetGPA = targetGPA;
    }

    /**
     * Constructor that requires username, email, passHas, name, id, degree, target GPA and gradingSchema
     * @param username The username of the user, used for identification purposes
     * @param email The email of the user
     * @param passHash The hashed password of the user
     * @param name The full name of the user e.g. Azel Daniel
     * @param id The student identification number of the user e.g. 81600XXXX
     * @param degree The name of the degree the user is undertaking e.g. BSc. Biology
     * @param gradingSchema The mapping of grades to percentage points for that user's institution
     */
    public User(String username, String email, String passHash, String name, long id, String degree, double targetGPA, HashMap<String, Double> gradingSchema) {
        this(username, email, passHash, name, id, degree, targetGPA);
        this.gradingSchema = gradingSchema;
    }

    @Override
    public boolean equals(Object obj) {
        return ((User)obj).username.equals(this.username);
    }
}
