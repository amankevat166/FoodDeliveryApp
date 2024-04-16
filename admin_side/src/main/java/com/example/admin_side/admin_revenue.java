package com.example.admin_side;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class admin_revenue extends AppCompatActivity {
    TextView todayamount, weekamout, mouthamout,week_start_day,week_end_day,today_order,month_order,week_order,today_date,custom_date,custom_date2;
    String rest_name,startDateString,endDateString;
    Date cut_date_start, cut_date_end;
    Long startDate,endDate;
    DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("order_history");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_revenue);
        todayamount = findViewById(R.id.today_amount);
        weekamout = findViewById(R.id.week_amount);
        mouthamout = findViewById(R.id.month_amount);
        week_start_day = findViewById(R.id.week_date);
        today_order = findViewById(R.id.today_order);
        month_order = findViewById(R.id.month_order);
        week_order = findViewById(R.id.week_order);
        today_date = findViewById(R.id.today_date);
        custom_date = findViewById(R.id.custom_date);
        custom_date2 = findViewById(R.id.custom_date2);
        Intent i = getIntent();
        rest_name = i.getStringExtra("rest_name");

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDateString = dateFormat.format(currentDate);
        today_date.setText(currentDateString);



        // Calculate start and end of the week
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date weekStartDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndDate = calendar.getTime();

        String weekstart = dateFormat.format(weekStartDate);
        String weekend = dateFormat.format(weekEndDate);
        week_start_day.setText(weekstart);

        // Calculate start and end of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStartDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date monthEndDate = calendar.getTime();

        Query query = ordersRef.orderByChild("rest_name").equalTo(rest_name);

        custom_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatePickerdialog();
                //Toast.makeText(admin_revenue.this, "In-Onclike", Toast.LENGTH_SHORT).show();
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("Select a date range");

                // Building the date picker dialog
                MaterialDatePicker<Pair<Long,Long>> datePicker = builder.build();
                datePicker.addOnPositiveButtonClickListener(selection -> {

                    // Retrieving the selected start and end dates
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());



                    startDate = selection.first;
                    endDate = selection.second;
                    // Formating the selected dates as strings.

                    startDateString = sdf.format(new Date(startDate));
                    endDateString = sdf.format(new Date(endDate));
                    try {
                        // Parse String to Date
                        cut_date_start = sdf.parse(startDateString);
                        cut_date_end = sdf.parse(startDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // Creating the date range string
                    String selectedDateRange = startDateString + " - " + endDateString;
                    custom_date.setText(selectedDateRange);

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                                int qty = Integer.parseInt(data.getQty_txt());
                                int price = Integer.parseInt(data.getItem_price());
                                String date = data.getDate();
                                int cum_amount = 0;
                                int cum_ordercount = 0;
                                Date orderDateObj = null;
                                if (cut_date_start != null){
                                if (orderDateObj.after(cut_date_start) && orderDateObj.before(cut_date_end)) {
                                    cum_amount += qty * price;
                                    cum_ordercount++;
                                  }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                });
                datePicker.show(getSupportFragmentManager(),"tag");
            }
        });



        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int todayTotal = 0;
                int weektotal = 0;
                int monthtotal = 0;
                int orderCount = 0;
                int orderCountweek = 0;
                int orderCountmounth = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    admin_datamodule data = dataSnapshot.getValue(admin_datamodule.class);
                    int qty = Integer.parseInt(data.getQty_txt());
                    int price = Integer.parseInt(data.getItem_price());
                    String date = data.getDate();
                    //Toast.makeText(admin_revenue.this, ""+weekStartDate, Toast.LENGTH_LONG).show();
                    if (date.equals(currentDateString)) {
                        todayTotal += (qty * price); // Add the order total to today's total amount
                        orderCount++;
                    }
                    Date orderDateObj = null;
                    try {
                        orderDateObj = dateFormat.parse(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if (orderDateObj.after(weekStartDate) && orderDateObj.before(weekEndDate)) {
                        weektotal += qty * price;
                        orderCountweek++;
                    }

                    if (orderDateObj.after(monthStartDate) && orderDateObj.before(monthEndDate)) {
                        monthtotal += qty * price;
                        orderCountmounth++;
                    }



                        todayamount.setText(String.valueOf(todayTotal));
                        today_order.setText(String.valueOf(orderCount));
                        weekamout.setText(String.valueOf(weektotal));
                        week_order.setText(String.valueOf(orderCountweek));
                        mouthamout.setText(String.valueOf(monthtotal));
                        month_order.setText(String.valueOf(orderCountmounth));
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

//    public void DatePickerdialog () {
//        // Creating a MaterialDatePicker builder for selecting a date range
//        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
//        builder.setTitleText("Select a date range");
//
//        // Building the date picker dialog
//        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
//        datePicker.addOnPositiveButtonClickListener(selection -> {
//
//            // Retrieving the selected start and end dates
//            Long startDate = selection.first;
//            Long endDate = selection.second;
//
//            // Formating the selected dates as strings
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//            String startDateString = sdf.format(new Date(startDate));
//            String endDateString = sdf.format(new Date(endDate));
//
//            // Creating the date range string
//            String selectedDateRange = startDateString + " - " + endDateString;
//            custom_date.setText(selectedDateRange);
//            // Displaying the selected date range in the TextView
//            //data.setText(selectedDateRange);
//        });
//    }
}