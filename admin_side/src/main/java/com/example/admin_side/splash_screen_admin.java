package com.example.admin_side;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_screen_admin extends AppCompatActivity {
    Animation top_anim,bottom_anim;
    ImageView logo_name;
    ImageView logo_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_admin);
        logo_name=findViewById(R.id.logo_name);
        logo_img=findViewById(R.id.logo_img);

        top_anim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
    //Set animation to elements
        logo_img.setAnimation(top_anim);
        logo_name.setAnimation(bottom_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splash_screen_admin.this,admin_register.class);
                startActivity(intent);
                finish();

            }
        },4000);
    }
}