package swe2slayers.gpacalculationapplication.controllers;

import java.io.File;

import swe2slayers.gpacalculationapplication.models.Assignment;

public class AssignmentController extends GradableController {

    /**
     * Constructor that requires an assignment
     * @param assignment The assignment to control
     */
    public AssignmentController(Assignment assignment) {
        super(assignment);
    }

    /**
     * Function that returns the handout file
     * @return File variable that holds the files
     */
    public File getAssignmentHandout() {
        return ((Assignment)this.gradable).handout;
    }

    /**
     * Function that sets handout
     * @param handout The new handout file
     */
    public void setAssignmentHandout(File handout) {
        if(handout != null) {
            ((Assignment)this.gradable).handout = handout;
            this.notifyObservers();
        }
    }
}
