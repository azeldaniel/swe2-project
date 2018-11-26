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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Sorter;
import swe2slayers.gpacalculationapplication.views.adapters.SemesterRecyclerViewAdapter;

public class SemesterFragment extends Fragment {

    private OnListFragmentInteractionListener listener;

    private List<Semester> semesters;

    private User user;
    private Year year;

    private View empty;
    private RecyclerView recyclerView;

    public SemesterFragment() {
    }

    public static SemesterFragment newInstance() {
        SemesterFragment fragment = new SemesterFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        user = ((User)args.getSerializable("user"));
        year = ((Year) args.getSerializable("year"));

        semesters = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                semesters.clear();

                for (DataSnapshot sem: dataSnapshot.getChildren()) {
                    Semester semester = sem.getValue(Semester.class);
                    semesters.add(semester);
                }

                Sorter.sortSemesters(semesters);

                if(semesters.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                }else{
                    empty.setVisibility(View.GONE);
                }

                recyclerView.swapAdapter(new SemesterRecyclerViewAdapter(semesters, listener), true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

        if(year == null) {
            UserController.attachSemestersListenerForUser(user, eventListener);
        }else{
            YearController.attachSemesterListenerForYear(year, eventListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semester_list, container, false);

        empty = view.findViewById(R.id.empty);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(!semesters.isEmpty()) {
            SemesterRecyclerViewAdapter adapter = new SemesterRecyclerViewAdapter(semesters, listener);
            recyclerView.setAdapter(adapter);
            empty.setVisibility(View.GONE);
        }else{
            empty.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SemesterFragment.OnListFragmentInteractionListener) {
            listener = (SemesterFragment.OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Semester semester);
    }
}
