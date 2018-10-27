package swe2slayers.gpacalculationapplication.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;

public class EditYear extends AppCompatActivity {

    private TextInputEditText yearEditText;
    private TextInputEditText startEditText;
    private TextInputEditText endEditText;

    private User user;
    private Year year;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_year);


        user = (User) getIntent().getSerializableExtra("user");
        year = (Year) getIntent().getSerializableExtra("year");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        yearEditText = (TextInputEditText) findViewById(R.id.year);
        startEditText = (TextInputEditText) findViewById(R.id.start);
        endEditText = (TextInputEditText) findViewById(R.id.end);

        if(year == null){
            toolbar.setTitle("Add New Year");
            year = new Year("", user.getUserId());
        } else {
            toolbar.setTitle("Edit Year");
            editMode = true;
            updateUI();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(editMode) {
                        new DatePickerDialog(EditYear.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        }, year.getStart().getYear(), year.getStart().getMonth()-1, year.getStart().getDay()).show();
                    }else{
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(EditYear.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
            }
        });

        endEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(editMode) {
                        new DatePickerDialog(EditYear.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        }, year.getEnd().getYear(), year.getEnd().getMonth()-1, year.getEnd().getDay()).show();
                    }else{
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(EditYear.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
            }
        });

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = yearEditText.getText().toString().trim();

                if(title.equals("")){
                    yearEditText.setError("Please enter a year title");
                    return;
                }

                year.setTitle(title);

                try{
                    String[] date = startEditText.getText().toString().trim().split("/");
                    Date start = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    year.setStart(start);
                }catch (Exception e){
                    startEditText.setError("Please enter correctly formatted start date!");
                    return;
                }

                try{
                    String[] date = endEditText.getText().toString().trim().split("/");
                    Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    year.setEnd(end);
                }catch (Exception e){
                    endEditText.setError("Please enter correctly formatted end date!");
                    return;
                }

                if(editMode){
                    UserController.updateYearForUser(user, year);
                }else{
                    UserController.addYearForUser(user, year);
                }

                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(editMode) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.view_model_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.delete:
                UserController.removeYearForUser(user, year);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUI(){
        yearEditText.setText(year.getTitle());
        startEditText.setText(year.getStart().toString());
        endEditText.setText(year.getEnd().toString());
    }
}
