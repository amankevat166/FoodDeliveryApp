package com.example.food_delivery_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user_location extends AppCompatActivity {
        EditText user_loc,user_add;
        user_datamodule userdatamodule;
        Button loc_save;
        FirebaseAuth auth;
         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_address");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        user_loc = findViewById(R.id.loc_city_user);
        user_add = findViewById(R.id.loc_add_user);
        loc_save = findViewById(R.id.loc_btn);
        userdatamodule = new user_datamodule();
        auth=FirebaseAuth.getInstance();


        loc_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uplaodaddress();
                Intent i = new Intent(getApplicationContext(),user_dashboard.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void uplaodaddress(){
        String loction = user_loc.getText().toString();
        String address = user_add.getText().toString();
        String uid = auth.getUid();

        userdatamodule.setUser_city(loction);
        userdatamodule.setUser_add(address);
        userdatamodule.setUid_user(uid);
        databaseReference.push().setValue(userdatamodule);
    }
}