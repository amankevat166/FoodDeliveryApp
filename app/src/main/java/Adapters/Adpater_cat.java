package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_app.ItemClickListner;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.user_datamodule;

import java.util.ArrayList;

public class Adpater_cat  extends RecyclerView.Adapter<Adpater_cat.ViewHolder> {

    Context context;
    ArrayList<user_datamodule> list;
    private ItemClickListner itemClickListner;


    public void setClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }
    public Adpater_cat(Context context, ArrayList<user_datamodule> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adpater_cat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adpater_cat.ViewHolder holder, int position) {
        user_datamodule data = list.get(position);
        holder.cat_name.setText(data.getSub_cat());
        Glide.with(context).load(data.getImguri()).into(holder.cat_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cat_img;
        TextView cat_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_img=itemView.findViewById(R.id.cat_img);
            cat_name=itemView.findViewById(R.id.cat_name);
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
