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
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.CourseController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Sorter;
import swe2slayers.gpacalculationapplication.views.adapters.CourseRecyclerViewAdapter;
import swe2slayers.gpacalculationapplication.views.adapters.GradableRecyclerViewAdapter;

public class AssignmentFragment extends Fragment {

    private AssignmentFragment.OnListFragmentInteractionListener listener;

    private List<Gradable> assignments;

    private User user;
    private Course course;

    private View empty;
    private RecyclerView recyclerView;

    public AssignmentFragment() {}

    public static AssignmentFragment newInstance() {
        AssignmentFragment fragment = new AssignmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        user = ((User)args.getSerializable("user"));
        course = ((Course) args.getSerializable("course"));

        assignments = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assignments.clear();

                for (DataSnapshot ass : dataSnapshot.getChildren()) {
                    Assignment assignment = ass.getValue(Assignment.class);
                    assignments.add(assignment);
                }

                if (assignments.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.INVISIBLE);
                }

                Sorter.sortGradables(assignments);

                recyclerView.swapAdapter(new GradableRecyclerViewAdapter(assignments, listener, null), true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if(course == null) {
            UserController.attachAssignmentsListenerForUser(user, eventListener);
        }else {
            CourseController.attachAssignmentsListenerForCourse(course, eventListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gradable_list, container, false);

        empty = view.findViewById(R.id.empty);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(!assignments.isEmpty()) {
            GradableRecyclerViewAdapter adapter = new GradableRecyclerViewAdapter(assignments, listener, null);
            recyclerView.setAdapter(adapter);
            empty.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AssignmentFragment.OnListFragmentInteractionListener) {
            listener = (AssignmentFragment.OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Assignment assignment);
    }
}

