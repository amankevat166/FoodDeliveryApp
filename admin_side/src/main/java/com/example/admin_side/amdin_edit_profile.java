package com.example.admin_side;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class amdin_edit_profile extends AppCompatActivity {
    FirebaseAuth auth ;
    TextView name_rest;
    EditText name,number,add;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amdin_edit_profile);
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        name_rest = findViewById(R.id.textView);
        name = findViewById(R.id.prof_name);
        number = findViewById(R.id.prof_mono);
        add = findViewById(R.id.prof_add);
        DatabaseReference Ref_userinfo = FirebaseDatabase.getInstance().getReference("Admin_RegData");
        Query query = Ref_userinfo.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    admin_datamodule datamodule = dataSnapshot.getValue(admin_datamodule.class);
                    name.setText(datamodule.getName());
                    number.setText(datamodule.getNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference Ref_useradd = FirebaseDatabase.getInstance().getReference("Admin_Info");
        Query queryadd = Ref_useradd.orderByChild("uid").equalTo(uid);
        queryadd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                        add.setText(data.getAddress());
                        name_rest.setText(data.getRest_name());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}