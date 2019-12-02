package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.adapters.RecentSearchQueryAdapter;
import com.appzone.freshcrops.adapters.SearchProductsAdapter;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.models.ProductPaginationModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment {
    private TextView tv_no_searched_products,tv_recent_visited;
    private LinearLayout ll_back;
    private ImageView image_back,image_logo,image_cancel;
    private EditText edt_search;
    private ExpandableLayout expand_layout;
    private RecyclerView recViewRecentSearch,recView;
    private RecyclerView.LayoutManager manager,managerRecentSearch;
    private RecentSearchQueryAdapter recentSearchQueryAdapter;
    private SearchProductsAdapter searchProductsAdapter;
    private List<MainCategory.Products> normal_productsList;
    private List<MainCategory.Products> productsList;
    private ProgressBar progBar;
    private Button btn_search;
    private String currentLang;
    private List<String> queryList;
    private Preferences preferences;
    private HomeActivity activity;
    private int current_page = 1;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        initView(view);
        return view;
    }

    public static  Fragment_Search newInstance()
    {
        return new Fragment_Search();
    }
    private void initView(View view)
    {
        activity = (HomeActivity) getActivity();
        queryList = new ArrayList<>();
        productsList = new ArrayList<>();
        normal_productsList = new ArrayList<>();
        tv_recent_visited = view.findViewById(R.id.tv_recent_visited);

        preferences = Preferences.getInstance();
        Paper.init(getActivity());
        currentLang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        image_back = view.findViewById(R.id.image_back);

        if (currentLang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
            {
                image_back.setImageResource(R.drawable.arrow_left);

            }

        ll_back = view.findViewById(R.id.ll_back);
        image_cancel = view.findViewById(R.id.image_cancel);
        image_logo = view.findViewById(R.id.image_logo);
        btn_search = view.findViewById(R.id.btn_search);
        edt_search = view.findViewById(R.id.edt_search);
        expand_layout = view.findViewById(R.id.expand_layout);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tv_no_searched_products = view.findViewById(R.id.tv_no_searched_products);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recView.setDrawingCacheEnabled(true);
        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(25);
        recViewRecentSearch = view.findViewById(R.id.recViewRecentSearch);
        managerRecentSearch = new LinearLayoutManager(getActivity());

        recViewRecentSearch.setLayoutManager(managerRecentSearch);
        recViewRecentSearch.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recViewRecentSearch.setDrawingCacheEnabled(true);
        recViewRecentSearch.setHasFixedSize(true);
        recViewRecentSearch.setItemViewCacheSize(25);

        recentSearchQueryAdapter = new RecentSearchQueryAdapter(getActivity(),queryList,this);
        recViewRecentSearch.setAdapter(recentSearchQueryAdapter);

        searchProductsAdapter = new SearchProductsAdapter(getActivity(),productsList,this,recView);
        recView.setAdapter(searchProductsAdapter);

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy>0)
                {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recView.getLayoutManager();
                    int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();

                    if (lastVisibleItem >= (totalItemCount-5)&& !isLoading)
                    {
                        isLoading = true;
                        String q = edt_search.getText().toString().trim();
                        int next_page = current_page+1;
                        productsList.add(null);
                        searchProductsAdapter.notifyDataSetChanged();

                        LoadMore(q,next_page);
                    }
                }


            }
        });


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = edt_search.getText().toString().trim();
                if (query.length()>0)
                {
                    image_logo.setVisibility(View.GONE);
                    image_cancel.setVisibility(View.VISIBLE);
                    btn_search.setVisibility(View.VISIBLE);
                    if (queryList.size()>0)
                    {
                        expand_layout.setExpanded(true,true);

                    }

                }else
                    {

                        if (normal_productsList.size()>0)
                        {
                            tv_recent_visited.setVisibility(View.VISIBLE);
                            tv_no_searched_products.setVisibility(View.GONE);
                        }else
                            {
                                tv_no_searched_products.setText(getString(R.string.no_products_searched_for_recently));
                                tv_no_searched_products.setVisibility(View.VISIBLE);
                                tv_recent_visited.setVisibility(View.GONE);


                            }
                        image_logo.setVisibility(View.VISIBLE);
                        image_cancel.setVisibility(View.GONE);
                        btn_search.setVisibility(View.GONE);
                        expand_layout.collapse(true);
                        productsList.clear();
                        productsList.addAll(normal_productsList);
                        searchProductsAdapter.notifyDataSetChanged();

                    }

            }
        });
        image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
                image_cancel.setVisibility(View.GONE);
                btn_search.setVisibility(View.GONE);
                image_logo.setVisibility(View.VISIBLE);
                expand_layout.collapse(true);
                DisplayNormalList();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.CloseKeyBoard(getActivity(),edt_search);
                String query = edt_search.getText().toString().trim();
                Search(query);
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        getSavedQueries();
        getSavedVisitedProductsIds();


    }
    public void Search(final String query)
    {
        tv_recent_visited.setVisibility(View.GONE);
        edt_search.setText(query);
        Common.CloseKeyBoard(getActivity(),edt_search);

        if (expand_layout.isExpanded())
        {
            expand_layout.collapse(true);
        }
        progBar.setVisibility(View.VISIBLE);
        recView.setAlpha(.5f);
        Api.getService()
                .search(query,1)
                .enqueue(new Callback<ProductPaginationModel>() {
                    @Override
                    public void onResponse(Call<ProductPaginationModel> call, Response<ProductPaginationModel> response) {
                        if (response.isSuccessful())
                        {
                            activity.dismissSnackBar();
                            progBar.setVisibility(View.GONE);
                            recView.setAlpha(1.0f);
                            if (response.body().getData().size()>0)
                            {
                                tv_no_searched_products.setVisibility(View.GONE);
                                preferences.addRecentSearchQuery(getActivity(),query);
                                productsList.clear();
                                productsList.addAll(response.body().getData());
                                isLoading = false;
                                searchProductsAdapter.notifyDataSetChanged();

                            }else
                                {
                                    tv_no_searched_products.setText(R.string.no_ser_res);
                                    tv_no_searched_products.setVisibility(View.VISIBLE);

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPaginationModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            recView.setAlpha(1.0f);
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }


    private void LoadMore(String query , int page_index)
    {

        Api.getService()
                .search(query,page_index)
                .enqueue(new Callback<ProductPaginationModel>() {
                    @Override
                    public void onResponse(Call<ProductPaginationModel> call, Response<ProductPaginationModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            recView.setAlpha(1.0f);
                            if (response.body().getData().size()>0)
                            {
                                current_page = response.body().getCurrent_page();
                                productsList.remove(productsList.size()-1);
                                productsList.addAll(response.body().getData());
                                isLoading = false;
                                searchProductsAdapter.notifyDataSetChanged();

                            }else
                            {
                                productsList.remove(productsList.size()-1);
                                isLoading = false;
                                searchProductsAdapter.notifyDataSetChanged();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPaginationModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            recView.setAlpha(1.0f);
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void getSavedQueries()
    {
        for (String q : preferences.getAllQueries(getActivity()))
        {
            Log.e("query",q+"_");
        }
        queryList.addAll(preferences.getAllQueries(getActivity()));
        recentSearchQueryAdapter.notifyDataSetChanged();

    }

    private void getSavedVisitedProductsIds()
    {
        List<String> productsIdsList = preferences.getAllVisitedIds(getActivity());
        if (productsIdsList.size()>0)
        {
            tv_recent_visited.setVisibility(View.VISIBLE);
            progBar.setVisibility(View.VISIBLE);
            getVisitedProducts(productsIdsList);
        }else
            {
                tv_recent_visited.setVisibility(View.GONE);
                progBar.setVisibility(View.GONE);
            }
    }

    private void getVisitedProducts(List<String> productsIdsList)
    {
        if (productsIdsList.size()>0)
        {

            tv_no_searched_products.setVisibility(View.GONE);
            Api.getService()
                    .getRecentSearchProducts(productsIdsList)
                    .enqueue(new Callback<ProductPaginationModel>() {
                        @Override
                        public void onResponse(Call<ProductPaginationModel> call, Response<ProductPaginationModel> response) {
                            if (response.isSuccessful())
                            {
                                progBar.setVisibility(View.GONE);
                                activity.dismissSnackBar();

                                if (response.body().getData().size()>0)
                                {
                                    productsList.clear();
                                    productsList.addAll(response.body().getData());
                                    normal_productsList.clear();
                                    normal_productsList.addAll(response.body().getData());
                                    searchProductsAdapter.notifyDataSetChanged();
                                    tv_no_searched_products.setVisibility(View.GONE);
                                }else
                                    {
                                        tv_no_searched_products.setVisibility(View.VISIBLE);

                                    }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductPaginationModel> call, Throwable t) {
                            try {
                                progBar.setVisibility(View.GONE);
                                activity.CreateSnackBar(getString(R.string.something));
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });

        }else
            {
                tv_no_searched_products.setVisibility(View.VISIBLE);

            }
    }

    private void DisplayNormalList()
    {
        productsList.clear();
        productsList.addAll(normal_productsList);
        searchProductsAdapter.notifyDataSetChanged();

    }

    public void setItemForDetails(MainCategory.Products products)
    {
        if (products!=null)
        {

            preferences.saveVisitedProductIds(getActivity(),String.valueOf(products.getId()));

            activity.NavigateToProductDetailsActivity(products,activity.getSimilarProducts(products.getMain_category_id(),products.getSub_category_id(),products.getId()));

        }
    }
}
