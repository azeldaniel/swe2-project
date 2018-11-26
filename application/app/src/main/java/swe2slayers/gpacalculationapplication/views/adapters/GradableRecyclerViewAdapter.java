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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.GradableController;
import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Gradable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.views.fragments.AssignmentFragment;
import swe2slayers.gpacalculationapplication.views.fragments.ExamFragment;

public class GradableRecyclerViewAdapter extends RecyclerView.Adapter<GradableRecyclerViewAdapter.ViewHolder>  {

    private final List<Gradable> gradables;
    private final AssignmentFragment.OnListFragmentInteractionListener aListener;
    private final ExamFragment.OnListFragmentInteractionListener eListener;

    public GradableRecyclerViewAdapter(List<Gradable> items, AssignmentFragment.OnListFragmentInteractionListener aListener,
                                       ExamFragment.OnListFragmentInteractionListener eListener) {
        this.gradables = items;
        this.aListener = aListener;
        this.eListener = eListener;
    }

    @Override
    public GradableRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_gradable, parent, false);
        return new GradableRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GradableRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.gradable = gradables.get(position);

        holder.gradeView.setText(GradableController.calculateLetterGrade(holder.gradable));

        holder.titleView.setText(holder.gradable.getTitle());

        if(!holder.gradable.getCourseId().equals("")) {
            Course c = FirebaseDatabaseHelper.getCourse(holder.gradable.getCourseId());
            if(c != null){
                holder.courseView.setText(c.getCode());
            }else{
                holder.courseView.setText("");
            }
        }else{
            holder.courseView.setText("");
        }

        if(holder.gradable.getDate() != null && holder.gradable.getDate().getYear() == -1){
            holder.dueView.setText("No date");
        }else {
            if (holder.gradable instanceof Exam) {
                if (holder.gradable.getDate().daysUntil().contains("ago")) {
                    holder.current.setVisibility(View.GONE);
                    holder.dueView.setText("Completed on " + holder.gradable.getDate().toStringFancy());
                } else {
                    holder.current.setVisibility(View.VISIBLE);
                    holder.dueView.setText("To be held " + holder.gradable.getDate().daysUntil());
                }
            } else {
                if (holder.gradable.getDate().daysUntil().contains("ago")) {
                    holder.current.setVisibility(View.GONE);
                    holder.dueView.setText("Completed on " + holder.gradable.getDate().toStringFancy());
                } else {
                    holder.current.setVisibility(View.VISIBLE);
                    holder.dueView.setText("Due " + holder.gradable.getDate().daysUntil());
                }
            }
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position=holder.getAdapterPosition();
                if (aListener != null) {
                    aListener.onListFragmentInteraction((Assignment) gradables.get(position));
                } else if (eListener != null) {
                    eListener.onListFragmentInteraction((Exam) gradables.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gradables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView current;
        public final TextView gradeView;
        public final TextView titleView;
        public final TextView courseView;
        public final TextView dueView;
        public Gradable gradable;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            gradeView = (TextView) view.findViewById(R.id.grade);
            titleView = (TextView) view.findViewById(R.id.title);
            courseView = (TextView) view.findViewById(R.id.course);
            dueView = (TextView) view.findViewById(R.id.due);
            current = (ImageView) view.findViewById(R.id.current);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}
