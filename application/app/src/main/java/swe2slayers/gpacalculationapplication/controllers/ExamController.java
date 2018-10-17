package swe2slayers.gpacalculationapplication.controllers;

import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;

public class ExamController extends GradableController {

    private static final ExamController instance = new ExamController();

    /**
     * Private default constructor
     */
    private ExamController() {}

    /**
     * Function that returns singleton instance
     * @return Singleton instance
     */
    public static ExamController getInstance(){
        return instance;
    }

    /**
     * Function that returns the room of the exam
     * @return String value of the room
     */
    public String getExamRoom(Exam exam) {
        return exam.room;
    }

    /**
     * FUnction that sets the exam room
     * @param room The new room
     */
    public void setExamRoom(Exam exam, String room) {
        if(room != null) {
            exam.room = room;
            this.setChanged();
            this.notifyObservers(exam);
        }
    }

    /**
     * Functiont that returns the duration of the exam in minutes
     * @return Integer minutes of the duration
     */
    public int getExamDuration(Exam exam) {
        return exam.duration;
    }

    /**
     * Function to set the duration
     * @param duration The new duarion of the exam in minutes
     */
    public void setExamDuration(Exam exam, int duration) {
        if(duration >= 0) {
            exam.duration = duration;
            this.setChanged();
            this.notifyObservers(exam);
        }
    }

    /**
     * Function that returns the topics
     * @return ArrayList of topics
     */
    public ArrayList<String> getExamTopics(Exam exam) {
        return exam.topics;
    }

    /**
     * Function that sets the topics
     * @param topics The new set of topics
     */
    public void setExamTopics(Exam exam, ArrayList<String> topics) {
        if(topics != null) {
            exam.topics = topics;
            this.setChanged();
            this.notifyObservers(exam);
        }
    }
}
