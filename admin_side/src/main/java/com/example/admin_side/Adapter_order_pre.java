package com.example.admin_side;

import static java.lang.String.valueOf;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Adapter_order_pre extends RecyclerView.Adapter<Adapter_order_pre.MyViewHolder>{

    Context context;
    ArrayList<admin_datamodule> list;
    private Handler handler = new Handler();

    public Adapter_order_pre(Context context, ArrayList<admin_datamodule> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_order_pre.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter_order_pre.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_order_item,null));

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_order_pre.MyViewHolder holder, int position) {
        admin_datamodule datamodule = list.get(position);
        holder.order_qty.setText(datamodule.getQty_txt());
        holder.food_items_txt.setText(datamodule.getItem_name());
        holder.item_price.setText(datamodule.getItem_price());
        holder.customer_name.setText(datamodule.getName());
        holder.bind(datamodule);
        String ref_id = valueOf(list.get(position).getReference());
        String tk_id = list.get(position).getRef_ord();
        // Check if item quantity and price are not null before parsing
        if (datamodule.getQty_txt() != null && datamodule.getItem_price() != null) {
            int qty = Integer.parseInt(datamodule.getQty_txt());
            int price = Integer.parseInt(datamodule.getItem_price());
            int total = price * qty;
            holder.total_amount.setText(valueOf(total));
        } else {
            // Handle the case where item quantity or price is null
            // You can set a default value or handle it in any other way appropriate for your application
            holder.total_amount.setText("N/A");
        }

        holder.order_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref_track = FirebaseDatabase.getInstance().getReference("order_traking").child(tk_id);
                ref_track.child("ord_place").setValue("100");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pre_data")
                        .child(ref_id);
                ref.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView order_id, order_time, customer_name, order_qty, food_items_txt, item_price, total_amount, minute_txt;
        Button order_ready;
        private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        private long startTime;
        private long timerDuration;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id_value);
            order_time = itemView.findViewById(R.id.order_time);
            customer_name = itemView.findViewById(R.id.customer_name);
            order_qty = itemView.findViewById(R.id.quantity_txt);
            food_items_txt = itemView.findViewById(R.id.food_items_txt);
            item_price = itemView.findViewById(R.id.item_price);
            total_amount = itemView.findViewById(R.id.total_amount);
            minute_txt = itemView.findViewById(R.id.min_num);
            order_ready = itemView.findViewById(R.id.accept_btn);
        }

        private long parseDurationToMilliseconds(String durationString) {
            try {
                long minutes = Long.parseLong(durationString);
                // Convert minutes to milliseconds
                return minutes * 60 * 1000;
            } catch (NumberFormatException e) {
                // Handle parsing errors
                e.printStackTrace(); // Log the error for debugging purposes
                return 0; // Return 0 or throw an exception, depending on your requirements
            }
        }

        void bind(admin_datamodule data) {
            minute_txt.setText(data.getTime());
            timerDuration = parseDurationToMilliseconds(data.getTime());
            startTimer(timerDuration);
            updateTime(); // Update time initially
        }
        //real-time code


        private void updateTime() {
            // Update time TextView with current time
            String currentTime = sdf.format(new Date());
            order_time.setText(currentTime);

            // Schedule the update to run again after 1 second
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateTime();
                }
            }, 1000);
        }

        //timer code for which is give by admin to pre-food
        private void startTimer(long timerDuration) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateTimer(timerDuration);
                    }
                }, 1000); // Update timer every second
            }

        private void updateTimer(long timerDuration) {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime;
                    long remainingTime = timerDuration - elapsedTime;

                    if (remainingTime <= 0) {
                        // Timer expired
                        minute_txt.setText("Timer: 00:00");
                    } else {
                        // Convert remaining time from milliseconds to minutes and seconds
                        int minutes = (int) (remainingTime / (1000 * 60));
                        int seconds = (int) ((remainingTime / 1000) % 60);
                        // Display the remaining time in the format "MM:SS"
                        minute_txt.setText(String.format("Timer: %02d:%02d", minutes, seconds));

                        // Schedule the timer to run again after 1 second
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateTimer(timerDuration);
                            }
                        }, 1000);
                    }
            }



        }
    }
