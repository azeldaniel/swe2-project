package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
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
import swe2slayers.gpacalculationapplication.views.adapters.ViewPagerAdapter;
import swe2slayers.gpacalculationapplication.views.fragments.AssignmentFragment;
import swe2slayers.gpacalculationapplication.views.fragments.ExamFragment;

public class ViewGradable extends AppCompatActivity {

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
        if(gradable.getMark() == 0 || gradable.getTotal() == 0) {
            grade.setVisibility(View.GONE);
            caption.setText("Not Graded");
        }else {
            caption.setText(String.format("%.2f", GradableController.calculatePercentageGrade(gradable)) + "%");
        }
        if(gradable.getDate() != null) {
            due.setText(gradable.getDate().toString());
        }
        weight.setText(String.valueOf(gradable.getWeight()) + "%");
        mark.setText(String.valueOf(gradable.getMark()));
        total.setText(String.valueOf(gradable.getTotal()));

        if(!gradable.getNote().equals("")) {
            notes.setText(gradable.getNote());
        }

        if(exam != null){
            if(!exam.getRoom().equals("")) {
                room.setText(exam.getRoom());
            }
            room.setVisibility(View.VISIBLE);

            duration.setText(String.valueOf(exam.getDuration()));
            duration.setVisibility(View.VISIBLE);

            if(exam.getTime() != null) {
                time.setText(exam.getTime().toString());
            }
            time.setVisibility(View.VISIBLE);
        }

        Course c = GradableController.getCourseForGradable(gradable);

        if(c != null ){
            course.setText(c.getName());
        }else{
            course.setText("None");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_model_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
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
                    UserController.removeExamForUser(user, exam);
                }else if(assignment != null){
                    UserController.removeAssignmentForUser(user, assignment);
                }
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
