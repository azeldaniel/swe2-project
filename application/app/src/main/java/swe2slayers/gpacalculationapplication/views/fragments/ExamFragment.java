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
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Sorter;
import swe2slayers.gpacalculationapplication.views.adapters.CourseRecyclerViewAdapter;
import swe2slayers.gpacalculationapplication.views.adapters.GradableRecyclerViewAdapter;

public class ExamFragment extends Fragment {

    private ExamFragment.OnListFragmentInteractionListener listener;

    private List<Gradable> exams;

    private View empty;
    private RecyclerView recyclerView;

    public ExamFragment() {}

    public static ExamFragment newInstance() {
        ExamFragment fragment = new ExamFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        User user = ((User)args.getSerializable("user"));
        Course course = ((Course)args.getSerializable("course"));

        exams = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                exams.clear();

                for (DataSnapshot ass: dataSnapshot.getChildren()) {
                    Exam exam = ass.getValue(Exam.class);
                    exams.add(exam);
                }

                if(exams.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                }else{
                    empty.setVisibility(View.INVISIBLE);
                }

                Sorter.sortGradables(exams);

                recyclerView.swapAdapter(new GradableRecyclerViewAdapter(exams, null, listener), true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if(course == null) {
            UserController.attachExamsListenerForUser(user, eventListener);
        }else{
            CourseController.attachExamsListenerForCourse(course, eventListener);
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
        if(!exams.isEmpty()) {
            GradableRecyclerViewAdapter adapter = new GradableRecyclerViewAdapter(exams, null, listener);
            recyclerView.setAdapter(adapter);
            empty.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExamFragment.OnListFragmentInteractionListener) {
            listener = (ExamFragment.OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Exam exam);
    }
}

