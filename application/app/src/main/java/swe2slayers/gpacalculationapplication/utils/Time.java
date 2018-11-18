package swe2slayers.gpacalculationapplication.utils;

import java.io.Serializable;

public class Time implements Serializable {

    private int hour;

    private int minute;

    /**
     * Empty Constructor
     */
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
}
