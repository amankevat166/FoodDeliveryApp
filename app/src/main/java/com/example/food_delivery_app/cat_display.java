package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cat_display extends AppCompatActivity implements ItemClickListner{
    RecyclerView recyclerView;
    Recycleview_adpater recycleview_adpater;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Menu");
    ArrayList<user_datamodule> menu_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_display);
        recyclerView = findViewById(R.id.recycleview_cat);

        recycleview_adpater = new Recycleview_adpater(menu_list, getApplicationContext());
        recycleview_adpater.setClickListner(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recycleview_adpater);
        Intent i = getIntent();
        String Subcat_name = i.getStringExtra("subcat");
        Query query = databaseReference.orderByChild("sub_cat").equalTo(Subcat_name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menu_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    menu_list.add(data);
                }
                recycleview_adpater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view, int pos) {
        user_datamodule u = menu_list.get(pos);
        Intent i = new Intent(getApplicationContext(),Rest_details.class);
        i.putExtra("rest",u.getRest_name());
        startActivity(i);
    }
}