package com.example.pentimento;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnMainSignUp, btnMainLogIn;

    LinearLayout appName;
    LinearLayout mainButtons;
    ImageView eye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViews();
        setListeners();
        createAnimation();
    }

    private void findViews() {
        btnMainLogIn = findViewById(R.id.btnMainLogIn);
        btnMainSignUp = findViewById(R.id.btnMainSignUp);
        appName = findViewById(R.id.appName);
        mainButtons = findViewById(R.id.mainButtons);
        eye = findViewById(R.id.eye);
    }

    private void setListeners() {
        btnMainLogIn.setOnClickListener(this);
        btnMainSignUp.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        if (v == btnMainLogIn) {

            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        } else if (v == btnMainSignUp) {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }

    public void createAnimation() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(
                appName, "scaleX", 0f, 1.0f);
        scaleXAnimator.setDuration(2000);
        scaleXAnimator.setInterpolator(new DecelerateInterpolator()); //same speed in animation

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(
                appName, "scaleY", 0f, 1.0f);
        scaleYAnimator.setDuration(2000);
        scaleYAnimator.setInterpolator(new DecelerateInterpolator()); //same speed in animation



        float startY = 0f;  // Starting Y position
        float endY = -300f;  // Ending Y position, in pixels
        int duration = 1000;

        ObjectAnimator moveY = ObjectAnimator.ofFloat(appName, "translationY", startY, endY);
        moveY.setDuration(duration); // Duration in milliseconds (1 second in this case)
        moveY.setInterpolator(new AccelerateDecelerateInterpolator()); // Optional: Change interpolator as needed


        scaleYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                moveY.start();
            }
        });

        ObjectAnimator buttonsFadeIn = ObjectAnimator.ofFloat(mainButtons, "alpha", 0.0f, 1.0f);
        buttonsFadeIn.setDuration(1000); // Duration of the animation (1 second in this case)

        moveY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                buttonsFadeIn.start();
            }
        });

        ObjectAnimator eyeFlip = ObjectAnimator.ofFloat(eye, "rotationX", 0, 180);
        eyeFlip.setDuration(400); // Duration of the animation (1 second in this case)

        buttonsFadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                eyeFlip.start();
            }
        });

        scaleXAnimator.start();
        scaleYAnimator.start();
    }


}