package com.appzone.freshcrops.activities_fragments.activity_order_details.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Fragment_Client_Order_Details extends Fragment {
    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private ImageView image_back,image_chat,image_call;
    private LinearLayout ll_back,ll_delegate_data_container;
    private CircleImageView image;
    private TextView tv_delegate_name,tv_rate;
    private SimpleRatingBar rateBar;
    private String current_lang;
    private TextView tv_not_approved;
    private RelativeLayout rl;
    private LinearLayout ll;
    private AppBarLayout app_bar;
    private Button btn_update;

    ////////////////////////////////
    private ImageView image1,image2,image3,image4,image5;
    private TextView tv1,tv2,tv3,tv4,tv5,tv_order_id;
    private View view1,view2,view3,view4;
    ////////////////////////////////
    private OrdersModel.Order order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_order_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Order_Details newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);
        Fragment_Client_Order_Details fragment_client_order_details = new Fragment_Client_Order_Details();
        fragment_client_order_details.setArguments(bundle);
        return fragment_client_order_details;
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
        tv_not_approved = view.findViewById(R.id.tv_not_approved);
        btn_update = view.findViewById(R.id.btn_update);

        /////////////////////////////////////////////////
        app_bar = view.findViewById(R.id.app_bar);
        rl = view.findViewById(R.id.rl);
        ll = view.findViewById(R.id.ll);

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalRang = appBarLayout.getTotalScrollRange();

                if ((totalRang+verticalOffset)<=30)
                {
                    ll.setVisibility(View.GONE);
                    rl.setVisibility(View.GONE);
                }else
                    {
                        ll.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.VISIBLE);
                    }
            }
        });

        /////////////////////////////////////////////////
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);
        image4 = view.findViewById(R.id.image4);
        image5 = view.findViewById(R.id.image5);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv_order_id = view.findViewById(R.id.tv_order_id);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        view4 = view.findViewById(R.id.view4);

        /////////////////////////////////////////////////
        image_chat = view.findViewById(R.id.image_chat);
        image_call = view.findViewById(R.id.image_call);
        ll_back = view.findViewById(R.id.ll_back);
        image = view.findViewById(R.id.image);
        tv_delegate_name = view.findViewById(R.id.tv_delegate_name);
        tv_rate = view.findViewById(R.id.tv_rate);
        rateBar = view.findViewById(R.id.rateBar);



        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            this.order = (OrdersModel.Order) bundle.getSerializable(TAG);
            UpdateUI(this.order);
        }

        image_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+"00966"+order.getDelegate().getPhone()));
                getActivity().startActivity(intent);
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        image_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToChatActivity();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.UpdateOrder(order);
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

                ll_delegate_data_container.setVisibility(View.VISIBLE);
                image_chat.setVisibility(View.VISIBLE);
                image_call.setVisibility(View.VISIBLE);
                tv_not_approved.setVisibility(View.GONE);
            }else
                {
                    ll_delegate_data_container.setVisibility(View.GONE);
                    image_chat.setVisibility(View.GONE);
                    image_call.setVisibility(View.GONE);
                    tv_not_approved.setVisibility(View.VISIBLE);
                    updateStepView(0);
                }

            updateStepView(order.getStatus());
            tv_order_id.setText(getString(R.string.order_number_is)+" #"+order.getId());

        }

    }



    public void updateStepView(int completePosition)
    {
        switch (completePosition)
        {
            case 0:
                ClearStepUI();
                break;
            case 1:
                image1.setBackgroundResource(R.drawable.step_green_circle);
                image1.setImageResource(R.drawable.step_green_true);
                view1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                btn_update.setVisibility(View.GONE);

                break;
            case 2:
                image1.setBackgroundResource(R.drawable.step_green_circle);
                image1.setImageResource(R.drawable.step_green_true);
                view1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image2.setBackgroundResource(R.drawable.step_green_circle);
                image2.setImageResource(R.drawable.step_green_list);
                view2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                btn_update.setVisibility(View.GONE);

                break;
            case 3:
                image1.setBackgroundResource(R.drawable.step_green_circle);
                image1.setImageResource(R.drawable.step_green_true);
                view1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image2.setBackgroundResource(R.drawable.step_green_circle);
                image2.setImageResource(R.drawable.step_green_list);
                view2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image3.setBackgroundResource(R.drawable.step_green_circle);
                image3.setImageResource(R.drawable.step_green_box);
                view3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv3.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                btn_update.setVisibility(View.GONE);

                break;
            case 4:
                image1.setBackgroundResource(R.drawable.step_green_circle);
                image1.setImageResource(R.drawable.step_green_true);
                view1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image2.setBackgroundResource(R.drawable.step_green_circle);
                image2.setImageResource(R.drawable.step_green_list);
                view2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image3.setBackgroundResource(R.drawable.step_green_circle);
                image3.setImageResource(R.drawable.step_green_box);
                view3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv3.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image4.setBackgroundResource(R.drawable.step_green_circle);
                image4.setImageResource(R.drawable.step_green_truck);
                view4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv4.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                btn_update.setVisibility(View.GONE);

                break;
            case 5:
                image1.setBackgroundResource(R.drawable.step_green_circle);
                image1.setImageResource(R.drawable.step_green_true);
                view1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image2.setBackgroundResource(R.drawable.step_green_circle);
                image2.setImageResource(R.drawable.step_green_list);
                view2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image3.setBackgroundResource(R.drawable.step_green_circle);
                image3.setImageResource(R.drawable.step_green_box);
                view3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv3.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image4.setBackgroundResource(R.drawable.step_green_circle);
                image4.setImageResource(R.drawable.step_green_truck);
                view4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
                tv4.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image5.setBackgroundResource(R.drawable.step_green_circle);
                image5.setImageResource(R.drawable.step_green_heart);
                tv5.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                btn_update.setVisibility(View.GONE);

                break;

        }
    }
    private void ClearStepUI()
    {
        image1.setBackgroundResource(R.drawable.step_gray_circle);
        image1.setImageResource(R.drawable.step_gray_true);
        view1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray3));
        tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

        image2.setBackgroundResource(R.drawable.step_gray_circle);
        image2.setImageResource(R.drawable.step_gray_list);
        view2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray3));
        tv2.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

        image3.setBackgroundResource(R.drawable.step_gray_circle);
        image3.setImageResource(R.drawable.step_gray_box);
        view3.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray3));
        tv3.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

        image4.setBackgroundResource(R.drawable.step_gray_circle);
        image4.setImageResource(R.drawable.step_gray_truck);
        view4.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray3));
        tv4.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

        image5.setBackgroundResource(R.drawable.step_gray_circle);
        image5.setImageResource(R.drawable.step_gray_heart);
        tv5.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));
        btn_update.setVisibility(View.VISIBLE);

    }


}
