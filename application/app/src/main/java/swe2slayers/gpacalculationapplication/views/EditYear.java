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
 *
 * This activity adds a new year or edits a current one
 */

package swe2slayers.gpacalculationapplication.views;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.InputEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.DateDialogHelper;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Utils;

public class EditYear extends AppCompatActivity implements Closable {

    private User user;
    private Year year;

    private TextInputLayout yearTextInputLayout;
    private TextInputLayout startTextInputLayout;
    private TextInputLayout endTextInputLayout;
    private ScrollView scrollView;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_year);

        user = (User) getIntent().getSerializableExtra("user");
        year = (Year) getIntent().getSerializableExtra("year");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yearTextInputLayout = (TextInputLayout) findViewById(R.id.yearLayout);
        startTextInputLayout = (TextInputLayout) findViewById(R.id.startLayout);
        endTextInputLayout = (TextInputLayout) findViewById(R.id.endLayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        if (year == null) {
            // If new year is being created
            getSupportActionBar().setTitle("Add Year");
            year = new Year("", user.getUserId());
        } else {
            // If an existing year is being edited
            getSupportActionBar().setTitle("Edit Year");
            editMode = true;
            updateUI();
        }

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        updateUI();
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
        yearTextInputLayout.getEditText().setText(year.getTitle());

        if(year.getStart().getYear() != -1) {
            startTextInputLayout.getEditText().setText(year.getStart().toString());
        }else{
            startTextInputLayout.getEditText().setText("");
        }

        if(year.getEnd().getYear() != -1) {
            endTextInputLayout.getEditText().setText(year.getEnd().toString());
        }else{
            endTextInputLayout.getEditText().setText("");
        }

        startTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentStart = null;
                Date currentEnd = null;

                try {
                    if (!startTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                        String[] date = startTextInputLayout.getEditText().getText().toString().trim().split("/");
                        currentStart = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    }

                    if (!endTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                        String[] date = endTextInputLayout.getEditText().getText().toString().trim().split("/");
                        currentEnd = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    }
                }catch (NumberFormatException e){}

                DateDialogHelper.showDateDialog(EditYear.this,
                        (TextInputEditText) startTextInputLayout.getEditText(), currentStart,
                        null, currentEnd);
            }
        });

        endTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentStart = null;
                Date currentEnd = null;

                try {
                    if (!startTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                        String[] date = startTextInputLayout.getEditText().getText().toString().trim().split("/");
                        currentStart = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    }

                    if (!endTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                        String[] date = endTextInputLayout.getEditText().getText().toString().trim().split("/");
                        currentEnd = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    }
                }catch (NumberFormatException e){}

                DateDialogHelper.showDateDialog(EditYear.this,
                        (TextInputEditText) endTextInputLayout.getEditText(), currentEnd,
                        currentStart, null);
            }
        });
    }

    public void save(){
        String title = yearTextInputLayout.getEditText().getText().toString().trim();

        if(title.equals("")){
            yearTextInputLayout.setError("*Required. Please enter the title of the year e.g. 'Year 1'");
            scrollView.smoothScrollTo(0, (
                    (View) yearTextInputLayout.getParent()).getTop() + yearTextInputLayout.getTop()
                    - (int) Utils.convertDpToPixel(16, this));
            return;
        }else{
            yearTextInputLayout.setError(null);
        }
        year.setTitle(title);

        try{
            if(!startTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                String[] date = startTextInputLayout.getEditText().getText().toString().trim().split("/");
                Date start = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                year.setStart(start);
            }
            startTextInputLayout.setError(null);
        }catch (Exception e){
            startTextInputLayout.setError("Please enter when the year starts in the format DD/MM/YYYY");
            scrollView.smoothScrollTo(0,
                    ((View) startTextInputLayout.getParent()).getTop() + startTextInputLayout.getTop()
                            - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try{
            if(!endTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                String[] date = endTextInputLayout.getEditText().getText().toString().trim().split("/");
                Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                year.setEnd(end);
            }
            endTextInputLayout.setError(null);
        }catch (Exception e){
            endTextInputLayout.setError("Please enter when the year ends in the format DD/MM/YYYY");
            scrollView.smoothScrollTo(0,
                    ((View) endTextInputLayout.getParent()).getTop() + endTextInputLayout.getTop()
                            - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        yearTextInputLayout.getEditText().clearFocus();
        startTextInputLayout.getEditText().clearFocus();
        endTextInputLayout.getEditText().clearFocus();

        if(editMode){
            UserController.updateYearForUser(user, year, this);
        }else{
            UserController.addYearForUser(user, year, this);
        }
    }

    @Override
    public void close(User user) {
        finish();
    }
}
