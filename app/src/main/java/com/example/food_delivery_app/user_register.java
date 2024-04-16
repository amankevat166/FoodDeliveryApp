package com.example.food_delivery_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class user_register extends AppCompatActivity {
    Button register;
    TextView login_text;
    EditText name_txt,email_txt,pass_txt,num_txt;
    SpinKitView spinKitView_reg;
    CheckBox showpasscheckbox;
    user_datamodule userDatamodule;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User_reg_info");
    private FirebaseAuth auth;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MIN_Number_LENGTH = 10;

    //make method for checking that user's data is valid
    public boolean validinput(String email,String pass,String num,String name){
        spinKitView_reg.setVisibility(View.VISIBLE);
        email = String.valueOf(email_txt.getText());
        pass = String.valueOf(pass_txt.getText());
        num = String.valueOf(num_txt.getText());
        name = String.valueOf(name_txt.getText());

        if (TextUtils.isEmpty(email)){
            spinKitView_reg.setVisibility(View.GONE);
            email_txt.requestFocus();
            return false;
        }
        else if (!isValidEmail(email)) {
            email_txt.setError("Invalid email format.");
            email_txt.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(pass)){
            spinKitView_reg.setVisibility(View.GONE);
            pass_txt.requestFocus();
            return false;
        } if (!isValidPassword(pass)) {
            spinKitView_reg.setVisibility(View.GONE);
            pass_txt.setError("Password Should Be minimum length requirement 8 ");
            pass_txt.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(num)){
            spinKitView_reg.setVisibility(View.GONE);
            num_txt.requestFocus();
            return false;
        }  else if (!isValidmobilenumber(num)) {
            spinKitView_reg.setVisibility(View.GONE);
            num_txt.setError("Number Should Be minimum length requirement 10");
            num_txt.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(name)){
            spinKitView_reg.setVisibility(View.GONE);
            name_txt.requestFocus();
            return false;
        }
    return true;
    }
    private boolean isValidEmail(String email) {
        spinKitView_reg.setVisibility(View.GONE);
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
    @Override
    protected void onRestart() {
        super.onRestart();
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (auth.getCurrentUser().isEmailVerified()) {
                            Intent intent = new Intent(getApplicationContext(), user_location.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);

        //Finding Id's of Widgets
        auth = FirebaseAuth.getInstance();
        name_txt = findViewById(R.id.name_user);
        email_txt = findViewById(R.id.email_user);
        num_txt = findViewById(R.id.num_user);
        pass_txt=findViewById(R.id.pass_user);
        spinKitView_reg =findViewById(R.id.spin_kit_reg);
        register = findViewById(R.id.reg_btn_user);
        login_text = findViewById(R.id.login_text);
        showpasscheckbox = findViewById(R.id.checkBox_reg);
        userDatamodule = new user_datamodule();
        //if user need to login click on login_text

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_register.this, user_login.class);
                startActivity(intent);
                finish();
            }
        });

        //doing authentication in firebase
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,email,password,num;
                name=name_txt.getText().toString();
                email=email_txt.getText().toString();
                password=pass_txt.getText().toString();
                num=num_txt.getText().toString();

                if(validinput(email,password,num,name)){
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(user_register.this, "Please Verify Your E-mail", Toast.LENGTH_SHORT).show();
                                    if(task.isSuccessful()){
                                        showAlert(user_register.this,"E-mail Verification","Please Verify Your E-mail from your Mail-box");
                                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                uploadReg_user();
                                            }
                                        });
                                    }
                                    spinKitView_reg.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
        showpasscheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    pass_txt.setTransformationMethod(null); // Show password
                } else {
                    pass_txt.setTransformationMethod(new PasswordTransformationMethod()); // Hide password
                }
            }
        });
    }
    public void uploadReg_user(){
        String user_name = name_txt.getText().toString();
        String user_email = email_txt.getText().toString();
        String user_num = num_txt.getText().toString();
        String uid = auth.getUid();

        userDatamodule.setName_reg(user_name);
        userDatamodule.setEmail_reg(user_email);
        userDatamodule.setNum_reg(user_num);
        userDatamodule.setUid_user(uid);

        databaseReference.push().setValue(userDatamodule);
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