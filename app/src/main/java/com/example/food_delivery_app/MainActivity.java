package com.example.food_delivery_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {
    ImageView img_color,logo_food;
    LottieAnimationView lottieAnimationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_color=findViewById(R.id.img_color);
        logo_food=findViewById(R.id.logo_food);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,user_onboard.class);
                startActivity(intent);
                finish();

            }
        },5000);

        lottieAnimationView=findViewById(R.id.burger_anim);
        img_color.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1600).setDuration(1000).setStartDelay(4000);
        logo_food.animate().translationY(1400).setDuration(1000).setStartDelay(4000);

    }
}