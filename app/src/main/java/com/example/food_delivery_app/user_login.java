package com.example.food_delivery_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.car.ui.AlertDialogBuilder;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class user_login extends AppCompatActivity {
    Button login;
    TextView regis_text,text1,forgot_pass;
    EditText email_login,pass_login;
    CheckBox showpasscheakbox;
    private FirebaseAuth auth;
    private static final int MIN_PASSWORD_LENGTH = 8;
    SpinKitView spinKitView;
    public boolean validinput(String email,String pass){
        spinKitView.setVisibility(View.VISIBLE);
        email = String.valueOf(email_login.getText());
        pass = String.valueOf(pass_login.getText());
        if (TextUtils.isEmpty(email)){
            spinKitView.setVisibility(View.GONE);
            email_login.requestFocus();
            return false;
        }
        else if (!isValidEmail(email)) {
            spinKitView.setVisibility(View.GONE);
            email_login.setError("Invalid email format.");
            email_login.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(pass)){
            spinKitView.setVisibility(View.GONE);
            pass_login.requestFocus();
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        spinKitView.setVisibility(View.GONE);
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
            auth = FirebaseAuth.getInstance();
            email_login = findViewById(R.id.login_email_user);
            pass_login = findViewById(R.id.login_pass_user);
            regis_text = findViewById(R.id.register_text);
            login = findViewById(R.id.login_btn);
            text1 = findViewById(R.id.textView1);
            forgot_pass=findViewById(R.id.forgot_pass);
            spinKitView = findViewById(R.id.spin_kit_login);
            showpasscheakbox = findViewById(R.id.checkBox_login);


            forgot_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(user_login.this);
                    View dialogview=getLayoutInflater().inflate(R.layout.forgot_dialogue,null);
                    EditText email_dialogue=dialogview.findViewById(R.id.email_dialogue);

                    builder.setView(dialogview);
                    AlertDialog alertDialog= builder.create();
                    dialogview.findViewById(R.id.reset_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String email_string=email_dialogue.getText().toString();
                            if (TextUtils.isEmpty(email_string) && !Patterns.EMAIL_ADDRESS.matcher(email_string).matches()){
                                Toast.makeText(user_login.this, "Enter Your Registered Email Id", Toast.LENGTH_SHORT).show();
                            }
                            auth.sendPasswordResetEmail(email_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(user_login.this, "Check Your Email-Box", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(user_login.this, "Unable to send !failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    dialogview.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    if (alertDialog.getWindow()!=null){
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();
                }
            });

            //if you don't have an account then click this textview coding
            regis_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(user_login.this, user_register.class);
                    startActivity(i);
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name,email,password;
                    email=email_login.getText().toString();
                    password=pass_login.getText().toString();

                    if(validinput(email,password)){
                            auth.signInWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                            Intent intent = new Intent(getApplicationContext(),user_dashboard.class);
                                            startActivity(intent);
                                            finish();
                                    }
                                    else{
                                        Toast.makeText(user_login.this, "your email or password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                    spinKitView.setVisibility(View.GONE);
                           }
                       });
                    }
                }
            });

        showpasscheakbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    pass_login.setTransformationMethod(null); // Show password
                } else {
                    pass_login.setTransformationMethod(new PasswordTransformationMethod()); // Hide password
                }
            }
        });
    }
}