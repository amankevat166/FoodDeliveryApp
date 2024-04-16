package com.example.food_delivery_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class happy_dance extends AppCompatActivity {
    LottieAnimationView dance_boy;
    MediaPlayer happy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_dance);
        happy=MediaPlayer.create(getApplicationContext(),R.raw.happy_happy);
        happy.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                happy.pause();
                Intent intent=new Intent(happy_dance.this, user_dashboard.class);
                startActivity(intent);
                finish();

            }
        },3000);
    }
}