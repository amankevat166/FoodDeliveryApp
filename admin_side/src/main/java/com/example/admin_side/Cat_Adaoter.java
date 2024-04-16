package com.example.admin_side;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Cat_Adaoter extends RecyclerView.Adapter<Cat_Adaoter.MyViewHolder>{
    private ArrayList<admin_datamodule> items;
    private Context context;

    private ItemClickListner itemClickListner;

    public Cat_Adaoter(ArrayList<admin_datamodule> items, Context context) {
        this.items = items;
        this.context = context;
    }
    public void setClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }
    @NonNull
    @Override
    public Cat_Adaoter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Cat_Adaoter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list_items, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Cat_Adaoter.MyViewHolder holder, int position) {
        admin_datamodule datamodule = items.get(position);
        holder.item_name.setText(items.get(position).getItem_name());
        holder.item_price.setText(items.get(position).getItem_price());
        String ref = datamodule.getReference().toString();
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, admin_updata_delete.class);
                intent.putExtra("Ref",ref);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
       holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref_cart = FirebaseDatabase.getInstance().getReference("Menu").child(ref);
                ref_cart.removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_name,item_price;
        public ImageView edit,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item_name = itemView.findViewById(R.id.text_food_name);
            this.item_price = itemView.findViewById(R.id.text_food_price);
            this.edit = itemView.findViewById(R.id.image_edit);
            this.delete = itemView.findViewById(R.id.image_delete);
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
