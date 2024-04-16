package com.example.admin_side;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Order_Preparing extends AppCompatActivity {
    RecyclerView recyclerView_pre;
    ArrayList<admin_datamodule> dataList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("pre_data");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preparing);

        bottomNavigationView  = findViewById(R.id.bottomNavigationView);
        recyclerView_pre = findViewById(R.id.recycleview_pre);
        Adapter_order_pre adapterOrderPre = new Adapter_order_pre(getApplicationContext(), dataList);
        recyclerView_pre.setHasFixedSize(true);
        recyclerView_pre.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_pre.setAdapter(adapterOrderPre);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    admin_datamodule datamodule = dataSnapshot.getValue(admin_datamodule.class);
                    dataList.add(datamodule);
                }
                adapterOrderPre.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.ord){
                    startActivity(new Intent(getApplicationContext(),New_Order.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.pre) {
                    return true;
                } else if (id == R.id.rev) {
                    startActivity(new Intent(getApplicationContext(), Menu.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                }else if (id == R.id.setting){
                    startActivity(new Intent(getApplicationContext(), admin_profile.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            }
        });


    }
}