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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.views.ViewSemester;
import swe2slayers.gpacalculationapplication.views.ViewYear;

public class OverviewFragment extends Fragment {

    private User user;

    private View view;
    private ViewGroup container;

    private TextView degree;
    private TextView cumulative;

    private TextView cumulativeTargetText;
    private TextView degreeTargetText;

    private ImageView cumulativeTargetIcon;
    private ImageView degreeTargetIcon;

    private GraphView cumulativeGraph;
    private GraphView semesterGraph;

    public OverviewFragment() {}

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        user = ((User)args.getSerializable("user"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_overview, container, false);

        this.container = container;

        degree = (TextView) view.findViewById(R.id.degree);
        cumulative = (TextView) view.findViewById(R.id.cumulative);

        degreeTargetText = (TextView) view.findViewById(R.id.degreeTargetSummary);
        cumulativeTargetText = (TextView) view.findViewById(R.id.cumulativeTargetSummary);

        degreeTargetIcon = (ImageView) view.findViewById(R.id.degreeTargetIcon);
        cumulativeTargetIcon = (ImageView) view.findViewById(R.id.cumulativeTargetIcon);

        cumulativeGraph = (GraphView) view.findViewById(R.id.cumulative_graph);
        semesterGraph = (GraphView) view.findViewById(R.id.semester_cumulative_graph);

        ValueEventListener listener =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(view != null && isAdded()) {
                    update();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        UserController.attachUserListener(user, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot usr: dataSnapshot.getChildren()) {
                    user = usr.getValue(User.class);
                    if (view != null && isAdded()) {
                        update();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        UserController.attachYearsListenerForUser(user, listener);
        UserController.attachSemestersListenerForUser(user, listener);
        UserController.attachCoursesListenerForUser(user, listener);
        UserController.attachExamsListenerForUser(user, listener);
        UserController.attachAssignmentsListenerForUser(user, listener);

        update();

        return view;
    }

    public void update(){
        double degreeGPA = UserController.calculateDegreeGPA(user);
        double cumulativeGPA = UserController.calculateCumulativeGPA(user);

        if(user.getTargetDegreeGPA() != -1) {
            degreeTargetText.setVisibility(View.VISIBLE);
            degreeTargetIcon.setVisibility(View.VISIBLE);
            if (degreeGPA >= user.getTargetDegreeGPA()) {
                degreeTargetIcon.setImageResource(R.drawable.ic_menu_up);
                degreeTargetText.setText(String.format("%.2f", (degreeGPA - user.getTargetDegreeGPA())) + " ABOVE TARGET");
            } else {
                degreeTargetIcon.setImageResource(R.drawable.ic_menu_down);
                degreeTargetText.setText(String.format("%.2f", (user.getTargetDegreeGPA() - degreeGPA)) + " BELOW TARGET");
            }
        }else{
            degreeTargetText.setVisibility(View.GONE);
            degreeTargetIcon.setVisibility(View.GONE);
        }

        if(user.getTargetCumulativeGPA() != -1) {
            cumulativeTargetText.setVisibility(View.VISIBLE);
            cumulativeTargetIcon.setVisibility(View.VISIBLE);
            if (cumulativeGPA >= user.getTargetCumulativeGPA()) {
                cumulativeTargetIcon.setImageResource(R.drawable.ic_menu_up);
                cumulativeTargetText.setText(String.format("%.2f", (cumulativeGPA - user.getTargetCumulativeGPA())) + " ABOVE TARGET");
            } else {
                cumulativeTargetIcon.setImageResource(R.drawable.ic_menu_down);
                cumulativeTargetText.setText(String.format("%.2f", (user.getTargetCumulativeGPA() - cumulativeGPA)) + " BELOW TARGET");
            }
        }else{
            cumulativeTargetText.setVisibility(View.GONE);
            cumulativeTargetIcon.setVisibility(View.GONE);
        }

        degree.setText(String.format("%.2f", degreeGPA));
        cumulative.setText(String.format("%.2f", UserController.calculateCumulativeGPA(user)));

        ArrayList<Year> years = UserController.getYearsForUser(user);
        if(years.size() > 1) {
            DataPoint[] dataPoints = new DataPoint[years.size()];
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
            if(ll.getChildCount() > 3) {
                ll.removeViews(3, ll.getChildCount()-3);
            }
            final ArrayList<String> titles = new ArrayList<>();
            for (int i = 0; i < years.size(); i++) {
                final Year y = years.get(i);
                double gpa = YearController.calculateGpaForYear(y);
                dataPoints[i] = new DataPoint(i, gpa);

                View v = getLayoutInflater().inflate(R.layout.fragment_overview_year_list, container, false);
                TextView titleView = (TextView) v.findViewById(R.id.title);
                TextView gpaView = (TextView) v.findViewById(R.id.gpa);

                titleView.setText(y.getTitle());
                gpaView.setText(String.format("%.2f", gpa));

                v.findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ViewYear.class);
                        intent.putExtra("year", y);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                });

                titles.add(y.getTitle());

                ll.addView(v);
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            series.setColor(Color.WHITE);
            series.setDrawDataPoints(true);
            series.setThickness(10);
            series.setDataPointsRadius(5);
            cumulativeGraph.removeAllSeries();
            cumulativeGraph.addSeries(series);
            cumulativeGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
            cumulativeGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
            cumulativeGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
            cumulativeGraph.getViewport().setYAxisBoundsManual(true);
            cumulativeGraph.getViewport().setMinY(0);
            cumulativeGraph.getViewport().setMaxY(4.3);
            cumulativeGraph.getViewport().setXAxisBoundsManual(true);
            cumulativeGraph.getViewport().setMinX(0);
            cumulativeGraph.getViewport().setMaxX(years.size()-1);
        }else{
            view.findViewById(R.id.ll_card).setVisibility(View.GONE);
        }

        ArrayList<Semester> semesters = UserController.getSemestersForUser(user);
        if(semesters.size() > 1) {
            DataPoint[] dataPoints = new DataPoint[semesters.size()];
            LinearLayout semesterLl = (LinearLayout) view.findViewById(R.id.semester_ll);
            if(semesterLl.getChildCount() > 3) {
                semesterLl.removeViews(3, semesterLl.getChildCount()-3);
            }
            final ArrayList<String> titles = new ArrayList<>();
            for (int i = 0; i < semesters.size(); i++) {
                final Semester sem = semesters.get(i);
                double gpa = SemesterController.calculateGpaForSemester(sem);
                String title;
                dataPoints[i] = new DataPoint(i, gpa);

                Year yr = SemesterController.getYearForSemester(sem);

                if(yr != null){
                    title = yr.getTitle() + " " + sem.getTitle();
                }else {
                    title = sem.getTitle();
                }

                View v = getLayoutInflater().inflate(R.layout.fragment_overview_year_list, container, false);
                TextView titleView = (TextView) v.findViewById(R.id.title);
                TextView gpaView = (TextView) v.findViewById(R.id.gpa);

                titleView.setText(title);
                gpaView.setText(String.format("%.2f", gpa));

                v.findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ViewSemester.class);
                        intent.putExtra("semester", sem);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                });

                titles.add(title);

                semesterLl.addView(v);
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            series.setColor(Color.WHITE);
            series.setDrawDataPoints(true);
            series.setThickness(10);
            series.setDataPointsRadius(5);
            semesterGraph.removeAllSeries();
            semesterGraph.addSeries(series);
            semesterGraph.getGridLabelRenderer().setVerticalLabelsVisible(false);
            semesterGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
            semesterGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
            semesterGraph.getViewport().setYAxisBoundsManual(true);
            semesterGraph.getViewport().setMinY(0);
            semesterGraph.getViewport().setMaxY(4.3);
            semesterGraph.getViewport().setXAxisBoundsManual(true);
            semesterGraph.getViewport().setMinX(0);
            semesterGraph.getViewport().setMaxX(semesters.size()-1);
        }else{
            view.findViewById(R.id.semester_ll_card).setVisibility(View.GONE);
        }
    }

}
