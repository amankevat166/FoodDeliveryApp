package com.example.admin_side;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_Spinner extends BaseAdapter {

    Context context;
    String[] item_nm;
    int[] item_img;

    public Adapter_Spinner(Context context, String[] item_name, int[] item_img) {
        this.context = context;
        this.item_nm = item_name;
        this.item_img = item_img;
    }

    @Override
    public int getCount() {
        return item_nm.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.item_spinner,viewGroup,false);
        ImageView item_image=view.findViewById(R.id.item_image);
        TextView item_name=view.findViewById(R.id.item_name);

        item_image.setImageResource(item_img[i]);
        item_name.setText(item_nm[i]);
        return view;
    }
}
