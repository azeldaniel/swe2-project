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

public class Time implements Serializable {

    private int hour;

    private int minute;

    public Time() {
        this.hour = -1;
        this.minute = -1;
    }

    /**
     * Constructor that requires hour
     * @param hour The hour of day in 24 hour format
     */
    public Time(int hour) {
        this();
        this.hour = hour;
    }

    /**
     * Constructor that requires hour
     * @param hour The hour of day in 24 hour format
     * @param minute The minute of the hour
     */
    public Time(int hour, int minute) {
        this(hour);
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return hour +":" + String.format("%02d", minute);
    }

    /**
     * Function that returns the time in 12 hour format
     * @return String containing the time in 12 hour format
     */
    public String toStringFancy(){
        if(hour == 0){
            return "12:" + String.format("%02d", minute) + "AM";
        }else if(hour < 12){
            return hour + ":" + String.format("%02d", minute) + "AM";
        }else{
            return (hour - 12) + ":" + String.format("%02d", minute) + "PM";
        }
    }
}
