package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;

public class EditSemester extends AppCompatActivity implements Observer {

    private Year year;
    private Semester semester;

    private TextInputEditText semesterEditText;
    private TextInputEditText startEditText;
    private TextInputEditText endEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_semester);

        Intent intent = getIntent();

        year = (Year) intent.getSerializableExtra("year");
        semester = (Semester) intent.getSerializableExtra("semester");


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(semester == null){
            toolbar.setTitle("Add New Semester");
            semester = new Semester("");
        } else {
            toolbar.setTitle("Edit Semester");
            updateUI();
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        semesterEditText = (TextInputEditText) findViewById(R.id.year);
        startEditText = (TextInputEditText) findViewById(R.id.start);
        endEditText = (TextInputEditText) findViewById(R.id.end);

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String semesterTitle = semesterEditText.getText().toString().trim();

                if(semesterTitle.equals("")){
                    semesterEditText.setError("Please enter a semester title!");
                    return;
                }

                SemesterController.getInstance().setSemesterTitle(semester, semesterTitle);

                try{
                    String[] date = startEditText.getText().toString().trim().split("/");
                    Date start = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    SemesterController.getInstance().setSemesterStart(semester, start);
                }catch (Exception e){
                    startEditText.setError("Please enter correctly formatted start date!");
                    return;
                }

                try{
                    String[] date = endEditText.getText().toString().trim().split("/");
                    Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    SemesterController.getInstance().setSemesterEnd(semester, end);
                }catch (Exception e){
                    endEditText.setError("Please enter correctly formatted end date!");
                    return;
                }

                YearController.getInstance().addSemester(year, semester);

                finish();
            }
        });

        SemesterController.getInstance().addObserver(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SemesterController.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals(semester)) {
            updateUI();
        }
    }

    public void updateUI(){
        semesterEditText.setText(SemesterController.getInstance().getSemesterTitle(semester));
        startEditText.setText(SemesterController.getInstance().getSemesterStart(semester).toString());
        endEditText.setText(SemesterController.getInstance().getSemesterEnd(semester).toString());
    }
}
