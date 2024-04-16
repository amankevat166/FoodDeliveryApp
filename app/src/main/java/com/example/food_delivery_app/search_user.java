package com.example.food_delivery_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class search_user extends AppCompatActivity implements ItemClickListner {

    SearchView searchView;
    user_datamodule datamodule;
    Search_Adpater adpater;
    RecyclerView recyclerView;
    ImageView filter;
    String foodtype, amount_less, mid_amount1, mid_amount2, high_amount,low_high,high_low;
    int rang1, rang2;
    ArrayList<user_datamodule> datalist_ser = new ArrayList<>();
    ArrayList<user_datamodule> dataList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        searchView = findViewById(R.id.search);
        filter = findViewById(R.id.filter);
        datamodule = new user_datamodule();
        recyclerView = findViewById(R.id.recycleview_sre);
        adpater = new Search_Adpater(dataList, search_user.this);
        recyclerView.setHasFixedSize(true);
        adpater.setClickListner(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(adpater);

        Intent i = getIntent();
        foodtype = i.getStringExtra("food_type");
        amount_less = i.getStringExtra("amountless");
        mid_amount1 = i.getStringExtra("amountmid1");
        mid_amount2 = i.getStringExtra("amountmid2");
        high_amount = i.getStringExtra("highamount");
        low_high = i.getStringExtra("lowtohigh");
        high_low = i.getStringExtra("hightolow");
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(search_user.this, search_filter.class));
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        recyclerView.setAdapter(adpater);
        DatabaseReference Ref_menu = FirebaseDatabase.getInstance().getReference("Menu");
        Ref_menu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    datalist_ser.add(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (mid_amount1 != null || amount_less != null
                || high_amount != null || foodtype != null
                || low_high != null || high_low != null){
            Filter_rage_price();
        }

    }

    public void searchList(String text) {
        Set<user_datamodule> uniqueDataList = new HashSet<>();
        for (user_datamodule userDatamodule : datalist_ser) {
            if (userDatamodule.getItem_name().toLowerCase().contains(text.toLowerCase())) {
                uniqueDataList.add(userDatamodule);
            } else if (userDatamodule.getCat_name().toLowerCase().contains(text.toLowerCase())) {
                uniqueDataList.add(userDatamodule);
            } else if (userDatamodule.getSub_cat().toLowerCase().contains(text.toLowerCase())) {
                uniqueDataList.add(userDatamodule);
            }
        }
        dataList.clear(); // Clearing dataList before adding unique items
        dataList.addAll(uniqueDataList);
        adpater.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view, int pos) {
        user_datamodule u = dataList.get(pos);
        Intent i = new Intent(getApplicationContext(), item_layout.class);
        i.putExtra("item_name", u.getItem_name());
        startActivity(i);
    }

    private void Filter_rage_price() {
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Menu");
        Query query = menuRef;
        //Query foodquery = menuRef;

        if (foodtype != null) {
            query = query.orderByChild("food_type").equalTo(foodtype);
        } else if (amount_less != null || mid_amount1 != null
                || high_amount != null  ) {
            query = query.orderByChild("item_price");
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //Toast.makeText(search_user.this, "IN-foodtype", Toast.LENGTH_SHORT).show();
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    dataList.add(data);
                }
                adpater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user_datamodule item = snapshot.getValue(user_datamodule.class);
                    Toast.makeText(search_user.this, item.getItem_price(), Toast.LENGTH_SHORT).show();

                        Toast.makeText(search_user.this, "IN-IF", Toast.LENGTH_SHORT).show();
                        int itemPrice = Integer.parseInt(item.getItem_price());

                        if (mid_amount1 != null){
                            if (itemPrice >= 150 && itemPrice <= 250) {
                                dataList.add(item);
                                //Toast.makeText(search_user.this, "Data-minPrice", Toast.LENGTH_SHORT).show();
                            }
                        } else if (amount_less != null) {
                            if (itemPrice <= 100) {
                                dataList.add(item);
                                //Toast.makeText(search_user.this, "Data-LessAmount", Toast.LENGTH_SHORT).show();
                            }
                        } else if (high_amount != null) {
                            if (itemPrice >= 250) {
                                dataList.add(item);
                                //Toast.makeText(search_user.this, "Data-Highamount", Toast.LENGTH_SHORT).show();
                            }
                        }
//                        else if (low_high != null) {
//                                if (itemPrice > 0){
//                                    dataList.add(item);
//                                }
//                            } else if (high_low != null) {
//                                if (itemPrice > 0){
//                                    dataList.add(0,item);
//                                }
//                            }
                        }
                adpater.notifyDataSetChanged();
                // Populate your RecyclerView with the filtered data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}