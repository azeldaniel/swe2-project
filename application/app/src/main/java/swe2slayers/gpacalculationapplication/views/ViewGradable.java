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
 * This activity allows a user to view an exam or assignment
 */

package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.CourseController;
import swe2slayers.gpacalculationapplication.controllers.GradableController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.views.adapters.ViewPagerAdapter;
import swe2slayers.gpacalculationapplication.views.fragments.AssignmentFragment;
import swe2slayers.gpacalculationapplication.views.fragments.ExamFragment;

public class ViewGradable extends AppCompatActivity implements Closable {

    private User user;

    private Exam exam;
    private Assignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gradable);

        user = (User) getIntent().getSerializableExtra("user");
        exam = (Exam) getIntent().getSerializableExtra("exam");
        assignment = (Assignment) getIntent().getSerializableExtra("assignment");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ViewCompat.setElevation(appBarLayout, 12);
            }
        });

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot grad : dataSnapshot.getChildren()){
                    if(exam != null){
                        exam = grad.getValue(Exam.class);
                    }else{
                        assignment = grad.getValue(Assignment.class);
                    }
                }
                update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if(exam != null){
            GradableController.attachGradableListener(exam, listener);
        }else{
            GradableController.attachGradableListener(assignment, listener);
        }

        update();
    }

    public void update(){
        if(exam != null){
            getSupportActionBar().setTitle(exam.getTitle());
        }

        if(assignment != null){
            getSupportActionBar().setTitle(assignment.getTitle());
        }

        TextView course = (TextView) findViewById(R.id.course);
        TextView grade = (TextView) findViewById(R.id.grade);
        TextView caption = (TextView) findViewById(R.id.caption);
        TextView due = (TextView) findViewById(R.id.due);
        TextView weight = (TextView) findViewById(R.id.weight);
        TextView mark = (TextView) findViewById(R.id.mark);
        TextView total = (TextView) findViewById(R.id.total);
        TextView notes = (TextView) findViewById(R.id.notes);

        TextView room = (TextView) findViewById(R.id.room);
        TextView duration = (TextView) findViewById(R.id.duration);
        TextView time = (TextView) findViewById(R.id.time);

        final Gradable gradable;

        if(exam != null){
            gradable = exam;
        }else{
            gradable = assignment;
        }

        grade.setText(GradableController.calculateLetterGrade(gradable));

        if(gradable.getTotal() <= 0 || gradable.getMark() < 0) {
            grade.setVisibility(View.GONE);
            caption.setText("Not Graded");
        }else {
            grade.setVisibility(View.VISIBLE);
            caption.setText(String.format("%.2f", GradableController.calculatePercentageGrade(gradable)) + "% score");
        }

        if(gradable.getDate() != null && gradable.getDate().getYear() != -1) {
            due.setText(gradable.getDate().toStringFancy());
        }else{
            due.setText("No date");
        }

        if(gradable.getWeight() >= 0) {
            weight.setText(String.format("%.2f", gradable.getWeight()) + "%");
        }else{
            weight.setText("No weight");
        }

        if(gradable.getMark() >= 0) {
            mark.setText(String.format("%.2f", gradable.getMark()));
        }else{
            mark.setText("No mark");
        }

        if(gradable.getTotal() >= 0) {
            total.setText(String.format("%.2f", gradable.getTotal()));
        }else{
            total.setText("No total");
        }

        if(!gradable.getNote().equals("")) {
            notes.setText(gradable.getNote());
        }else{
            notes.setText("No notes");
        }

        if(exam != null){
            findViewById(R.id.roomLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.durationLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.timeLayout).setVisibility(View.VISIBLE);

            if(!exam.getRoom().equals("")) {
                room.setText(exam.getRoom());
            }

            if(exam.getDuration() != -1) {
                duration.setText(String.valueOf(exam.getDuration()) + " minutes");
            }

            if(exam.getTime() != null && exam.getTime().getHour() != -1) {
                time.setText(exam.getTime().toStringFancy());
            }
        }

        Course c = GradableController.getCourseForGradable(gradable);

        if(c != null ){
            course.setText(c.getCode() + " (" + c.getName() + ")");
        }else{
            course.setText("Unassigned");
        }

        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_model_menu_current, menu);

        Gradable gradable;

        if(exam != null){
            gradable = exam;
        }else{
            gradable = assignment;
        }

        if (gradable.getDate() != null && gradable.getDate().getYear() != -1 &&
                !gradable.getDate().daysUntil().contains("ago")) {
            menu.findItem(R.id.current).setEnabled(true);
        }else{
            menu.findItem(R.id.current).setEnabled(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.current:
                if(exam != null) {
                    Snackbar.make(findViewById(R.id.content), "This exam is due to be held soon", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(findViewById(R.id.content), "This assignment is due soon", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            case R.id.edit:
                if(exam != null){
                    Intent intent = new Intent(ViewGradable.this, EditExam.class);
                    intent.putExtra("exam", exam);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }else if(assignment != null){
                    Intent intent = new Intent(ViewGradable.this, EditAssignment.class);
                    intent.putExtra("assignment", assignment);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
                return true;
            case R.id.delete:
                if(exam != null){
                    UserController.removeExamForUser(user, exam, this);
                }else if(assignment != null){
                    UserController.removeAssignmentForUser(user, assignment, this);
                }
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void close(User user) {
        finish();
    }
}
