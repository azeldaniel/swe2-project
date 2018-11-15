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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Semester;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.Date;

public class EditSemester extends AppCompatActivity {

    private User user;
    private Semester semester;

    private Spinner yearSpinner;
    private TextInputEditText semesterEditText;
    private TextInputEditText startEditText;
    private TextInputEditText endEditText;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_semester);

        user = (User) getIntent().getSerializableExtra("user");
        semester = (Semester) getIntent().getSerializableExtra("semester");
        final Year year = (Year) getIntent().getSerializableExtra("year");


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        yearSpinner = (Spinner)findViewById(R.id.years);
        semesterEditText = (TextInputEditText) findViewById(R.id.year);
        startEditText = (TextInputEditText) findViewById(R.id.start);
        endEditText = (TextInputEditText) findViewById(R.id.end);

        if(semester == null){
            getSupportActionBar().setTitle("Add New Semester");
            semester = new Semester("", "","");
            if(year != null){
                semester.setYearId(year.getYearId());
            }
        } else {
            getSupportActionBar().setTitle("Edit Semester");
            editMode = true;
            updateUI();
        }

        final List<Year> years = new ArrayList<>();
        startEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(EditSemester.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            startEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    if(editMode) {
                        dialog.updateDate(semester.getStart().getYear(), semester.getStart().getMonth()-1, semester.getStart().getDay());
                    }

                    try {
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                        java.util.Date d = myFormat.parse(years.get(yearSpinner.getSelectedItemPosition()).getStart().toString());
                        dialog.getDatePicker().setMinDate(d.getTime());

                        myFormat = new SimpleDateFormat("dd/MM/yyyy");
                        d = myFormat.parse(years.get(yearSpinner.getSelectedItemPosition()).getEnd().toString());
                        dialog.getDatePicker().setMaxDate(d.getTime());
                    }catch (Exception e){}

                    dialog.show();
                }
            }
        });

        endEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(EditSemester.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            endEditText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    if(editMode) {
                        dialog.updateDate(semester.getEnd().getYear(), semester.getEnd().getMonth()-1, semester.getEnd().getDay());
                    }

                    try {
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                        java.util.Date d = myFormat.parse(startEditText.getText().toString().trim());
                        dialog.getDatePicker().setMinDate(d.getTime());

                        myFormat = new SimpleDateFormat("dd/MM/yyyy");
                        d = myFormat.parse(years.get(yearSpinner.getSelectedItemPosition()).getEnd().toString());
                        dialog.getDatePicker().setMaxDate(d.getTime());
                    }catch (Exception e){}

                    dialog.show();
                }
            }
        });

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                years.clear();

                final List<String> yearTitles = new ArrayList<>();

                yearTitles.add("Select One");

                for (DataSnapshot yr: dataSnapshot.getChildren()) {
                    Year year = yr.getValue(Year.class);
                    years.add(year);
                    if(year.getStart().getYear() == year.getEnd().getYear()){
                        yearTitles.add(year.getTitle() + " (" + year.getStart().getYear() + ")");
                    }else{
                        yearTitles.add(year.getTitle() + " (" + year.getStart().getYear() + " - " + year.getEnd().getYear() + ")");
                    }
                }

                Collections.sort(yearTitles);

                Collections.sort(years, new Comparator<Year>() {
                    @Override
                    public int compare(Year y1, Year y2) {
                        return y1.getTitle().compareTo(y2.getTitle());
                    }
                });

                yearTitles.add("Add Year");

                yearSpinner.setAdapter(new ArrayAdapter<String>(EditSemester.this, android.R.layout.simple_list_item_1, yearTitles));

                yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == yearTitles.size() - 1){
                            Intent intent1 = new Intent(EditSemester.this, EditYear.class);
                            intent1.putExtra("user", user);
                            startActivity(intent1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                if(editMode || year != null) {
                    for (int i = 0; i < years.size(); i++) {
                        if (semester.getYearId().equals(years.get(i).getYearId())) {
                            yearSpinner.setSelection(i + 1);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        UserController.attachYearsListenerForUser(user, listener);

        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String semesterTitle = semesterEditText.getText().toString().trim();

                if(semesterTitle.equals("")){
                    semesterEditText.setError("Please enter a semester title");
                    return;
                }

                semester.setTitle(semesterTitle);

                try{
                    String[] date = startEditText.getText().toString().trim().split("/");
                    Date start = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    semester.setStart(start);
                }catch (Exception e){
                    startEditText.setError("Please enter correctly formatted start date");
                    return;
                }

                try{
                    String[] date = endEditText.getText().toString().trim().split("/");
                    Date end = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    semester.setEnd(end);
                }catch (Exception e){
                    endEditText.setError("Please enter correctly formatted end date");
                    return;
                }

                if(yearSpinner.getSelectedItemPosition()==0){
                    semester.setYearId("");
                }else {
                    semester.setYearId(years.get(yearSpinner.getSelectedItemPosition() - 1).getYearId());
                }

                semester.setUserId(user.getUserId());

                if(editMode){
                    UserController.updateSemesterForUser(user, semester);
                }else{
                    UserController.addSemesterForUser(user, semester);
                }

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
        semesterEditText.setText(semester.getTitle());
        startEditText.setText(semester.getStart().toString());
        endEditText.setText(semester.getEnd().toString());
    }
}
