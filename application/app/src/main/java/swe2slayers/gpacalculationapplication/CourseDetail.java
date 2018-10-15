package swe2slayers.gpacalculationapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Observable;
import java.util.Observer;

public class CourseDetail extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

    }

    @Override
    public void update(Observable o, Object arg) {
        //TODO
    }
}
