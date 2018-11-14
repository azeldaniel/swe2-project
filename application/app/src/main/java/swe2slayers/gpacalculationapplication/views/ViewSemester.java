package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.views.fragments.CourseFragment;
import swe2slayers.gpacalculationapplication.views.fragments.SemesterFragment;

public class ViewSemester extends AppCompatActivity {

    private static User user;

    private Semester semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_semester);

        user = (User) getIntent().getSerializableExtra("user");
        semester = (Semester) getIntent().getSerializableExtra("semester");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(semester.getTitle());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

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
        adapter.addFrag(courseFragment, "Semesters");

        viewPager.setAdapter(adapter);

        TextView gpa = (TextView) findViewById(R.id.gpa);

        gpa.setText(String.format("%.2f", SemesterController.calculateGpaForSemester(semester)));

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //some other code here
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
                startActivity(intent);
            }
        });
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
            case R.id.delete:
                UserController.removeSemesterForUser(user, semester);
                // todo remove references from all gradables
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    public static class OverviewFragment extends Fragment {
        private Semester semester;

        public OverviewFragment(){

        }

        public static OverviewFragment newInstance() {
            OverviewFragment fragment = new OverviewFragment();
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle args = getArguments();
            semester = ((Semester) args.getSerializable("semester"));
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

            start.setText(semester.getStart().toString());
            end.setText(semester.getEnd().toString());


            Button edit = (Button) view.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditSemester.class);
                    intent.putExtra("semester", semester);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
