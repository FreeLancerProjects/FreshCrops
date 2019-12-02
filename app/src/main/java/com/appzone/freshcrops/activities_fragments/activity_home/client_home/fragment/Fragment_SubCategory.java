package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.adapters.DepartmentAdapter;
import com.appzone.freshcrops.adapters.ProductsAdapter;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.models.ProductPaginationModel;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SubCategory extends Fragment {
    private static final String TAG = "DATA";
    private LinearLayout ll_back,ll_search;
    private ImageView image_back,image;
    private TextView tv_no_products,tv_name;
    private RecyclerView recViewDepartment,recView;
    private RecyclerView.LayoutManager manager,managerDepartment;
    private DepartmentAdapter departmentAdapter;
    private MainCategory.MainCategoryItems mainCategoryItems;
    private String current_lang;
    private HomeActivity activity;
    private List<MainCategory.Products> productsList;
    private ProductsAdapter productsAdapter;
    private int sub_category_id;
    private int current_page_index = 1;
    private boolean isLoading = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_category,container,false);
        initView(view);
        return view;
    }

    public static Fragment_SubCategory newInstance(MainCategory.MainCategoryItems mainCategoryItems)
    {
        Fragment_SubCategory fragment_subCategory = new Fragment_SubCategory();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,mainCategoryItems);
        fragment_subCategory.setArguments(bundle);
        return fragment_subCategory;
    }
    private void initView(View view) {
        productsList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        ll_back = view.findViewById(R.id.ll_back);
        image_back = view.findViewById(R.id.image_back);
        Paper.init(getActivity());
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }
        image = view.findViewById(R.id.image);
        ll_search = view.findViewById(R.id.ll_search);
        tv_name = view.findViewById(R.id.tv_name);

        tv_no_products = view.findViewById(R.id.tv_no_products);
        recViewDepartment = view.findViewById(R.id.recViewDepartment);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(activity,2);
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(true);
        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(15);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy>0)
                {


                    GridLayoutManager manager = (GridLayoutManager) recView.getLayoutManager();

                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();

                    int totalItemCount = manager.getItemCount();

                    if (lastVisibleItem >=(totalItemCount-5) && !isLoading)
                    {
                        isLoading = true;
                        LoadMore(current_page_index);

                    }
                }



            }
        });

        managerDepartment = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recViewDepartment.setLayoutManager(managerDepartment);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentHome();
            }
        });
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setLast_selected_fragment("fragment_sub_category");
                activity.DisplayFragmentSearch();
            }
        });
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            mainCategoryItems = (MainCategory.MainCategoryItems) bundle.getSerializable(TAG);
            UpdateUI(mainCategoryItems);
        }





    }


    private void UpdateUI(MainCategory.MainCategoryItems mainCategoryItems) {
        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+mainCategoryItems.getImage())).into(image);
        if (current_lang.equals("ar"))
        {
            tv_name.setText(mainCategoryItems.getName_ar());
        }else
        {
            tv_name.setText(mainCategoryItems.getName_en());

        }
        departmentAdapter = new DepartmentAdapter(getActivity(),mainCategoryItems.getSub_categories(),this);
        recViewDepartment.setAdapter(departmentAdapter);

        ///////////////////////////////////////////////////
        if (mainCategoryItems.getSub_categories().size()>0)
        {
            if (mainCategoryItems.getSub_categories().get(0).getProducts().size()>0)
            {
                recView.setVisibility(View.VISIBLE);
                tv_no_products.setVisibility(View.GONE);

                sub_category_id = mainCategoryItems.getSub_categories().get(0).getId();
                UpdateSubCategoryAdapter(mainCategoryItems.getSub_categories().get(0).getProducts());

            }else
                {
                    recView.setVisibility(View.GONE);
                    tv_no_products.setVisibility(View.VISIBLE);
                }

        }else
            {
                recView.setVisibility(View.GONE);
                tv_no_products.setVisibility(View.VISIBLE);
            }


    }


    private void UpdateSubCategoryAdapter(final List<MainCategory.Products> products) {
        productsList.addAll(products);
        productsAdapter = new ProductsAdapter(getActivity(),productsList,this,recView);
        recView.setAdapter(productsAdapter);




    }

    private void LoadMore(final int current_page_index)
    {
        int next_page = current_page_index+1;

        productsList.add(null);
        productsAdapter.notifyDataSetChanged();

        Api.getService()
                .getProductPagination(sub_category_id,next_page)
                .enqueue(new Callback<ProductPaginationModel>() {
                    @Override
                    public void onResponse(Call<ProductPaginationModel> call, Response<ProductPaginationModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {
                                productsList.remove(productsList.size()-1);
                                productsAdapter.notifyItemRemoved(productsList.size()-1);
                                isLoading = false;
                                if (response.body().getData().size()>0)
                                {
                                    Fragment_SubCategory.this.current_page_index = response.body().getCurrent_page();
                                    productsList.addAll(response.body().getData());
                                }
                                productsAdapter.notifyDataSetChanged();



                            }else
                                {
                                    productsList.remove(productsList.size()-1);
                                    productsAdapter.notifyItemRemoved(productsList.size()-1);
                                    isLoading = false;

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPaginationModel> call, Throwable t) {

                    }
                });
    }

    public void setItemForDepartment(MainCategory.SubCategory itemForDepartment)
    {
        if (itemForDepartment.getProducts().size()>0)
        {
            recView.setVisibility(View.VISIBLE);
            tv_no_products.setVisibility(View.GONE);

            current_page_index = 1;
            sub_category_id = itemForDepartment.getId();
            productsList.clear();
            productsList.addAll(itemForDepartment.getProducts());

            if (productsAdapter==null)
            {
                productsAdapter = new ProductsAdapter(getActivity(),productsList,this,recView);
                recView.setAdapter(productsAdapter);
            }else
                {
                    productsAdapter.notifyDataSetChanged();

                }

        }else
            {
                recView.setVisibility(View.GONE);
                tv_no_products.setVisibility(View.VISIBLE);
            }

    }

    public void setItemForDetails(MainCategory.Products product) {
        List<MainCategory.Products> similarProducts = getSimilarProducts(productsList, product.getId());
        activity.NavigateToProductDetailsActivity(product,similarProducts );

    }

    private List<MainCategory.Products> getSimilarProducts(List<MainCategory.Products> productList,int selectedProductId)
    {
        List<MainCategory.Products> products = new ArrayList<>();

        for (MainCategory.Products product : productList)
        {
            if (product!=null)
            {
                if (product.getId()!=0&&selectedProductId!=0)
                {
                    if (product.getId()!=selectedProductId)
                    {
                        products.add(product);
                    }
                }
            }



        }

        return products;
    }
}
