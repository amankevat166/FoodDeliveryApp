package com.example.admin_side;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

public class New_Order extends AppCompatActivity {

    ArrayList<admin_datamodule> list=new ArrayList<>();
    RecyclerView recyclerView;
    Accept_Order_Adapter accept_order_adapter;
    admin_datamodule adminDatamodule = new admin_datamodule();
    FirebaseAuth auth;
    TextView restname;
    String  rest;
    DatabaseReference Ref_restname = FirebaseDatabase.getInstance().getReference("Admin_Info");
    DatabaseReference Ref_order = FirebaseDatabase.getInstance().getReference("order_place");


    BottomNavigationView bottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        restname = findViewById(R.id.rest);
        bottomNavigationView  = findViewById(R.id.bottomNavigationView);

        recyclerView=findViewById(R.id.recycleview_order);
        accept_order_adapter =new Accept_Order_Adapter(getApplicationContext(),list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(accept_order_adapter);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        Query query = Ref_restname.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot restSnapshot = snapshot.getChildren().iterator().next();
                String restName=restSnapshot.child("rest_name").getValue(String.class);
                restname.setText(restName);

                Query query_rest = Ref_order.orderByChild("rest_name").equalTo(restName);
                query_rest.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                            list.add(data);
                        }
                        accept_order_adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*String name = adminDatamodule.getRest_name();
        Query query_rest = Ref_order.orderByChild("rest_name").equalTo(name);
        Ref_order.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                    list.add(data);
                }
                accept_order_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.ord){
                    return true;
                } else if (id == R.id.pre) {
                    startActivity(new Intent(getApplicationContext(),Order_Preparing.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.rev) {
                    startActivity(new Intent(getApplicationContext(),Menu.class));
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