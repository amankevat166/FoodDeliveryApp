package com.example.admin_side;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Accept_Order_Adapter extends RecyclerView.Adapter<Accept_Order_Adapter.MyViewHolder> {
    Context context;
    ArrayList<admin_datamodule> list;
    public Accept_Order_Adapter(Context context, ArrayList<admin_datamodule> list) {
        this.context=context;
        this.list = list;
    }

    @NonNull
    @Override
    public Accept_Order_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Accept_Order_Adapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_order_item,null));
    }

@Override
public void onBindViewHolder(@NonNull Accept_Order_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
    admin_datamodule datamodule = list.get(position);
    holder.order_qty.setText(datamodule.getQty_txt());
    holder.food_items_txt.setText(datamodule.getItem_name());
    holder.item_price.setText(datamodule.getItem_price());
    holder.customer_name.setText(datamodule.getName_reg());
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

    holder.plus_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int qty = Integer.valueOf(holder.minute_txt.getText().toString());
            int temp = qty + 1;
            holder.minute_txt.setText(valueOf(temp));
        }
    });

    holder.minus_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int qty = Integer.valueOf(holder.minute_txt.getText().toString());
            if (qty > 1) {
                int temp = qty - 1;
                holder.minute_txt.setText(valueOf(temp));
            }
        }
    });
    holder.decline.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("order_place")
                    .child(ref_id);
            ref.removeValue();
            DatabaseReference ref_trk = FirebaseDatabase.getInstance().getReference("order_traking")
                    .child(ref_id);
            ref_trk.removeValue();
            DatabaseReference ref_his = FirebaseDatabase.getInstance().getReference("order_history")
                    .child(ref_id);
            ref_his.removeValue();
        }
    });
    holder.accepct.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("order_place")
                    .child(ref_id);
            DatabaseReference pre_ref = FirebaseDatabase.getInstance().getReference("pre_data");
            datamodule.setTime(holder.minute_txt.getText().toString());
            
            pre_ref.child(ref.getKey()).setValue(datamodule);
            DatabaseReference ref_track = FirebaseDatabase.getInstance().getReference("order_traking").child(tk_id);
            ref_track.child("ord_place").setValue("66");


            ref.removeValue();
        }
    });
}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView order_id,order_time,customer_name,order_qty,food_items_txt,item_price,total_amount,plus_btn,minus_btn,minute_txt;
        Button accepct,decline;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id=itemView.findViewById(R.id.order_id_value);
            order_time=itemView.findViewById(R.id.order_time);
            customer_name=itemView.findViewById(R.id.customer_name);
            order_qty=itemView.findViewById(R.id.quantity_txt);
            food_items_txt=itemView.findViewById(R.id.food_items_txt);
            item_price=itemView.findViewById(R.id.item_price);
            total_amount=itemView.findViewById(R.id.total_amount);
            plus_btn=itemView.findViewById(R.id.plus_btn);
            minus_btn=itemView.findViewById(R.id.minus_btn);
            minute_txt=itemView.findViewById(R.id.min_num);
            accepct=itemView.findViewById(R.id.accept_btn);
            decline=itemView.findViewById(R.id.decline_btn);
        }
    }
}
