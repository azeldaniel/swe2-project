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
 * This activity allows a user to view a semester
 */

package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.InfoDialogHelper;
import swe2slayers.gpacalculationapplication.views.adapters.ViewPagerAdapter;
import swe2slayers.gpacalculationapplication.views.fragments.CourseFragment;
import swe2slayers.gpacalculationapplication.views.fragments.SemesterFragment;

public class ViewSemester extends AppCompatActivity
        implements CourseFragment.OnListFragmentInteractionListener, Closable {

    private static User user;

    private static Semester semester;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_semester);

        user = (User) getIntent().getSerializableExtra("user");
        semester = (Semester) getIntent().getSerializableExtra("semester");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        final FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                        add.hide();break;
                    case 1:
                        add.show();break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putSerializable("semester", semester);

        OverviewFragment overviewFragment = OverviewFragment.newInstance();
        overviewFragment.setArguments(bundle);
        adapter.addFrag(overviewFragment, "Overview");

        CourseFragment courseFragment = CourseFragment.newInstance();
        courseFragment.setArguments(bundle);
        adapter.addFrag(courseFragment, "Courses");

        viewPager.setAdapter(adapter);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ViewCompat.setElevation(appBarLayout, 12);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (tabLayout.getSelectedTabPosition()){
                    default:
                    case 1:
                        intent = new Intent(ViewSemester.this, EditCourse.class);
                        break;
                }
                intent.putExtra("user", user);
                intent.putExtra("semester", semester);
                startActivity(intent);
            }
        });

        update();

        SemesterController.attachSemesterListener(semester, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot sem: dataSnapshot.getChildren()){
                    semester = sem.getValue(Semester.class);
                }

                update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SemesterController.attachCoursesListenerForSemester(semester, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                }, 500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void update(){
        getSupportActionBar().setTitle(semester.getTitle());

        TextView gpa = (TextView) findViewById(R.id.gpa);

        gpa.setText(String.format("%.2f", SemesterController.calculateGpaForSemester(semester)));

        viewPager.getAdapter().notifyDataSetChanged();
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
                Intent intent = new Intent(this, EditSemester.class);
                intent.putExtra("semester", semester);
                intent.putExtra("user", user);
                startActivity(intent);
                return true;
            case R.id.delete:
                UserController.removeSemesterForUser(user, semester, this);
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                return true;
            case R.id.help:
                InfoDialogHelper.showInfoDialog(this,
                        semester.getTitle(),
                        "<b>Header</b>" + "<br>" +
                                "The header section shows your GPA for " + semester.getTitle() + "." +
                                "The top right menu also allows you to edit and delete this semester." + "<br><br>" +
                                "<b>Overview</b>" + "<br>" +
                                "The overview section shows the information pertinent to " + semester.getTitle() + ".<br><br>" +
                                "<b>Courses</b>" + "<br>" +
                                "The courses section shows a list of courses for " + semester.getTitle() + ".");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class OverviewFragment extends Fragment {

        public OverviewFragment(){

        }

        public static OverviewFragment newInstance() {
            OverviewFragment fragment = new OverviewFragment();
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_semester_overview, container, false);

            final TextView year = (TextView) view.findViewById(R.id.semester);
            TextView start = (TextView) view.findViewById(R.id.start);
            TextView end = (TextView) view.findViewById(R.id.end);


            Year yr = SemesterController.getYearForSemester(semester);
            if(yr != null){
                year.setText(yr.getTitle());
            }

            if(semester.getStart().getYear() != -1) {
                start.setText(semester.getStart().toStringFancy());
            }

            if(semester.getEnd().getYear() != -1) {
                end.setText(semester.getEnd().toStringFancy());
            }

            return view;
        }
    }

    @Override
    public void onListFragmentInteraction(Course course) {
        Intent intent = new Intent(this, ViewCourse.class);
        intent.putExtra("course", course);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void close(User user) {
        finish();
    }
}
