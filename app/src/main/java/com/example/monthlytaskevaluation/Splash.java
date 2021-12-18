package com.example.monthlytaskevaluation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {


    //variable
    Animation topAnim, bottomAnim, bounce;
    LottieAnimationView animation_view;

    TextView txtView, txtView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);


        //animations
        topAnim = AnimationUtils.loadAnimation(Splash.this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(Splash.this, R.anim.bottom_animation);

        bounce = AnimationUtils.loadAnimation(Splash.this, R.anim.bounce);


        //Hooks
        animation_view = findViewById(R.id.animation_view);
        txtView = findViewById(R.id.txtView);
        txtView2 = findViewById(R.id.txtView2);

        animation_view.setAnimation(topAnim);
        txtView.setAnimation(bounce);
        txtView2.setAnimation(bottomAnim);


        //splash
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, Welcome.class));
                finish();

            }
        }, 5000);
    }
}