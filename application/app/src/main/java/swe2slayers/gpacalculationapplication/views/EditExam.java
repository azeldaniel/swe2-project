/*
 * Copyright (c) 2018. Software Engineering Slayers
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Christopher Joseph (814000605)
 * Michael Bristol (816003612)
 * Maya Bannis (816000144)
 *
 * COMP 3613
 * Software Engineering II
 *
 * GPA Calculator Project
 *
 * This activity will add a new exam or edit a current one
 */

package swe2slayers.gpacalculationapplication.views;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.CourseController;
import swe2slayers.gpacalculationapplication.controllers.GradableController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.DateDialogHelper;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Time;
import swe2slayers.gpacalculationapplication.utils.Utils;

public class EditExam extends AppCompatActivity implements Closable {

    private User user;
    private Exam exam;
    private Course course;

    private TextView courseText;
    private TextInputLayout titleTextInputLayout;
    private TextInputLayout dateTextInputLayout;
    private TextInputLayout weightTextInputLayout;
    private TextInputLayout markTextInputLayout;
    private TextInputLayout totalTextInputLayout;
    private TextInputLayout noteTextInputLayout;
    private TextInputLayout roomTextInputLayout;
    private TextInputLayout durationTextInputLayout;
    private TextInputLayout timeTextInputLayout;

