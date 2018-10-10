public class Course{

	private String code;

	private String name;

	private int credits;

	private ArrayList<Gradable> gradables;

	private int level;

	private double targetGrade;

	public Course(String code, String name, int credits, int level){
		this.code = code;
		this.name = name;
		this.credits = credits;
		this.level = level;		
		this.gradables = new ArrayList<>();
	}

	public Course(String code, String name, int credits, int level, double finalGrade){
		this(code, name, credits, level);
		this.finalGrade = finalGrade
	}

	public double calculateFinalGrade(){

		double finalGrade = 0;

		for(Gradable gradable: this.gradables){
			finalGrade += gradable.calculateWeightedGrade();
		}

		return finalGrade;
	}

	public double calculateMinimumGrade(){

		double finalGrade = this.calculateFinalGrade();

		double minimum = 0;

		if(finalGrade < 50){
			minimum = 50 - finalGrade;
		}

		return minimum;
	}

	public String getCode(){
		return this.code;
	}

	public void setCode(String code){
		this.code = code;
	}


	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}


	public int getCredits(){
		return this.credits;
	}

	public void setCredits(int credits){
		this.credits = credits;
	}


	public double getFinalGrade(){
		return this.calculateFinalGrade();
	}

	public ArrayList<Gradable> getAssignments(){
		ArrayList<Gradable> assignments = new ArrayList<>();

		for(Gradable gradable : this.gradables){
			if(gradable instanceof Assignment){
				assignments.add(gradable);
			}
		}

		return assignments;
	}

	public ArrayList<Gradable> getExams(){
		ArrayList<Gradable> assignments = new ArrayList<>();

		for(Gradable gradable : this.gradables){
			if(gradable instanceof Exam){
				assignments.add(gradable);
			}
		}

		return assignments;
	}

	public void addGradable(Gradable gradable){
		this.gradables.add(gradable);
	}

	public void removeGradable(Gradable gradable){
		this.gradables.remove(gradable);
	}


	public int getLevel(){
		return this.level;
	}

	public void setLevel(int level){
		this.level = level;
	}


	public double getTargetGrade(){
		return this.targetGrade;
	}

	public void setTargetGrade(double targetGrade){
		this.targetGrade = targetGrade;
	}
}