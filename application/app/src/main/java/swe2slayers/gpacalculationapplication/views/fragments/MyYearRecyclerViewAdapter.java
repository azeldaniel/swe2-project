package swe2slayers.gpacalculationapplication.views.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.views.fragments.YearFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyYearRecyclerViewAdapter extends RecyclerView.Adapter<MyYearRecyclerViewAdapter.ViewHolder> {

    private final List<Year> years;
    private final OnListFragmentInteractionListener mListener;

    public MyYearRecyclerViewAdapter(List<Year> items, OnListFragmentInteractionListener listener) {
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
        holder.titleView.setText(YearController.getInstance().getYearTitle(years.get(position)));

        int courses = 0;

        for(Semester s : YearController.getInstance().getYearSemesters(years.get(position))){
            courses += SemesterController.getInstance().getSemesterCourses(s).size();
        }

        holder.contentView.setText(YearController.getInstance().getYearSemesters(years.get(position)).size() + " Semesters, " +
                courses + " Courses");


        holder.metaView.setText(YearController.getInstance().getYearStart(years.get(position)).getYear() + "-" + YearController.getInstance().getYearEnd(years.get(position)).getYear());

        holder.gpaView.setText(String.valueOf(YearController.getInstance().calculateYearGPA(years.get(position))));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(years.get(position));
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
        public final TextView contentView;
        public final TextView metaView;
        public final TextView gpaView;
        public Year year;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleView = (TextView) view.findViewById(R.id.title);
            contentView = (TextView) view.findViewById(R.id.content);
            metaView = (TextView) view.findViewById(R.id.meta);
            gpaView = (TextView) view.findViewById(R.id.gpa);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }
    }
}
