package com.example.admin_side;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Menu_Adapter extends RecyclerView.Adapter<Menu_Adapter.MyViewHolder>{
    public Menu_Adapter(ArrayList<admin_datamodule> items, Context context) {
        this.items = items;
        this.context = context;
    }

    private ArrayList<admin_datamodule> items;
    private Context context;

    private ItemClickListner itemClickListner;
    public void setClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }
    @NonNull
    @Override
    public Menu_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout,null));

    }

    @Override
    public void onBindViewHolder(@NonNull Menu_Adapter.MyViewHolder holder, int position) {
        holder.cat_name.setText(items.get(position).getCat_name());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView cat_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cat_name = itemView.findViewById(R.id.cat_name_tv);
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
