package swe2slayers.gpacalculationapplication.models;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class createUserTest {
    //User(String userId, String email, String firstName, String lastName, long studentId, String degree, double targetGPA, double TargetCumulativeGPA)
    User user = new User("ON37QKmAbBW0Yu0hXK6jifmScU83","test@tests.com","John","Bonne",1929493,"Computer Science Special",3.6,3.8);

    @Test
    public void getUserId() {
        assertTrue(user.getUserId()=="ON37QKmAbBW0Yu0hXK6jifmScU83");
        user.setUserId("ON37QKmAbBW0Yu0hXK6jifmScM85");
        assertTrue(user.getUserId()=="ON37QKmAbBW0Yu0hXK6jifmScM85");
    }

    @Test
    public void getEmail() {
        assertTrue(user.getEmail()=="test@tests.com");
        user.setEmail("test@ReTesting.com");
        assertTrue(user.getEmail()=="test@ReTesting.com");
    }

    @Test
    public void getFirstName() {
        assertTrue(user.getFirstName()=="John");
        user.setFirstName("Donne");
        assertTrue(user.getFirstName()=="Donne");
    }


    @Test
    public void getLastName() {
        assertTrue(user.getLastName()=="Bonne");
        user.setLastName("Baptiste");
        assertTrue(user.getLastName()=="Baptiste");
    }

    @Test
    public void getStudentId() {
        assertTrue(user.getStudentId()==1929493);
        user.setStudentId(123459);
        assertTrue(user.getStudentId()==123459);
    }


    @Test
    public void getDegree() {
        assertTrue(user.getDegree()=="Computer Science Special");
        user.setDegree("Bio-chemical Engineering");
        assertTrue(user.getDegree()=="Bio-chemical Engineering");
    }


    @Test
    public void getTargetGPA() {
        assertTrue(user.getTargetDegreeGPA()==3.6);
        user.setTargetDegreeGPA(3.5);
        assertTrue(user.getTargetDegreeGPA()==3.5);
    }

    @Test
    public void getTargetCumulativeGPA() {
        assertTrue(user.getTargetCumulativeGPA()==3.8);
        user.setTargetCumulativeGPA(3.8);
        assertTrue(user.getTargetCumulativeGPA()==3.8);
    }

    @Test
    public void getGradingSchemaId(){
        user.setGradingSchemaId("default");
        assertTrue(user.getGradingSchemaId()=="default");
        assertFalse(user.getGradingSchemaId()=="Customize 1");
    }

}
