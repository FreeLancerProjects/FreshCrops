package com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.DelegateHomeActivity;

public class Fragment_Delegate_Notification extends Fragment {

    private DelegateHomeActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_notification, container, false);;
        initView(view);
        return view;
    }

    public static Fragment_Delegate_Notification newInstance() {

        return new Fragment_Delegate_Notification();
    }

    private void initView(View view) {
        activity = (DelegateHomeActivity) getActivity();
    }




}
