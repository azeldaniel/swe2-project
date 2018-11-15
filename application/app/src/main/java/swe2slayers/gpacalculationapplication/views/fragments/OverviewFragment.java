package swe2slayers.gpacalculationapplication.views.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.views.EditYear;
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

        TextView degree = (TextView) view.findViewById(R.id.degree);
        TextView cumulative = (TextView) view.findViewById(R.id.cumulative);

        degree.setText(String.format("%.2f", UserController.calculateDegreeGPA(user)));
        cumulative.setText(String.format("%.2f", UserController.calculateCumulativeGPA(user)));

        GraphView graph = (GraphView) view.findViewById(R.id.cumulative_graph);

        ArrayList<Year> years = UserController.getYearsForUser(user);

        if(years.size() > 1) {

            DataPoint[] dataPoints = new DataPoint[years.size()];

            ArrayList<String> titles = new ArrayList<>();

            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);

            for (int i = 0; i < years.size(); i++) {
                final Year y = years.get(i);
                double gpa = YearController.calculateGpaForYear(y);
                dataPoints[i] = new DataPoint(i, gpa);
                titles.add(y.getTitle());

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

            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(4.3);
        }else{
            view.findViewById(R.id.ll_card).setVisibility(View.GONE);
        }

        return view;
    }

}
