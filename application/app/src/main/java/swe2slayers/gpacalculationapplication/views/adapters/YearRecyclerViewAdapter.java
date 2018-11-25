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

package swe2slayers.gpacalculationapplication.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.views.fragments.YearFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class YearRecyclerViewAdapter extends RecyclerView.Adapter<YearRecyclerViewAdapter.ViewHolder> {

    private final List<Year> years;
    private final OnListFragmentInteractionListener mListener;

    public YearRecyclerViewAdapter(List<Year> items, OnListFragmentInteractionListener listener) {
        years = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_year, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.year = years.get(position);
        holder.titleView.setText(holder.year.getTitle());

        YearController.attachSemesterListenerForYear(holder.year, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Semester> semesters = new ArrayList<>();

                for(DataSnapshot sem: dataSnapshot.getChildren()){
                    semesters.add(sem.getValue(Semester.class));
                }

                if(semesters.size() == 1){
                    holder.metaView.setText(semesters.size() + " Semester");
                }else{
                    holder.metaView.setText(semesters.size() + " Semesters");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (holder.year.getStart().getYear() != -1 && holder.year.getEnd().getYear() != -1) {
            if(holder.year.getStart().getYear() == holder.year.getEnd().getYear()){
                holder.yearView.setText(String.valueOf(holder.year.getStart().getYear()));
            }else{
                holder.yearView.setText(holder.year.getStart().getYear() + " - " + holder.year.getEnd().getYear());
            }
        }

        holder.gpaView.setText(String.format("%.2f", YearController.calculateGpaForYear(holder.year)));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.year);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return years.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleView;
        public final TextView metaView;
        public final TextView yearView;
        public final TextView gpaView;
        public Year year;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleView = (TextView) view.findViewById(R.id.title);
            metaView = (TextView) view.findViewById(R.id.meta);
            yearView = (TextView) view.findViewById(R.id.year);
            gpaView = (TextView) view.findViewById(R.id.gpa);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}
