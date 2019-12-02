package com.appzone.freshcrops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.models.WeekOfferModel;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderWeekPagerAdapter extends PagerAdapter {

    private List<WeekOfferModel.Offer> offerList;
    private Context context;

    public SliderWeekPagerAdapter(List<WeekOfferModel.Offer> offerList, Context context) {
        this.offerList = offerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return offerList.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);
        ImageView image = view.findViewById(R.id.image);
        WeekOfferModel.Offer offer = offerList.get(position);
        Picasso.with(context).load(Tags.IMAGE_URL+offer.getImage()).priority(Picasso.Priority.HIGH).fit().into(image);
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
