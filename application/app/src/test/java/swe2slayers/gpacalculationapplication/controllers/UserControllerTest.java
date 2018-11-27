package swe2slayers.gpacalculationapplication.controllers;

import org.junit.After;
import org.junit.Before;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static swe2slayers.gpacalculationapplication.controllers.UserController.getYearsForUser;

public class UserControllerTest {
    User user;
    Year yr, yr2, yr3;
    Semester s, s2, s3, s4;
    Assignment a;
    Exam e;
    private static boolean alreadySetUp = false;

    @Before
    public void before() {

        FirebaseDatabaseHelper.enableTestingMode();
        FirebaseDatabaseHelper.load(user, null);
        Date dateE = new Date(19, 12, 2019);

        user = new User("S9oThHsvlAX8OVSBA0Xp09mNKMr2", "test@test.com", "SWETest", "Cases", 816000111, "Computer Science", 3.8, 3.8);

        yr = new Year("Year 1", user.getUserId());
        yr.setYearId("123456");
        yr2 = new Year("Year 2", user.getUserId());
        yr2.setYearId("789101");
        yr3 = new Year("Year 3", user.getUserId());
        yr3.setYearId("987654");

        s = new Semester("Semester 1", yr.getYearId(), user.getUserId());
        s.setSemesterId("d324nk34iN3DNSD");
        s2 = new Semester("Semester 1", yr2.getYearId(), user.getUserId());
        s2.setSemesterId("aeDE9452wd14iw2");
        s3 = new Semester("Semester 2", yr2.getYearId(), user.getUserId());
        s3.setSemesterId("n38d4jAdfmn465");
        s4 = new Semester("Semester 1", yr3.getYearId(), user.getUserId());
        s4.setSemesterId("fjrwio283oedi0");


        Course c1 = new Course("Math 2250","Industrial Statistics",s.getSemesterId(),user.getUserId(),3,2,85);
        c1.setCourseId("128102jkosj");
        Course c2 = new Course("Foun 1100","Caribbean Civ",s.getSemesterId(),user.getUserId(),3,1,50);
        c2.setCourseId("s239123sds");
        Course c3 = new Course("COMP3613", "Software Engineering II", s2.getSemesterId(), user.getUserId(), 3, 3, 82);
        c3.setCourseId("5cul21b8j9");
        Course c4 = new Course("COMP2603", "Computer Architecture", s2.getSemesterId(), user.getUserId(), 3, 2, 79);
        c4.setCourseId("pk5whxsdok");
        Course c5 = new Course("COMP3607", "Object Oriented Programming II", s3.getSemesterId(), user.getUserId(), 3, 3, 81);
        c5.setCourseId("dnp2vl33lr");
        Course c6 = new Course("COMP3613", "Software Engineering II", s3.getSemesterId(), user.getUserId(), 3, 3, 59);
        c6.setCourseId("hs6f21yrwe");
        Course c7 = new Course("COMP1700", "Intro to Neural Networks", s4.getSemesterId(), user.getUserId(), 3,1, 77);
        c7.setCourseId("3zy6btao7r");
        Course c8 = new Course("COMP1603", "Computer Programming III", s4.getSemesterId(), user.getUserId(), 3,1, 73);
        c8.setCourseId("02652kuzdi");
        Course c9 = new Course("INFO1600", "Intro to Information Technology", s4.getSemesterId(), user.getUserId(), 3,1, 46);
        c9.setCourseId("cdk15phni9");

        a = new Assignment("1289", "Assignment1", dateE, 100, 20, 100);
        a.setUserId(user.getUserId());
        a.setCourseId(c1.getCourseId());

        e = new Exam("1293", "CW Exam 1", dateE, 50, 60, 84);
        e.setUserId(user.getUserId());
        e.setCourseId(c2.getCourseId());

        if (alreadySetUp) return;
        UserController.addYearForUser(user, yr, null);
        UserController.addYearForUser(user, yr2, null);
        UserController.addYearForUser(user, yr3, null);
        UserController.addSemesterForUser(user, s, null);
        UserController.addSemesterForUser(user, s2, null);
        UserController.addSemesterForUser(user, s3, null);
        UserController.addSemesterForUser(user, s4, null);
        UserController.addCourseForUser(user, c1, null);
        UserController.addCourseForUser(user, c2, null);
        UserController.addCourseForUser(user, c3, null);
        UserController.addCourseForUser(user, c4, null);
        UserController.addCourseForUser(user, c5, null);
        UserController.addCourseForUser(user, c6, null);
        UserController.addCourseForUser(user, c7, null);
        UserController.addCourseForUser(user, c8, null);
        UserController.addCourseForUser(user, c9, null);

        alreadySetUp = true;
    }

