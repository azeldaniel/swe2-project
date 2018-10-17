package swe2slayers.gpacalculationapplication.utils;

import android.icu.util.Calendar;

import java.io.Serializable;

public class Date implements Serializable {

    private int day;

    private int month;

    private int year;

    /**
     * Empty Constructor
     */
    public Date() {
        this.day = 1;
        this.month = 1;
        this.year = 2018;
    }

    /**
     * Constructor that requires day, month and year
     * @param day The day of the month
     * @param month The month of the year
     * @param year The year as a number
     */
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return day + "/" + month + "/" + year ;
    }
}
