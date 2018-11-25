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
 * This function will add a new semester or edit a current one
 */

package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.SemesterController;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.controllers.YearController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.Date;
import swe2slayers.gpacalculationapplication.utils.DateDialogHelper;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class EditSemester extends AppCompatActivity implements Closable {

    private User user;
    private Semester semester;
    private Year year;

    private TextView yearText;
    private TextInputLayout semesterTextInputLayout;
    private TextInputLayout startTextInputLayout;
    private TextInputLayout endTextInputLayout;
    private ScrollView scrollView;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_semester);

        user = (User) getIntent().getSerializableExtra("user");
        semester = (Semester) getIntent().getSerializableExtra("semester");
        year = (Year) getIntent().getSerializableExtra("year");


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yearText = (TextView) findViewById(R.id.years);
        semesterTextInputLayout = (TextInputLayout) findViewById(R.id.titleLayout);
        startTextInputLayout = (TextInputLayout) findViewById(R.id.startLayout);
        endTextInputLayout = (TextInputLayout) findViewById(R.id.endLayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        if(semester == null){
            // If new semester is being created
            getSupportActionBar().setTitle("Add Semester");
            semester = new Semester("", "","");

            // If a year was passed to create the semester for
            if(year != null){
                semester.setYearId(year.getYearId());
            }
        } else {
            // If an existing semester is being edited
            getSupportActionBar().setTitle("Edit Semester");
            editMode = true;
            year = SemesterController.getYearForSemester(semester);
        }

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Year> years = new ArrayList<>();

                final android.support.v7.widget.PopupMenu menu = new android.support.v7.widget.PopupMenu(EditSemester.this, yearText);
                menu.getMenuInflater()
                        .inflate(R.menu.default_menu, menu.getMenu());

                for (DataSnapshot yr: dataSnapshot.getChildren()) {
                    years.add(yr.getValue(Year.class));
                }

                Collections.sort(years, new Comparator<Year>() {
                    @Override
                    public int compare(Year y1, Year y2) {
                        return y1.getTitle().compareTo(y2.getTitle());
                    }
                });

                for(Year yr: years){
                    menu.getMenu().add(R.id.default_group, yr.hashCode(), Menu.NONE, YearController.getYearTitleWithYears(yr));
                }

                menu.getMenu().add(R.id.add_group, 0, Menu.NONE, "None");
                menu.getMenu().add(R.id.add_group, 1, Menu.NONE, "Add Year");

                menu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == 0){
                            year = null;
                            yearText.setText("None");
                            updateUI();
                            semester.setStart(new Date());
                            semester.setEnd(new Date());
                            startTextInputLayout.getEditText().setText("");
                            endTextInputLayout.getEditText().setText("");
                            return true;
                        }else if(menuItem.getItemId() == 1){
                            Intent intent = new Intent(EditSemester.this, EditYear.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            return true;
                        }else {
                            for (Year yr : years) {
                                if (yr.hashCode() == menuItem.getItemId()) {
                                    year = yr;
                                    yearText.setText(YearController.getYearTitleWithYears(yr));
                                    updateUI();
                                    semester.setStart(new Date());
                                    semester.setEnd(new Date());
                                    startTextInputLayout.getEditText().setText("");
                                    endTextInputLayout.getEditText().setText("");
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });

                MenuCompat.setGroupDividerEnabled(menu.getMenu(), true);

                yearText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menu.show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        UserController.attachYearsListenerForUser(user, listener);

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
        if(!semester.getTitle().equals("")) {
            semesterTextInputLayout.getEditText().setText(semester.getTitle());
        }

        if(semester.getStart().getYear() != -1) {
            startTextInputLayout.getEditText().setText(semester.getStart().toString());
        }else{
            startTextInputLayout.getEditText().setText("");
        }

        if(semester.getEnd().getYear() != -1) {
            endTextInputLayout.getEditText().setText(semester.getEnd().toString());
        }else{
            endTextInputLayout.getEditText().setText("");
        }

        if(year != null){
            yearText.setText(YearController.getYearTitleWithYears(year));
        }

        startTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentStart = null;
                Date currentEnd = null;

                if(!endTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                    String[] date = endTextInputLayout.getEditText().getText().toString().trim().split("/");
                    currentEnd = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                }

                if(!startTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                    String[] date = startTextInputLayout.getEditText().getText().toString().trim().split("/");
                    currentStart = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                }

                if(year != null) {
                    if(currentEnd != null){
                        DateDialogHelper.showDateDialog(EditSemester.this, (TextInputEditText) startTextInputLayout.getEditText(), currentStart, year.getStart(), currentEnd);
                    }else{
                        DateDialogHelper.showDateDialog(EditSemester.this, (TextInputEditText) startTextInputLayout.getEditText(), currentStart, year.getStart(), year.getEnd());
                    }
                }else{
                    DateDialogHelper.showDateDialog(EditSemester.this, (TextInputEditText) startTextInputLayout.getEditText(), currentStart, null, currentEnd);
                }
            }
        });

        endTextInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentStart = null;
                Date currentEnd = null;

                if(!startTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                    String[] date = startTextInputLayout.getEditText().getText().toString().trim().split("/");
                    currentStart = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                }

                if(!endTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                    String[] date = endTextInputLayout.getEditText().getText().toString().trim().split("/");
                    currentEnd = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                }

                if (year != null) {
                    DateDialogHelper.showDateDialog(EditSemester.this, (TextInputEditText) endTextInputLayout.getEditText(), currentEnd, currentStart, year.getEnd());
                }else{
                    DateDialogHelper.showDateDialog(EditSemester.this, (TextInputEditText) endTextInputLayout.getEditText(), currentEnd, currentStart, null);
                }
            }
        });
    }

    public void save(){
        String semesterTitle = semesterTextInputLayout.getEditText().getText().toString().trim();

        if(semesterTitle.equals("")){
            semesterTextInputLayout.setError("*Required. Please enter the title of the semester e.g. 'Semester 1'");
            scrollView.smoothScrollTo(0, ((View) semesterTextInputLayout.getParent().getParent()).getTop() + semesterTextInputLayout.getTop() - 40);
            return;
        }else{
            semesterTextInputLayout.setError(null);
        }
        semester.setTitle(semesterTitle);

        try{
            if(!startTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                String[] date = startTextInputLayout.getEditText().getText().toString().trim().split("/");
                Date start = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                semester.setStart(start);
            }
            startTextInputLayout.setError(null);
        }catch (Exception e){
            startTextInputLayout.setError("Please enter when the semester starts in the format DD/MM/YYYY");
            scrollView.smoothScrollTo(0, ((View) startTextInputLayout.getParent().getParent()).getTop() + startTextInputLayout.getTop() - 40);
            return;
        }

        try{
            if(!endTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                String[] date = endTextInputLayout.getEditText().getText().toString().trim().split("/");
                Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                semester.setEnd(end);
            }
            endTextInputLayout.setError(null);
        }catch (Exception e){
            endTextInputLayout.setError("Please enter when the semester ends in the format DD/MM/YYYY");
            scrollView.smoothScrollTo(0, ((View) endTextInputLayout.getParent().getParent()).getTop() + endTextInputLayout.getTop() - 40);
            return;
        }

        if(year == null){
            semester.setYearId("");
        }else {
            semester.setYearId(year.getYearId());
        }

        semester.setUserId(user.getUserId());

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        semesterTextInputLayout.getEditText().clearFocus();
        startTextInputLayout.getEditText().clearFocus();
        endTextInputLayout.getEditText().clearFocus();

        if(editMode){
            UserController.updateSemesterForUser(user, semester, this);
        }else{
            UserController.addSemesterForUser(user, semester, this);
        }
    }

    @Override
    public void close(User user) {
        finish();
    }
}
