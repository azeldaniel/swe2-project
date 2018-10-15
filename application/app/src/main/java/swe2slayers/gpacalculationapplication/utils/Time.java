package swe2slayers.gpacalculationapplication.utils;

import java.io.Serializable;

public class Time implements Serializable {

    private int hour;

    private int minute;

    private int seconds;

    /**
     * Empty Constructor
     */
    public Time() {
        this.hour = 0;
        this.minute = 0;
        this.seconds = 0;
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

    /**
     * Constructor that requires hour
     * @param hour The hour of day in 24 hour format
     * @param minute The minute of the hour
     * @param seconds The seconds of the minute
     */
    public Time(int hour, int minute, int seconds) {
        this(hour, minute);
        this.seconds = seconds;
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

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return hour +":" + minute + ":" + seconds;
    }
}
