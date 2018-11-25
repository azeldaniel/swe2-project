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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import swe2slayers.gpacalculationapplication.models.Year;

public class Sorter {

    /**
     * Function that sorts a given list of years alphabetically
     * @param years The list of years to sort
     */
    public static void sortYears(List years){
        Collections.sort(years, new Comparator<Year>() {
            @Override
            public int compare(Year y1, Year y2) {
                return y1.getTitle().compareTo(y2.getTitle());
            }
        });
    }

    /**
     * Function that sorts a given list of semesters alphabetically by year
     * @param semesters The list of semesters to sort
     */
    public static void sortSemesters(List semesters){

    }

    /**
     * Function that sorts a given list of courses alphabetically by year then semester
     * @param courses The list of courses to sort
     */
    public static void sortCourses(List courses){

    }
}
