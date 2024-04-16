package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_app.R;
import com.example.food_delivery_app.user_datamodule;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.ViewHolder> {
    Context context;
    String ref_cart;
    ArrayList<user_datamodule> list;


    public Cart_Adapter(Context context, ArrayList<user_datamodule> list) {
        this.context=context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        user_datamodule datamodule = list.get(position);
        holder.item_name.setText(list.get(position).getItem_name());
        holder.item_price.setText(list.get(position).getItem_price());
        holder.item_qty.setText(list.get(position).getQty_txt());
        holder.item_desc.setText(list.get(position).getDec_item());
        holder.seller_name.setText(list.get(position).getRest_name());
        Glide.with(context).load(list.get(position).getImguri()).into(holder.item_img);
        String ref_id = String.valueOf(list.get(position).getReference());
        holder.plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty= Integer.parseInt(holder.item_qty.getText().toString());
                qty++;
                holder.item_qty.setText(String.valueOf(qty));
                updata_qty(String.valueOf(qty),ref_id);
            }
        });
        holder.minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(holder.item_qty.getText().toString());
                if (qty > 1 ){
                        int temp = qty - 1;
                        holder.item_qty.setText(String.valueOf(temp));
                        String qty_size = holder.item_qty.getText().toString();
                        updata_qty(qty_size,ref_id);
                }
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Cart_item")
                        .child(ref_id);
                ref.removeValue();

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<user_datamodule> getDataList() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        ImageButton plus_btn,minus_btn,delete_btn;
        TextView item_name,item_price,item_qty,item_desc,seller_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_img=itemView.findViewById(R.id.item_img);
            item_name=itemView.findViewById(R.id.item_name);
            item_price=itemView.findViewById(R.id.item_price);
            item_qty=itemView.findViewById(R.id.item_qty);
            item_desc=itemView.findViewById(R.id.desc);
            seller_name=itemView.findViewById(R.id.seller_name);
            plus_btn=itemView.findViewById(R.id.plus_btn);
            minus_btn=itemView.findViewById(R.id.minus_btn);
            delete_btn=itemView.findViewById(R.id.delete_btn);

        }
    }
    private void updata_qty(String qty,String ref_cart) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Cart_item")
                .child(ref_cart).child("qty_txt");
        ref.setValue(qty);
    }
}
