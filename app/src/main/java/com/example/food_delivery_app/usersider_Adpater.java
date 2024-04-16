package com.example.food_delivery_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class usersider_Adpater extends PagerAdapter {
    private Context context;
    private int[] images={R.drawable.onboard1, R.drawable.onboard2, R.drawable.onboard3};
    int[] heading={R.string.heading1, R.string.heading2, R.string.heading3};
    int[] desc={R.string.desc1, R.string.desc2, R.string.desc3};

    public usersider_Adpater(Context context) {
        this.context = context;
    }



    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @SuppressLint("MissingInflatedId")

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.slider, container, false);


        ImageView imageView = itemView.findViewById(R.id.onboard_image);
        TextView heading_txt=itemView.findViewById(R.id.onboard_title);
        TextView desc_txt=itemView.findViewById(R.id.onboard_desc);

        imageView.setImageResource(images[position]);
        heading_txt.setText(heading[position]);
        desc_txt.setText(desc[position]);
        container.addView(itemView);
        return  itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
