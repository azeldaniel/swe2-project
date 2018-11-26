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

import swe2slayers.gpacalculationapplication.controllers.CourseController;
import swe2slayers.gpacalculationapplication.controllers.GradableController;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.models.Semester;
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
        Collections.sort(semesters, new Comparator<Semester>() {
            @Override
            public int compare(Semester s1, Semester s2) {
                Year y1 = FirebaseDatabaseHelper.getYear(s1.getYearId());
                Year y2 = FirebaseDatabaseHelper.getYear(s2.getYearId());

                int c = s1.getYearId().compareTo(s2.getYearId());

                if(y1 != null && y2 != null){
                    c = y1.getTitle().compareTo(y2.getTitle());
                }

                if(c == 0){
                    c = s1.getTitle().compareTo(s2.getTitle());
                }

                return c;
            }
        });
    }

    /**
     * Function that sorts a given list of courses alphabetically by year then semester
     * @param courses The list of courses to sort
     */
    public static void sortCourses(List courses){
        Collections.sort(courses, new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                int c = 0;

                Semester s1 = CourseController.getSemesterForCourse(c1);
                Semester s2 = CourseController.getSemesterForCourse(c2);

                if(s1 != null && s2 != null){
                    Year y1 = FirebaseDatabaseHelper.getYear(s1.getYearId());
                    Year y2 = FirebaseDatabaseHelper.getYear(s2.getYearId());

                    if(y1 != null && y2 != null){
                        c = y1.getTitle().compareTo(y2.getTitle());;
                    }

                    if (c == 0) {
                        c = s1.getTitle().compareTo(s2.getTitle());
                    }
                }

                if(c == 0) {
                    c = c1.getCode().compareTo(c2.getCode());
                }

                return c;
            }
        });
    }

    /**
     * Function that sorts a given list of gradables alphabetically by year, semester then course
     * @param gradables The list of courses to sort
     */
    public static void sortGradables(List gradables){
        Collections.sort(gradables, new Comparator<Gradable>() {
            @Override
            public int compare(Gradable g1, Gradable g2) {
                int c = 0;

                Course c1 = GradableController.getCourseForGradable(g1);
                Course c2 = GradableController.getCourseForGradable(g2);

                if(c1 != null && c2 != null){

                    Semester s1 = CourseController.getSemesterForCourse(c1);
                    Semester s2 = CourseController.getSemesterForCourse(c2);

                    if(s1 != null && s2 != null) {

                        Year y1 = FirebaseDatabaseHelper.getYear(s1.getYearId());
                        Year y2 = FirebaseDatabaseHelper.getYear(s2.getYearId());

                        if (y1 != null && y2 != null) {
                            c = y1.getTitle().compareTo(y2.getTitle());
                        }

                        if (c == 0) {
                            c = s1.getTitle().compareTo(s2.getTitle());
                        }
                    }
                    if(c == 0) {
                        c = c1.getCode().compareTo(c2.getCode());
                    }
                }

                if(c == 0) {
                    c = g1.getTitle().compareTo(g2.getTitle());
                }

                return c;
            }
        });
    }
}
