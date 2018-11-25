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
 * This activity is the main activity that will be launched whenever the app is launched.
 * This activity is responsible for determining whether a user is authenticated with the
 * application.
 *
 * If they are authenticated, then this activity will close and proceed to the Home activity
 *
 * If they are not authenticated, they will be presented with two options for sign up: via Email
 * or via Google. This activity will facilitate the Google sign up process; however, it will delegate
 * the email signup process to the edit user activity.
 */

package swe2slayers.gpacalculationapplication.views;

import android.animation.Animator;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.controllers.UserController;
import swe2slayers.gpacalculationapplication.models.Grade;
import swe2slayers.gpacalculationapplication.models.GradingSchema;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.Closable;
import swe2slayers.gpacalculationapplication.utils.FirebaseDatabaseHelper;

public class SplashActivity extends AppCompatActivity implements Closable {

    private final int RC_SIGN_IN = 10001;
    // The time spent showing the loading animation
    private final int LOADING_LENGTH = 1000;

    // Firebase and Google auth variables
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseUser currentUser;

    // The views
    private RelativeLayout rl;
    private LinearLayout ll;
    private ProgressBar progressBar;
    private ImageView icon;
    private Button signUpButton;
    private Button signInButton;
    private Button googleSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // set up views
        rl = (RelativeLayout) findViewById(R.id.rl);
        ll = (LinearLayout) findViewById(R.id.ll);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        icon = (ImageView) findViewById(R.id.icon);
        signUpButton = (Button) findViewById(R.id.sign_up_email);
        googleSignUp = (Button) findViewById(R.id.sign_in_google);
        signInButton = (Button) findViewById(R.id.sign_in_email);

        icon.animate().translationY(-(40 * getResources().getDisplayMetrics().density)).setStartDelay(300);

        progressBar.setAlpha(0f);
        progressBar.animate().translationY((40 * getResources().getDisplayMetrics().density)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.animate().alpha(1f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ll.animate().translationY((65 * getResources().getDisplayMetrics().density));

        // set up firebase and google authentication
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // start animations and setup buttons
        startAnimations();
        setUpButtons();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Snackbar.make(rl, "Google sign in failed. Try again.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Function that adds animations to the various views
     */
    private void startAnimations(){

        ScaleAnimation grow = new ScaleAnimation(1f, 1.1f, 1f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        grow.setDuration(500);
        grow.setInterpolator(new AccelerateInterpolator());
        grow.setRepeatCount(Animation.INFINITE);
        grow.setRepeatMode(Animation.REVERSE);
        icon.startAnimation(grow);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                promptUserSignInOrStartApp();
            }
        }, LOADING_LENGTH);
    }

    /**
     * Function that stops the animation
     */
    private void stopAnimation(){
        icon.setAnimation(null);
        icon.animate().translationY(-(130 * getResources().getDisplayMetrics().density));

        ll.setVisibility(View.VISIBLE);
        ll.setAlpha(0.0f);
        ll.animate().alpha(1.0f).scaleY(1).scaleX(1).setStartDelay(600);

        progressBar.animate().alpha(0.0f).scaleY(0);
    }

    /**
     * Function that adds on click listeners to the sign up buttons
     */
    private void setUpButtons(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, EditUser.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }

    /**
     * Function that can be called to prompt the user to sign up or start the home activity
     */
    private void promptUserSignInOrStartApp(){
        if(currentUser == null){
            final Snackbar s = Snackbar.make(rl, "You must sign up in order to use this app.", Snackbar.LENGTH_INDEFINITE);
            s.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.dismiss();
                }
            });
            s.show();
            stopAnimation();
        }else {
            ValueEventListener l = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    FirebaseDatabaseHelper.load(user, SplashActivity.this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            FirebaseDatabaseHelper.getFirebaseDatabaseInstance().getReference().child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(l);
        }
    }

    /**
     * Function that can be called to sign into google
     */
    private void googleSignIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Function hat can be called to setup a google account with firebase
     * @param acct Google account to be set up
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = firebaseAuth.getCurrentUser();
                    User mUser = new User(currentUser.getUid(), currentUser.getEmail(), currentUser.getDisplayName(), "");
                    mUser.setGradingSchemaId("default");

                    UserController.save(mUser, null);

                    FirebaseDatabaseHelper.load(mUser, SplashActivity.this);

                } else {
                    Snackbar.make(rl, "Google sign in failed. Try again.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void close(User user) {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}
