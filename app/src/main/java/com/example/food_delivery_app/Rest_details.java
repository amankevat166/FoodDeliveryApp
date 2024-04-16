package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Rest_details extends AppCompatActivity implements ItemClickListner {
    TextView rest_name,user_add;
    RecyclerView recyclerView_rest_di;
    user_datamodule userDatamodule;
    Recycleview_Rest_Adapter restAdapter;
    ArrayList<user_datamodule> items = new ArrayList<>();
    DatabaseReference databaseReference_menu = FirebaseDatabase.getInstance().getReference("Menu");

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_address");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_details);
        userDatamodule = new user_datamodule();
        rest_name = findViewById(R.id.rest_name);
        user_add = findViewById(R.id.user_add);
        recyclerView_rest_di = findViewById(R.id.recycleview_rest_de);

        recyclerView_rest_di.setHasFixedSize(true);
        recyclerView_rest_di.setLayoutManager(new LinearLayoutManager(this));
        restAdapter = new Recycleview_Rest_Adapter(items,getApplicationContext());
        recyclerView_rest_di.setAdapter(restAdapter);
        restAdapter.setClickListner(this);

        Intent i = getIntent();
        String rest = i.getStringExtra("rest");
        rest_name.setText(rest);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    user_add.setText(data.getUser_add());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query = databaseReference_menu.orderByChild("rest_name").equalTo(rest);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    items.add(data);
                }
                restAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view, int pos) {
        user_datamodule u = items.get(pos);
        Intent i = new Intent(getApplicationContext(),item_layout.class);
        i.putExtra("item_name",u.getItem_name());
        startActivity(i);
    }
}