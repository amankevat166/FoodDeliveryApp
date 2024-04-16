package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class edit_profile extends AppCompatActivity {
    EditText prof_name,prof_mono,prof_add;
    ImageButton imageButton,imageButton1,imageButton2,imag_back;
    TextView name;
    FirebaseAuth auth;
    Button save;
    DatabaseReference Ref_userinfo = FirebaseDatabase.getInstance().getReference("User_reg_info");
    DatabaseReference Ref_useradd = FirebaseDatabase.getInstance().getReference("user_address");
    private static final int MIN_Number_LENGTH = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prof_name=findViewById(R.id.prof_name);
        prof_mono=findViewById(R.id.prof_mono);
        prof_add=findViewById(R.id.prof_add);
        imageButton=findViewById(R.id.imageButton);
        imageButton1=findViewById(R.id.imageButton1);
        imageButton2=findViewById(R.id.imageButton2);
        imag_back = findViewById(R.id.back_btn);
        name = findViewById(R.id.textView);
        save=findViewById(R.id.save_btn);
        auth = FirebaseAuth.getInstance();

        //Disable edittext
        prof_name.setEnabled(false);
        prof_mono.setEnabled(false);
        prof_add.setEnabled(false);

        //imagebutton coding
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prof_name.setEnabled(true);
                prof_name.requestFocus();
            }
        });

        //imagebutton1 coding
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prof_mono.setEnabled(true);
                prof_mono.requestFocus();
            }
        });

        //imagebutton2 coding
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prof_add.setEnabled(true);
                prof_add.requestFocus();
            }
        });

        imag_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),user_dashboard.class);
                startActivity(i);
                finish();
            }
        });

        //save button coding
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get edittext data in variable
                String name=prof_name.getText().toString();
                String num=prof_mono.getText().toString();
                String add=prof_add.getText().toString();

                //calling method for checking validation
                validinput(name,num,add);
            }
        });


        Query query = Ref_userinfo.orderByChild("uid_user").equalTo(auth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule info = dataSnapshot.getValue(user_datamodule.class);
                    prof_name.setText(info.getName_reg());
                    prof_mono.setText(info.getNum_reg());
                    name.setText(info.getName_reg());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query_add = Ref_useradd.orderByChild("uid_user").equalTo(auth.getCurrentUser().getUid());
        query_add.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user_datamodule datamodule = dataSnapshot.getValue(user_datamodule.class);
                    prof_add.setText(datamodule.getUser_add());
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //method for edittext validation
    public boolean validinput(String name,String num,String add ){
        num = String.valueOf(prof_mono.getText());
        name = String.valueOf(prof_name.getText());
        add=String.valueOf(prof_add.getText());

        if (TextUtils.isEmpty(add)){
            prof_add.requestFocus();
            prof_add.setError("address should not be Empty");
            return false;
        }

        if (TextUtils.isEmpty(num)){
            prof_mono.requestFocus();
            return false;
        }  if (!isValidmobilenumber(num)) {
            prof_mono.setError("Number Should Be minimum length requirement 10");
            prof_mono.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(name)){
            prof_name.requestFocus();
            prof_name.setError("name should not be Empty");
            return false;
        }
        return true;
    }

    //mobile number validation method
    public  boolean isValidmobilenumber(String num){
        if (num.length() <  MIN_Number_LENGTH) {
            return false;
        }
        return true;
    }
}
