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

    public String daysUntil(){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date d = myFormat.parse(this.toString());
            long diff = d.getTime() - new java.util.Date().getTime();

            long daysdiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if(daysdiff < 0){
                return -(daysdiff) + " days ago";
            }else if(daysdiff == 0){
                return "today";
            }else if(daysdiff == 1){
                return "tomorrow";
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
