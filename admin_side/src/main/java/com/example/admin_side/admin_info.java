package com.example.admin_side;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class admin_info extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] item_name={"Veg","Nonveg"};
    int[] item_image={R.drawable.veg_symbol,R.drawable.nonveg_symbol};
    String[] time_select_item={"10:00 AM To 5:00 PM","5:00 PM To 11:59 PM"};
    String[] days={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    String [] city_list = {
            "Ahemdabad","Surat" ,"Botad" ,"Kalol"
            ,"Rajkot","Vadodara","Jamnagar","Bharuch"
           ,"Valsad","Amreli" ,"Kheda"
           , " Mundra","Pavagada","Bhuj"," Gondal","Morbi"
            ,"Ambaji" ,"Dwarka","Rajpipla","Chikhli"," Dahod","Palanpur","Anand"
           ,  "Navsari",  "Vapi", "Nadiad" ,"Patan","Mehsana" ,"Godhra","Mahuva"};


    Button save_data;
    EditText rest_name,rest_add,pincode;
    TextView state_name;
    Spinner day_start,day_end,time_select,type_select,city_name;

    admin_datamodule adminDatamodule;
    FirebaseAuth auth;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Admin_Info");


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info);
        auth=FirebaseAuth.getInstance();

        //find Id of Childs
        save_data=findViewById(R.id.save_data);
        rest_name=findViewById(R.id.restname);
        city_name=findViewById(R.id.city_name);
        state_name=findViewById(R.id.state_name);
        rest_add=findViewById(R.id.rest_add);
        day_start=findViewById(R.id.day_start);
        day_end=findViewById(R.id.day_end);
        time_select=findViewById(R.id.time_select);
        type_select=findViewById(R.id.type_select);
        pincode=findViewById(R.id.pincode);

        adminDatamodule=new admin_datamodule();


        //day selection spinner
        ArrayAdapter<String> day_adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,days);
        day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_start.setAdapter(day_adapter);
        day_end.setAdapter(day_adapter);

        //selected time spinner code
        ArrayAdapter<String> time_select_adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,time_select_item);
        time_select_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_select.setAdapter(time_select_adapter);


        ArrayAdapter<String> city_adapter =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,city_list);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_name.setAdapter(city_adapter);

        //save data button to go to next activity and save data
        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=rest_name.getText().toString();
                //String city=city_name.getText().toString();
                String state=state_name.getText().toString();
                String address=rest_add.getText().toString();
                if(validinput(name,address)){
                    Intent intent = new Intent(admin_info.this, Admin_Back_Info.class);
                    uploaddata2();
                    startActivity(intent);
                }
                else {

                }
            }
        });

        city_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                adminDatamodule.setCity(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // time spinner coding for saving the data to database
        time_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                adminDatamodule.setTime(selectedItem);
                Toast.makeText(admin_info.this, adminDatamodule.getTime(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        day_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedday = adapterView.getItemAtPosition(i).toString();
                adminDatamodule.setDay1(selectedday);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        day_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedendday = adapterView.getItemAtPosition(i).toString();
                adminDatamodule.setDay2(selectedendday);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //food type spinner coding
        type_select.setOnItemSelectedListener(this);
        Adapter_Spinner adapter_spinner=new Adapter_Spinner(this,item_name,item_image);
        type_select.setAdapter(adapter_spinner);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




    //making method for checking that information is fullfilled
    public boolean validinput(String string_name,String string_add){

        if (TextUtils.isEmpty(string_name)){
            rest_name.setError("Restaurant name is required!");
            return false;
        }
        if (TextUtils.isEmpty(string_add)){
            rest_add.setError("Addresss is Required!");
            return false;
        }
        return true;
    }
    private void uploaddata2() {
        String restname = rest_name.getText().toString();
        String statename = state_name.getText().toString();
        String pin_code = pincode.getText().toString();
        String address = rest_add.getText().toString();
        String uid = auth.getCurrentUser().getUid();

        adminDatamodule.setRest_name(restname);
        adminDatamodule.setState(statename);
        adminDatamodule.setPincode(pin_code);
        adminDatamodule.setAddress(address);
        adminDatamodule.setUid(uid);

        // Create a new child node under "Admin_Info" using push() to generate a unique key
        DatabaseReference newReference = databaseReference.push();

        // Set the value of the new node with the data from adminDatamodule
        newReference.setValue(adminDatamodule);

        // Get the key of the new node and store it in adminDatamodule as a reference
        String referenceKey = newReference.getKey();
        adminDatamodule.setSetReference(referenceKey);

        // Update the reference in the database
        newReference.child("Reference").setValue(referenceKey);
    }
}
