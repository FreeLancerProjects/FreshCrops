package com.appzone.freshcrops.activities_fragments.activity_order_details.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.freshcrops.adapters.DelegateShowProductAdapter;
import com.appzone.freshcrops.models.OrdersModel;

import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Delegate_Order_Products_Details extends Fragment {

    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private DelegateShowProductAdapter adapter;
    private ImageView image_back;
    private LinearLayout ll_back;
    private String current_lang;
    private OrdersModel.Order order;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delegate_order_products_details,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Delegate_Order_Products_Details newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);

        Fragment_Delegate_Order_Products_Details fragment_DelegateCollecting_order_products = new Fragment_Delegate_Order_Products_Details();
        fragment_DelegateCollecting_order_products.setArguments(bundle);
        return fragment_DelegateCollecting_order_products;
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
        ll_back = view.findViewById(R.id.ll_back);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(25);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            order = (OrdersModel.Order) bundle.getSerializable(TAG);
            UpdateUI(order);
        }

    }

    private void UpdateUI(OrdersModel.Order order) {
        if (order != null)
        {
            adapter = new DelegateShowProductAdapter(getActivity(),order.getProducts());
            recView.setAdapter(adapter);
        }
    }
}
