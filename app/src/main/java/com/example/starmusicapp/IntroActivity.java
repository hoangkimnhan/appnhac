package com.example.starmusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.starmusicapp.R;
import com.example.starmusicapp.UploadActivity;

public class IntroActivity extends AppCompatActivity {

    View view;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);if (getIntent().getBooleanExtra("EXIT", false)){
            finish();
        }else {
            view = findViewById(R.id.logomain);
            animation = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.anim_intent_in_main);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                    view.startAnimation(animation);
                }
            }, 2500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }, 2500);
        }
    }
}