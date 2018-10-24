package swe2slayers.gpacalculationapplication.controllers;

import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.utils.Globals;

public class GradableController {

    /**
     *
     * @param gradable
     * @return
     */
    public static double calculatePercentageGrade(Gradable gradable){
        return (gradable.getMark()/gradable.getTotal()*100);
    }

    /**
     *
     * @param gradable
     * @return
     */
    public static String calculateLetterGrade(Gradable gradable){
        int percent = (int) calculatePercentageGrade(gradable);

        return Globals.getGrade(percent).getGrade();
    }
}
