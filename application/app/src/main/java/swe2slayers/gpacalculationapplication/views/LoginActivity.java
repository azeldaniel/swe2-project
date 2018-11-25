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
 * This activity allows a user login to the application
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;
import swe2slayers.gpacalculationapplication.utils.Utils;

public class LoginActivity extends AppCompatActivity implements Closable {

    private View loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setTitle("");

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final TextInputLayout emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        final TextInputLayout passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        loading = findViewById(R.id.progressBar);
        Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailTextInputLayout.getEditText().getText().toString().trim();
                String password = passwordTextInputLayout.getEditText().getText().toString().trim();

                if(!Utils.isValidEmail(email)){
                    emailTextInputLayout.setError("Please enter a valid email");
                    return;
                }else{
                    emailTextInputLayout.setError(null);
                }

                if(password.length() < 8){
                    passwordTextInputLayout.setError("Password must be at least 8 characters long");
                    return;
                }else{
                    passwordTextInputLayout.setError(null);
                }

                loading.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("users").child(currentUser.getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            FirebaseDatabaseHelper.load(user, LoginActivity.this);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Snackbar.make(findViewById(R.id.rl), "Incorrect email or password. Try again", Snackbar.LENGTH_SHORT).show();
                                    loading.setVisibility(View.GONE);
                                }

                            }
                        });
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

    @Override
    public void close(User user) {
        loading.setVisibility(View.GONE);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        this.finish();
    }
}
