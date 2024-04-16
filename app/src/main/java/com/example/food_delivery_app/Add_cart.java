package com.example.food_delivery_app;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Adapters.Cart_Adapter;

public class Add_cart extends AppCompatActivity implements PaymentResultListener {
    ArrayList<user_datamodule> list = new ArrayList<>();
    Button Pay,Buysomething;
    int pay = 0;
    RecyclerView recyclerView;
    Cart_Adapter cart_adapter;
    LottieAnimationView lottieAnimationView;
    FirebaseAuth auth;
    TextView total_price, price, tax_price,yourcatr,buysomething;
    user_datamodule userDatamodule;
    Tracking_Data trackingData;
    ConstraintLayout constraintLayout;
    String name;

    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIFICATION_ID = 123;

    DatabaseReference dataRef_cart = FirebaseDatabase.getInstance().getReference("Cart_item");
    DatabaseReference Ref_cart = FirebaseDatabase.getInstance().getReference("order_data");
    DatabaseReference Ref_username = FirebaseDatabase.getInstance().getReference("User_reg_info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        Checkout.preload(getApplicationContext());

        recyclerView = findViewById(R.id.recycleview);
        price = findViewById(R.id.final_price);
        tax_price = findViewById(R.id.delivery_charge);
        total_price = findViewById(R.id.Total_amount);
        Pay = findViewById(R.id.place_order);
        yourcatr = findViewById(R.id.textView8);
        buysomething = findViewById(R.id.textView9);
        Buysomething = findViewById(R.id.buy_btn);
        constraintLayout = findViewById(R.id.constraintLayout_cart);
        lottieAnimationView = findViewById(R.id.empty_cart_anim);
        auth = FirebaseAuth.getInstance();
        userDatamodule = new user_datamodule();
        trackingData = new Tracking_Data();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cart_adapter = new Cart_Adapter(getApplicationContext(), list);
        recyclerView.setAdapter(cart_adapter);
        recyclerView.setVisibility(View.GONE);
        String uid = auth.getCurrentUser().getUid();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           if (ContextCompat.checkSelfPermission(Add_cart.this, Manifest.permission.POST_NOTIFICATIONS)!=
                   PackageManager.PERMISSION_GRANTED){
               ActivityCompat.requestPermissions(Add_cart.this,
                       new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
           }
        }
        Query query = dataRef_cart.orderByChild("uid_user").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    list.add(data);
                }
                    if (list.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        constraintLayout.setVisibility(View.GONE);
                        yourcatr.setVisibility(View.VISIBLE);
                        buysomething.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        Buysomething.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        constraintLayout.setVisibility(View.VISIBLE);
                        yourcatr.setVisibility(View.GONE);
                        buysomething.setVisibility(View.GONE);
                        lottieAnimationView.setVisibility(View.GONE);
                        Buysomething.setVisibility(View.GONE);

                    }
                    int sum = 0, i;
                    //loop for get price and total-price with tax
                    for (i = 0; i < list.size(); i++) {
                        int qty = Integer.parseInt((list.get(i).getQty_txt()));
                        int price_ = Integer.parseInt(list.get(i).getItem_price());
                        sum = sum + (price_ * qty);
                        int total = sum;
                        int tax_pirce = Integer.parseInt(String.valueOf(tax_price.getText()));
                        String total_tax = String.valueOf(total + tax_pirce);
                        price.setText(String.valueOf(total));
                        total_price.setText(total_tax);
                        userDatamodule.setTotal_price(total_price.getText().toString());
                    }
                    cart_adapter.notifyDataSetChanged();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
        Query query_name = Ref_username.orderByChild("uid_user").equalTo(uid);
        query_name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_datamodule data = dataSnapshot.getValue(user_datamodule.class);
                    name = data.getName_reg();
                    //userDatamodule.setName_reg(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Buysomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Add_cart.this, user_dashboard.class));
            }
        });

    }
    public void startPayment() {
        int total_100 =  Integer.parseInt(String.valueOf(total_price.getText()));
        int to_pay = pay + (100 * total_100);
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_rnnV2vHPIYH1ap");

        checkout.setImage(R.drawable.logo2);

        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Merchant Name");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount",to_pay);//500 x 100
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","6354531293");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity,options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "PaymentSuccess" + s, Toast.LENGTH_SHORT).show();
        order_data();
        makenotification();
        list.clear();
    }
    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "PaymentError"+s, Toast.LENGTH_SHORT).show();
    }

    public void order_data(){
        DatabaseReference Ref_history = FirebaseDatabase.getInstance().getReference("order_history");
        DatabaseReference Ref_order = FirebaseDatabase.getInstance().getReference("order_place");
        DatabaseReference Ref_track= FirebaseDatabase.getInstance().getReference("order_traking");

        //Query query = dataRef_cart.orderByChild("uid_user").equalTo(auth.getCurrentUser().getUid());
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        ArrayList<user_datamodule> dataList = new ArrayList<>();
        if (adapter instanceof Cart_Adapter) {
            dataList = ((Cart_Adapter) adapter).getDataList();
            for (user_datamodule data : dataList) {

                Calendar calendar = Calendar.getInstance();

                Date currentDate = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currentDateString =  dateFormat.format(currentDate);

                int hour = calendar.get(Calendar.HOUR_OF_DAY); // Get hour in 24-hour format
                int minute = calendar.get(Calendar.MINUTE);

                // Convert hour to 12-hour format
                String am_pm;
                if (hour >= 12) {
                    am_pm = "PM";
                    if (hour > 12) {
                        hour -= 12;
                    }
                } else {
                    am_pm = "AM";
                    if (hour == 0) {
                        hour = 12;
                    }
                }

                // Format the time
                String formattedTime = String.format("%02d:%02d %s", hour, minute, am_pm);

                //code for saving Reference of node for tracking
                //order_traking
                DatabaseReference track_Ref = Ref_track.push();
                String ref_tk = track_Ref.getKey();


                //order_place
                DatabaseReference newReference = Ref_order.push();
                newReference.setValue(userDatamodule);
                String referenceKey = newReference.getKey();

                //order_hri
                DatabaseReference Ref_his = Ref_history.push();
                Ref_his.setValue(userDatamodule);
                String reference_his_Key = Ref_his.getKey();


                //order_hri
                data.setReference(reference_his_Key);
                data.setTotal_price(userDatamodule.getTotal_price());
                data.setDate(currentDateString);
                data.setTime(formattedTime);
                Ref_history.child(reference_his_Key).setValue(data);

                //order_place
                data.setReference(referenceKey);
                data.setDate(currentDateString);
                data.setRef_ord(ref_tk);
                data.setOrd_place("33");
                data.setTime(formattedTime);
                data.setName_reg(name);
                Ref_order.child(referenceKey).setValue(data);

                //order_traking
                data.setReference(referenceKey);
                data.setRef_ord(ref_tk);
                data.setDate(currentDateString);
                data.setOrd_place("33");
                data.setTime(formattedTime);
                track_Ref.setValue(data);

            }

            Ref_cart.removeValue();
            //makenotification();
        }
    }
    public void makenotification() {
            String chanelID = "CHANNEL_ID_NOTIFICATION";
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext(), chanelID);
            builder.setSmallIcon(R.drawable.logo2)
                    .setContentTitle("HungryFine")
                    .setContentText("Your order has been placed order will be deliver in short time")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel (chanelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,
                        "some_description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0,builder.build());
    }


    public void trackdata(){
        DatabaseReference Ref_updata = FirebaseDatabase.getInstance().getReference("updata");

        String order = "place_order";
        String acc = "";
        String pre = "";

        trackingData.setOrd_place(order);
        trackingData.setOrd_apc(acc);
        trackingData.setOrd_pre(pre);

        DatabaseReference updata = Ref_updata.push();
        //updata.setValue(trackingData);
        String ref = updata.getKey();
        trackingData.setTrk_Reference(ref);
        trackingData.setUid_user(auth.getCurrentUser().getUid());
        updata.setValue(trackingData);

    }

    public void alert_dialog(Context context){
        AlertDialog.Builder builder=new AlertDialog.Builder(Add_cart.this);
        View dialogview=getLayoutInflater().inflate(R.layout.address_dialog,null);

        builder.setView(dialogview);
        AlertDialog alertDialog= builder.create();
        dialogview.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, user_address.class));
            }
        });
        dialogview.findViewById(R.id.address_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //payment code
            }
        });
        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }

}