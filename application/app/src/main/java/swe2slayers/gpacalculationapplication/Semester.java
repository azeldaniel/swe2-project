public class Semester{

	private int semesterNum;

	private ArraList<Course> courses;

	private Date start;

	private Date end;

	public Semester(int semesterNum){
		this.semesterNum = semesterNum;
		this.courses = new ArraList<>();
		this.start = new Date();
		this.end = new Date();
	}

	public Semester(int semesterNum, Date start, Date end){
		this(semesterNum);
		this.start = start;
		this.end = end;
	}

	public void addCourse(Course course){
		this.courses.add(course);
	}

	public void removeCourse(Course course){
		this.courses.remove(course);
	}

	// TODO
	public double calculateSemesterGPA(){
		return 0;
	}
}