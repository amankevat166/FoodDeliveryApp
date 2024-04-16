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

public class Search_Adpater extends RecyclerView.Adapter<Search_Adpater.MyViewHolder>{
    private ArrayList<user_datamodule> items;
    private Context context;
    private ItemClickListner itemClickListner;

    public Search_Adpater(ArrayList<user_datamodule> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void setClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }


    @NonNull
    @Override
    public Search_Adpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Search_Adpater.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Search_Adpater.MyViewHolder holder, int position) {
        user_datamodule list = items.get(position);
        holder.item_name.setText(list.getCat_name());
        holder.cat_name.setText(list.getItem_name());
        holder.rest_name.setText(list.getRest_name());
        holder.item_price.setText(list.getItem_price());
        Glide.with(context).load(list.getImguri()).into(holder.item_img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_name, cat_name,rest_name,item_price;
        public ImageView item_img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item_name = itemView.findViewById(R.id.item_name);
            this.cat_name = itemView.findViewById(R.id.cat_name_tv);
            this.rest_name = itemView.findViewById(R.id.rest_name_tv);
            this.item_img = itemView.findViewById(R.id.img);
            this.item_price = itemView.findViewById(R.id.price);
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
