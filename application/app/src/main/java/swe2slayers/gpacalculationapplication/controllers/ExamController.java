package swe2slayers.gpacalculationapplication.controllers;

import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.models.Exam;

public class ExamController extends GradableController {

    /**
     * Constructor that requires an exam
     * @param exam The exam to control
     */
    public ExamController(Exam exam) {
        super(exam);
    }

    /**
     * Function that returns the room of the exam
     * @return String value of the room
     */
    public String getExamRoom() {
        return ((Exam) this.gradable).room;
    }

    /**
     * FUnction that sets the exam room
     * @param room The new room
     */
    public void setExamRoom(String room) {
        if(room != null) {
            ((Exam) this.gradable).room = room;
            this.notifyObservers();
        }
    }

    /**
     * Functiont that returns the duration of the exam in minutes
     * @return Integer minutes of the duration
     */
    public int getExamDuration() {
        return ((Exam) this.gradable).duration;
    }

    /**
     * Function to set the duration
     * @param duration The new duarion of the exam in minutes
     */
    public void setExamDuration(int duration) {
        if(duration >= 0) {
            ((Exam) this.gradable).duration = duration;
            this.notifyObservers();
        }
    }

    /**
     * Function that returns the topics
     * @return ArrayList of topics
     */
    public ArrayList<String> getExamTopics() {
        return ((Exam) this.gradable).topics;
    }

    /**
     * Function that sets the topics
     * @param topics The new set of topics
     */
    public void setExamTopics(ArrayList<String> topics) {
        if(topics != null) {
            ((Exam) this.gradable).topics = topics;
            this.notifyObservers();
        }
    }
}
