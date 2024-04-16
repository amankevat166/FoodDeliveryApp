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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class Menu extends AppCompatActivity implements ItemClickListner {
    ArrayList<admin_datamodule> dataList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    ImageView back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        Query query;
        FloatingActionButton additem = findViewById(R.id.add_item);
        back = findViewById(R.id.imageback);
        RecyclerView recyclerView = findViewById(R.id.recycleview_menu);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this, New_Order.class));
            }
        });

        Menu_Adapter menuAdapter = new Menu_Adapter(dataList,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        menuAdapter.setClickListner(this);
        recyclerView.setAdapter(menuAdapter);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Menu");
        query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                // HashSet to store unique names
                HashSet<String> uniqueNames = new HashSet<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                    // Check if the name has already been encountered
                    if (!uniqueNames.contains(data.getCat_name())) {
                        dataList.add(data);
                        uniqueNames.add(data.getCat_name());
                    }
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, Add_item.class);
                startActivity(i);
            }
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView1);
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
    @Override
    public void onClick(View view, int pos) {
        admin_datamodule u = dataList.get(pos);
        Intent i  = new Intent(Menu.this,cat_item_list.class);
        i.putExtra("cat",u.getCat_name());
        i.putExtra("rest",u.getRest_name());
        startActivity(i);
    }
}