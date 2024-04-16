package com.example.food_delivery_app;

import static java.lang.Double.sum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class user_order_details extends AppCompatActivity {
    SeekBar bar;
    String rest_add;
    FirebaseAuth auth;
    DatabaseReference Ref_track = FirebaseDatabase.getInstance().getReference("order_traking");
    DatabaseReference Ref_user_add = FirebaseDatabase.getInstance().getReference("user_address");
    TextView rest_name,time,date,price,qty,deliveryCha,tax,add_rest,from_name,add,price_final,total_price,total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_details);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        bar = findViewById(R.id.progressbar_order_status);
        rest_name =findViewById(R.id.rest_name);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        price = findViewById(R.id.item_price);
        qty = findViewById(R.id.quantity_txt);
        from_name = findViewById(R.id.from_name);
        total_price = findViewById(R.id.Total_amount);
        total=findViewById(R.id.total_amount);
        price_final = findViewById(R.id.final_price);
        add = findViewById(R.id.address);
        add_rest = findViewById(R.id.rest_add);
        deliveryCha = findViewById(R.id.delivery_charge);
        tax = findViewById(R.id.tax_charge);
        Intent i = getIntent();
        String rest = i.getStringExtra("Ref");
        Query query = Ref_track.orderByChild("reference").equalTo(rest);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                   rest_name.setText(data.getRest_name());
                   rest_add = data.getRest_name().toString();
                   time.setText(data.getTime());
                   date.setText(data.getDate());
                   price.setText(data.getItem_price());
                   qty.setText(data.getQty_txt());
                   from_name.setText(data.getRest_name());
                   int pri = Integer.parseInt(data.getItem_price());
                   int qty_to = Integer.parseInt(data.getQty_txt());
                   int tot = pri * qty_to;
                   total.setText(String.valueOf(tot));
                   price_final.setText(String.valueOf(tot));
                   deliveryCha.setText("40");
                   // Get the price from the TextView and convert it to a long integer
                   long amount = Long.parseLong(total.getText().toString());
                   // Define the tax rate (9% in this case)
                   int taxRate = 9;
                   // Calculate the tax amount
                   long taxAmount = (amount * taxRate) / 100;
                   // Display the tax amount in the tax TextView
                   tax.setText(String.valueOf(taxAmount));
                   long final_amount = tot + 40 + taxAmount;
                   total_price.setText(String.valueOf(final_amount));
                   bar.setProgress(Integer.parseInt(data.getOrd_place()));

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query queryadd = Ref_user_add.orderByChild("uid_user").equalTo(uid);
        queryadd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    add.setText(data.getUser_add());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Query queryadmin = Ref_admindata.orderByChild("rest_name").equalTo(rest_add);
        DatabaseReference Ref_admindata = FirebaseDatabase.getInstance()
                .getReference("Admin_Info");//.child(rest_add);
        Ref_admindata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    add_rest.setText(data.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}