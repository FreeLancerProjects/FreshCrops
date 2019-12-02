package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.adapters.OfferedProductsAdapter;
import com.appzone.freshcrops.adapters.SliderWeekPagerAdapter;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.models.ProductPaginationModel;
import com.appzone.freshcrops.models.WeekOfferModel;
import com.appzone.freshcrops.remote.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Offers extends Fragment {

    private LinearLayout ll_back;
    private ImageView image_back;
    private ViewPager pager;
    private TabLayout tab;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private LinearLayout ll_no_offers;
    private OfferedProductsAdapter offeredProductsAdapter;
    private List<MainCategory.Products> productsList;
    private FrameLayout fr_collapsing_container;
    private ProgressBar progBar;
    private int current_page = 1;
    private boolean isLoading = false;
    private HomeActivity homeActivity;
    private WeekOfferModel weekOfferModel;
    private SliderWeekPagerAdapter sliderWeekPagerAdapter;
    private Timer timer;
    private TimerTask timerTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Offers newInstance()
    {
        return new Fragment_Offers();
    }
    private void initView(View view)
    {
        homeActivity = (HomeActivity) getActivity();
        productsList = new ArrayList<>();
        ll_back = view.findViewById(R.id.ll_back);
        image_back = view.findViewById(R.id.image_back);
        pager = view.findViewById(R.id.pager);
        tab = view.findViewById(R.id.tab);
        tab.setupWithViewPager(pager);
        fr_collapsing_container = view.findViewById(R.id.fr_collapsing_container);

        Paper.init(getActivity());
        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }
        ll_no_offers = view.findViewById(R.id.ll_no_offers);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(getActivity(),2);
        recView.setLayoutManager(manager);
        offeredProductsAdapter = new OfferedProductsAdapter(getActivity(),productsList,this,recView);
        recView.setAdapter(offeredProductsAdapter);


        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy>0)
                {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) recView.getLayoutManager();
                    int lastVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = gridLayoutManager.getItemCount();

                    if (lastVisibleItem >= (totalItemCount-5) && !isLoading)
                    {
                        isLoading = true;
                        int next_page = current_page+1;
                        getOfferedProducts(next_page,true);
                    }
                }


            }
        });


        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.Back();
            }
        });

        getWeekOffers();
        getOfferedProducts(current_page,false);


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
                                weekOfferModel = response.body();
                                if (response.body().getData().size()>0)
                                {
                                    fr_collapsing_container.setVisibility(View.VISIBLE);
                                    UpdateSliderUI(response.body().getData());
                                }else
                                    {

                                        fr_collapsing_container.setVisibility(View.GONE);

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
        sliderWeekPagerAdapter = new SliderWeekPagerAdapter(data,getActivity());
        pager.setAdapter(sliderWeekPagerAdapter);

        if (data.size()>1)
        {
            timer = new Timer();
            timerTask = new MyTimerTask();
            timer.scheduleAtFixedRate(timerTask,6000,6000);
        }
    }

    private void getOfferedProducts(final int page_index, final boolean loadMore)
    {
        Api.getService()
                .getOfferedProductPagination(page_index)
                .enqueue(new Callback<ProductPaginationModel>() {
                    @Override
                    public void onResponse(Call<ProductPaginationModel> call, Response<ProductPaginationModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            homeActivity.dismissSnackBar();

                            if (response.body()!=null)
                            {
                                if (response.body().getData().size()>0)
                                {
                                    ll_no_offers.setVisibility(View.GONE);

                                    if (loadMore)
                                    {
                                        Fragment_Offers.this.productsList.remove(Fragment_Offers.this.productsList.size()-1);
                                        offeredProductsAdapter.notifyItemRemoved(Fragment_Offers.this.productsList.size()-1);

                                        if (response.body().getData().size()>0)
                                        {
                                            Fragment_Offers.this.productsList.addAll(response.body().getData());
                                            current_page = response.body().getCurrent_page();

                                        }

                                        isLoading = false;
                                        offeredProductsAdapter.notifyDataSetChanged();

                                    }else
                                        {
                                            Fragment_Offers.this.productsList.addAll(response.body().getData());
                                            offeredProductsAdapter.notifyDataSetChanged();
                                        }

                                }else
                                    {


                                        if (loadMore)
                                        {
                                            Fragment_Offers.this.productsList.remove(Fragment_Offers.this.productsList.size()-1);
                                            offeredProductsAdapter.notifyItemRemoved(Fragment_Offers.this.productsList.size()-1);
                                            isLoading = false;
                                            offeredProductsAdapter.notifyDataSetChanged();

                                        }else
                                            {
                                                ll_no_offers.setVisibility(View.VISIBLE);
                                            }
                                    }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPaginationModel> call, Throwable t) {
                        try {
                            homeActivity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void setItemForDetails(MainCategory.Products products) {

        homeActivity.NavigateToProductDetailsActivity(products,homeActivity.getSimilarProducts(products.getMain_category_id(),products.getSub_category_id(),products.getId()));
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
