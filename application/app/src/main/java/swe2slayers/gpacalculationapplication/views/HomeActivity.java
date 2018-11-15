package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
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
    private FloatingActionButton fab;

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


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment instanceof YearFragment) {
                    Intent intent = new Intent(HomeActivity.this, EditYear.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }else if(fragment instanceof SemesterFragment){
                    Intent intent = new Intent(HomeActivity.this, EditSemester.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }else if(fragment instanceof CourseFragment){
                    Intent intent = new Intent(HomeActivity.this, EditCourse.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }else if(fragment instanceof AssignmentFragment){
                    Intent intent = new Intent(HomeActivity.this, EditAssignment.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }else if(fragment instanceof ExamFragment){
                    Intent intent = new Intent(HomeActivity.this, EditExam.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation);
        View headerLayout = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
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
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void updateUI(){
        if(user != null) {
            navName.setText(user.getFirstName() + " " + user.getLastName());
            navId.setText("#"+String.valueOf(user.getStudentId()));
        }
    }

    public void selectDrawerItem(MenuItem menuItem) {
        fab.show();

        Class fragmentClass = OverviewFragment.class;
        switch(menuItem.getItemId()) {
            case R.id.nav_overview:
                fab.hide();
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

    // TODO ADD LONG CLICK INTERAION FOR RECYLERVIEWS that will delete an item
}