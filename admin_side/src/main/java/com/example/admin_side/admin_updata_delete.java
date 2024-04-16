package com.example.admin_side;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class admin_updata_delete extends AppCompatActivity {
    DatabaseReference databaseReference_menu = FirebaseDatabase.getInstance().getReference("Menu");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference Autocompsub_reference =  FirebaseDatabase.getInstance().getReference("Sub_Category");
    DatabaseReference Autocompcat_reference =  FirebaseDatabase.getInstance().getReference("Category_List");
    EditText price,dec,name;
    ImageView food_imag;
    AutoCompleteTextView cat_autocomp,subcat_autocomp;
    Button save;
    Uri imageUri;
    admin_datamodule Admindatamodule = new admin_datamodule();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_updata_delete);
        //cat_item = findViewById(R.id.edit_cat_name);
        //subcat_item = findViewById(R.id.edit_subcat_name);
        cat_autocomp = findViewById(R.id.edit_cat_name);
        subcat_autocomp =findViewById(R.id.edit_subcat_name);
        name = findViewById(R.id.edit_item_name);
        price = findViewById(R.id.edit_item_price);
        dec =findViewById(R.id.Dec_item);
        food_imag = findViewById(R.id.imageView5);
        save = findViewById(R.id.button_edit);

        Intent i = getIntent();
        String Ref = i.getStringExtra("Ref");

        setUpAutoCompleteTextView();
        setUpAutoCompleteSubcat();
        cat_autocomp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Admindatamodule.setCat_name(selectedItem);
                //Toast.makeText(Add_item.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        subcat_autocomp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String selectedsub_Item = (String) parent.getItemAtPosition(position);
                Admindatamodule.setSub_cat(selectedsub_Item);
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            food_imag.setImageURI(imageUri);
                        } else {
                            Uri img = Admindatamodule.getImage_uri();
                            databaseReference_menu.child("imguri").setValue(img);
                            Toast.makeText(admin_updata_delete.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        food_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Defining Implicit Intent to mobile gallery
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });


        Query query = databaseReference_menu.orderByChild("Reference").equalTo(Ref);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    admin_datamodule datamodule = dataSnapshot.getValue(admin_datamodule.class);
                    Glide.with(getApplicationContext()).load(datamodule.getImguri()).into(food_imag);
                    cat_autocomp.setText(datamodule.getCat_name());
                    subcat_autocomp.setText(datamodule.getSub_cat());
                    name.setText(datamodule.getItem_name());
                    price.setText(datamodule.getItem_price());
                    dec.setText(datamodule.getDec_item());
                    Admindatamodule.setImage_uri(datamodule.getImage_uri());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatadata(imageUri);
            }
        });
    }
    public void updatadata(Uri uri){
        String cat_name = cat_autocomp.getText().toString();
        String subcat_name = subcat_autocomp.getText().toString();
        String item_name = name.getText().toString();
        String item_price = price.getText().toString();
        String item_dec = dec.getText().toString();
        food_imag.setImageURI(Admindatamodule.getImage_uri());
        Admindatamodule.setItem_price(item_price);
        Admindatamodule.setItem_name(item_name);
        Admindatamodule.setCat_name(cat_name);
        Admindatamodule.setSub_cat(subcat_name);
        Admindatamodule.setDec_item(item_dec);

        Intent i = getIntent();
        String Ref = i.getStringExtra("Ref");

        DatabaseReference databaseReference_menu = FirebaseDatabase.getInstance().getReference("Menu").child(Ref);
        databaseReference_menu.child("item_price").setValue(item_price);
        databaseReference_menu.child("item_name").setValue(item_name);
        databaseReference_menu.child("dec_item").setValue(item_dec);
        databaseReference_menu.child("cat_name").setValue(cat_name);
        databaseReference_menu.child("subcat_name").setValue(subcat_name);



        StorageReference imageRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
        imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Admindatamodule.setImguri(String.valueOf(uri));
                        String img = Admindatamodule.getImguri();
                        databaseReference_menu.child("imguri").setValue(img);
                    }
                });
            }
        });
    }

            private void setUpAutoCompleteTextView() {
                final List<String> foodItemsList = new ArrayList<>();
                Autocompcat_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String foodItem = dataSnapshot.getValue(String.class);
                            if (foodItem != null) {
                                foodItemsList.add(foodItem);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                ArrayAdapter<String> adapter = new ArrayAdapter<>(admin_updata_delete.this, android.R.layout.simple_dropdown_item_1line, foodItemsList);
                // Set adapter to AutoCompleteTextView
                cat_autocomp.setAdapter(adapter);
            }
            private void setUpAutoCompleteSubcat(){
                final List<String> cat_sub = new ArrayList<>();
                Autocompsub_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String sub_items = dataSnapshot.getValue(String.class);
                            if (sub_items != null) {
                                cat_sub.add(sub_items);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(admin_updata_delete.this, android.R.layout.simple_dropdown_item_1line, cat_sub);
                            subcat_autocomp.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }