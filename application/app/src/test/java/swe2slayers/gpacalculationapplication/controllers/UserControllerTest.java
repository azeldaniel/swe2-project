package swe2slayers.gpacalculationapplication.controllers;

import org.junit.Test;

import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Time;

import static org.junit.Assert.assertFalse;
import static swe2slayers.gpacalculationapplication.controllers.UserController.getYearsForUser;

public class UserControllerTest {
Date dateS= new Date(19,12,2018);
Date dateE= new Date(19,12,2019);
Time Time1= new Time(9,30);
    User user= new User("S9oThHsvlAX8OVSBA0Xp09mNKMr2","test@test.com","SWETest","Cases",816000111,"Computer Science",3.8,3.8);
    Year yr = new Year("Year 1",user.getUserId(),dateS,dateE);
    Semester s= new Semester("Semester 1",yr.getYearId(),user.getUserId(),dateS,dateE);
    Course c= new Course("Comp 3613","Software Engineering 2",s.getSemesterId(),user.getUserId(),3,3,85);
    Assignment a =new Assignment("1289","Assignment1",dateE,100,20,100);
    Exam e= new Exam("1293","CW Exam 1",dateE,50,60,84);

    @Test
    public void updateYearForUser() {
        FirebaseDatabaseHelper.enableTestingMode();
        UserController.addYearForUser(user,yr,null);
        yr.setYearId("123456");
        yr.setUserId("S9oThHsvlAX8OVSBA0Xp09mNKMr1");
        UserController.updateYearForUser(user,yr,null);
        assertFalse(yr.getUserId()!="S9oThHsvlAX8OVSBA0Xp09mNKMr1");
        assertFalse(yr.getYearId()!="123456");

    }

    @Test
    public void updateSemesterForUser() {
        FirebaseDatabaseHelper.enableTestingMode();
        UserController.addSemesterForUser(user,s,null);
        assertFalse(s.getSemesterId()=="192934");
        s.setTitle("Semester 3");
        s.setSemesterId("192934");

        UserController.updateSemesterForUser(user,s,null);
        assertFalse(s.getSemesterId()!="192934");
        assertFalse(s.getTitle()!="Semester 3");
        assertFalse(s.getUserId()!=user.getUserId());

    }

    @Test
    public void updateCourseForUser() {
        FirebaseDatabaseHelper.enableTestingMode();
        User use=user;
        Course c= new Course("Comp 3613","Software Engineering 2",s.getSemesterId(),user.getUserId(),3,3,85);
        c.setCourseId("1weqweqwe");
        UserController.addCourseForUser(use,c,null);
        c.setLevel(2);
        c.setCredits(1);
        c.setCode("Comp3603");
        c.setName("Human and Computer Interaction");
        UserController.updateCourseForUser(use,c,null);
        assertFalse(c.getLevel()!=2);
        assertFalse(c.getCredits()!=1);
        assertFalse(c.getCode()!="Comp3603");
        assertFalse(c.getName()!="Human and Computer Interaction");
        assertFalse(c.getUserId()!=user.getUserId());

    }

    @Test
    public void updateAssignmentForUser() {
        FirebaseDatabaseHelper.enableTestingMode();
        UserController.addAssignmentForUser(user,a,null);
        a.setWeight(15);
        a.setTotal(100);
        a.setUserId(user.getUserId());
        UserController.updateAssignmentForUser(user,a,null);
        assertFalse(a.getWeight()!=15);
        assertFalse(a.getTotal()!=100);
        assertFalse(a.getUserId()!=user.getUserId());

    }

    @Test
    public void updateExamForUser() {
        FirebaseDatabaseHelper.enableTestingMode();
        UserController.addExamForUser(user,e,null);
        e.setDuration(60);
        e.setRoom("FST CSL 2");
        e.setTime(Time1);
        e.setNote("Ask Lecuturer about question 2 b");
        UserController.updateExamForUser(user,e,null);
        assertFalse(e.getRoom()!="FST CSL 2");
        assertFalse(e.getTitle()!="CW Exam 1");
        assertFalse(e.getTime()!=Time1);
        assertFalse(e.getNote()==null);
    }

    @Test
    public void calculateDegreeGPA() {
        FirebaseDatabaseHelper.enableTestingMode();
        FirebaseDatabaseHelper.load(user,null);

        assertFalse(FirebaseDatabaseHelper.getGradingSchema()==null);
        Year y1= new Year("year 1",user.getUserId(),dateS,dateE);
        y1.setYearId("232weER2SF3kn12");
        UserController.addYearForUser(user,y1,null);

        Semester s1=new Semester("Semester 1",y1.getYearId(),user.getUserId(),dateS,dateE);
        s1.setSemesterId("d324nk34iN3DNSD");
        UserController.addSemesterForUser(user,s1,null);

        Course c1 =new Course("Math 2250","Industrial Statistics",s1.getSemesterId(),user.getUserId(),3,2,85);
        c1.setCourseId("128102jkosj");
        Course c2 =new Course("Foun 1100","Caribbean Civ",s1.getSemesterId(),user.getUserId(),3,1,50);
        c2.setCourseId("s239123sds");

        UserController.addCourseForUser(user,c1,null);
        UserController.addCourseForUser(user,c2,null);

        UserController.updateSemesterForUser(user,s1,null);
        UserController.updateYearForUser(user,y1,null);
        User u=user;
        System.out.println(UserController.calculateDegreeGPA(u));
        assertFalse(UserController.calculateDegreeGPA(u)==0.0);
        assertFalse(UserController.calculateDegreeGPA(u)>4.3);

    }

    @Test
    public void calculateCumulativeGPA() {
        FirebaseDatabaseHelper.enableTestingMode();
        FirebaseDatabaseHelper.load(user,null);


        assertFalse(FirebaseDatabaseHelper.getGradingSchema()==null);
        Year y1= new Year("year 1",user.getUserId(),dateS,dateE);
        y1.setYearId("232weER2SF3kn12");
        UserController.addYearForUser(user,y1,null);

        Semester s1=new Semester("Semester 1",y1.getYearId(),user.getUserId(),dateS,dateE);
        s1.setSemesterId("d324nk34iN3DNSD");
        UserController.addSemesterForUser(user,s1,null);

        Course c1 =new Course("Math 2250","Industrial Statistics",s1.getSemesterId(),user.getUserId(),3,2,85);
        Course c2 =new Course("Comp 1601","Computer Concepts",s1.getSemesterId(),user.getUserId(),3,1,53);
        c1.setCourseId("sdn12912uebwks");
        c2.setCourseId("qejnk2398293sd");
        UserController.addCourseForUser(user,c1,null);
        UserController.addCourseForUser(user,c2,null);

        UserController.updateSemesterForUser(user,s1,null);
        UserController.updateYearForUser(user,y1,null);
        User u=user;
        System.out.println(UserController.calculateCumulativeGPA(u));
        assertFalse(UserController.calculateCumulativeGPA(u)==0.0);
        assertFalse(UserController.calculateCumulativeGPA(u)>4.3);

    }

}