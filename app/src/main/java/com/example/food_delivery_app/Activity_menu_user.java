package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.Adpater_cat;

public class Activity_menu_user extends AppCompatActivity implements ItemClickListner{
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    Adpater_cat adpaterCat;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Category_Menu");
    ArrayList<user_datamodule> menu_list = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        bottomNavigationView = findViewById(R.id.botnav_user);
        recyclerView = findViewById(R.id.recyclerView_menu_user);

        adpaterCat = new Adpater_cat(getApplicationContext(), menu_list);
        adpaterCat.setClickListner(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(adpaterCat);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menu_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);

                    menu_list.add(data);
                }
                adpaterCat.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.profile){
                    startActivity(new Intent(getApplicationContext(), Profile_user.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.menu) {
                    return true;
                } else if (id == R.id.order) {
                    startActivity(new Intent(getApplicationContext(),Activity_order_user.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                }else if (id == R.id.home){
                    startActivity(new Intent(getApplicationContext(),user_dashboard.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view, int pos) {
        user_datamodule u = menu_list.get(pos);
        Intent i = new Intent(getApplicationContext(),cat_display.class);
        i.putExtra("subcat",u.getSub_cat());
        startActivity(i);
    }
}