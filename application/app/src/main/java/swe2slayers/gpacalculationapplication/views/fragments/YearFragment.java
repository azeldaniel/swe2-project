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
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Sorter;
import swe2slayers.gpacalculationapplication.views.adapters.YearRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class YearFragment extends Fragment {

    private OnListFragmentInteractionListener listener;

    private RecyclerView recyclerView;
    private View empty;
    private User user;

    private List<Year> years;

    public YearFragment() {
    }

    public static YearFragment newInstance() {
        YearFragment fragment = new YearFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        user = ((User)args.getSerializable("user"));

        years = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                years.clear();

                for (DataSnapshot yr: dataSnapshot.getChildren()) {
                    Year year = yr.getValue(Year.class);
                    years.add(year);
                }

                Sorter.sortYears(years);

                if(years.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                }else{
                    empty.setVisibility(View.INVISIBLE);
                }

                recyclerView.swapAdapter(new YearRecyclerViewAdapter(years, listener), true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        UserController.attachYearsListenerForUser(user, eventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_year_list, container, false);

        empty = view.findViewById(R.id.empty);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(!years.isEmpty()) {
            YearRecyclerViewAdapter adapter = new YearRecyclerViewAdapter(years, listener);
            recyclerView.setAdapter(adapter);
            empty.setVisibility(View.GONE);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Year year);
    }
}
