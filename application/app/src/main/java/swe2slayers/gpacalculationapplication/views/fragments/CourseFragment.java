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
 */

package swe2slayers.gpacalculationapplication.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.CourseController;
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Sorter;
import swe2slayers.gpacalculationapplication.views.adapters.CourseRecyclerViewAdapter;
import swe2slayers.gpacalculationapplication.views.adapters.SemesterRecyclerViewAdapter;

public class CourseFragment extends Fragment {

    private CourseFragment.OnListFragmentInteractionListener listener;

    private List<Course> courses;

    private User user;
    private Semester semester;

    private View empty;
    private RecyclerView recyclerView;

    public CourseFragment() {}

    public static CourseFragment newInstance() {
        CourseFragment fragment = new CourseFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        user = ((User)args.getSerializable("user"));
        semester = ((Semester) args.getSerializable("semester"));
        courses = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                courses.clear();

                for (DataSnapshot cour: dataSnapshot.getChildren()) {
                    Course semester = cour.getValue(Course.class);
                    courses.add(semester);
                }

                Sorter.sortCourses(courses);

                if(courses.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                }else{
                    empty.setVisibility(View.INVISIBLE);
                }

                recyclerView.swapAdapter(new CourseRecyclerViewAdapter(courses, listener), true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if(semester == null) {
            UserController.attachCoursesListenerForUser(user, eventListener);
        }else{
            SemesterController.attachCoursesListenerForSemester(semester, eventListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        empty = view.findViewById(R.id.empty);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(!courses.isEmpty()) {
            CourseRecyclerViewAdapter adapter = new CourseRecyclerViewAdapter(courses, listener);
            recyclerView.setAdapter(adapter);
            empty.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CourseFragment.OnListFragmentInteractionListener) {
            listener = (CourseFragment.OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Course course);
    }
}
