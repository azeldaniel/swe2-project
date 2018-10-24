package swe2slayers.gpacalculationapplication.models;

import java.io.Serializable;

public class Grade implements Serializable {

    private String grade;

    private double max;

    private double min;

    private double GPA;

    public Grade() {
    }

    public Grade(String grade, double max, double min, double GPA) {
        this.grade = grade;
        this.max = max;
        this.min = min;
        this.GPA = GPA;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }
}