    private ScrollView scrollView;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exam);

        user = (User) getIntent().getSerializableExtra("user");
        exam = (Exam) getIntent().getSerializableExtra("exam");
        course = (Course) getIntent().getSerializableExtra("course");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseText = (TextView) findViewById(R.id.courses);
        titleTextInputLayout = (TextInputLayout) findViewById(R.id.titleLayout);
        dateTextInputLayout = (TextInputLayout) findViewById(R.id.dateLayout);
        weightTextInputLayout = (TextInputLayout) findViewById(R.id.weightLayout);
        markTextInputLayout = (TextInputLayout) findViewById(R.id.markLayout);
        totalTextInputLayout = (TextInputLayout) findViewById(R.id.totalLayout);
        noteTextInputLayout = (TextInputLayout) findViewById(R.id.noteLayout);
        roomTextInputLayout = (TextInputLayout) findViewById(R.id.roomLayout);
        durationTextInputLayout = (TextInputLayout) findViewById(R.id.durationLayout);
        timeTextInputLayout = (TextInputLayout) findViewById(R.id.timeLayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        if(exam == null){
            getSupportActionBar().setTitle("Add Exam");
            exam = new Exam("", "");
            if(course != null) {
                exam.setCourseId(course.getCourseId());
            }
        } else {
            getSupportActionBar().setTitle("Edit Exam");
            course = GradableController.getCourseForGradable(exam);
            editMode = true;
            updateUI();
        }

        timeTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EditExam.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeTextInputLayout.getEditText().setText(hourOfDay + ":" + String.format("%02d", minute));
                    }
                }, 12, 0, false).show();
            }
        });

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Course> courses = new ArrayList<>();

                final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(EditExam.this, courseText);
                menu.getMenuInflater()
                        .inflate(R.menu.default_menu, menu.getMenu());

                for (DataSnapshot sem: dataSnapshot.getChildren()) {
                    courses.add(sem.getValue(Course.class));
                }

                Collections.sort(courses, new Comparator<Course>() {
                    @Override
                    public int compare(Course c1, Course c2) {
                        return c1.getCode().compareTo(c2.getCode());
                    }
                });

                for(Course course: courses) {
                    menu.getMenu().add(R.id.default_group, course.hashCode(), Menu.NONE, course.getCode() + " (" + course.getName() + ")");
                }

                menu.getMenu().add(R.id.add_group, 0, Menu.NONE, "None");
                menu.getMenu().add(R.id.add_group, 1, Menu.NONE, "Add Course");

                menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == 0){
                            course = null;
                            courseText.setText("None");
                            updateUI();
                            return true;
                        }else if(menuItem.getItemId() == 1){
                            Intent intent = new Intent(EditExam.this, EditCourse.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            return true;
                        }else {
                            for (Course cour : courses) {
                                if (cour.hashCode() == menuItem.getItemId()) {
                                    course = cour;
                                    courseText.setText(course.getCode() + " (" + course.getName() + ")");
                                    dateTextInputLayout.getEditText().setText("");
                                    updateUI();
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });


                MenuCompat.setGroupDividerEnabled(menu.getMenu(), true);

                courseText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menu.show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        UserController.attachCoursesListenerForUser(user, listener);

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });


        setMarkListener(markTextInputLayout, totalTextInputLayout);

        updateUI();
    }

    /**
     * Function that sets a pair of listeners to the mark and total edit text variables
     * @param markTextInputLayout The mark edit text layout
     * @param totalTextInputLayout the total edit text layout
     */
    public void setMarkListener(final TextInputLayout markTextInputLayout, final TextInputLayout totalTextInputLayout){

        markTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {

            double mark = -1;
            double total = -1;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    mark = -1;
                    total = -1;
                    try {
                        mark = Double.parseDouble(markTextInputLayout.getEditText().getText().toString().trim());
                        total = Double.parseDouble(totalTextInputLayout.getEditText().getText().toString().trim());
                    }catch (NumberFormatException e){
                    }
                }else{
                    exam.setMark(-1);
                    mark = -1;
                    if (markTextInputLayout.getError() != null) {
                        markTextInputLayout.setError(null);
                    }
                    if(totalTextInputLayout.getError() != null) {
                        totalTextInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mark <= total || total == -1){
                    if(markTextInputLayout.getError() != null) {
                        markTextInputLayout.setError(null);
                    }
                    if(totalTextInputLayout.getError() != null) {
                        totalTextInputLayout.setError(null);
                    }
                }else{
                    if(markTextInputLayout.getError() == null) {
                        markTextInputLayout.setError("Mark cannot be higher than the total");
                    }
                }
            }
        });

        totalTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {

            double mark = -1;
            double total = -1;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    mark = -1;
                    total = -1;
                    try {
                        total = Double.parseDouble(totalTextInputLayout.getEditText().getText().toString().trim());
                        mark = Double.parseDouble(markTextInputLayout.getEditText().getText().toString().trim());
                    }catch (NumberFormatException e){
                    }
                }else{
                    exam.setTotal(-1);
                    total = -1;
                    if (markTextInputLayout.getError() != null) {
                        markTextInputLayout.setError(null);
                    }
                    if(totalTextInputLayout.getError() != null) {
                        totalTextInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(total == 0){
                    totalTextInputLayout.setError("Total cannot be 0");
                }

                if(mark <= total || mark == -1 ||total == -1){
                    if(markTextInputLayout.getError() != null) {
                        markTextInputLayout.setError(null);
                    }
                    if(totalTextInputLayout.getError() != null) {
                        totalTextInputLayout.setError(null);
                    }
                }else{
                    if(totalTextInputLayout.getError() == null) {
                        totalTextInputLayout.setError("Total cannot be lower than the mark");
                    }
                }
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

    public void save(){
        String title = titleTextInputLayout.getEditText().getText().toString().trim();
        if(title.equals("")){
            titleTextInputLayout.setError("*Required. Please enter the title of the exam e.g. 'CW Exam 1'");
            scrollView.smoothScrollTo(0, ((View) titleTextInputLayout.getParent()).getTop() + titleTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }else{
            titleTextInputLayout.setError(null);
        }
        exam.setTitle(title);

        try{
            if(!dateTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                String[] date = dateTextInputLayout.getEditText().getText().toString().trim().split("/");
                Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                exam.setDate(end);
            }
            dateTextInputLayout.setError(null);
        }catch (Exception e){
            dateTextInputLayout.setError("Please enter when the exam is to be held in the format DD/MM/YYYY");
            scrollView.smoothScrollTo(0, ((View) dateTextInputLayout.getParent()).getTop() + dateTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            if(!weightTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                exam.setWeight(Double.parseDouble(weightTextInputLayout.getEditText().getText().toString().trim()));
            }
            weightTextInputLayout.setError(null);
        }catch (NumberFormatException e){
            weightTextInputLayout.setError("Please enter the weight of the exam as a percent e.g. 10");
            scrollView.smoothScrollTo(0, ((View) weightTextInputLayout.getParent()).getTop() + weightTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            if(markTextInputLayout.getError() == null){
                if(!markTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                    exam.setMark(Double.parseDouble(markTextInputLayout.getEditText().getText().toString().trim()));
                }
            }else{
                scrollView.smoothScrollTo(0, ((View) markTextInputLayout.getParent()).getTop() + markTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
                return;
            }
        }catch (NumberFormatException e){
            markTextInputLayout.setError("Please enter the mark attained for the exam as a fraction of the total e.g. 30");
            scrollView.smoothScrollTo(0, ((View) markTextInputLayout.getParent()).getTop() + markTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            if(totalTextInputLayout.getError() == null){
                if(!totalTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                    exam.setTotal(Double.parseDouble(totalTextInputLayout.getEditText().getText().toString().trim()));
                }
            }else{
                scrollView.smoothScrollTo(0, ((View) totalTextInputLayout.getParent()).getTop() + totalTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
                return;
            }
        }catch (NumberFormatException e){
            totalTextInputLayout.setError("Please enter the highest possible mark that can be attained for the exam e.g. 40");
            scrollView.smoothScrollTo(0, ((View) totalTextInputLayout.getParent()).getTop() + totalTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        exam.setRoom(roomTextInputLayout.getEditText().getText().toString().trim());

        try {
            if(!durationTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                String duration = durationTextInputLayout.getEditText().getText().toString().trim();
                if (!durationTextInputLayout.equals("")) {
                    exam.setDuration(Integer.parseInt(duration));
                }
            }
            durationTextInputLayout.setError(null);
        } catch (NumberFormatException e) {
            durationTextInputLayout.setError("Please enter the duration of the exam in minutes e.g. 60");
            scrollView.smoothScrollTo(0, ((View) durationTextInputLayout.getParent()).getTop() + durationTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }


        try{
            if(!timeTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                String[] t = timeTextInputLayout.getEditText().getText().toString().trim().split(":");
                Time time = new Time(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
                exam.setTime(time);
            }
            timeTextInputLayout.setError(null);
        }catch (Exception e){
            timeTextInputLayout.setError("Please enter which time the exam will be held in the format HH:MM");
            scrollView.smoothScrollTo(0, ((View) timeTextInputLayout.getParent()).getTop() + timeTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        exam.setNote(noteTextInputLayout.getEditText().getText().toString().trim());

        if(course == null){
            exam.setCourseId("");
        }else {
            exam.setCourseId(course.getCourseId());
        }

        exam.setUserId(user.getUserId());

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        titleTextInputLayout.getEditText().clearFocus();
        dateTextInputLayout.getEditText().clearFocus();
        weightTextInputLayout.getEditText().clearFocus();
        markTextInputLayout.getEditText().clearFocus();
        totalTextInputLayout.getEditText().clearFocus();
        noteTextInputLayout.getEditText().clearFocus();

        roomTextInputLayout.getEditText().clearFocus();
        durationTextInputLayout.getEditText().clearFocus();
        timeTextInputLayout.getEditText().clearFocus();

        if(editMode){
            UserController.updateExamForUser(user, exam, this);
        }else{
            UserController.addExamForUser(user, exam, this);
        }
    }

    public void updateUI(){
        if(!exam.getTitle().equals("")) {
            titleTextInputLayout.getEditText().setText(exam.getTitle());
        }

        if(exam.getDate().getYear() != -1) {
            dateTextInputLayout.getEditText().setText(exam.getDate().toString());
        }

        if(exam.getWeight() != -1) {
            weightTextInputLayout.getEditText().setText(String.valueOf(exam.getWeight()));
        }

        if(exam.getMark() != -1) {
            markTextInputLayout.getEditText().setText(String.valueOf(exam.getMark()));
        }

        if(exam.getTotal() != -1) {
            totalTextInputLayout.getEditText().setText(String.valueOf(exam.getTotal()));
        }

        noteTextInputLayout.getEditText().setText(exam.getNote());

        if(course != null){
            courseText.setText(course.getCode() + " (" + course.getName() + ")");
        }

        roomTextInputLayout.getEditText().setText(exam.getRoom());

        if(exam.getDuration() != -1) {
            durationTextInputLayout.getEditText().setText(String.valueOf(exam.getDuration()));
        }

        if(exam.getTime() != null && exam.getTime().getHour() != -1) {
            timeTextInputLayout.getEditText().setText(exam.getTime().toString());
        }

        dateTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(course != null){
                    Semester s = CourseController.getSemesterForCourse(course);

                    if(s != null){
                        Date startDate = s.getStart();
                        Date endDate = s.getEnd();
                        DateDialogHelper.showDateDialog(EditExam.this, (TextInputEditText) dateTextInputLayout.getEditText(), exam.getDate(), startDate, endDate);
                        return;
                    }
                }
                DateDialogHelper.showDateDialog(EditExam.this, (TextInputEditText) dateTextInputLayout.getEditText(), exam.getDate(),null, null);
            }
        });
    }

    @Override
    public void close(User user) {
        finish();
    }
}
