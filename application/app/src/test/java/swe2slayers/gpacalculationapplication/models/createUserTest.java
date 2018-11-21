package swe2slayers.gpacalculationapplication.models;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class createUserTest {

    @Test
    public void getUserId() {
        User user= new User();
        user.setUserId("ON37QKmAbBW0Yu0hXK6jifmScU83");
        assertTrue(user.getUserId()=="ON37QKmAbBW0Yu0hXK6jifmScU83");
    }

    @Test
    public void getEmail() {
        User user= new User();
        user.setEmail("test@tests.com");
        assertTrue(user.getEmail()=="test@tests.com");

    }

    @Test
    public void getFirstName() {

    }


    @Test
    public void getLastName() {
    }

    @Test
    public void getStudentId() {
    }


    @Test
    public void getDegree() {
    }


    @Test
    public void getTargetGPA() {
    }

    @Test
    public void getGradingSchemaId() {
    }

}