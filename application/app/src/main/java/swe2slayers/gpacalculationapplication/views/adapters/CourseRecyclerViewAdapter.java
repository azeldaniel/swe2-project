package swe2slayers.gpacalculationapplication.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.CourseController;
import swe2slayers.gpacalculationapplication.models.Assignment;
import swe2slayers.gpacalculationapplication.models.Course;
import swe2slayers.gpacalculationapplication.models.Exam;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.views.fragments.CourseFragment;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder>  {

    private final List<Course> courses;
    private final CourseFragment.OnListFragmentInteractionListener mListener;

    public CourseRecyclerViewAdapter(List<Course> items, CourseFragment.OnListFragmentInteractionListener listener) {
        courses = items;
        mListener = listener;
    }

    @Override
    public CourseRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_course, parent, false);
        return new CourseRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CourseRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.course = courses.get(position);

        holder.codeView.setText(holder.course.getCode());

        holder.nameView.setText(holder.course.getName());

        holder.gradeView.setText(CourseController.calculateLetterAverage(holder.course));

        CourseController.attachAssignmentsListenerForCourse(holder.course, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Assignment> assignments = new ArrayList<>();
                for(DataSnapshot ass: dataSnapshot.getChildren()){
                    assignments.add(ass.getValue(Assignment.class));
                }
                if(assignments.size() == 1){
                    holder.assignmentsView.setText(assignments.size() + " Assignment");
                }else{
                    holder.assignmentsView.setText(assignments.size() + " Assignments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CourseController.attachExamsListenerForCourse(holder.course, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Exam> exams = new ArrayList<>();
                for(DataSnapshot ass: dataSnapshot.getChildren()){
                    exams.add(ass.getValue(Exam.class));
                }
                if(exams.size() == 1){
                    holder.examsView.setText(exams.size() + " Exam");
                }else{
                    holder.examsView.setText(exams.size() + " Exams");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Semester sem = CourseController.getSemesterForCourse(holder.course);

        if(sem != null) {
            holder.semesterView.setText(sem.getTitle());
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(courses.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView gradeView;
        public final TextView codeView;
        public final TextView nameView;
        public final TextView examsView;
        public final TextView assignmentsView;
        public final TextView semesterView;
        public Course course;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            gradeView = (TextView) view.findViewById(R.id.finalGrade);
            codeView = (TextView) view.findViewById(R.id.code);
            nameView = (TextView) view.findViewById(R.id.name);
            examsView = (TextView) view.findViewById(R.id.exams);
            assignmentsView = (TextView) view.findViewById(R.id.assignments);
            semesterView = (TextView) view.findViewById(R.id.semester);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
