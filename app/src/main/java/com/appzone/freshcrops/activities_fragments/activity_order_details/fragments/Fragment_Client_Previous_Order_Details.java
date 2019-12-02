package com.appzone.freshcrops.activities_fragments.activity_order_details.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.freshcrops.adapters.ClientPreviousDetailsProductAdapter;
import com.appzone.freshcrops.models.OrderItemListModel;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;
import com.appzone.freshcrops.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Client_Previous_Order_Details extends Fragment {
    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private ImageView image_back;
    private LinearLayout ll_back,ll_delegate_data_container;
    private CircleImageView image;
    private AppBarLayout app_bar;
    private TextView tv_delegate_name,tv_rate,tv_order_number,tv_order_cost,tv_payment,tv_notes;
    private SimpleRatingBar rateBar;

    private Button btn_request_again;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ClientPreviousDetailsProductAdapter adapter;
    private String current_lang;
    private OrdersModel.Order order;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_previous_order_details,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Client_Previous_Order_Details newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);
        Fragment_Client_Previous_Order_Details fragment_client_previous_order_details = new Fragment_Client_Previous_Order_Details();
        fragment_client_previous_order_details.setArguments(bundle);
        return fragment_client_previous_order_details;
    }
    private void initView(View view)
    {
        activity = (OrderDetailsActivity) getActivity();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


        image_back = view.findViewById(R.id.image_back);
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);

        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }

        ll_delegate_data_container = view.findViewById(R.id.ll_delegate_data_container);
        app_bar = view.findViewById(R.id.app_bar);
        //////////////////////////////////////////
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int appBarRange = app_bar.getTotalScrollRange();
                if ((appBarRange+verticalOffset)<=70)
                {
                    ll_delegate_data_container.setVisibility(View.GONE);
                }else
                    {
                        ll_delegate_data_container.setVisibility(View.VISIBLE);

                    }
            }
        });
        /////////////////////////////////////////
        ll_back = view.findViewById(R.id.ll_back);
        image = view.findViewById(R.id.image);
        tv_delegate_name = view.findViewById(R.id.tv_delegate_name);
        tv_rate = view.findViewById(R.id.tv_rate);
        rateBar = view.findViewById(R.id.rateBar);

        tv_order_number = view.findViewById(R.id.tv_order_number);
        tv_order_cost = view.findViewById(R.id.tv_order_cost);
        tv_payment = view.findViewById(R.id.tv_payment);
        tv_notes = view.findViewById(R.id.tv_notes);
        btn_request_again = view.findViewById(R.id.btn_request_again);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setDrawingCacheEnabled(true);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setItemViewCacheSize(25);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            this.order = (OrdersModel.Order) bundle.getSerializable(TAG);
            UpdateUI(this.order);
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });



        btn_request_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderProducts();
            }
        });


    }
    private void UpdateUI(OrdersModel.Order order)
    {
        if (order!=null)
        {
            if (order.getDelegate()!=null)
            {

                tv_delegate_name.setText(order.getDelegate().getName());
                Picasso.with(activity).load(Uri.parse(Tags.IMAGE_URL+order.getDelegate().getAvatar())).into(image);
                tv_rate.setText("("+order.getDelegate().getRate()+")");
                SimpleRatingBar.AnimationBuilder builder = rateBar.getAnimationBuilder();
                builder.setDuration(1500);
                builder.setRepeatCount(0);
                builder.setRatingTarget((float) order.getDelegate().getRate());
                builder.setInterpolator(new AccelerateInterpolator());
                builder.start();

            }

            tv_order_number.setText("#"+new DecimalFormat("#").format(order.getId()));
            tv_order_cost.setText(new DecimalFormat("##.##").format(getProductsCost(order.getProducts()))+" "+getString(R.string.rsa));

            if (order.getNote()!=null  || !TextUtils.isEmpty(order.getNote()))
            {
                tv_notes.setText(order.getNote());
            }else
                {
                    tv_notes.setText(R.string.no_notes);
                }

            if (order.getPayment_method()==Integer.parseInt(Tags.payment_cash))
            {
                tv_payment.setText("Cash نقدي");
            }else if (order.getPayment_method()==Integer.parseInt(Tags.payment_mada))
            {
                tv_payment.setText(getString(R.string.mada));

            }else if (order.getPayment_method()==Integer.parseInt(Tags.payment_visa))
            {
                tv_payment.setText(getString(R.string.visa));

            }

            adapter = new ClientPreviousDetailsProductAdapter(getActivity(),order.getProducts());
            recView.setAdapter(adapter);




        }

    }
    private double getProductsCost(List<OrdersModel.Products> productsList)
    {
        double cost=0.0;

        for (OrdersModel.Products products :productsList)
        {
            if (products.getFeature()!=null)
            {
                cost += (products.getQuantity()*products.getFeature().getDiscount());
            }else
            {
                cost += (products.getQuantity()*products.getProduct_price().getNet_price());

            }
        }
        return cost;
    }
    private void DisplayOrderCost(final OrdersModel.Order order, double tax, double order_cost, double delivery_cost)
    {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_resend_order_cost,null);

        TextView tv_tax  = view.findViewById(R.id.tv_tax);
        TextView tv_product_cost  = view.findViewById(R.id.tv_product_cost);
        TextView tv_delivery_cost  = view.findViewById(R.id.tv_delivery_cost);
        TextView tv_total  = view.findViewById(R.id.tv_total);

        tv_tax.setText(tax+"%");
        tv_product_cost.setText(order_cost+" "+getString(R.string.rsa));
        tv_delivery_cost.setText(delivery_cost+" "+getString(R.string.rsa));
        Button btn_send  = view.findViewById(R.id.btn_send);
        Button btn_cancel  = view.findViewById(R.id.btn_cancel);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void getOrderProducts() {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService()
                .getProductsToSendAgain(order.getId())
                .enqueue(new Callback<OrderItemListModel>() {
                    @Override
                    public void onResponse(Call<OrderItemListModel> call, Response<OrderItemListModel> response) {
                        if (response.isSuccessful())
                        {
                            activity.dismissSnackBar();
                            dialog.dismiss();
                            if (response.body()!=null)
                            {
                                activity.SendOrderAgain(response.body().getData());
                            }
                        }else
                            {
                                activity.dismissSnackBar();

                                dialog.dismiss();
                                Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<OrderItemListModel> call, Throwable t) {

                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }


}
