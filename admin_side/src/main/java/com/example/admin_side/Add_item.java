package com.example.admin_side;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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


public class Add_item extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText item_name,item_price,dec_item;
    LinearLayout pizza_size_linear,food_type_linear;
    ImageView img;
    TextView checkBox;
    Spinner weight_unit,food_type_spinner;
    String[] food_type_txt={"Veg","Nonveg"};
    int[] food_type_image={R.drawable.veg_symbol,R.drawable.nonveg_symbol};
    Button btn_save;
    Uri imageUri;
    AutoCompleteTextView cat_autocomp,subcat_autocomp;
    admin_datamodule adminDatamodule;
    FirebaseAuth auth;
    String[] food_weight={"Gm","Kg"};
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Category_Menu");
    DatabaseReference dataRef_get= FirebaseDatabase.getInstance().getReference("Admin_Info");
    DatabaseReference Autocompsub_reference =  FirebaseDatabase.getInstance().getReference("Sub_Category");
    DatabaseReference Autocompcat_reference =  FirebaseDatabase.getInstance().getReference("Category_List");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        cat_autocomp = findViewById(R.id.edit_cat_name);
        subcat_autocomp =findViewById(R.id.edit_subcat_name);
        item_name = findViewById(R.id.edit_item_name);
        item_price = findViewById(R.id.edit_item_price);
        img = findViewById(R.id.image_item);
        dec_item = findViewById(R.id.Dec_item);
        btn_save = findViewById(R.id.button_add_edit);
        weight_unit=findViewById(R.id.weight_unit);
        food_type_spinner=findViewById(R.id.food_type_spinner);
        pizza_size_linear=findViewById(R.id.pizza_size_linear);
        food_type_linear=findViewById(R.id.food_weight_linear);
        adminDatamodule=new admin_datamodule();
        auth = FirebaseAuth.getInstance();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            img.setImageURI(imageUri);
                        } else {
                            Toast.makeText(Add_item.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
           );

        //spinner for food type
        ArrayAdapter<String> type_adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,food_weight);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weight_unit.setAdapter(type_adapter);

        //food type spinner coding
        food_type_spinner.setOnItemSelectedListener(this);
        Adapter_Spinner adapter_spinner=new Adapter_Spinner(this,food_type_txt,food_type_image);
        food_type_spinner.setAdapter(adapter_spinner);
        food_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adminDatamodule.setFood_type(food_type_txt[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Defining Implicit Intent to mobile gallery
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTodatabase(imageUri);
                item_name.setText("");
                item_price.setText("");
                cat_autocomp.setText("");
                subcat_autocomp.setText("");
                dec_item.setText("");
                img.setImageResource(R.drawable.select_image);
            }
        });
        setUpAutoCompleteTextView();
        setUpAutoCompleteSubcat();
        String uid = auth.getUid();
        Query query = dataRef_get.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                    String rest = data.getRest_name();
                    adminDatamodule.setRest_name(rest);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cat_autocomp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                adminDatamodule.setCat_name(selectedItem);
                Toast.makeText(Add_item.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        subcat_autocomp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String selectedsub_Item = (String) parent.getItemAtPosition(position);
                adminDatamodule.setSub_cat(selectedsub_Item);
                if(adminDatamodule.getSub_cat().equals("Pizza")){
                    pizza_size_linear.setVisibility(View.VISIBLE);
                }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Add_item.this, android.R.layout.simple_dropdown_item_1line, foodItemsList);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Add_item.this, android.R.layout.simple_dropdown_item_1line, cat_sub);
                    subcat_autocomp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void uploadTodatabase(Uri  uri){
        String itemname = item_name.getText().toString();
        String itemprice = item_price.getText().toString();
        String item_dec = dec_item.getText().toString();
        String uid = auth.getUid();

        adminDatamodule.setItem_name(itemname);
        adminDatamodule.setItem_price(itemprice);
        adminDatamodule.setDec_item(item_dec);
        adminDatamodule.setUid(uid);

        StorageReference imageRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
        imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                         @Override
                         public void onSuccess(Uri uri) {
                            adminDatamodule.setImguri(String.valueOf(uri));
                            DatabaseReference newReference = databaseReference.push();
                             newReference.setValue(adminDatamodule);
                             String referenceKey = newReference.getKey();
                             adminDatamodule.setReference(referenceKey);
                             newReference.child("Reference").setValue(referenceKey);
                         }
                     });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


   /*private void uploaddata(){
        DatabaseReference Spinner_reference =  FirebaseDatabase.getInstance().getReference("Category_List");
        String[] foodItems = {
                "Starters",
                "Indian cuisine",
                "Snacks",
                "Indian snacks",
                "Indian bread", "Vegetarian", "Non-vegetarian", "Noodles",
                "Dessert","Beverages","Rice","Soup"
        };

        // Upload the list to Firebase
        for (String foodItem : foodItems) {
            // Push the data to the database
            Spinner_reference.push().setValue(foodItem);
        }
    }*/

}