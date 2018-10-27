package swe2slayers.gpacalculationapplication.views;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Date;

public class EditAssignment extends AppCompatActivity {

    private User user;
    private Assignment assignment;

    private Spinner courseSpinner;
    private TextInputEditText titleEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText weightEditText;
    private TextInputEditText markEditText;
    private TextInputEditText totalEditText;
    private TextInputEditText noteEditText;


    private boolean editMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);

        final Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        assignment = (Assignment) intent.getSerializableExtra("assignment");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseSpinner = (Spinner) findViewById(R.id.courses);
        titleEditText = (TextInputEditText) findViewById(R.id.title);
        dateEditText = (TextInputEditText) findViewById(R.id.date);
        weightEditText = (TextInputEditText) findViewById(R.id.weight);
        markEditText = (TextInputEditText) findViewById(R.id.mark);
        totalEditText = (TextInputEditText) findViewById(R.id.total);
        noteEditText = (TextInputEditText) findViewById(R.id.note);

        if(assignment == null){
            getSupportActionBar().setTitle("Add New Assignment");
            assignment = new Assignment();
        } else {
            getSupportActionBar().setTitle("Edit Assignment");
            editMode = true;
            updateUI();
        }

        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(editMode) {
                        new DatePickerDialog(EditAssignment.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        }, assignment.getDate().getYear(), assignment.getDate().getMonth()-1, assignment.getDate().getDay()).show();
                    }else{
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(EditAssignment.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
            }
        });

        final List<Course> courses = new ArrayList<>();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courses.clear();

                final List<String> courseTitles = new ArrayList<>();

                courseTitles.add("Select One");

                for (DataSnapshot sem: dataSnapshot.getChildren()) {
                    Course course = sem.getValue(Course.class);
                    courses.add(course);
                    courseTitles.add(course.getCode() + " (" + course.getName() + ")");
                }

                courseTitles.add("Add Course");

                courseSpinner.setAdapter(new ArrayAdapter<String>(EditAssignment.this, android.R.layout.simple_list_item_1, courseTitles));

                courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == courseTitles.size() - 1){
                            Intent intent1 = new Intent(EditAssignment.this, EditCourse.class);
                            intent1.putExtra("user", user);
                            startActivity(intent1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                if(editMode) {
                    for (int i = 0; i < courses.size(); i++) {
                        if (assignment.getCourseId().equals(courses.get(i).getCourseId())) {
                            courseSpinner.setSelection(i + 1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        UserController.attachCoursesListenerForUser(user, listener);

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleEditText.getText().toString().trim();
                if(title.equals("")){
                    titleEditText.setError("Please enter an assignment title!");
                    return;
                }
                assignment.setTitle(title);

                try{
                    String[] date = dateEditText.getText().toString().trim().split("/");
                    Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    assignment.setDate(end);
                }catch (Exception e){
                    dateEditText.setError("Please enter correctly formatted date!");
                    return;
                }

                try{
                    assignment.setWeight(Double.parseDouble(weightEditText.getText().toString().trim()));
                }catch (NumberFormatException e){
                    weightEditText.setError("Please enter a valid weight");
                }

                try{
                    assignment.setMark(Double.parseDouble(markEditText.getText().toString().trim()));
                }catch (NumberFormatException e){
                    markEditText.setError("Please enter a valid mark");
                }

                try{
                    assignment.setTotal(Double.parseDouble(totalEditText.getText().toString().trim()));
                }catch (NumberFormatException e){
                    totalEditText.setError("Please enter a valid total");
                }

                assignment.setNote(noteEditText.getText().toString().trim());

                if(courseSpinner.getSelectedItemPosition()==0){
                    assignment.setCourseId("");
                }else {
                    assignment.setCourseId(courses.get(courseSpinner.getSelectedItemPosition()).getCourseId());
                }

                assignment.setUserId(user.getUserId());

                if(editMode){
                    UserController.updateAssignmentForUser(user, assignment);
                }else{
                    UserController.addAssignmentForUser(user, assignment);
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
            case R.id.delete:
                UserController.removeAssignmentForUser(user, assignment);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(editMode) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.view_model_menu, menu);
        }
        return true;
    }

    public void updateUI(){
        titleEditText.setText(assignment.getTitle());
        dateEditText.setText(assignment.getDate().toString());
        weightEditText.setText(String.valueOf(assignment.getWeight()));
        markEditText.setText(String.valueOf(assignment.getMark()));
        totalEditText.setText(String.valueOf(assignment.getTotal()));
        noteEditText.setText(assignment.getNote());
    }
}
