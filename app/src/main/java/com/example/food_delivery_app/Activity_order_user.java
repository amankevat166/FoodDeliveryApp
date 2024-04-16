package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_order_user extends AppCompatActivity implements ItemClickListner {
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    Tracking_Adpater trackingAdpater;
    ArrayList<user_datamodule> tracking_order = new ArrayList<>();
    String ref;
    DatabaseReference Ref_track = FirebaseDatabase.getInstance().getReference("order_traking");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_user);
        bottomNavigationView = findViewById(R.id.botnav_user);
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.all_order);
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
                    startActivity(new Intent(getApplicationContext(),Activity_menu_user.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.order) {
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
        String uid = auth.getCurrentUser().getUid();

        trackingAdpater = new Tracking_Adpater(tracking_order, getApplicationContext());
        trackingAdpater.setClickListner(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(trackingAdpater);

        Query query = Ref_track.orderByChild("uid_user").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tracking_order.clear();
                for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    ref = data.getReference();
                    tracking_order.add(data);
                }
                trackingAdpater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view, int pos) {
        user_datamodule u = tracking_order.get(pos);
        Intent i = new Intent(getApplicationContext(),user_order_details.class);
        i.putExtra("Ref",u.getReference());
        startActivity(i);
    }
}