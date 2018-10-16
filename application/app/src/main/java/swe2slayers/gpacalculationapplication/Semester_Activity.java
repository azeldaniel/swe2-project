package swe2slayers.gpacalculationapplication;

import android.os.Bundle;
import android.app.Activity;

import java.util.Observable;
import java.util.Observer;

public class Semester_Activity extends Activity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO
    }
}
