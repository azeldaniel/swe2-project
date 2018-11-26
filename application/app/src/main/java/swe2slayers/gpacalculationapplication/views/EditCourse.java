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
 * This activity will add a new course or edit a current one
 */

package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Sorter;
import swe2slayers.gpacalculationapplication.utils.Utils;

public class EditCourse extends AppCompatActivity implements Closable {

    private User user;
    private Course course;
    private Semester semester;

    private TextView semesterText;
    private TextInputLayout codeTextInputLayout;
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout creditsTextInputLayout;
    private TextInputLayout finalGradeEditText;
    private TextInputLayout levelEditText;
    private TextInputLayout targetGradeEditText;
    private CheckBox autoFinalGrade;
    private ScrollView scrollView;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        user = (User) getIntent().getSerializableExtra("user");
        course = (Course) getIntent().getSerializableExtra("course");
        semester = (Semester) getIntent().getSerializableExtra("semester");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        semesterText = (TextView) findViewById(R.id.semesters);
        codeTextInputLayout = (TextInputLayout) findViewById(R.id.codeLayout);
        nameTextInputLayout = (TextInputLayout) findViewById(R.id.nameLayout);
        creditsTextInputLayout = (TextInputLayout) findViewById(R.id.creditsLayout);
        finalGradeEditText = (TextInputLayout) findViewById(R.id.finalGradeLayout);
        levelEditText = (TextInputLayout) findViewById(R.id.levelLayout);
        targetGradeEditText = (TextInputLayout) findViewById(R.id.targetGradeLayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        LinearLayout ll = (LinearLayout) findViewById(R.id.auto_ll);
        autoFinalGrade = (CheckBox) findViewById(R.id.auto);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoFinalGrade.isChecked()){
                    autoFinalGrade.setChecked(false);
                }else{
                    autoFinalGrade.setChecked(true);
                }
            }
        });

        autoFinalGrade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    findViewById(R.id.finalGradeLayout).setVisibility(View.GONE);
                    findViewById(R.id.targetGradeLayout).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.finalGradeLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.targetGradeLayout).setVisibility(View.GONE);
                    course.setTargetGrade(-1);
                }
            }
        });

        if(course == null){
            // If new course is being created
            getSupportActionBar().setTitle("Add Course");
            course = new Course("", "", "", "", 0, -1);
            course.setFinalGrade(-1);

            // If a semester was passed to create the course for
            if(semester != null){
                course.setSemesterId(semester.getSemesterId());
            }
        } else {
            // If an existing course is being edited
            getSupportActionBar().setTitle("Edit Course");
            editMode = true;
            semester = CourseController.getSemesterForCourse(course);

            if(course.getFinalGrade() != -1){
                autoFinalGrade.setChecked(false);
            }

            updateUI();
        }

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<Semester> semesters = new ArrayList<>();

                final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(EditCourse.this, semesterText);
                menu.getMenuInflater()
                        .inflate(R.menu.default_menu, menu.getMenu());

                for (DataSnapshot sem: dataSnapshot.getChildren()) {
                    semesters.add(sem.getValue(Semester.class));
                }

                Sorter.sortSemesters(semesters);

                for(Semester sem: semesters){
                    menu.getMenu().add(R.id.default_group, sem.hashCode(), Menu.NONE, SemesterController.getSemesterTitleWithYear(sem));
                }

                menu.getMenu().add(R.id.add_group, 0, Menu.NONE, "None");
                menu.getMenu().add(R.id.add_group, 1, Menu.NONE, "Add Semester");

                menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == 0){
                            semester = null;
                            semesterText.setText("None");
                            updateUI();
                            return true;
                        }else if(menuItem.getItemId() == 1){
                            Intent intent = new Intent(EditCourse.this, EditSemester.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            return true;
                        }else {
                            for (Semester sem : semesters) {
                                if (sem.hashCode() == menuItem.getItemId()) {
                                    semester = sem;
                                    semesterText.setText(SemesterController.getSemesterTitleWithYear(sem));
                                    updateUI();
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });


                MenuCompat.setGroupDividerEnabled(menu.getMenu(), true);

                semesterText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menu.show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        UserController.attachSemestersListenerForUser(user, listener);

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        levelEditText.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(EditCourse.this, levelEditText);
                menu.getMenuInflater()
                        .inflate(R.menu.default_menu, menu.getMenu());

                menu.getMenu().add(R.id.default_group, 1, Menu.NONE, "Level 1 (Undergraduate)");
                menu.getMenu().add(R.id.default_group, 2, Menu.NONE, "Level 2 (Undergraduate)");
                menu.getMenu().add(R.id.default_group, 3, Menu.NONE, "Level 3 (Undergraduate)");
                menu.getMenu().add(R.id.default_group, 6, Menu.NONE, "Level 6 (Postgraduate)");
                menu.getMenu().add(R.id.default_group, 7, Menu.NONE, "Level 7 (Postgraduate)");

                menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        levelEditText.getEditText().setText(String.valueOf(menuItem.getItemId()));
                        return false;
                    }
                });

                menu.show();
            }
        });

        codeTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    levelEditText.getEditText().setText(String.valueOf(Integer.parseInt(String.valueOf(s.charAt(4)))));
                    return;
                }catch (Exception e){}

                try{
                    levelEditText.getEditText().setText(String.valueOf(Integer.parseInt(String.valueOf(s.charAt(5)))));
                    return;
                }catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateUI();
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
        if(!course.getCode().equals("")) {
            codeTextInputLayout.getEditText().setText(course.getCode());
        }
        if(!course.getName().equals("")) {
            nameTextInputLayout.getEditText().setText(course.getName());
        }

        if(course.getCredits() != 0) {
            creditsTextInputLayout.getEditText().setText(String.valueOf(course.getCredits()));
        }

        if(course.getLevel() != -1) {

            levelEditText.getEditText().setText(String.valueOf(course.getLevel()));
        }

        if(course.getTargetGrade() != -1) {
            targetGradeEditText.getEditText().setText(String.format("%.2f", course.getTargetGrade()));
        }

        if(!autoFinalGrade.isChecked() && course.getFinalGrade() != -1 ){
            finalGradeEditText.getEditText().setText(String.format("%.2f", course.getFinalGrade()));
        }

        if(semester != null){
            semesterText.setText(SemesterController.getSemesterTitleWithYear(semester));
        }
    }

    public void save(){

        String code = codeTextInputLayout.getEditText().getText().toString().trim();
        if(code.equals("")){
            codeTextInputLayout.setError("*Required. Please enter the code of the course e.g. 'COMP 3613'");
            scrollView.smoothScrollTo(0, ((View) codeTextInputLayout.getParent()).getTop() + codeTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }else{
            codeTextInputLayout.setError(null);
        }
        course.setCode(code);

        String name = nameTextInputLayout.getEditText().getText().toString().trim();
        if(name.equals("")){
            nameTextInputLayout.setError("*Required. Please enter the name of the course e.g. 'Software Engineering II'");
            scrollView.smoothScrollTo(0, ((View) nameTextInputLayout.getParent()).getTop() + nameTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }else{
            nameTextInputLayout.setError(null);
        }
        course.setName(name);

        try{
            course.setCredits(Integer.parseInt(creditsTextInputLayout.getEditText().getText().toString().trim()));
            creditsTextInputLayout.setError(null);
        }catch (NumberFormatException e){
            creditsTextInputLayout.setError("*Required. Please enter the amount of credits the course is worth");
            scrollView.smoothScrollTo(0, ((View) creditsTextInputLayout.getParent()).getTop() + creditsTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            String level = levelEditText.getEditText().getText().toString().trim();
            course.setLevel(Integer.parseInt(level));
            levelEditText.setError(null);
        }catch (NumberFormatException e){
            levelEditText.setError("*Required. Please enter the year level of the course e.g. '1' for year 1 courses");
            scrollView.smoothScrollTo(0, ((View) levelEditText.getParent()).getTop() + levelEditText.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            String targetGrade = targetGradeEditText.getEditText().getText().toString().trim();
            if(!targetGrade.equals("")) {
                course.setTargetGrade(Double.parseDouble(targetGrade));
            }
            targetGradeEditText.setError(null);
        }catch (NumberFormatException e){
            targetGradeEditText.setError("Please enter the grade that you want to achieve for the course as a percent e.g. 80");
            scrollView.smoothScrollTo(0, ((View) targetGradeEditText.getParent()).getTop() + targetGradeEditText.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            if(!autoFinalGrade.isChecked()) {
                String finalGrade = finalGradeEditText.getEditText().getText().toString().trim();
                course.setFinalGrade(Double.parseDouble(finalGrade));
            }else{
                course.setFinalGrade(-1);
            }
            finalGradeEditText.setError(null);
        }catch (NumberFormatException e){
            finalGradeEditText.setError("*Required. Please enter the final grade attained for a completed course as a percentage.");
            scrollView.smoothScrollTo(0, ((View) finalGradeEditText.getParent()).getTop() + finalGradeEditText.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        if(semester == null){
            course.setSemesterId("");
        }else {
            course.setSemesterId(semester.getSemesterId());
        }

        course.setUserId(user.getUserId());

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        codeTextInputLayout.getEditText().clearFocus();
        nameTextInputLayout.getEditText().clearFocus();
        creditsTextInputLayout.getEditText().clearFocus();
        finalGradeEditText.getEditText().clearFocus();
        levelEditText.getEditText().clearFocus();
        targetGradeEditText.getEditText().clearFocus();

        if(editMode){
            UserController.updateCourseForUser(user, course, this);
        }else{
            UserController.addCourseForUser(user, course, this);
        }
    }

    @Override
    public void close(User user) {
        finish();
    }
}
