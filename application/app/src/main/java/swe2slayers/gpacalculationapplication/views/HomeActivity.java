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
 * This activity is the main activity that users will be greeted with after authenticating with the system
 */

package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.InfoDialogHelper;
import swe2slayers.gpacalculationapplication.views.fragments.AssignmentFragment;
import swe2slayers.gpacalculationapplication.views.fragments.CourseFragment;
import swe2slayers.gpacalculationapplication.views.fragments.ExamFragment;
import swe2slayers.gpacalculationapplication.views.fragments.OverviewFragment;
import swe2slayers.gpacalculationapplication.views.fragments.SemesterFragment;
import swe2slayers.gpacalculationapplication.views.fragments.YearFragment;

public class HomeActivity extends AppCompatActivity implements YearFragment.OnListFragmentInteractionListener,
        SemesterFragment.OnListFragmentInteractionListener, CourseFragment.OnListFragmentInteractionListener,
        AssignmentFragment.OnListFragmentInteractionListener, ExamFragment.OnListFragmentInteractionListener {

    private ActionBarDrawerToggle toggle;
    private FabSpeedDial fab;
    private FloatingActionButton secFab;

    private User user;

    private TextView navName;
    private TextView navId;

    private DrawerLayout drawerLayout;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = (User) getIntent().getExtras().getSerializable("user");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Overview");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_closed);

        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);

        fab = (FabSpeedDial) findViewById(R.id.speedDial);
        final View obscure = findViewById(R.id.obscure);

        fab.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                Intent intent;

                switch (menuItem.getItemId()){
                    case R.id.nav_years:
                        intent = new Intent(HomeActivity.this, EditYear.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        break;
                    case R.id.nav_semesters:
                        intent = new Intent(HomeActivity.this, EditSemester.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        break;
                    case R.id.nav_courses:
                        intent = new Intent(HomeActivity.this, EditCourse.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        break;
                    case R.id.nav_assignments:
                        intent = new Intent(HomeActivity.this, EditAssignment.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        break;
                    case R.id.nav_exams:
                        intent = new Intent(HomeActivity.this, EditExam.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                }
                return true;
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fab.isMenuOpen() && obscure.getVisibility() == View.GONE){
                    obscure.setVisibility(View.VISIBLE);
                }else if(!fab.isMenuOpen() && obscure.getVisibility() == View.VISIBLE){
                    obscure.setVisibility(View.GONE);
                }
                handler.postDelayed(this, 100);
            }
        }, 100);


        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation);
        View headerLayout = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                fab.closeMenu();
                return true;
            }
        });

        navName = (TextView) headerLayout.findViewById(R.id.navName);
        navId = (TextView) headerLayout.findViewById(R.id.navId);

        FloatingActionButton editUser = (FloatingActionButton) headerLayout.findViewById(R.id.edit_user);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditUser.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        updateUI();

        fragment = OverviewFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        UserController.attachUserListener(user, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot usr: dataSnapshot.getChildren()){
                    user = usr.getValue(User.class);
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Snackbar offlineSnackbar = Snackbar.make(findViewById(R.id.content_frame),
                "No internet. All changes saved locally.", Snackbar.LENGTH_INDEFINITE);
        offlineSnackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        offlineSnackbar.dismiss();
                    }
                });

        FirebaseDatabaseHelper.attachIsOnlineListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean online = dataSnapshot.getValue(Boolean.class);
                if(online){
                    offlineSnackbar.dismiss();
                }else{
                    offlineSnackbar.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                if(fragment instanceof OverviewFragment) {
                    InfoDialogHelper.showInfoDialog(this,
                            "Overview",
                            "This screen shows you your automatically calculated cumulative " +
                                    "and degree GPAs. It also compares them to your target GPAs " +
                                    "(if any)." + "<br><br>" +
                                    "<b>Yearly GPA Histogram</b>" + "<br>" +
                                    "A histogram of your yearly GPAs will be displayed if you have " +
                                    "GPA data for two or more years. A list of clickable links to " +
                                    "each year will be under the graph." + "<br><br>" +
                                    "<b>Semesterly GPA Histogram</b>" + "<br>" +
                                    "A histogram of your semesterly GPAs will be displayed if you have " +
                                    "GPA data for two or more semesters. A list of clickable links to " +
                                    "each semester will be under the graph." + "<br><br>" +
                                    "<b>Adding information</b>" + "<br>" +
                                    "You can add information for years, semesters, courses, " +
                                    "assignments and exams by clicking the green button on the " +
                                    "bottom right.");
                }else if(fragment instanceof YearFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Years",
                            "This screen shows you a list of all of your years. " + "<br><br>" +
                                    "<b>Adding a Year</b>" + "<br>" +
                                    "You can add a year using the green plus button below." + "<br><br>" +
                                    "<b>Viewing a Year</b>" + "<br>" +
                                    "You can also click on any year from the list (if any) to view more information on it.");
                }else if(fragment instanceof SemesterFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Semesters",
                            "This screen shows you a list of all of your semesters. " + "<br><br>" +
                                    "<b>Adding a Semester</b>" + "<br>" +
                                    "You can add a semester using the green plus button below." + "<br><br>" +
                                    "<b>Viewing a Semester</b>" + "<br>" +
                                    "You can also click on any semester from the list (if any) to view more information on it.");
                }else if(fragment instanceof CourseFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Course",
                            "This screen shows you a list of all of your courses. " + "<br><br>" +
                                    "<b>Adding a Course</b>" + "<br>" +
                                    "You can add a course using the green plus button below." + "<br><br>" +
                                    "<b>Viewing a Course</b>" + "<br>" +
                                    "You can also click on any course from the list (if any) to view more information on it.");
                }else if(fragment instanceof AssignmentFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Assignments",
                            "This screen shows you a list of all of your assignments. " + "<br><br>" +
                                    "<b>Adding an Assignment</b>" + "<br>" +
                                    "You can add an assignment using the green plus button below." + "<br><br>" +
                                    "<b>Viewing an Assignment</b>" + "<br>" +
                                    "You can also click on any assignment from the list (if any) to view more information on it.");
                }else if(fragment instanceof ExamFragment){
                    InfoDialogHelper.showInfoDialog(this,
                            "Exams",
                            "This screen shows you a list of all of your exams. " + "<br><br>" +
                                    "<b>Adding an Exam</b>" + "<br>" +
                                    "You can add an exam using the green plus button below." + "<br><br>" +
                                    "<b>Viewing an Exam</b>" + "<br>" +
                                    "You can also click on any exam from the list (if any) to view more information on it.");
                }
                return true;
            case R.id.edit_user:
                Intent intent = new Intent(HomeActivity.this, EditUser.class);
                intent.putExtra("user", user);
                startActivity(intent);
                return true;
            default:
                toggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Function that updates the UI for the user
     */
    public void updateUI(){
        if(user != null) {
            navName.setText(user.getFirstName() + " " + user.getLastName());
            if(user.getStudentId() != -1) {
                navId.setText("#" + String.valueOf(user.getStudentId()));
            }else{
                navId.setText(user.getEmail());
            }
        }
    }

    /**
     * Function that handles the selection of the navigation drawer items
     * @param menuItem The menu item from the navigation drawer
     */
    public void selectDrawerItem(MenuItem menuItem) {

        Class fragmentClass = OverviewFragment.class;
        switch(menuItem.getItemId()) {
            case R.id.nav_overview:
                fragmentClass = OverviewFragment.class;
                getSupportActionBar().setTitle("Overview");
                break;
            case R.id.nav_years:
                fragmentClass = YearFragment.class;
                getSupportActionBar().setTitle("Years");
                break;
            case R.id.nav_semesters:
                fragmentClass = SemesterFragment.class;
                getSupportActionBar().setTitle("Semesters");
                break;
            case R.id.nav_courses:
                fragmentClass = CourseFragment.class;
                getSupportActionBar().setTitle("Courses");
                break;
            case R.id.nav_assignments:
                fragmentClass = AssignmentFragment.class;
                getSupportActionBar().setTitle("Assignments");
                break;
            case R.id.nav_exams:
                fragmentClass = ExamFragment.class;
                getSupportActionBar().setTitle("Exams");
                break;
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SplashActivity.class));
                finish();
                break;
            default:
                fragmentClass = YearFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
    }

    @Override
    public void onListFragmentInteraction(Year year) {
        Intent intent = new Intent(this, ViewYear.class);
        intent.putExtra("year", year);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Semester semester) {
        Intent intent = new Intent(this, ViewSemester.class);
        intent.putExtra("semester", semester);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Course course) {
        Intent intent = new Intent(this, ViewCourse.class);
        intent.putExtra("course", course);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Assignment assignment) {
        Intent intent = new Intent(this, ViewGradable.class);
        intent.putExtra("assignment", assignment);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Exam exam) {
        Intent intent = new Intent(this, ViewGradable.class);
        intent.putExtra("exam", exam);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}