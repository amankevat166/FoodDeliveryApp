package Adapters;

import static java.lang.String.valueOf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_app.R;
import com.example.food_delivery_app.user_datamodule;

import java.util.ArrayList;

public class Adapter_history extends RecyclerView.Adapter<Adapter_history.MyViewHolder>{

    private ArrayList<user_datamodule> items;
    private Context context;

    public Adapter_history(ArrayList<user_datamodule> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_history.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter_history.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_history.MyViewHolder holder, int position) {
            user_datamodule datamodule = items.get(position);
            holder.item_name.setText(datamodule.getItem_name());
            holder.item_price.setText(datamodule.getItem_price());
            holder.rest_name.setText(datamodule.getRest_name());
            holder.qty_text.setText(datamodule.getQty_txt());
        if (datamodule.getQty_txt() != null && datamodule.getItem_price() != null) {
            int qty = Integer.parseInt(datamodule.getQty_txt());
            int price = Integer.parseInt(datamodule.getItem_price());
            int total = price * qty;
            holder.total_price.setText(valueOf(total));
        } else {
            // Handle the case where item quantity or price is null
            // You can set a default value or handle it in any other way appropriate for your application
            holder.total_price.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_name,qty_text,item_price,total_price,rest_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item_name =  itemView.findViewById(R.id.food_items_txt);
            this.qty_text = itemView.findViewById(R.id.quantity_txt);
            this.rest_name = itemView.findViewById(R.id.rest_name);
            this.item_price=itemView.findViewById(R.id.item_price);
            this.total_price = itemView.findViewById(R.id.total_amount);

        }
    }
}
