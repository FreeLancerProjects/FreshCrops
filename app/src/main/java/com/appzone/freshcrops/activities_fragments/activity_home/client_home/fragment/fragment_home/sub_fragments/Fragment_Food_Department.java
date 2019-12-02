package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_home.sub_fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.adapters.MainCategoryAdapter;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.remote.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Food_Department extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private MainCategoryAdapter adapter;
    private List<MainCategory.MainCategoryItems> mainCategoryItemsList;
    private HomeActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_department,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Food_Department newInstance()
    {
        return new Fragment_Food_Department();
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        mainCategoryItemsList = new ArrayList<>();
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheEnabled(true);
        adapter = new MainCategoryAdapter(getActivity(),mainCategoryItemsList,this);
        recView.setAdapter(adapter);
        getMainCategoryData();
    }

    private void getMainCategoryData()
    {
        Api.getService()
                .getMainCategory()
                .enqueue(new Callback<MainCategory>() {
                    @Override
                    public void onResponse(Call<MainCategory> call, Response<MainCategory> response) {
                        if (response.isSuccessful())
                        {


                            progBar.setVisibility(View.GONE);
                            if (response.body().getData().size()>0)
                            {
                                mainCategoryItemsList.clear();
                                mainCategoryItemsList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                activity.setMainCategory(response.body());
                                activity.dismissSnackBar();
                            }else
                                {
                                    activity.CreateSnackBar(getString(R.string.There_are_no_departments_to_display));


                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<MainCategory> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    public void setItem(MainCategory.MainCategoryItems mainCategoryItems)
    {
        activity.DisplayFragmentSubCategory(mainCategoryItems);
    }
}
