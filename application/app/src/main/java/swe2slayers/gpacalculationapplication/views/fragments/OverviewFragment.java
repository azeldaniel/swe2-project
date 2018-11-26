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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
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
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        final TextView degree = (TextView) view.findViewById(R.id.degree);
        TextView cumulative = (TextView) view.findViewById(R.id.cumulative);

        degree.setText(String.format("%.2f", UserController.calculateDegreeGPA(user)));
        cumulative.setText(String.format("%.2f", UserController.calculateCumulativeGPA(user)));


        GraphView graph = (GraphView) view.findViewById(R.id.cumulative_graph);
        ArrayList<Year> years = UserController.getYearsForUser(user);
        if(years.size() > 1) {
            DataPoint[] dataPoints = new DataPoint[years.size()];
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
            final ArrayList<String> titles = new ArrayList<>();
            for (int i = 0; i < years.size(); i++) {
                final Year y = years.get(i);
                double gpa = YearController.calculateGpaForYear(y);
                dataPoints[i] = new DataPoint(i, gpa);

                View v = inflater.inflate(R.layout.fragment_overview_year_list, container, false);
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
            graph.addSeries(series);
            graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
            graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(4.3);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(years.size()-1);
            /*graph.setCursorMode(true);
            graph.getCursorMode().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            graph.getCursorMode().setTextColor(Color.WHITE);
            graph.getCursorMode().setWidth(350);
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        return titles.get((int)value);
                    } else {
                        return super.formatLabel(value, isValueX);
                    }
                }
            });
            /*graph.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });*/
        }else{
            view.findViewById(R.id.ll_card).setVisibility(View.GONE);
        }

        final GraphView semesterGraph = (GraphView) view.findViewById(R.id.semester_cumulative_graph);
        ArrayList<Semester> semesters = UserController.getSemestersForUser(user);
        if(semesters.size() > 1) {
            DataPoint[] dataPoints = new DataPoint[semesters.size()];
            LinearLayout semesterLl = (LinearLayout) view.findViewById(R.id.semester_ll);
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

                View v = inflater.inflate(R.layout.fragment_overview_year_list, container, false);
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
            /*semesterGraph.setCursorMode(true);
            semesterGraph.getCursorMode().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            semesterGraph.getCursorMode().setTextColor(Color.WHITE);
            semesterGraph.getCursorMode().setWidth(350);
            semesterGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        return titles.get((int)value);
                    } else {
                        return super.formatLabel(value, isValueX);
                    }
                }
            });
            /*semesterGraph.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });*/
        }else{
            view.findViewById(R.id.semester_ll_card).setVisibility(View.GONE);
        }

        return view;
    }

}
