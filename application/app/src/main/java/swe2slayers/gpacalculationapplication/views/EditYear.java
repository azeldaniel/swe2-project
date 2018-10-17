package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.SerializableManager;

public class EditYear extends AppCompatActivity {

    private TextInputEditText yearEditText;
    private TextInputEditText startEditText;
    private TextInputEditText endEditText;

    private User user;
    private Year year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_year);

        Intent intent = getIntent();

        user = (User) intent.getSerializableExtra("user");
        year = (Year) intent.getSerializableExtra("year");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(year == null){
            toolbar.setTitle("Add New Year");
            year = new Year("");
        } else {
            toolbar.setTitle("Edit Year");
            updateUI();
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yearEditText = (TextInputEditText) findViewById(R.id.year);
        startEditText = (TextInputEditText) findViewById(R.id.start);
        endEditText = (TextInputEditText) findViewById(R.id.end);

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String yearTitle = yearEditText.getText().toString().trim();

                if(yearTitle.equals("")){
                    yearEditText.setError("Please enter a year title!");
                    return;
                }

                YearController.getInstance().setYearTitle(year, yearTitle);

                try{
                    String[] date = startEditText.getText().toString().trim().split("/");
                    Date start = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    YearController.getInstance().setYearStart(year, start);
                }catch (Exception e){
                    startEditText.setError("Please enter correctly formatted start date!");
                    return;
                }

                try{
                    String[] date = endEditText.getText().toString().trim().split("/");
                    Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    YearController.getInstance().setYearEnd(year, end);
                }catch (Exception e){
                    endEditText.setError("Please enter correctly formatted end date!");
                    return;
                }

                UserController.getInstance().addYear(user, year);

                SerializableManager.saveSerializable(EditYear.this, user, "user.txt");

                finish();
            }
        });


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

    public void updateUI(){
        yearEditText.setText(YearController.getInstance().getYearTitle(year));
        startEditText.setText(YearController.getInstance().getYearStart(year).toString());
        endEditText.setText(YearController.getInstance().getYearEnd(year).toString());
    }
}
