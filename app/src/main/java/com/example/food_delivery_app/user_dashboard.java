package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class user_dashboard extends AppCompatActivity implements ItemClickListner {
    RecyclerView recyclerView;
    ArrayList<user_datamodule> list;
    TextView loc;
    TextView src;
    BottomNavigationView bottomNavigationView;
    Recycleview_adpater recycleview_adpater;
    CardView cd1,cd2,cd3,cd4,cd5,cd6;
    ImageView cart;
    DatabaseReference databaseReference_add = FirebaseDatabase.getInstance().getReference("user_address");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Menu");
    ArrayList<user_datamodule> menu_list = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);
        
        bottomNavigationView = findViewById(R.id.botnav_user);
        cd1 = findViewById(R.id.imageCard);
        cd2 = findViewById(R.id.videoCard2);
        cd3 = findViewById(R.id.videoCard3);
        cd4 = findViewById(R.id.videoCard4);
        cd5 = findViewById(R.id.videoCard5);
        cd6 = findViewById(R.id.videoCard6);
        cart = findViewById(R.id.cart);
        loc = findViewById(R.id.location);
        src = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recycler_view);

        src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_dashboard.this, search_user.class);
                startActivity(i);
            }
        });

        recycleview_adpater = new Recycleview_adpater(menu_list, getApplicationContext());
        recycleview_adpater.setClickListner(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recycleview_adpater);
        databaseReference.addValueEventListener(new ValueEventListener() {
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

        databaseReference_add.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    loc.setText(data.getUser_city());
                }
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
                    startActivity(new Intent(getApplicationContext(),Activity_menu_user.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (id == R.id.order) {
                    startActivity(new Intent(getApplicationContext(),Activity_order_user.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                }else if (id == R.id.home){
                    return true;
                }
                return false;
            }
        });

        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_dashboard.this, cat_display.class);
                startActivity(i);
            }
        });
        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_dashboard.this, cat_display.class);
                startActivity(i);
            }
        });
        cd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_dashboard.this, cat_display.class);
                startActivity(i);
            }
        });
        cd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_dashboard.this, cat_display.class);
                startActivity(i);
            }
        });
        cd5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_dashboard.this, cat_display.class);
                startActivity(i);
            }
        });
        cd6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_dashboard.this, cat_display.class);
                startActivity(i);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user_dashboard.this, Add_cart.class);
                startActivity(i);
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