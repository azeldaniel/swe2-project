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

import java.util.Observable;
import java.util.Observer;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.views.fragments.OverviewFragment;
import swe2slayers.gpacalculationapplication.views.fragments.YearFragment;

public class HomeActivity extends AppCompatActivity implements Observer, YearFragment.OnListFragmentInteractionListener {

    private ActionBarDrawerToggle toggle;

    private User user;

    private TextView navName;
    private TextView navId;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("GPA Calculator");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_closed);

        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent yearIntent = new Intent(HomeActivity.this, EditYear.class);
                yearIntent.putExtra("user", user);
                startActivity(yearIntent);
            }
        });

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

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

        updateUI();

        UserController.getInstance().addObserver(HomeActivity.this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, OverviewFragment.newInstance()).commit();
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
    protected void onDestroy() {
        super.onDestroy();
        UserController.getInstance().deleteObserver(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals(user)){
            updateUI();
        }
    }

    public void updateUI(){
        navName.setText(UserController.getInstance().getUserFullName(user));
        navId.setText(String.valueOf(UserController.getInstance().getUserId(user)));
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = OverviewFragment.class;
        switch(menuItem.getItemId()) {
            case R.id.nav_overview:
                //fragmentClass = .class;
                getSupportActionBar().setTitle("GPA Calculator");
                break;
            case R.id.nav_years:
                fragmentClass = YearFragment.class;
                getSupportActionBar().setTitle("Years");
                break;
            case R.id.nav_semesters:
                //fragmentClass = ThirdFragment.class;
                break;
            case R.id.nav_courses:
                //fragmentClass = ThirdFragment.class;
                break;
            case R.id.nav_assignments:
                //fragmentClass = ThirdFragment.class;
                break;
            case R.id.nav_exams:
                //fragmentClass = ThirdFragment.class;
                break;
            case R.id.sign_out:
                // TODO logout user
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

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public void onListFragmentInteraction(Year year) {
        Intent intent = new Intent(this, ViewYear.class);
        intent.putExtra("year", year);
        startActivity(intent);
    }
}