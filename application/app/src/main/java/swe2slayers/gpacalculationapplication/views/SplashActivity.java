package swe2slayers.gpacalculationapplication.views;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import swe2slayers.gpacalculationapplication.R;
import swe2slayers.gpacalculationapplication.models.User;
import swe2slayers.gpacalculationapplication.utils.SerializableManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(3000);

        final ImageView icon = (ImageView) findViewById(R.id.icon);
        ScaleAnimation grow = new ScaleAnimation(1f, 1.1f, 1f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        grow.setDuration(500);
        grow.setInterpolator(new AccelerateInterpolator());
        grow.setRepeatCount(Animation.INFINITE);
        grow.setRepeatMode(Animation.REVERSE);
        icon.startAnimation(grow);

        CountDownTimer countDownTimer = new CountDownTimer(3000, 50) {

            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(3000-(int)millisUntilFinished);
            }

            @Override
            public void onFinish() {

            }
        }.start();

        final Button signUp = (Button) findViewById(R.id.signUp);
        signUp.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                User user = (User) SerializableManager.readSerializable(SplashActivity.this, "user.txt");
                if(user == null){

                    signUp.setVisibility(View.VISIBLE);
                    signUp.setAlpha(0.0f);
                    signUp.animate().alpha(1.0f).setStartDelay(300);
                    progressBar.animate().alpha(0.0f).scaleY(0);
                    signUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SplashActivity.this, EditUser.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    });
                    final Snackbar s = Snackbar.make(rl, "You must sign up in order to use this app.", Snackbar.LENGTH_INDEFINITE);
                    s.setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            s.dismiss();
                        }
                    });

                    s.show();

                }else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }, 3000);

    }
}
