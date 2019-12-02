package com.appzone.freshcrops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.product_details.activity.ProductDetailsActivity;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private List<String> img_end_pointList;
    private Context context;
    private ProductDetailsActivity activity;

    public SliderPagerAdapter(List<String> img_end_pointList, Context context) {
        this.img_end_pointList = img_end_pointList;
        this.context = context;
        activity = (ProductDetailsActivity) context;
    }

    @Override
    public int getCount() {
        return img_end_pointList.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);
        final ImageView image = view.findViewById(R.id.image);
        String endPoint = img_end_pointList.get(position);
        Picasso.with(context).load(Tags.IMAGE_URL+endPoint).priority(Picasso.Priority.HIGH).fit().into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endPoint = img_end_pointList.get(position);
                activity.setItemEndPoint(endPoint);

            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
