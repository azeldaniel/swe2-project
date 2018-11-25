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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.views.fragments.SemesterFragment;

public class SemesterRecyclerViewAdapter extends RecyclerView.Adapter<SemesterRecyclerViewAdapter.ViewHolder>  {

    private final List<Semester> semesters;
    private final SemesterFragment.OnListFragmentInteractionListener mListener;

    public SemesterRecyclerViewAdapter(List<Semester> items, SemesterFragment.OnListFragmentInteractionListener listener) {
        semesters = items;
        mListener = listener;
    }

    @Override
    public SemesterRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_year, parent, false);
        return new SemesterRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SemesterRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.semester = semesters.get(position);

        holder.titleView.setText(holder.semester.getTitle());

        ArrayList<Course> courses = SemesterController.getCoursesForSemester(holder.semester);

        if(courses.size() == 1){
            holder.metaView.setText(courses.size()+ " Course");
        }else{
            holder.metaView.setText(courses.size()+ " Courses");
        }

        holder.gpaView.setText(String.format("%.2f", SemesterController.calculateGpaForSemester(holder.semester)));

        try {
            holder.yearView.setText(SemesterController.getYearForSemester(holder.semester).getTitle());
        }catch (NullPointerException e){
            holder.yearView.setText("");
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.semester);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return semesters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleView;
        public final TextView metaView;
        public final TextView yearView;
        public final TextView gpaView;
        public Semester semester;

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
