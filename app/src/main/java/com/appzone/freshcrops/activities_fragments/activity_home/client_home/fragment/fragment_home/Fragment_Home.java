package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.adapters.SliderWeekPagerAdapter;
import com.appzone.freshcrops.models.WeekOfferModel;
import com.appzone.freshcrops.remote.Api;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    //private ImageView image_back;
    private AppBarLayout app_bar;
    private LinearLayout ll_search,ll_search_container;
    private HomeActivity activity;
    private ViewPager pager;
    private TabLayout tab;
    private Toolbar toolBar;
    private SliderWeekPagerAdapter sliderWeekPagerAdapter;
    private Timer timer;
    private TimerTask timerTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Home newInstance()
    {
        return new Fragment_Home();
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        toolBar = view.findViewById(R.id.toolBar);
        pager = view.findViewById(R.id.pager);
        tab = view.findViewById(R.id.tab);
        ll_search = view.findViewById(R.id.ll_search);
        ll_search_container = view.findViewById(R.id.ll_search_container);

        tab.setupWithViewPager(pager);
        app_bar = view.findViewById(R.id.app_bar);
        Paper.init(getActivity());




        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scroll_range = app_bar.getTotalScrollRange();

                if ((scroll_range+verticalOffset)==0)
                {
                    toolBar.setVisibility(View.VISIBLE);

                }else
                    {
                        ll_search_container.setVisibility(View.VISIBLE);
                        toolBar.setVisibility(View.GONE);
                    }


            }
        });




        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setLast_selected_fragment("fragment_home");
                activity.DisplayFragmentSearch();
            }
        });

        getWeekOffers();

    }

    private void getWeekOffers() {
        Api.getService()
                .getWeekOffers()
                .enqueue(new Callback<WeekOfferModel>() {
                    @Override
                    public void onResponse(Call<WeekOfferModel> call, Response<WeekOfferModel> response) {

                        if (response.isSuccessful())
                        {

                            if (response.body()!=null)
                            {
                                if (response.body().getData().size()>0)
                                {
                                    UpdateSliderUI(response.body().getData());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeekOfferModel> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdateSliderUI(List<WeekOfferModel.Offer> data) {
        if (data.size()>0)
        {
            sliderWeekPagerAdapter = new SliderWeekPagerAdapter(data,getActivity());
            pager.setAdapter(sliderWeekPagerAdapter);
            for (int i =0;i<tab.getTabCount();i++)
            {
                View view = ((ViewGroup)tab.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.setMargins(5,0,5,0);
                tab.requestLayout();
            }


        }


        if (data.size()>1)
        {
            timer = new Timer();
            timerTask = new MyTimerTask();
            timer.scheduleAtFixedRate(timerTask,6000,6000);
        }
    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (pager.getCurrentItem()<sliderWeekPagerAdapter.getCount()-1)
                    {
                        pager.setCurrentItem(pager.getCurrentItem()+1);
                    }else
                    {
                        pager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        if (timer!=null)
        {
            timer.purge();
            timer.cancel();
        }
        if (timerTask!=null)
        {
            timerTask.cancel();
        }
        super.onDestroyView();
        Log.e("Fragment destroy view","destroy view");
    }

}
