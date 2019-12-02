package com.appzone.freshcrops.activities_fragments.activity_order_details.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.freshcrops.adapters.DelegateCollectOrderAdapter;
import com.appzone.freshcrops.adapters.RecyclerItemTouchHelper;
import com.appzone.freshcrops.models.DelegateCollectingOrderUploadModel;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.models.Products;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Delegate_Collecting_Order_Products extends Fragment implements RecyclerItemTouchHelper.SwipeListener{
    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private DelegateCollectOrderAdapter adapter;
    private ImageView image_back;
    private LinearLayout ll_back;
    private String current_lang;
    private OrdersModel.Order order;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private List<OrdersModel.Products> orderProductList,productsList;
    private List<OrdersModel.Products> choosedProductList;
    private TextView tv_order_price,tv_no_product;
    private Button btn_collected,btn_cancel;
    private double order_total_cost=0.0;
    private int alternativeItemPos =-1;
    private List<Products> choosedUploadProductsList;
    private DelegateCollectingOrderUploadModel uploadModel;
    private int total_products_deleted = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delegate_collecting_order_products,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Delegate_Collecting_Order_Products newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);

        Fragment_Delegate_Collecting_Order_Products fragment_DelegateCollecting_order_products = new Fragment_Delegate_Collecting_Order_Products();
        fragment_DelegateCollecting_order_products.setArguments(bundle);
        return fragment_DelegateCollecting_order_products;
    }
    private void initView(View view)
    {
        choosedProductList = new ArrayList<>();
        orderProductList = new ArrayList<>();
        productsList = new ArrayList<>();
        choosedUploadProductsList = new ArrayList<>();
        uploadModel = new DelegateCollectingOrderUploadModel();

        activity = (OrderDetailsActivity) getActivity();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        image_back = view.findViewById(R.id.image_back);
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }
        ll_back = view.findViewById(R.id.ll_back);
        //tv_order_price = view.findViewById(R.id.tv_order_price);
        btn_collected = view.findViewById(R.id.btn_collected);
        btn_cancel = view.findViewById(R.id.btn_cancel);

        tv_no_product = view.findViewById(R.id.tv_no_product);

        recView = view.findViewById(R.id.recView);

        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);


        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        btn_collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrepareOrderToUpload();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.CancelOrder();
            }
        });
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            order = (OrdersModel.Order) bundle.getSerializable(TAG);
            productsList.addAll(order.getProducts());
            UpdateUI(order);
        }

    }



    private void UpdateUI(OrdersModel.Order order)
    {
        orderProductList.clear();
        orderProductList.addAll(order.getProducts());

        adapter = new DelegateCollectOrderAdapter(getActivity(),orderProductList,this);
        recView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback recyclerItemTouchHelper = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(recyclerItemTouchHelper).attachToRecyclerView(recView);

        if (order.getStatus()== Tags.status_delegate_accept_order)
        {
            Api.getService()
                    .updateOrderStatus(order.getId(),userModel.getToken(),Tags.status_delegate_collect_order)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful())
                            {
                                Log.e("success","true");
                            }else
                                {
                                    Log.e("code",response.code()+"");
                                }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                Log.e("error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }

    }

    public void updateOrderCost(OrdersModel.Products product,int status)
    {
        double product_cost = getProductCost(product);
        order_total_cost += product_cost;

       /* tv_order_price.setText(getString(R.string.products_cost3)+" "+order_total_cost+" "+getString(R.string.rsa));
        tv_order_price.setVisibility(View.VISIBLE);*/

        if (alternativeItemPos != -1)
        {
            choosedProductList.add(product);
            Products products = new Products(product.getId(),status);
            choosedUploadProductsList.add(products);

            this.orderProductList.remove(alternativeItemPos);
            this.adapter.notifyItemRemoved(alternativeItemPos);
            if (orderProductList.size()==0)
            {
                tv_no_product.setVisibility(View.VISIBLE);
                btn_collected.setVisibility(View.VISIBLE);
            }else
                {
                    tv_no_product.setVisibility(View.GONE);
                    btn_collected.setVisibility(View.GONE);
                }
            alternativeItemPos = -1;
        }

    }
    private void PrepareOrderToUpload()
    {
        uploadModel.setOrder_id(order.getId());
        uploadModel.setTotal_order_cost(order_total_cost);
        uploadModel.setProductsList(choosedUploadProductsList);
        uploadModel.setToken(userModel.getToken());

        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService()
                .uploadCollectedProducts(uploadModel)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            activity.dismissSnackBar();
                            dialog.dismiss();
                            btn_collected.setVisibility(View.GONE);
                            activity.DisplayFragment_Delegate_Current_Order_Details(true);

                        }else
                            {
                                Log.e("code",response.code()+"_");
                                try {
                                    Log.e("error_body",response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                                Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_LONG).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private double getProductCost(OrdersModel.Products product)
    {

        double cost;
        if (product.getFeature()!=null)
        {
            cost = product.getQuantity()*product.getFeature().getDiscount();

        }else
            {

                cost = product.getQuantity()*product.getProduct_price().getNet_price();

            }
            return cost;
    }
    public void setItemToShowAlternativeProducts(OrdersModel.Products alternative, int pos) {

        alternativeItemPos = pos;
        activity.UpdateBottomSheetUI(alternative);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position)
    {


        OrdersModel.Products product = orderProductList.get(position);
        Products product2 = new Products(product.getId(),Tags.product_not_changed);

        if (direction == ItemTouchHelper.LEFT)
        {
            updateOrderCost(product,Tags.product_not_changed);

            choosedUploadProductsList.add(product2);
            choosedProductList.add(product);

            this.orderProductList.remove(position);
            this.adapter.notifyItemRemoved(position);
            if (orderProductList.size()==0)
            {
                tv_no_product.setVisibility(View.VISIBLE);
                btn_collected.setVisibility(View.VISIBLE);
            }else
                {
                    tv_no_product.setVisibility(View.GONE);
                    btn_collected.setVisibility(View.GONE);
                }


        }else if (direction == ItemTouchHelper.RIGHT)
        {

            CreateDeleteAlertDialog(product,position);

        }
    }


    public void CreateDeleteAlertDialog(final OrdersModel.Products product, final int pos)
    {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete,null);
        FrameLayout fl_delete = view.findViewById(R.id.fl_delete);
        FrameLayout fl_cancel = view.findViewById(R.id.fl_cancel);


        fl_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                total_products_deleted+=1;

                Products products = new Products(product.getId(),Tags.product_deleted);
                choosedUploadProductsList.add(products);


                orderProductList.remove(pos);
                adapter.notifyItemRemoved(pos);
                if (orderProductList.size()==0)
                {
                    tv_no_product.setVisibility(View.VISIBLE);

                    if (total_products_deleted < order.getProducts().size())
                    {
                        btn_collected.setVisibility(View.VISIBLE);

                    }else
                        {
                            btn_cancel.setVisibility(View.VISIBLE);
                        }
                }else
                {
                    tv_no_product.setVisibility(View.GONE);
                    btn_collected.setVisibility(View.GONE);
                }
            }
        });
        fl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
}
