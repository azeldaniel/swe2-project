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

    private User user;

    private boolean editMode = false;

    private FirebaseUser currentUser;

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

        Button signUp = (Button) findViewById(R.id.signUp);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(currentUser == null){
            getSupportActionBar().setTitle("Create New Account");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            user = new User("","","", "");
            user.setGradingSchemaId("default");
        } else {
            editMode = true;

            getSupportActionBar().setTitle("Edit Your Account");

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(currentUser.getUid());

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
            updateUI();
            signUp.setText("Save");
        }

        emailEditText = (TextInputEditText)findViewById(R.id.email);
        passwordEditText = (TextInputEditText)findViewById(R.id.password);
        firstNameEditText = (TextInputEditText)findViewById(R.id.firstName);
        lastNameEditText = (TextInputEditText) findViewById(R.id.lastName);
        idEditText = (TextInputEditText)findViewById(R.id.id);
        degreeEditText = (TextInputEditText)findViewById(R.id.degree);
        targetGPAEditText = (TextInputEditText)findViewById(R.id.targetGPA);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstNameEditText.getText().toString().trim();
                user.setFirstName(firstName);

                String lastName = lastNameEditText.getText().toString().trim();
                user.setLastName(lastName);

                String email = emailEditText.getText().toString().trim();
                user.setEmail(email);

                String password = passwordEditText.getText().toString().trim();

                if (password.equals("") || password.length() < 8) {
                    passwordEditText.setError("Enter a valid password");
                    return;
                }

                try {
                    long id = Long.parseLong(idEditText.getText().toString().trim());
                    user.setStudentId(id);
                } catch (Exception e) {
                    e.printStackTrace();
                    idEditText.setError("Enter a valid student studentId");
                    return;
                }

                user.setDegree(degreeEditText.getText().toString().trim());

                try {
                    double targetGPA = Double.parseDouble(targetGPAEditText.getText().toString().trim());
                    user.setTargetGPA(targetGPA);
                } catch (Exception e) {
                    e.printStackTrace();
                    targetGPAEditText.setError("Enter a valid target GPA");
                    return;
                }

                if(editMode){

                }else {

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

    }
}
