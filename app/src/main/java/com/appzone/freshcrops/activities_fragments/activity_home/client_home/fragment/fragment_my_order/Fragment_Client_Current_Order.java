package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_my_order;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.adapters.Client_Order_Adapter;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Client_Current_Order extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private List<OrdersModel.Order> orderList;
    private Client_Order_Adapter client_order_adapter;
    private LinearLayout ll_no_order;
    private UserModel userModel;
    private UserSingleTone userSingleTone;
    private HomeActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_previous_order,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Client_Current_Order newInstance()
    {
        return new Fragment_Client_Current_Order();
    }

    private void initView(View view) {

        activity = (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        orderList = new ArrayList<>();
        ll_no_order = view.findViewById(R.id.ll_no_order);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        client_order_adapter = new Client_Order_Adapter(getActivity(),orderList,this);
        recView.setAdapter(client_order_adapter);
        getOrders();
    }
    public void getOrders()
    {
        Api.getService()
                .getOrders(userModel.getToken(), Tags.order_current)
                .enqueue(new Callback<OrdersModel>() {
                    @Override
                    public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            activity.dismissSnackBar();
                            orderList.clear();
                            if (response.body()!=null&&response.body().getData()!=null)
                            {
                                orderList.addAll(response.body().getData());
                                if (orderList.size()>0)
                                {
                                    ll_no_order.setVisibility(View.GONE);
                                    client_order_adapter.notifyDataSetChanged();

                                }else
                                {
                                    ll_no_order.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrdersModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());

                        }catch (Exception e){}
                    }
                });
    }


    public void setItemData(OrdersModel.Order order, int pos) {
        activity.NavigateToOrderDetailsActivity(order,Tags.order_current);

    }
}
