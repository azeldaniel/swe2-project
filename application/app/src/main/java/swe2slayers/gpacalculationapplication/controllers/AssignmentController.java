package swe2slayers.gpacalculationapplication.controllers;

import java.io.File;

import swe2slayers.gpacalculationapplication.models.Assignment;

public class AssignmentController extends GradableController {

    private static final AssignmentController instance = new AssignmentController();

    /**
     * Private default constructor
     */
    private AssignmentController() {}

    /**
     * Function that returns singleton instance
     * @return Singleton instance
     */
    public static AssignmentController getInstance(){
        return instance;
    }

    /**
     * Function that returns the handout file
     * @return File variable that holds the files
     */
    public File getAssignmentHandout(Assignment assignment) {
        return assignment.handout;
    }

    /**
     * Function that sets handout
     * @param handout The new handout file
     */
    public void setAssignmentHandout(Assignment assignment, File handout) {
        if(handout != null) {
            assignment.handout = handout;
            this.setChanged();
            this.notifyObservers(assignment);
        }
    }
}
