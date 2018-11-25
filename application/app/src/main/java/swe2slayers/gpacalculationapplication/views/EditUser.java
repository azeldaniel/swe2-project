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
 * This activity adds a new user or edits a current one
 */

package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Utils;

public class EditUser extends AppCompatActivity implements Closable {

    private FirebaseUser currentUser;
    private User user;

    private boolean editMode = false;

    private FirebaseAuth firebaseAuth;

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout firstNameTextInputLayout;
    private TextInputLayout lastNameTextInputLayout;
    private TextInputLayout idTextInputLayout;
    private TextInputLayout degreeTextInputLayout;
    private TextInputLayout targetCumulativeGPATextInputLayout;
    private TextInputLayout targetDegreeGPATextInputLayout;

    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button signUp = (Button) findViewById(R.id.signUp);

        emailTextInputLayout = (TextInputLayout)findViewById(R.id.emailLayout);
        passwordTextInputLayout = (TextInputLayout)findViewById(R.id.passwordLayout);
        firstNameTextInputLayout = (TextInputLayout)findViewById(R.id.firstNameLayout);
        lastNameTextInputLayout = (TextInputLayout) findViewById(R.id.lastNameLayout);
        idTextInputLayout = (TextInputLayout)findViewById(R.id.idLayout);
        degreeTextInputLayout = (TextInputLayout)findViewById(R.id.degreeLayout);
        targetCumulativeGPATextInputLayout = (TextInputLayout)findViewById(R.id.targetCumulativeGPALayout);
        targetDegreeGPATextInputLayout = (TextInputLayout)findViewById(R.id.targetDegreeGPALayout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordLayout);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        if(currentUser == null){
            getSupportActionBar().setTitle("Create Account");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            user = new User("","","", "", -1, "", -1,-1);
            user.setGradingSchemaId("default");
        } else {
            editMode = true;

            user = (User) getIntent().getSerializableExtra("user");
            getSupportActionBar().setTitle("Edit Account");

            updateUI();

            emailTextInputLayout.setEnabled(false);

            Button resetPassword = (Button) findViewById(R.id.reset);
            resetPassword.setVisibility(View.VISIBLE);

            resetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    auth.sendPasswordResetEmail(user.getEmail())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(findViewById(R.id.content), "Password reset email send to " + user.getEmail(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            });

            passwordTextInputLayout.setVisibility(View.GONE);

            signUp.setText("Save");
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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

    public void save(){
        String firstName = firstNameTextInputLayout.getEditText().getText().toString().trim();
        String lastName = lastNameTextInputLayout.getEditText().getText().toString().trim();
        String degree = degreeTextInputLayout.getEditText().getText().toString().trim();

        try {
            if(!idTextInputLayout.getEditText().getText().toString().trim().equals("")) {
                long id = Long.parseLong(idTextInputLayout.getEditText().getText().toString().trim());
                user.setStudentId(id);
            }
            idTextInputLayout.setError(null);
        } catch (Exception e) {
            idTextInputLayout.setError("Please enter a valid student ID number");
            scrollView.smoothScrollTo(0,
                    ((View) idTextInputLayout.getParent()).getTop() +
                            idTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try {
            if(!targetCumulativeGPATextInputLayout.getEditText().getText().toString().trim().equals("")) {
                double targetGPA = Double.parseDouble(targetCumulativeGPATextInputLayout.getEditText().getText().toString().trim());
                user.setTargetCumulativeGPA(targetGPA);
            }
            targetCumulativeGPATextInputLayout.setError(null);
        } catch (Exception e) {
            targetCumulativeGPATextInputLayout.setError("Please enter a valid target cumulative GPA number");
            scrollView.smoothScrollTo(0,
                    ((View) targetCumulativeGPATextInputLayout.getParent()).getTop()
                            + targetCumulativeGPATextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        try {
            if(!targetDegreeGPATextInputLayout.getEditText().getText().toString().trim().equals("")) {
                double targetGPA = Double.parseDouble(targetDegreeGPATextInputLayout.getEditText().getText().toString().trim());
                user.setTargetDegreeGPA(targetGPA);
            }
            targetDegreeGPATextInputLayout.setError(null);
        } catch (Exception e) {
            targetDegreeGPATextInputLayout.setError("Please enter a valid target degree GPA number");
            scrollView.smoothScrollTo(0,
                    ((View) targetDegreeGPATextInputLayout.getParent()).getTop() +
                            targetDegreeGPATextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
            return;
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDegree(degree);

        if(editMode){
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

            UserController.save(user, this);
        }else {

            String email = emailTextInputLayout.getEditText().getText().toString().trim();
            String password = passwordTextInputLayout.getEditText().getText().toString().trim();

            if(!Utils.isValidEmail(email)){
                emailTextInputLayout.setError("*Required. Please enter a valid email address");
                scrollView.smoothScrollTo(0,
                        ((View) emailTextInputLayout.getParent()).getTop() +
                                emailTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
                return;
            }else{
                emailTextInputLayout.setError(null);
            }

            if (password.length() < 8) {
                passwordTextInputLayout.setError("*Required. Password a password with at least 8 characters");
                scrollView.smoothScrollTo(0,
                        ((View) passwordTextInputLayout.getParent()).getTop() +
                                passwordTextInputLayout.getTop() - (int) Utils.convertDpToPixel(16, this));
                return;
            }else{
                passwordTextInputLayout.setError(null);
            }

            user.setEmail(email);

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(EditUser.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        UserController.save(user, null);

                        FirebaseDatabaseHelper.load(user, EditUser.this);

                        Intent intent = new Intent(EditUser.this, HomeActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    } else {
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                        Snackbar.make(findViewById(R.id.content), "An error occurred please try again later.",
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            });

            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
    }

    private void updateUI(){
        emailTextInputLayout.getEditText().setText(user.getEmail());
        firstNameTextInputLayout.getEditText().setText(user.getFirstName());
        lastNameTextInputLayout.getEditText().setText(user.getLastName());

        if(user.getStudentId() != -1) {
            idTextInputLayout.getEditText().setText(String.valueOf(user.getStudentId()));
        }

        degreeTextInputLayout.getEditText().setText(user.getDegree());

        if(user.getTargetCumulativeGPA() != -1) {
            targetCumulativeGPATextInputLayout.getEditText().setText(String.format("%.2f", user.getTargetCumulativeGPA()));
        }

        if(user.getTargetDegreeGPA() != -1) {
            targetDegreeGPATextInputLayout.getEditText().setText(String.format("%.2f", user.getTargetDegreeGPA()));
        }
    }

    @Override
    public void close(User user) {
        finish();
    }
}
