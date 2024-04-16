package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class item_layout extends AppCompatActivity {
    TextView item_name,item_dec,item_price,plus_btn,minus_btn,qty_txt,rest;
    ImageView item_image;
    ImageButton back_btn;
    Button save_btn;
    Uri imageuri;
    FirebaseAuth auth;
    user_datamodule userDatamodule;
    ArrayList<user_datamodule> items = new ArrayList<>();
    DatabaseReference databaseReference_menu = FirebaseDatabase.getInstance().getReference("Menu");
    DatabaseReference databaseReference_cart = FirebaseDatabase.getInstance().getReference("Cart_item");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_layout);
        item_name = findViewById(R.id.product_name);
        item_dec = findViewById(R.id.product_desc);
        item_price = findViewById(R.id.total_price);
        item_image = findViewById(R.id.product_img);
        back_btn = findViewById(R.id.image_back);
        plus_btn=findViewById(R.id.plus_btn);
        minus_btn=findViewById(R.id.minus_btn);
        qty_txt=findViewById(R.id.min_num);
        save_btn =findViewById(R.id.place_order);
        rest = findViewById(R.id.rest_name);
        auth =  FirebaseAuth.getInstance();
        userDatamodule = new user_datamodule();

        Intent i = getIntent();
        String name = i.getStringExtra("item_name");
        item_name.setText(name);
        Query query =  databaseReference_menu.orderByChild("item_name").equalTo(name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    items.add(data);
                    item_price.setText(data.getItem_price());
                    item_dec.setText(data.getDec_item());
                    rest.setText(data.getRest_name());
                    Glide.with(getApplicationContext()).load(data.getImguri()).into(item_image);
                    userDatamodule.setImguri(data.getImguri());
                    imageuri = Uri.parse(data.getImguri());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Glide.with(getApplicationContext()).load(userDatamodule.getImguri()).into(item_image);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Rest_details.class);
                startActivity(i);
                finish();
            }
        });

        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty= Integer.valueOf(qty_txt.getText().toString());
                int temp=qty+1;
                qty_txt.setText(""+temp);
            }
        });

        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(qty_txt.getText().toString());
                if(qty > 1) {
                    int temp = qty - 1;
                    qty_txt.setText("" + temp);
                }
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Add_cart.class);
                startActivity(i);
                finish();
                upload_tocart();
            }
        });
    }
    public void upload_tocart(){

        String name = item_name.getText().toString();
        String price = item_price.getText().toString();
        String dec = item_dec.getText().toString();
        String rest_name = rest.getText().toString();
        String qty = qty_txt.getText().toString();
        String uid = auth.getCurrentUser().getUid();

        userDatamodule.setUid_user(uid);
        userDatamodule.setItem_name(name);
        userDatamodule.setItem_price(price);
        userDatamodule.setDec_item(dec);
        userDatamodule.setRest_name(rest_name);
        userDatamodule.setQty_txt(qty);

        DatabaseReference newReference = databaseReference_cart.push();
        newReference.setValue(userDatamodule);
        String referenceKey = newReference.getKey();
        userDatamodule.setReference(referenceKey);
        newReference.child("Reference").setValue(referenceKey);
        DatabaseReference dataref_cart_main = FirebaseDatabase.getInstance().getReference("Cart_main");
        dataref_cart_main.push().setValue(userDatamodule);

    }
}