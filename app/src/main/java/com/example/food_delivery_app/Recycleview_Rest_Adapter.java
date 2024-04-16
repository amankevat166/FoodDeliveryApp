package com.example.food_delivery_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class Recycleview_Rest_Adapter extends RecyclerView.Adapter<Recycleview_Rest_Adapter.MyViewHolder>{
    private ArrayList<user_datamodule> items;
    private Context context;
    private ItemClickListner itemClickListner;

    public void setClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }

    public Recycleview_Rest_Adapter(ArrayList<user_datamodule> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public Recycleview_Rest_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Recycleview_Rest_Adapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Recycleview_Rest_Adapter.MyViewHolder holder, int position) {
        user_datamodule list = items.get(position);
        holder.item_name.setText(list.getItem_name());
        holder.price.setText(list.getItem_price());
        holder.dec_item.setText(list.getDec_item());
        Glide.with(context).load(list.getImguri()).into(holder.item_img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_name, price,dec_item;
        public ImageView item_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item_name = itemView.findViewById(R.id.item_name);
            this.price = itemView.findViewById(R.id.item_price);
            this.item_img = itemView.findViewById(R.id.img);
            this.dec_item = itemView.findViewById(R.id.textView9);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (itemClickListner != null) {
                itemClickListner.onClick(view, getAdapterPosition());
            }
        }
    }
}

