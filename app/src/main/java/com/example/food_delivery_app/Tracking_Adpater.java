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

public class Tracking_Adpater extends RecyclerView.Adapter<Tracking_Adpater.MyViewHolder>{

    private ArrayList<user_datamodule> items;
    private Context context;
    private ItemClickListner itemClickListner;

    public Tracking_Adpater(ArrayList<user_datamodule> items, Context context) {
        this.items = items;
        this.context = context;
    }
    public void setClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }

    @NonNull
    @Override
    public Tracking_Adpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Tracking_Adpater.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Tracking_Adpater.MyViewHolder holder, int position) {
        user_datamodule list = items.get(position);
        holder.item_name.setText(list.getItem_name());
        Glide.with(context).load(list.getImguri()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView item_name;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            img = itemView.findViewById(R.id.trk_img);
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
