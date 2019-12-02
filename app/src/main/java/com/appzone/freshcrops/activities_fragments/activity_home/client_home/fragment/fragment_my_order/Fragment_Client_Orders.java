package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_my_order;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.adapters.OrderViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Client_Orders extends Fragment {

    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private OrderViewPagerAdapter orderViewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_order,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Orders newInstance()
    {
        return new Fragment_Client_Orders();
    }

    private void initView(View view)
    {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(3);
        fragmentList.add(Fragment_Client_New_Order.newInstance());
        fragmentList.add(Fragment_Client_Current_Order.newInstance());
        fragmentList.add(Fragment_Client_Previous_Order.newInstance());

        titleList.add(getString(R.string.new_orders));
        titleList.add(getString(R.string.cur_order));
        titleList.add(getString(R.string.prv_order));

        orderViewPagerAdapter = new OrderViewPagerAdapter(getChildFragmentManager());
        orderViewPagerAdapter.AddFragments(fragmentList);
        orderViewPagerAdapter.AddTitle(titleList);

        pager.setAdapter(orderViewPagerAdapter);


    }

    public void RefreshFragment_Previous_New_Order()
    {
        Fragment_Client_Previous_Order fragment_client_previous_order = (Fragment_Client_Previous_Order) orderViewPagerAdapter.getItem(2);
        fragment_client_previous_order.UpdateItem_Adapter();
        Fragment_Client_New_Order fragment_client_new_order = (Fragment_Client_New_Order) orderViewPagerAdapter.getItem(0);
        fragment_client_new_order.getOrders();
    }

    public void RefreshFragment_Current_Previous_Order()
    {
        Fragment_Client_Previous_Order fragment_client_previous_order = (Fragment_Client_Previous_Order) orderViewPagerAdapter.getItem(2);
        fragment_client_previous_order.getOrders();

        Fragment_Client_Current_Order fragment_client_current_order = (Fragment_Client_Current_Order) orderViewPagerAdapter.getItem(1);
        fragment_client_current_order.getOrders();
    }

    public void RefreshFragment_New_Current_Order()
    {

        Fragment_Client_New_Order fragment_client_new_order = (Fragment_Client_New_Order) orderViewPagerAdapter.getItem(0);
        fragment_client_new_order.getOrders();
        Fragment_Client_Current_Order fragment_client_current_order = (Fragment_Client_Current_Order) orderViewPagerAdapter.getItem(1);
        fragment_client_current_order.getOrders();
    }

    public void setPage(int index)
    {
        pager.setCurrentItem(index);
    }
}
