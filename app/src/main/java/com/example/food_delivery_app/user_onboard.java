package com.example.food_delivery_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class user_onboard extends AppCompatActivity {
    private usersider_Adpater usersiderAdpter;
    Button onbo_login, onbo_register;
    LinearLayout mdotlayout;
    ViewPager viewPager;
    FirebaseAuth mAuth;

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), user_dashboard.class);
            startActivity(intent);
            finish();
        }
    }
    public void setindicator(int position) {
        TextView[] dots = new TextView[3];
        mdotlayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.white, getApplicationContext().getTheme()));
            mdotlayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.light, getApplicationContext().getTheme()));
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_onboard);
        onbo_login = findViewById(R.id.onbo_login);
        onbo_register = findViewById(R.id.onbo_register);
        mdotlayout = findViewById(R.id.linearLayout);
        viewPager = findViewById(R.id.view_pager);
        mAuth = FirebaseAuth.getInstance();

        usersiderAdpter = new usersider_Adpater(this);
        viewPager.setAdapter(usersiderAdpter);
        setindicator(0);
        viewPager.addOnPageChangeListener(viewListener);

        onbo_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_onboard.this, user_login.class);
                startActivity(i);
            }
        });
        onbo_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_onboard.this, user_register.class);
                startActivity(i);
            }
        });


    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setindicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}