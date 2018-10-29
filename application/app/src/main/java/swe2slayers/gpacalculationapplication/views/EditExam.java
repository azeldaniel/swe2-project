package swe2slayers.gpacalculationapplication.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.Time;

public class EditExam extends AppCompatActivity {

    private User user;
    private Exam exam;

    private Spinner courseSpinner;
    private TextInputEditText titleEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText weightEditText;
    private TextInputEditText markEditText;
    private TextInputEditText totalEditText;
    private TextInputEditText noteEditText;

    private TextInputEditText roomEditText;
    private TextInputEditText durationEditText;
    private TextInputEditText timeEditText;

    private boolean editMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exam);

        final Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        exam = (Exam) intent.getSerializableExtra("exam");

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

        roomEditText = (TextInputEditText) findViewById(R.id.room);
        durationEditText = (TextInputEditText) findViewById(R.id.duration);
        timeEditText = (TextInputEditText) findViewById(R.id.time);

        if(exam == null){
            getSupportActionBar().setTitle("Add New Exam");
            exam = new Exam();
        } else {
            getSupportActionBar().setTitle("Edit Exam");
            editMode = true;
            updateUI();
        }

        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(editMode) {
                        new DatePickerDialog(EditExam.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        }, exam.getDate().getYear(), exam.getDate().getMonth()-1, exam.getDate().getDay()).show();
                    }else{
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(EditExam.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
            }
        });

        timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new TimePickerDialog(EditExam.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            timeEditText.setText(hourOfDay+":"+minute);
                        }
                    }, 0, 0, false).show();
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

                    if(editMode && !exam.getCourseId().equals(course.getCourseId())){
                        courseSpinner.setSelection(courses.size()-1);
                    }
                }

                courseTitles.add("Add Course");

                courseSpinner.setAdapter(new ArrayAdapter<String>(EditExam.this, android.R.layout.simple_list_item_1, courseTitles));

                courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == courseTitles.size() - 1){
                            Intent intent1 = new Intent(EditExam.this, EditCourse.class);
                            intent1.putExtra("user", user);
                            startActivity(intent1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                if(editMode) {
                    for (int i = 0; i < courses.size(); i++) {
                        if (exam.getCourseId().equals(courses.get(i).getCourseId())) {
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
                    titleEditText.setError("Please enter an exam title!");
                    return;
                }
                exam.setTitle(title);

                try{
                    String[] date = dateEditText.getText().toString().trim().split("/");
                    Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    exam.setDate(end);
                }catch (Exception e){
                    dateEditText.setError("Please enter correctly formatted date!");
                    return;
                }

                try{
                    exam.setWeight(Double.parseDouble(weightEditText.getText().toString().trim()));
                }catch (NumberFormatException e){
                    weightEditText.setError("Please enter a valid weight");
                }

                try{
                    exam.setMark(Double.parseDouble(markEditText.getText().toString().trim()));
                }catch (NumberFormatException e){
                    markEditText.setError("Please enter a valid mark");
                }

                try{
                    exam.setTotal(Double.parseDouble(totalEditText.getText().toString().trim()));
                }catch (NumberFormatException e){
                    totalEditText.setError("Please enter a valid total");
                }

                exam.setNote(noteEditText.getText().toString().trim());

                exam.setRoom(roomEditText.getText().toString().trim());

                /*if(!durationEditText.getText().toString().trim().equals("")) {
                    try {
                        String duration = durationEditText.getText().toString().trim();
                        if (!durationEditText.equals("")) {
                            exam.setDuration(Integer.parseInt(duration));
                        }
                    } catch (NumberFormatException e) {
                        durationEditText.setError("Please enter a valid duration");
                    }
                }*

                try{
                    String[] t = timeEditText.getText().toString().trim().split(":");
                    Time time = new Time(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
                    exam.setTime(time);
                }catch (Exception e){
                    dateEditText.setError("Please enter correctly formatted time!");
                    return;
                }*/

                if(courseSpinner.getSelectedItemPosition()==0){
                    exam.setCourseId("");
                }else{
                    exam.setCourseId(courses.get(courseSpinner.getSelectedItemPosition() - 1).getCourseId());
                }

                exam.setUserId(user.getUserId());

                if(editMode){
                    UserController.updateExamForUser(user, exam);
                }else{
                    UserController.addExamForUser(user, exam);
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
                UserController.removeExamForUser(user, exam);
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
        titleEditText.setText(exam.getTitle());
        dateEditText.setText(exam.getDate().toString());
        weightEditText.setText(String.valueOf(exam.getWeight()));
        markEditText.setText(String.valueOf(exam.getMark()));
        totalEditText.setText(String.valueOf(exam.getTotal()));
        noteEditText.setText(exam.getNote());
        roomEditText.setText(exam.getRoom());
        durationEditText.setText(String.valueOf(exam.getDuration()));
        if(exam.getTime() != null) {
            timeEditText.setText(exam.getTime().toString());
        }
    }
}
