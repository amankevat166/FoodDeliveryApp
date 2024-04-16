package com.example.admin_side;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class admin_register extends AppCompatActivity {
    Button register;
    TextView login_text;
    EditText name_admin,email_admin,pass_admin,number_admin;
    FirebaseAuth auth;
    admin_datamodule adminDatamodule;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Admin_RegData");

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MIN_Number_LENGTH = 10;

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), New_Order.class);
            startActivity(intent);
            finish();
        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_register);
        auth=FirebaseAuth.getInstance();
        name_admin = findViewById(R.id.name_admin);
        email_admin = findViewById(R.id.email_admin);
        pass_admin = findViewById(R.id.pass_admin);
        number_admin = findViewById(R.id.num_admin);
        register = findViewById(R.id.reg_btn);
        login_text = findViewById(R.id.login_txt);
        adminDatamodule=new admin_datamodule();

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_register.this, admin_login.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,email,password,num;
                name=name_admin.getText().toString();
                email=email_admin.getText().toString();
                password=pass_admin.getText().toString();
                num=number_admin.getText().toString();

                if(validinput(email,password,num,name)){
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                showAlert(admin_register.this,"E-mail Verification","Please Verify Your E-mail from your Mail-box");
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            uploadRegdata();
                                        }
                                        else {
                                            Toast.makeText(admin_register.this, "Your not registered", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                });
                            }

                        }
                    });
                }
            }
        });
    }

    public boolean validinput(String email,String pass,String num,String name){
        if (TextUtils.isEmpty(email)){
            email_admin.setError("E-mail is required!");
            return false;
        }else if (!isValidEmail(email)) {
            email_admin.setError("Invalid email format.");
            email_admin.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(pass)){
            pass_admin.setError("Password is required!");
            return false;
        } else if(!isValidPassword(pass)) {
            //spinKitView_reg.setVisibility(View.GONE);
            pass_admin.setError("Password Should Be minimum length requirement 8 ");
            pass_admin.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(num)){
            number_admin.setError("Number is Required!");
            return false;
        }else if (!isValidmobilenumber(num)) {
            //spinKitView_reg.setVisibility(View.GONE);
            number_admin.setError("Number Should Be minimum length requirement 10");
            number_admin.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(name)){
            name_admin.setError("Name is Required!");
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public  boolean isValidPassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        return true;
    }
    public  boolean isValidmobilenumber(String num){
        if (num.length() <  MIN_Number_LENGTH) {
            return false;
        }
        return true;
    }
    private void uploadRegdata(){
        String name = name_admin.getText().toString();
        String email = email_admin.getText().toString();
        String num = number_admin.getText().toString();

        adminDatamodule.setName(name);
        adminDatamodule.setEmail(email);
        adminDatamodule.setNumber(num);

        databaseReference.push().setValue(adminDatamodule);
    }
    protected void onRestart() {
        super.onRestart();
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (auth.getCurrentUser().isEmailVerified()) {
                            Intent intent = new Intent(getApplicationContext(), admin_info.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }
    }
    public void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the dialog title and message
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false) // Set if the dialog is cancelable

                // Set a positive button and its listener
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Close the dialog
                    }
                });

        // Create and display the dialog
        AlertDialog alert = builder.create();
        alert.show();
    }
}