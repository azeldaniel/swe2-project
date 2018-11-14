package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.CourseController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.views.fragments.AssignmentFragment;
import swe2slayers.gpacalculationapplication.views.fragments.ExamFragment;

public class ViewCourse extends AppCompatActivity {

    private static User user;

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);

        user = (User) getIntent().getSerializableExtra("user");
        course = (Course) getIntent().getSerializableExtra("course");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(course.getCode());

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
                    case 2:
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
        bundle.putSerializable("course", course);

        OverviewFragment overviewFragment = OverviewFragment.newInstance();
        overviewFragment.setArguments(bundle);
        adapter.addFrag(overviewFragment, "Overview");

        ExamFragment examFragment = ExamFragment.newInstance();
        examFragment.setArguments(bundle);
        adapter.addFrag(examFragment, "Exams");

        AssignmentFragment assignmentFragment = AssignmentFragment.newInstance();
        assignmentFragment.setArguments(bundle);
        adapter.addFrag(assignmentFragment, "Assignments");

        viewPager.setAdapter(adapter);

        TextView avg = (TextView) findViewById(R.id.avg);
        TextView caption = (TextView) findViewById(R.id.caption);
        TextView description = (TextView) findViewById(R.id.description);

        avg.setText(CourseController.calculateLetterAverage(course));
        caption.setText(String.format("%.2f", CourseController.calculateAverage(course)) + "% Average");

        double minimumGrade = CourseController.calculateMinimumGrade(course);
        if(minimumGrade == -2){
            description.setText("Target grade was not achieved");
        }else if(minimumGrade == -1){
            description.setText("Target grade was achieved");
        }else{
            description.setText(String.format("%.2f", minimumGrade) + "% average needed to achieve target grade");
        }

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
                        intent = new Intent(ViewCourse.this, EditExam.class);
                        break;
                    case 2:
                        intent = new Intent(ViewCourse.this, EditAssignment.class);
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
                UserController.removeCourseForUser(user, course);
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
        private Course course;

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
            course = ((Course) args.getSerializable("course"));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_course_overview, container, false);

            final TextView semester = (TextView) view.findViewById(R.id.semester);
            //TextView code = (TextView) view.findViewById(R.id.code);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView credits = (TextView) view.findViewById(R.id.credits);
            //TextView finalGrade = (TextView) view.findViewById(R.id.finalGrade);
            TextView level = (TextView) view.findViewById(R.id.level);
            TextView targetGrade = (TextView) view.findViewById(R.id.targetGrade);

            Semester sem = CourseController.getSemesterForCourse(course);
            if(sem != null){
                semester.setText(sem.getTitle());
            }

            //code.setText(course.getCode());
            name.setText(course.getName());
            credits.setText(String.valueOf(course.getCredits()));
            //finalGrade.setText(String.valueOf(course.getFinalGrade()));
            level.setText(String.valueOf(course.getLevel()));
            targetGrade.setText(String.valueOf(course.getTargetGrade()));

            Button edit = (Button) view.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditCourse.class);
                    intent.putExtra("course", course);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
