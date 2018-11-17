package swe2slayers.gpacalculationapplication.models;

import org.junit.Test;

import swe2slayers.gpacalculationapplication.utils.Date;

import static org.junit.Assert.assertTrue;

public class createAssignmentTest {

    @Test
    public void getAssignmentId() {
        String id = "buAPDFbfweoaubsdfoAWf890A73b";
        Assignment assn = new Assignment(id, "COMP3613");
        assertTrue(assn.getId().equals(id));
        id = "thisIsATest";
        assn.setId(id);
        assertTrue(assn.getId().equals(id));
    }

    @Test
    public void getTitle() {
        String title = "Software Engineering 2";
        Assignment assn = new Assignment("buAPDFbfweoaubsdfoAWf890A73b", title);
        assertTrue(assn.getTitle().equals(title));
        title = "Software Engineering 1";
        assn.setTitle(title);
        assertTrue(assn.getTitle().equals(title));
    }

    @Test
    public void getDate() {
        Date date = new Date(15, 4, 2018);
        Assignment assn = new Assignment("buAPDFbfweoaubsdfoAWf890A73b", "Software Engineering 2", date);
        assertTrue(assn.getDate().toString().equals(date.toString()));
        date = new Date(23, 9, 2018);
        assn.setDate(date);
        assertTrue(assn.getDate().toString().equals(date.toString()));
    }

    @Test
    public void getWeight() {
        double weight = 20;
        Assignment assn = new Assignment("buAPDFbfweoaubsdfoAWf890A73b", "Software Engineering 2", new Date(15, 4, 2018), weight);
        assertTrue(assn.getWeight() == weight);
        weight = 8;
        assn.setWeight(weight);
        assertTrue(assn.getWeight() == weight);
    }

    @Test
    public void getMark() {
        double mark = 56;
        Assignment assn = new Assignment("buAPDFbfweoaubsdfoAWf890A73b", "Software Engineering 2", new Date(15, 4, 2018), 20, mark, 70);
        assertTrue(assn.getMark() == mark);
        mark = 15;
        assn.setMark(mark);
        assertTrue(assn.getMark() == mark);
    }

    @Test
    public void getTotal() {
        double total = 70;
        Assignment assn = new Assignment("buAPDFbfweoaubsdfoAWf890A73b", "Software Engineering 2", new Date(15, 4, 2018), 20, 56, total);
        assertTrue(assn.getTotal() == total);
        total = 80;
        assn.setTotal(total);
        assertTrue(assn.getTotal() == total);
    }


}
