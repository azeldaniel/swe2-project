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

import swe2slayers.gpacalculationapplication.MainActivity;
import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.models.Year;
import swe2slayers.gpacalculationapplication.utils.SerializableManager;

public class EditUser extends AppCompatActivity implements Observer {

    private User user;

    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText idEditText;
    private TextInputEditText degreeEditText;
    private TextInputEditText targetGPAEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        user = (User) getIntent().getSerializableExtra("user");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Button signUp = (Button) findViewById(R.id.signUp);

        if(user == null){
            toolbar.setTitle("Create New Account");
            user = new User("", "", "", "", 0);
        } else {
            toolbar.setTitle("Edit Your Account");
            updateUI();
            signUp.setText("Save");
        }

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserController.getInstance().addObserver(this);

        usernameEditText = (TextInputEditText)findViewById(R.id.username);
        emailEditText = (TextInputEditText)findViewById(R.id.email);
        passwordEditText = (TextInputEditText)findViewById(R.id.password);
        nameEditText = (TextInputEditText)findViewById(R.id.name);
        idEditText = (TextInputEditText)findViewById(R.id.id);
        degreeEditText = (TextInputEditText)findViewById(R.id.degree);
        targetGPAEditText = (TextInputEditText)findViewById(R.id.targetGPA);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                if(username.equals("")){
                    usernameEditText.setError("Enter a valid username");
                    return;
                }
                UserController.getInstance().setUserUsername(user, username);

                UserController.getInstance().setUserEmail(user, emailEditText.getText().toString().trim());

                String password = passwordEditText.getText().toString().trim();

                if(password.equals("") || password.length() < 8){
                    passwordEditText.setError("Enter a valid password");
                    return;
                }

                UserController.getInstance().setUserPassHash(user, password);

                UserController.getInstance().setUserFullName(user, nameEditText.getText().toString().trim());

                try{
                    long id = Long.parseLong(idEditText.getText().toString().trim());
                    UserController.getInstance().setUserId(user, id);
                }catch(Exception e){
                    e.printStackTrace();
                    idEditText.setError("Enter a valid student id");
                    return;
                }

                UserController.getInstance().setUserDegree(user, degreeEditText.getText().toString().trim());

                try{
                    double targetGPA = Double.parseDouble(targetGPAEditText.getText().toString().trim());
                    UserController.getInstance().setUserTargetGPA(user, targetGPA);
                }catch(Exception e){
                    e.printStackTrace();
                    targetGPAEditText.setError("Enter a valid target GPA");
                    return;
                }

                SerializableManager.saveSerializable(EditUser.this, user, "user.txt");

                Intent intent = new Intent(EditUser.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);

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

    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals(user)){
            updateUI();
        }
    }

    public void updateUI(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserController.getInstance().deleteObserver(this);
    }
}
