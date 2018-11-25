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
 * This activity will add a new assignment or edit a current one
 */

package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import swe2slayers.gpacalculationapplication.controllers.GradableController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.DateDialogHelper;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Utils;

public class EditAssignment extends AppCompatActivity implements Closable {

    private User user;
    private Assignment assignment;
    private Course course;

    private TextView courseText;
    private TextInputLayout titleTextInputLayout;
    private TextInputLayout dateTextInputLayout;
    private TextInputLayout weightTextInputLayout;
    private TextInputLayout markTextInputLayout;
    private TextInputLayout totalTextInputLayout;
    private TextInputLayout noteTextInputLayout;
    private ScrollView scrollView;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);

        final Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        assignment = (Assignment) intent.getSerializableExtra("assignment");
        course = (Course) intent.getSerializableExtra("course");

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
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        if(assignment == null){
            getSupportActionBar().setTitle("Add Assignment");
            assignment = new Assignment("", "", new Date(), -1,-1, -1);
            if(course != null){
                assignment.setCourseId(course.getCourseId());
            }
        } else {
            getSupportActionBar().setTitle("Edit Assignment");
            editMode = true;
            course = GradableController.getCourseForGradable(assignment);
            updateUI();
        }

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Course> courses = new ArrayList<>();

                final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(EditAssignment.this, courseText);
                menu.getMenuInflater()
                        .inflate(R.menu.default_menu, menu.getMenu());

                for (DataSnapshot sem: dataSnapshot.getChildren()) {
                    courses.add(sem.getValue(Course.class));
                }

                Collections.sort(courses, new Comparator<Course>() {
                    @Override
                    public int compare(Course c1, Course c2) {
                        return c1.getName().compareTo(c2.getName());
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
                            Intent intent = new Intent(EditAssignment.this, EditCourse.class);
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

    public void save(){

        String title = titleTextInputLayout.getEditText().getText().toString().trim();
        if(title.equals("")){
            titleTextInputLayout.setError("*Required. Please enter the title of the assignment e.g. 'Assignment 1'");
            scrollView.smoothScrollTo(0, ((View) titleTextInputLayout.getParent()).getTop() + titleTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }else{
            titleTextInputLayout.setError(null);
        }
        assignment.setTitle(title);

        try{
            if(!dateTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                String[] date = dateTextInputLayout.getEditText().getText().toString().trim().split("/");
                Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                assignment.setDate(end);
            }
            dateTextInputLayout.setError(null);
        }catch (Exception e){
            dateTextInputLayout.setError("Please enter when the assignment is due in the format DD/MM/YYYY");
            scrollView.smoothScrollTo(0, ((View) dateTextInputLayout.getParent()).getTop() + dateTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            if(!weightTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                assignment.setWeight(Double.parseDouble(weightTextInputLayout.getEditText().getText().toString().trim()));
            }
            weightTextInputLayout.setError(null);
        }catch (NumberFormatException e){
            weightTextInputLayout.setError("Please enter the weight of the assignment as a percent e.g. 10%");
            scrollView.smoothScrollTo(0, ((View) weightTextInputLayout.getParent()).getTop() + weightTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            if(!markTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                assignment.setMark(Double.parseDouble(markTextInputLayout.getEditText().getText().toString().trim()));
            }
            markTextInputLayout.setError(null);
        }catch (NumberFormatException e){
            markTextInputLayout.setError("Please enter the mark attained for the assignment as a fraction of the total e.g. 30");
            scrollView.smoothScrollTo(0, ((View) markTextInputLayout.getParent()).getTop() + markTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            if(!totalTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                assignment.setTotal(Double.parseDouble(totalTextInputLayout.getEditText().getText().toString().trim()));
            }
            totalTextInputLayout.setError(null);
        }catch (NumberFormatException e){
            totalTextInputLayout.setError("Please enter the highest possible mark that can be attained for the assignment e.g. 40");
            scrollView.smoothScrollTo(0, ((View) totalTextInputLayout.getParent()).getTop() + totalTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        assignment.setNote(noteTextInputLayout.getEditText().getText().toString().trim());

        if(course == null){
            assignment.setCourseId("");
        }else {
            assignment.setCourseId(course.getCourseId());
        }

        assignment.setUserId(user.getUserId());

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        if(editMode){
            UserController.updateAssignmentForUser(user, assignment, this);
        }else{
            UserController.addAssignmentForUser(user, assignment, this);
        }
    }

    public void updateUI(){
        if(!assignment.getTitle().equals("")) {
            titleTextInputLayout.getEditText().setText(assignment.getTitle());
        }

        if(assignment.getDate().getYear() != -1) {
            dateTextInputLayout.getEditText().setText(assignment.getDate().toString());
        }

        if(assignment.getWeight() != -1) {
            weightTextInputLayout.getEditText().setText(String.valueOf(assignment.getWeight()));
        }

        if(assignment.getMark() != -1) {
            markTextInputLayout.getEditText().setText(String.valueOf(assignment.getMark()));
        }

        if(assignment.getTotal() != -1) {
            totalTextInputLayout.getEditText().setText(String.valueOf(assignment.getTotal()));
        }

        noteTextInputLayout.getEditText().setText(assignment.getNote());

        if(course != null){
            courseText.setText(course.getCode() + " (" + course.getName() + ")");
        }

        dateTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(course != null){
                    Semester s = CourseController.getSemesterForCourse(course);

                    if(s != null){
                        Date startDate = s.getStart();
                        Date endDate = s.getEnd();
                        DateDialogHelper.showDateDialog(EditAssignment.this, (TextInputEditText) dateTextInputLayout.getEditText(), assignment.getDate(), startDate, endDate);
                        return;
                    }
                }
                DateDialogHelper.showDateDialog(EditAssignment.this, (TextInputEditText) dateTextInputLayout.getEditText(), assignment.getDate(),null, null);
            }
        });
    }

    @Override
    public void close(User user) {
        finish();
    }
}
