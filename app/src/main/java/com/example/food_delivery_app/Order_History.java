package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.Adapter_history;

public class Order_History extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<user_datamodule> datalist = new ArrayList<>();
    Adapter_history adapterHistory;
    FirebaseAuth auth;
    DatabaseReference Ref_OrderHistory = FirebaseDatabase.getInstance().getReference("order_history");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView = findViewById(R.id.recycleview_OrderHistory);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        adapterHistory = new Adapter_history(datalist, getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterHistory);
        Query query = Ref_OrderHistory.orderByChild("uid_user").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    datalist.add(data);
                }
                adapterHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}