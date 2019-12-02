package com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment.fragment_delegate_orders;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.DelegateHomeActivity;
import com.appzone.freshcrops.adapters.OrderViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Delegate_Orders extends Fragment {
    private DelegateHomeActivity activity;
    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private OrderViewPagerAdapter orderViewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delegate_orders,container,false);
        initView(view);
        return view;

    }
    public static Fragment_Delegate_Orders newInstance()
    {
        return new Fragment_Delegate_Orders();
    }

    private void initView(View view) {
        activity = (DelegateHomeActivity) getActivity();
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);

        tab.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(3);

        fragmentList.add(Fragment_Delegate_New_Order.newInstance());
        fragmentList.add(Fragment_Delegate_Current_Order.newInstance());
        fragmentList.add(Fragment_Delegate_Previous_Order.newInstance());

        titleList.add(getString(R.string.new_orders));
        titleList.add(getString(R.string.cur_order));
        titleList.add(getString(R.string.prv_order));

        orderViewPagerAdapter = new OrderViewPagerAdapter(getChildFragmentManager());
        orderViewPagerAdapter.AddFragments(fragmentList);
        orderViewPagerAdapter.AddTitle(titleList);

        pager.setAdapter(orderViewPagerAdapter);

    }

    public void RefreshFragment()
    {
        Fragment_Delegate_New_Order fragment_delegate_new_order = (Fragment_Delegate_New_Order) orderViewPagerAdapter.getItem(0);
        fragment_delegate_new_order.getOrders();

        Fragment_Delegate_Current_Order fragment_delegate_current_order = (Fragment_Delegate_Current_Order) orderViewPagerAdapter.getItem(1);
        fragment_delegate_current_order.getOrders();

        Fragment_Delegate_Previous_Order fragment_delegate_previous_order = (Fragment_Delegate_Previous_Order) orderViewPagerAdapter.getItem(2);
        fragment_delegate_previous_order.getOrders();

    }



    public void setPage(int index)
    {
        pager.setCurrentItem(index);
    }

}
