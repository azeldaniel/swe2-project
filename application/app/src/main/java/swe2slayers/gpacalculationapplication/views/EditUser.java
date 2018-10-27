package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.User;

public class EditUser extends AppCompatActivity {

    private FirebaseUser currentUser;
    private User user;

    private boolean editMode = false;

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText idEditText;
    private TextInputEditText degreeEditText;
    private TextInputEditText targetGPAEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button signUp = (Button) findViewById(R.id.signUp);
        emailEditText = (TextInputEditText)findViewById(R.id.email);
        passwordEditText = (TextInputEditText)findViewById(R.id.password);
        firstNameEditText = (TextInputEditText)findViewById(R.id.firstName);
        lastNameEditText = (TextInputEditText) findViewById(R.id.lastName);
        idEditText = (TextInputEditText)findViewById(R.id.id);
        degreeEditText = (TextInputEditText)findViewById(R.id.degree);
        targetGPAEditText = (TextInputEditText)findViewById(R.id.targetGPA);

        if(currentUser == null){
            getSupportActionBar().setTitle("Create New Account");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            user = new User("","","", "");
            user.setGradingSchemaId("default");
        } else {
            editMode = true;

            user = (User) getIntent().getSerializableExtra("user");

            getSupportActionBar().setTitle("Edit Your Account");

            updateUI();

            emailEditText.setEnabled(false);
            findViewById(R.id.pw_layout).setVisibility(View.GONE);
            signUp.setText("Save");
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String degree = degreeEditText.getText().toString().trim();
                long id;
                double targetGPA;

                try {
                    id = Long.parseLong(idEditText.getText().toString().trim());
                } catch (Exception e) {
                    idEditText.setError("Student ID must be a number");
                    return;
                }

                try {
                    targetGPA = Double.parseDouble(targetGPAEditText.getText().toString().trim());
                } catch (Exception e) {
                    targetGPAEditText.setError("Target GPA must be be a number.");
                    return;
                }

                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setDegree(degree);
                user.setStudentId(id);
                user.setTargetGPA(targetGPA);

                if(editMode){
                    UserController.save(user);
                    finish();
                }else {

                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    if (password.equals("") || password.length() < 8) {
                        passwordEditText.setError("Password must be at least 8 characters long.");
                        return;
                    }

                    user.setEmail(email);

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(EditUser.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                UserController.save(user);

                                Intent intent = new Intent(EditUser.this, HomeActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);

                                finish();
                            } else {
                                Toast.makeText(EditUser.this, "An error occurred please try again later.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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

    private void updateUI(){
        emailEditText.setText(user.getEmail());
        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        idEditText.setText(String.valueOf(user.getStudentId()));
        degreeEditText.setText(user.getDegree());
        targetGPAEditText.setText(String.valueOf(user.getTargetGPA()));
    }
}