    @Test
    public void updateYearForUser() {
        assertFalse(yr.getUserId()!=user.getUserId());
        assertFalse(yr.getYearId()!="123456");
        yr.setYearId("56789");
        yr.setTitle("Year 3");
        UserController.updateYearForUser(user,yr,null);
        assertEquals("Year ID: ",yr.getYearId(),"56789");
        assertEquals("Year Title: ",yr.getTitle(),"Year 3");
        yr.setTitle("Year 1");
        yr.setYearId("123456");
    }

    @Test
    public void updateSemesterForUser() {
        assertFalse(s.getSemesterId()!="d324nk34iN3DNSD");
        assertFalse(s.getTitle()!="Semester 1");
        assertFalse(s.getUserId()!=user.getUserId());
        s.setSemesterId("298whe1282UJuw");
        s.setTitle("Semester 2");
        s.setYearId("56789");
        UserController.updateSemesterForUser(user,s,null);
        assertEquals("Semester ID: ",s.getSemesterId(),"298whe1282UJuw");
        assertEquals("Semester Title: ",s.getTitle(),"Semester 2");
        assertEquals("Year ID: ",s.getYearId(),"56789");
        s.setSemesterId("d324nk34iN3DNSD");
        s.setTitle("semester 1");
        s.setYearId(yr.getYearId());

    }

    @Test
    public void updateCourseForUser() {
        Course c= new Course("Comp 3613","Software Engineering 2",s.getSemesterId(),user.getUserId(),3,3,85);
        c.setCourseId("1weqweqwe");
        UserController.addCourseForUser(user,c,null);
        c.setLevel(2);
        c.setCredits(1);
        c.setUserId(user.getUserId());
        c.setCode("Comp3603");
        c.setName("Human and Computer Interaction");
        UserController.addCourseForUser(user,c,null);
        assertFalse(c.getLevel()!=2);
        assertFalse(c.getCredits()!=1);
        assertFalse(c.getCode()!="Comp3603");
        assertFalse(c.getName()!="Human and Computer Interaction");
        assertFalse(c.getUserId()!=user.getUserId());

    }

    @Test
    public void updateAssignmentForUser() {
        assertFalse(a.getWeight()!=100);
        assertFalse(a.getTotal()!=100);
        assertFalse(a.getMark()!=20);
        a.setWeight(15);
        a.setTotal(100);
        UserController.updateAssignmentForUser(user,a,null);
        assertFalse(a.getWeight()!=15);
        assertFalse(a.getTotal()!=100);
        assertFalse(a.getUserId()!=user.getUserId());
        a.setTotal(20);
        a.setWeight(100);


    }

    @Test
    public void updateExamForUser() {
        assertFalse(e.getWeight()!=50);
        assertFalse(e.getTotal()!=84);
        assertFalse(e.getMark()!=60);
        e.setDuration(60);
        e.setRoom("FST CSL 2");
        e.setNote("Ask Lecturer about question 2 b");
        UserController.updateExamForUser(user,e,null);
        assertFalse(e.getRoom()!="FST CSL 2");
        assertFalse(e.getTitle()!="CW Exam 1");
        assertFalse(e.getNote()==null);
    }

    @Test
    public void calculateDegreeGPA() {
        assertFalse(FirebaseDatabaseHelper.getGradingSchema()==null);

        double gpaD = UserController.calculateDegreeGPA(user);
        java.math.BigDecimal bd = new java.math.BigDecimal(Double.toString(gpaD));
        bd = bd.setScale(2, java.math.RoundingMode.HALF_UP);
        gpaD = bd.doubleValue();

        // Checks that the GPA is within the appropriate range
        assertFalse(gpaD==0.0);
        assertFalse(gpaD>4.3);
        // Checks the value of the degree GPA is calculated at 3.6
        assertTrue(gpaD==3.6);

    }

    @Test
    public void calculateCumulativeGPA() {
        assertFalse(FirebaseDatabaseHelper.getGradingSchema()==null);

        double gpaC = UserController.calculateCumulativeGPA(user);
        java.math.BigDecimal bd = new java.math.BigDecimal(Double.toString(gpaC));
        bd = bd.setScale(2, java.math.RoundingMode.HALF_UP);
        gpaC = bd.doubleValue();

        // Checks that the GPA is within the appropriate range
        assertFalse(gpaC==0.0);
        assertFalse(gpaC>4.3);
        // Checks the value of the degree GPA is calculated at 3.19
        assertTrue(gpaC==3.19);

    }

    @After
    public void after() {
        FirebaseDatabaseHelper.disableTestingMode();
    }

}