package com.example.admin_side;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class cat_item_list extends AppCompatActivity {
    DatabaseReference Reference_menu = FirebaseDatabase.getInstance().getReference("Menu");
    ArrayList<admin_datamodule> cat_list = new ArrayList<>();
    Cat_Adaoter cat_adapter;
    TextView cat_name;
    FirebaseAuth auth;
    RecyclerView recy_catlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_item_list);
        recy_catlist =findViewById(R.id.recycleview_catlist);
        cat_name = findViewById(R.id.catname);
        auth = FirebaseAuth.getInstance();

        String uid = auth.getCurrentUser().getUid();
        Intent i = getIntent();
        String name = i.getStringExtra("cat");
        String rest = i.getStringExtra("rest");
        cat_name.setText(name);
        cat_adapter = new Cat_Adaoter(cat_list,getApplicationContext());
        recy_catlist.setHasFixedSize(true);
        recy_catlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recy_catlist.setAdapter(cat_adapter);
        Query filteredQuery = Reference_menu.orderByChild("rest_name").equalTo(rest);
        filteredQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cat_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                    // Apply additional filtering based on "cat_name"
                    if (data != null && data.getCat_name().equals(name)) {
                        cat_list.add(data);
                    }
                }
                cat_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}