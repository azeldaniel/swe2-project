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

public class User implements Serializable {

    private String userId;

    private String email;

    private String firstName;

    private String lastName;

    private long studentId;

    private String degree;

    private double targetCumulativeGPA;

    private double targetDegreeGPA;

    private String gradingSchemaId;

    public User(){}

    /**
     * Constructor that requires userId, email, passHas, fullName and studentId
     * @param userId The studentId of the user, used for identification purposes
     * @param email The email of the user
     * @param firstName The first name of the user
     * @param lastName The first name of the user
     */
    public User(String userId, String email, String firstName, String lastName) {
        this();
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Constructor that requires userId, email, passHas, fullName and studentId
     * @param userId The userId of the user, used for identification purposes
     * @param email The email of the user
     * @param firstName The first name of the user
     * @param lastName The first name of the user
     * @param studentId The student identification number of the user e.g. 81600XXXX
     */
    public User(String userId, String email, String firstName, String lastName, long studentId) {
        this(userId, email, firstName, lastName);
        this.studentId = studentId;
    }

    /**
     * Constructor that requires userId, email, passHas, fullName, studentId and degree
     * @param userId The userId of the user, used for identification purposes
     * @param email The email of the user
     * @param firstName The first name of the user
     * @param lastName The first name of the user     * @param studentId The student identification number of the user e.g. 81600XXXX
     * @param degree The fullName of the degree the user is undertaking e.g. BSc. Biology
     */
    public User(String userId, String email, String firstName, String lastName,
                long studentId, String degree) {
        this(userId, email, firstName, lastName, studentId);
        this.degree = degree;
    }

    /**
     * Constructor that requires userId, email, passHas, fullName, studentId, degree and target GPA
     * @param userId The userId of the user, used for identification purposes
     * @param email The email of the user
     * @param firstName The first name of the user
     * @param lastName The first name of the user
     * @param studentId The student identification number of the user e.g. 81600XXXX
     * @param degree The fullName of the degree the user is undertaking e.g. BSc. Biology
     * @param targetGPA The user's target GPA e.g. 3.6
     */
    public User(String userId, String email, String firstName, String lastName, long studentId, String degree, double targetGPA, double targetCumulativeGPA) {
        this(userId, email, firstName, lastName, studentId, degree);
        this.targetCumulativeGPA = targetCumulativeGPA;
        this.targetDegreeGPA = targetGPA;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public double getTargetCumulativeGPA() {
        return targetCumulativeGPA;
    }

    public void setTargetCumulativeGPA(double targetCumulativeGPA) {
        this.targetCumulativeGPA = targetCumulativeGPA;
    }

    public double getTargetDegreeGPA() {
        return targetDegreeGPA;
    }

    public void setTargetDegreeGPA(double targetDegreeGPA) {
        this.targetDegreeGPA = targetDegreeGPA;
    }

    public String getGradingSchemaId() {
        return gradingSchemaId;
    }

    public void setGradingSchemaId(String gradingSchemaId) {
        this.gradingSchemaId = gradingSchemaId;
    }
}
