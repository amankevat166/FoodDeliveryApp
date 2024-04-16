package com.example.admin_side;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.components.Qualified;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class admin_profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    LinearLayout profile,logout,rev;
    FirebaseAuth auth;
    TextView rest;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin_Info");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        profile = findViewById(R.id.profile);
        rev = findViewById(R.id.go_revenue);
        rest =findViewById(R.id.name_rest);
        logout = findViewById(R.id.log);
        auth= FirebaseAuth.getInstance();
        String admin_uid = auth.getCurrentUser().getUid();
        Query query = ref.orderByChild("uid").equalTo(admin_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                    rest.setText(data.getRest_name());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_profile.this,admin_revenue.class);
                i.putExtra("rest_name",rest.getText().toString());
                startActivity(i);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.ord){
                    startActivity(new Intent(getApplicationContext(), New_Order.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.pre) {
                    startActivity(new Intent(getApplicationContext(),Order_Preparing.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.rev) {
                    startActivity(new Intent(getApplicationContext(), Menu.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                }else if (id == R.id.setting){
                    return true;
                }
                return false;
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_profile.this, amdin_edit_profile.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i = new Intent(admin_profile.this,admin_login.class);
                startActivity(i);
                finish();
            }
        });
    }
}