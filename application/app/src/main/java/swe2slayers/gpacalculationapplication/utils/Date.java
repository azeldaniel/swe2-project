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

package swe2slayers.gpacalculationapplication.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Date implements Serializable {

    private int day;

    private int month;

    private int year;

    /**
     * Empty Constructor
     */
    public Date() {
        this.day = -1;
        this.month = -1;
        this.year = -1;
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

    /**
     * Function that returns a fancy version of a date
     * @return String containing the fancy representation of a date
     */
    public String toStringFancy(){
        if(this.getYear() != -1){
            String months[] =
                    {"January", "February", "March", "April", "May", "June", "July", "August",
                            "September", "October", "November", "December"};

            return months[this.getMonth()-1] + " " + this.getDay() + ", " + this.getYear();
        }
        return this.toString();
    }

    /**
     * Function that returns a string representation of how many days until a date
     * @return String containing a representation of how many days until a date from this one
     */
    public String daysUntil(){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date d = myFormat.parse(this.toString());
            long diff = d.getTime() - new java.util.Date().getTime() + 86400000;
            long daysdiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if(daysdiff < 0){
                return -(daysdiff) + " days ago";
            }else if(daysdiff == 0){
                return "Today";
            }else if(daysdiff == 1){
                return "Tomorrow";
            }else if(daysdiff < 7) {
                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(d);
                    switch (c.get(Calendar.DAY_OF_WEEK)){
                        case Calendar.SUNDAY:
                            return "Sunday";
                        case Calendar.MONDAY:
                            return "Monday";
                        case Calendar.TUESDAY:
                            return "Tuesday";
                        case Calendar.WEDNESDAY:
                            return "Wednesday";
                        case Calendar.THURSDAY:
                            return "Thursday";
                        case Calendar.FRIDAY:
                            return "Friday";
                        case Calendar.SATURDAY:
                            return "Saturday";
                    }
                }catch (Exception e){
                    return daysdiff + " days from now";
                }
            }else{
                return daysdiff + " days from now";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
