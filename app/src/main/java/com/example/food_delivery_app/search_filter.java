package com.example.food_delivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class search_filter extends AppCompatActivity {
    LinearLayout sortby,price,vegnon,rating,price_checkboxLy,rating_chechboxLy;
    RadioGroup sortradiogroup,vegnon_rodiogroup;
    RadioButton lowtohigh,hightolow,btn_veg,btn_non;
    CheckBox less,mid,high;
    Button send_filter;
    String seleled_ops,less_amount,midamount_1,midamount_2,higeamount,low_to_high,high_to_low;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        sortby = findViewById(R.id.sortbyLy);
        price = findViewById(R.id.priceLy);
        vegnon = findViewById(R.id.vegnonLy);
        rating = findViewById(R.id.ratingLy);
        vegnon_rodiogroup = findViewById(R.id.type_radiogroup);
        price_checkboxLy = findViewById(R.id.linear_check);
        sortradiogroup = findViewById(R.id.sort_radiogroup);
        rating_chechboxLy =findViewById(R.id.linear_rating);
        lowtohigh = findViewById(R.id.low_high);
        hightolow = findViewById(R.id.high_low);
        send_filter = findViewById(R.id.add_item);
        btn_veg = findViewById(R.id.veg_radio);
        btn_non = findViewById(R.id.nonveg_radio);
        less = findViewById(R.id.less_check);
        mid = findViewById(R.id.between_check);
        high = findViewById(R.id.greater_check);
        sortby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sort_radiogroup = sortradiogroup.getVisibility();
                if (sort_radiogroup == View.VISIBLE) {
                    sortradiogroup.setVisibility(View.GONE);
                } else {
                    sortradiogroup.setVisibility(View.VISIBLE);
                }
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pricr_view = price_checkboxLy.getVisibility();
                if (pricr_view == View.VISIBLE) {
                    price_checkboxLy.setVisibility(View.GONE);
                } else {
                    price_checkboxLy.setVisibility(View.VISIBLE);
                }
            }
        });
        vegnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility_vngnon = vegnon_rodiogroup.getVisibility();
                if (visibility_vngnon == View.VISIBLE) {
                    vegnon_rodiogroup.setVisibility(View.GONE);
                } else {
                    vegnon_rodiogroup.setVisibility(View.VISIBLE);
                }
            }
        });
        rating.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int visibility_rating = rating_chechboxLy.getVisibility();
            if (visibility_rating == View.VISIBLE) {
                rating_chechboxLy.setVisibility(View.GONE);
            } else {
                rating_chechboxLy.setVisibility(View.VISIBLE);
            }
        }
    });
        vegnon_rodiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (btn_veg.isChecked()){
                    seleled_ops = "Veg";
                }else if (btn_non.isChecked()){
                    seleled_ops = "Nonveg";
                }
            }
        });

        sortradiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (lowtohigh.isChecked()){
                    low_to_high = "0";
                }else if (hightolow.isChecked()){
                    high_to_low = "100";
                }
            }
        });

        less.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (less.isChecked()) {
                    less_amount = "50";
                } else {

                }
            }
        });
        mid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mid.isChecked()) {
                    midamount_1 = "100";
                    midamount_2 = "150";
                } else {

                }
            }
        });
        high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (high.isChecked()) {
                    higeamount = "400";
                } else {

                }
            }
        });



        send_filter.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(search_filter.this, search_user.class);
            i.putExtra("food_type",seleled_ops);
            i.putExtra("amountless",less_amount);
            i.putExtra("amountmid1",midamount_1);
            i.putExtra("amountmid2",midamount_2);
            i.putExtra("highamount",higeamount);
            i.putExtra("lowtohigh",low_to_high);
            i.putExtra("hightolow",high_to_low);
            startActivity(i);
        }
    });
    }
}