package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile_user extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    LinearLayout editprofile,orderhistory;
    TextView log;
    FirebaseAuth auth;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        bottomNavigationView = findViewById(R.id.botnav_user);
        editprofile = findViewById(R.id.profile);
        orderhistory =findViewById(R.id.order_history);
        log = findViewById(R.id.logout_txt);
        auth = FirebaseAuth.getInstance();
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile_user.this, edit_profile.class);
                startActivity(i);
            }
        });
        orderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile_user.this,Order_History.class);
                startActivity(i);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), user_dashboard.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.profile) {
                    return true;
                } else if (id == R.id.menu) {
                    startActivity(new Intent(getApplicationContext(), Activity_menu_user.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.order) {
                    startActivity(new Intent(getApplicationContext(), Activity_order_user.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}