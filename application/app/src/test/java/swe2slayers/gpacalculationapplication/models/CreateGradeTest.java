package swe2slayers.gpacalculationapplication.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreateGradeTest {

    @Test
    public void getGrade() {
    }

    @Test
    public void setGrade() {
    }

    @Test
    public void getMax() {
        double tempmax=  45;
       Grade g = new Grade();
       g.setMax(tempmax);
        assertTrue(g.getMax()==tempmax);
    }


    @Test
    public void getMin() {
        double tempmin=  12;
        Grade g = new Grade();
        g.setMin(tempmin);
        assertTrue(g.getMin()==tempmin);
    }

    @Test
    public void getGPA() {
        double tempg=  2.3;
        Grade g = new Grade();
        g.setGPA(tempg);
        assertTrue(g.getGPA()==tempg);

    }


}