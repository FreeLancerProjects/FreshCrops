package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.tags.Tags;

import java.text.DecimalFormat;

public class Fragment_Order_Finish_Congratulation extends Fragment {
    public static final String TAG = "order";
    private TextView tv_order_id;
    private ImageView image_done;
    private FrameLayout fl_follow_order,fl_back,fl_update;
    private HomeActivity activity;
    private OrdersModel.Order order;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_finished_congratulation,container,false);
        initView(view);
        return view;
    }


    public static Fragment_Order_Finish_Congratulation newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);
        Fragment_Order_Finish_Congratulation fragment_order_finish_congratulation = new Fragment_Order_Finish_Congratulation();
        fragment_order_finish_congratulation.setArguments(bundle);
        return fragment_order_finish_congratulation;
    }

    private void initView(View view) {
        activity= (HomeActivity) getActivity();

        tv_order_id = view.findViewById(R.id.tv_order_id);
        fl_follow_order = view.findViewById(R.id.fl_follow_order);
        fl_back = view.findViewById(R.id.fl_back);
        fl_update = view.findViewById(R.id.fl_update);

        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.image_congtaulation);
        image_done = view.findViewById(R.id.image_done);
        image_done.clearAnimation();
        image_done.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                image_done.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            order = (OrdersModel.Order) bundle.getSerializable(TAG);
            UpdateUI(order);
        }

        fl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentHome();
            }
        });

        fl_follow_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.NavigateToOrderDetailsActivity(order, Tags.order_new);
            }
        });

        fl_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getProductsForUpdateOrder(order);
            }
        });




    }

    private void UpdateUI(OrdersModel.Order order) {
        tv_order_id.setText(getString(R.string.your_order_successfully_confirmed_your_order_number_is)+" #"+new DecimalFormat("#").format(order.getId()));

    }
}
