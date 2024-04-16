package com.example.admin_side;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin_Back_Info extends AppCompatActivity {
    Button save_data;
    EditText branch_name,acc_num,acc_type,acc_holder_name,acc_ifsc,acc_aadhar;
    private static final int MIN_Number_LENGTH = 12;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_back_info);
        branch_name = findViewById(R.id.Branchname);
        acc_num=findViewById(R.id.Accnum);
        acc_type=findViewById(R.id.Acctype);
        acc_holder_name=findViewById(R.id.Accname);
        acc_ifsc=findViewById(R.id.ifsc);
        acc_aadhar=findViewById(R.id.aadhar);
        save_data=findViewById(R.id.save_data);

        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String branch_name_string=branch_name.getText().toString();
                String acc_num_string=acc_num.getText().toString();
                String acc_type_string=acc_type.getText().toString();
                String acc_holder_string=acc_holder_name.getText().toString();
                String acc_ifsc_string=acc_ifsc.getText().toString();
                String acc_aadhar_string=acc_aadhar.getText().toString();

                if(validinput(branch_name_string,acc_num_string,acc_type_string,acc_holder_string,acc_ifsc_string,acc_aadhar_string)){
                    startActivity(new Intent(Admin_Back_Info.this, New_Order.class));
                }
                else{
                    Toast.makeText(Admin_Back_Info.this, "Please Fill Data correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validinput(String string_branch_name,String string_acc_num,String string_acctype,String string_holder,String string_ifsc,String string_adhar){

        if (TextUtils.isEmpty(string_branch_name)){
            branch_name.setError("Restaurant name is required!");
            branch_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(string_acc_num)){
            acc_num.setError("Account Number is required!");
            acc_num.requestFocus();
            return false;
        } else if (!isValidaccountnumber(string_acc_num)) {
            acc_num.setError("Account Number be between 12 to 17");
            acc_num.requestFocus();
        }
        if (TextUtils.isEmpty(string_acctype)){
            acc_type.setError("Account type is Required!");
            acc_type.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(string_holder)){
            acc_holder_name.setError("Account holder name is Required!");
            acc_holder_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(string_ifsc)){
            acc_ifsc.setError("IFSC CODE is Required!");
            acc_ifsc.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(string_adhar)){
            acc_aadhar.setError("Adhar Number is Required!");
            acc_aadhar.requestFocus();
            return false;

        }
        else if (!isValidadharnumber(string_adhar)) {
            acc_aadhar.setError("Adhar Number Should Be minimum length requirement 12");
            acc_aadhar.requestFocus();
        }
        return true;
    }

    public  boolean isValidadharnumber(String num){
        if (num.length() <  MIN_Number_LENGTH) {
            return false;
        }
        return true;
    }
    public  boolean isValidaccountnumber(String num){
        if (num.length() >= 12 && num.length() <= 17) {
            return false;
        }
        return true;
    }
}