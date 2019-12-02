package com.appzone.freshcrops.activities_fragments.activity_order_details.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.freshcrops.models.OrdersModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Delegate_New_Order_Details extends Fragment {
    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private ImageView image_back,image_arrow;
    private LinearLayout ll_back,ll_notes;
    private String current_lang;
    private Button btn_show_products,btn_accept,btn_refuse;
    private TextView tv_current_time,tv_client_name,tv_address,tv_payment,tv_notes,tv_order_cost;
    private FrameLayout fl_map;
    private OrdersModel.Order order;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delegate_new_order_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Delegate_New_Order_Details newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);
        Fragment_Delegate_New_Order_Details fragment_delegate_new_order_details = new Fragment_Delegate_New_Order_Details();
        fragment_delegate_new_order_details.setArguments(bundle);
        return fragment_delegate_new_order_details;
    }
    private void initView(View view) {
        activity = (OrderDetailsActivity) getActivity();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


        image_back = view.findViewById(R.id.image_back);
        image_arrow = view.findViewById(R.id.image_arrow);
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
            image_arrow.setImageResource(R.drawable.arrow_right);

        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);
            image_arrow.setImageResource(R.drawable.arrow_left);

        }
        ll_back = view.findViewById(R.id.ll_back);
        ll_notes = view.findViewById(R.id.ll_notes);
        btn_show_products = view.findViewById(R.id.btn_show_products);
        btn_accept = view.findViewById(R.id.btn_accept);
        btn_refuse = view.findViewById(R.id.btn_refuse);

        tv_current_time = view.findViewById(R.id.tv_current_time);
        tv_client_name = view.findViewById(R.id.tv_client_name);
        tv_address = view.findViewById(R.id.tv_address);
        tv_payment = view.findViewById(R.id.tv_payment);
        tv_notes = view.findViewById(R.id.tv_notes);
        tv_order_cost = view.findViewById(R.id.tv_order_cost);
        fl_map = view.findViewById(R.id.fl_map);

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

        btn_show_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragment_Delegate_Show_Order_Products();
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.AcceptOrder();
            }
        });
        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.RefuseOrder();
            }
        });

        fl_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragment_Map_Order_Details();
            }
        });


    }




    private void UpdateUI(OrdersModel.Order order)
    {
        tv_current_time.setText(getCurrentTime());
        tv_address.setText(order.getAddress()+" "+order.getStreet());
        if (order.getClient()!=null)
        {
            tv_client_name.setText(order.getClient().getName());

        }
        tv_order_cost.setText(getString(R.string.total2)+" "+new DecimalFormat("##.##").format(order.getTotal())+" " +getString(R.string.rsa));
        if (order.getPayment_method()== 1)
        {
            tv_payment.setText("Cash نقدي");
        }else if (order.getPayment_method()== 2)
        {
            tv_payment.setText(R.string.mada);

        }else if (order.getPayment_method()== 3)
        {
            tv_payment.setText(R.string.visa);

        }
        if (order.getNote()!=null&&!TextUtils.isEmpty(order.getNote()))
        {
            ll_notes.setVisibility(View.VISIBLE);
            tv_notes.setText(order.getNote());

        }else
            {
                ll_notes.setVisibility(View.GONE);
            }


    }
    private String getCurrentTime()
    {
        Calendar calendar = Calendar.getInstance(new Locale(current_lang));
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",new Locale(current_lang));
        return dateFormat.format(calendar.getTime());
    }




}
