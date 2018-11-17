package swe2slayers.gpacalculationapplication.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class createCourseTest {

    @Test
    public void getCourseId() {
        String id = "mq78Bif9hgfkBFa";
        Course course = new Course();
        course.setCourseId(id);
        assertTrue(course.getCourseId().equals(id));
    }

    @Test
    public void getSemesterId() {
        String id = "Year 3 Semester 1";
        Course course = new Course("COMP3613", "Software Engineering 2", id, "oDEebPmCXEuyn653", 3, 3);
        assertTrue(course.getSemesterId().equals(id));
        id = "Year 3 Semester 2";
        course.setSemesterId(id);
        assertTrue(course.getSemesterId().equals(id));
    }

    @Test
    public void getUserId() {
        String id = "oDEebPmCXEuyn653";
        Course course = new Course("COMP3613", "Software Engineering 2", "Year 3 Semester 1", id, 3, 3);
        assertTrue(course.getUserId().equals(id));
        id = "thisIsATestID";
        course.setUserId(id);
        assertTrue(course.getUserId().equals(id));
    }

    @Test
    public void getCode() {
        String code = "COMP3613";
        Course course = new Course(code, "Software Engineering 2", "Year 3 Semester 1", "oDEebPmCXEuyn653", 3, 3);
        assertTrue(course.getCode().equals(code));
        code = "INFO2602";
        course.setCode(code);
        assertTrue(course.getCode().equals(code));
    }

    @Test
    public void getName() {
        String name = "Software Engineering 2";
        Course course = new Course("COMP3613", name, "Year 3 Semester 1", "oDEebPmCXEuyn653", 3, 3);
        assertTrue(course.getName().equals(name));
        name = "Software Engineering 1";
        course.setName(name);
        assertTrue(course.getName().equals(name));
    }

    @Test
    public void getCredits() {
        int credits = 3;
        Course course = new Course("COMP3613", "Software Engineering 2", "Year 3 Semester 1", "oDEebPmCXEuyn653", credits, 3);
        assertTrue(course.getCredits() == credits);
        credits = 4;
        course.setCredits(credits);
        assertTrue(course.getCredits() == credits);
    }

    @Test
    public void getFinalGrade() {
        int finalGrade = 78;
        Course course = new Course("COMP3613", "Software Engineering 2", "Year 3 Semester 1", "oDEebPmCXEuyn653", 3, 3, finalGrade);
        assertTrue(course.getFinalGrade() == finalGrade);
        finalGrade = 87;
        course.setFinalGrade(finalGrade);
        assertTrue(course.getFinalGrade() == finalGrade);
    }

    @Test
    public void getLevel() {
        int level = 3;
        Course course = new Course("COMP3613", "Software Engineering 2", "Year 3 Semester 1", "oDEebPmCXEuyn653", 3, level);
        assertTrue(course.getLevel() == level);
        level = 4;
        course.setLevel(level);
        assertTrue(course.getLevel() == level);
    }

    @Test
    public void getTargetGrade() {
        double targetGrade = 95;
        Course course = new Course();
        course.setTargetGrade(targetGrade);
        assertTrue(course.getTargetGrade() == targetGrade);
    }
}
