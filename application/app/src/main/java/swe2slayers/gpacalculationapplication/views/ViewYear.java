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
 * This activity allows a user to view a year
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
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.views.adapters.ViewPagerAdapter;
import swe2slayers.gpacalculationapplication.views.fragments.CourseFragment;
import swe2slayers.gpacalculationapplication.views.fragments.SemesterFragment;

public class ViewYear extends AppCompatActivity
        implements SemesterFragment.OnListFragmentInteractionListener, Closable {

    private static User user;

    private static Year year;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_year);

        user = (User) getIntent().getSerializableExtra("user");
        year = (Year) getIntent().getSerializableExtra("year");

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
        bundle.putSerializable("year", year);

        OverviewFragment overviewFragment = OverviewFragment.newInstance();
        overviewFragment.setArguments(bundle);
        adapter.addFrag(overviewFragment, "Overview");

        SemesterFragment semesterFragment = SemesterFragment.newInstance();
        semesterFragment.setArguments(bundle);
        adapter.addFrag(semesterFragment, "Semesters");

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
                        intent = new Intent(ViewYear.this, EditSemester.class);
                        break;
                }
                intent.putExtra("user", user);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });

        YearController.attachYearListener(year, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot yr: dataSnapshot.getChildren()){
                    year = yr.getValue(Year.class);
                }

                update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update();

        YearController.attachSemesterListenerForYear(year, new ValueEventListener() {
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
        getSupportActionBar().setTitle(year.getTitle());

        TextView gpa = (TextView) findViewById(R.id.gpa);

        gpa.setText(String.format("%.2f", YearController.calculateGpaForYear(year)));

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
                Intent intent = new Intent(this, EditYear.class);
                intent.putExtra("year", year);
                intent.putExtra("user", user);
                startActivity(intent);
                return true;
            case R.id.delete:
                UserController.removeYearForUser(user, year, this);
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
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

            view.findViewById(R.id.ll).setVisibility(View.GONE);

            TextView start = (TextView) view.findViewById(R.id.start);
            TextView end = (TextView) view.findViewById(R.id.end);

            if(year.getStart().getYear() != -1) {
                start.setText(year.getStart().toStringFancy());
            }

            if(year.getEnd().getYear() != -1) {
                end.setText(year.getEnd().toStringFancy());
            }

            return view;
        }
    }

    @Override
    public void onListFragmentInteraction(Semester semester) {
        Intent intent = new Intent(this, ViewSemester.class);
        intent.putExtra("semester", semester);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void close(User user) {
        finish();
    }
}
