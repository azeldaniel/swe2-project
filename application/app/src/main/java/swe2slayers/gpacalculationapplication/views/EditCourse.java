package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.CourseController;
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class EditCourse extends AppCompatActivity {

    private User user;
    private Course course;

    private Spinner semesterSpinner;
    private TextInputEditText codeEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText creditsEditText;
    private TextInputEditText finalGradeEditText;
    private TextInputEditText levelEditText;
    private TextInputEditText targetGradeEditText;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        user = (User) getIntent().getSerializableExtra("user");
        course = (Course) getIntent().getSerializableExtra("course");
        final Semester semester = (Semester) getIntent().getSerializableExtra("semester");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        semesterSpinner = (Spinner) findViewById(R.id.semesters);
        codeEditText = (TextInputEditText) findViewById(R.id.code);
        nameEditText = (TextInputEditText) findViewById(R.id.name);
        creditsEditText = (TextInputEditText) findViewById(R.id.credits);
        finalGradeEditText = (TextInputEditText) findViewById(R.id.finalGrade);
        levelEditText = (TextInputEditText) findViewById(R.id.level);
        targetGradeEditText = (TextInputEditText) findViewById(R.id.targetGrade);

        if(course == null){
            getSupportActionBar().setTitle("Add New Course");
            course = new Course();
            course.setFinalGrade(-1);
            if(semester != null){
                course.setSemesterId(semester.getSemesterId());
            }
        } else {
            getSupportActionBar().setTitle("Edit Course");
            editMode = true;
            updateUI();
            if(CourseController.getAssignmentsForCourse(course).size()!=0 || CourseController.getExamsForCourse(course).size() != 0){
                finalGradeEditText.setEnabled(false);
                finalGradeEditText.setText(String.valueOf(CourseController.calculatePercentageFinalGrade(course)));
            }
        }

        final List<Semester> semesters = new ArrayList<>();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                semesters.clear();

                final List<String> semesterTitles = new ArrayList<>();

                semesterTitles.add("Select One");

                for (DataSnapshot sem: dataSnapshot.getChildren()) {
                    Semester semester = sem.getValue(Semester.class);
                    semesters.add(semester);
                    try{
                        semesterTitles.add(SemesterController.getYearForSemester(semester).getTitle() + " " + semester.getTitle());
                    }catch (NullPointerException e){
                        semesterTitles.add(semester.getTitle());
                    }
                }

                Collections.sort(semesterTitles);

                semesterTitles.add("Add Semester");

                Collections.sort(semesters, new Comparator<Semester>() {
                    @Override
                    public int compare(Semester s1, Semester s2) {
                        Year y1 = FirebaseDatabaseHelper.getYear(s1.getYearId());
                        Year y2 = FirebaseDatabaseHelper.getYear(s2.getYearId());

                        int c = s1.getYearId().compareTo(s2.getYearId());

                        if(y1 != null && y2 != null){
                            c = y1.getTitle().compareTo(y2.getTitle());
                        }

                        if(c == 0){
                            c = s1.getTitle().compareTo(s2.getTitle());
                        }

                        return c;
                    }
                });

                semesterSpinner.setAdapter(new ArrayAdapter<String>(EditCourse.this, android.R.layout.simple_list_item_1, semesterTitles));

                semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == semesterTitles.size() - 1){
                            Intent intent1 = new Intent(EditCourse.this, EditSemester.class);
                            intent1.putExtra("user", user);
                            startActivity(intent1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                if(editMode || semester != null) {
                    for (int i = 0; i < semesters.size(); i++) {
                        if (course.getSemesterId().equals(semesters.get(i).getSemesterId())) {
                            semesterSpinner.setSelection(i + 1);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        UserController.attachSemestersListenerForUser(user, listener);

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditText.getText().toString().trim();
                if(name.equals("")){
                    nameEditText.setError("Please enter a course name!");
                    return;
                }
                course.setName(name);

                String code = codeEditText.getText().toString().trim();
                if(code.equals("")){
                    codeEditText.setError("Please enter a course code!");
                    return;
                }
                course.setCode(code);

                try{
                    course.setCredits(Integer.parseInt(creditsEditText.getText().toString().trim()));
                }catch (NumberFormatException e){
                    creditsEditText.setError("Please enter a valid amount of credits");
                }

                try{
                    if(finalGradeEditText.isEnabled()) {
                        String finalGrade = finalGradeEditText.getText().toString().trim();
                        if (!finalGrade.equals("")) {
                            course.setFinalGrade(Double.parseDouble(finalGrade));
                        }
                    }
                }catch (NumberFormatException e){
                    finalGradeEditText.setError("Please enter a valid final grade");
                }

                try{
                    String level = levelEditText.getText().toString().trim();
                    if(!level.equals("")) {
                        course.setLevel(Integer.parseInt(level));
                    }
                }catch (NumberFormatException e){
                    levelEditText.setError("Please enter a valid course level");
                }

                try{
                    String targetGrade = targetGradeEditText.getText().toString().trim();
                    if(!targetGrade.equals("")) {
                        course.setTargetGrade(Double.parseDouble(targetGrade));
                    }
                }catch (NumberFormatException e){
                    targetGradeEditText.setError("Please enter a valid target grade");
                }

                if(semesterSpinner.getSelectedItemPosition()==0){
                    course.setSemesterId("");
                }else {
                    course.setSemesterId(semesters.get(semesterSpinner.getSelectedItemPosition() - 1).getSemesterId());
                }

                course.setUserId(user.getUserId());

                if(editMode){
                    UserController.updateCourseForUser(user, course);
                }else{
                    UserController.addCourseForUser(user, course);
                }

                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUI(){
        codeEditText.setText(course.getCode());
        nameEditText.setText(course.getName());
        creditsEditText.setText(String.valueOf(course.getCredits()));
        finalGradeEditText.setText(String.valueOf(course.getFinalGrade()));
        levelEditText.setText(String.valueOf(course.getLevel()));
        targetGradeEditText.setText(String.valueOf(course.getTargetGrade()));
    }
}
