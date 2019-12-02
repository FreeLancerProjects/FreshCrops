package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.models.DeliveryCostModel;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Date_Time extends Fragment{

    private ImageView image_back,image_arrow;
    private LinearLayout ll_back;
    private TextView tv_12_2,tv_2_4,tv_4_6,tv_6_8,tv_8_10,tv_date_time_details;
    private FrameLayout fl_save;
    private String current_lang;
    private Date_Time_Listener date_time_listener;
    private HomeActivity activity;
    private int time_type=-1;
    private String delivery_cost="";
    private String current_date="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_time,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Date_Time newInstance()

    {
        return new Fragment_Date_Time();
    }


    private void initView(View view)
    {
        Paper.init(getActivity());

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

        tv_12_2 = view.findViewById(R.id.tv_12_2);
        tv_2_4 = view.findViewById(R.id.tv_2_4);
        tv_4_6 = view.findViewById(R.id.tv_4_6);
        tv_6_8 = view.findViewById(R.id.tv_6_8);
        tv_8_10 = view.findViewById(R.id.tv_8_10);

        tv_date_time_details = view.findViewById(R.id.tv_date_time_details);
        fl_save = view.findViewById(R.id.fl_save);

        fl_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (time_type!=-1)
                {
                    date_time_listener.onDate_Time_Set(time_type);

                }else
                    {
                        Toast.makeText(activity, R.string.sel_delv_time, Toast.LENGTH_LONG).show();
                    }
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             activity.Back();
            }
        });

        tv_12_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_type = 1;
                tv_12_2.setBackgroundResource(R.drawable.ll_from_to_selected);
                tv_2_4.setBackgroundResource(R.drawable.ll_from_to);
                tv_4_6.setBackgroundResource(R.drawable.ll_from_to);
                tv_6_8.setBackgroundResource(R.drawable.ll_from_to);
                tv_8_10.setBackgroundResource(R.drawable.ll_from_to);

                tv_12_2.setTextColor(ContextCompat.getColor(activity,R.color.white));
                tv_2_4.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_4_6.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_6_8.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_8_10.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));

                update_delivery_time_cost_ui(getString(R.string.from_12_00_to_2_00));
            }
        });

        tv_2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_type = 2;
                tv_12_2.setBackgroundResource(R.drawable.ll_from_to);
                tv_2_4.setBackgroundResource(R.drawable.ll_from_to_selected);
                tv_4_6.setBackgroundResource(R.drawable.ll_from_to);
                tv_6_8.setBackgroundResource(R.drawable.ll_from_to);
                tv_8_10.setBackgroundResource(R.drawable.ll_from_to);

                tv_12_2.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_2_4.setTextColor(ContextCompat.getColor(activity,R.color.white));
                tv_4_6.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_6_8.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_8_10.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));

                update_delivery_time_cost_ui(getString(R.string.from_2_00_to_4_00));


            }
        });

        tv_4_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_type = 3;
                tv_12_2.setBackgroundResource(R.drawable.ll_from_to);
                tv_2_4.setBackgroundResource(R.drawable.ll_from_to);
                tv_4_6.setBackgroundResource(R.drawable.ll_from_to_selected);
                tv_6_8.setBackgroundResource(R.drawable.ll_from_to);
                tv_8_10.setBackgroundResource(R.drawable.ll_from_to);

                tv_12_2.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_2_4.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_4_6.setTextColor(ContextCompat.getColor(activity,R.color.white));
                tv_6_8.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_8_10.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));

                update_delivery_time_cost_ui(getString(R.string.from_4_00_to_6_00));

            }
        });

        tv_6_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_type = 4;
                tv_12_2.setBackgroundResource(R.drawable.ll_from_to);
                tv_2_4.setBackgroundResource(R.drawable.ll_from_to);
                tv_4_6.setBackgroundResource(R.drawable.ll_from_to);
                tv_6_8.setBackgroundResource(R.drawable.ll_from_to_selected);
                tv_8_10.setBackgroundResource(R.drawable.ll_from_to);

                tv_12_2.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_2_4.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_4_6.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_6_8.setTextColor(ContextCompat.getColor(activity,R.color.white));
                tv_8_10.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));

                update_delivery_time_cost_ui(getString(R.string.from_6_00_to_8_00));

            }
        });

        tv_8_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_type = 5;
                tv_12_2.setBackgroundResource(R.drawable.ll_from_to);
                tv_2_4.setBackgroundResource(R.drawable.ll_from_to);
                tv_4_6.setBackgroundResource(R.drawable.ll_from_to);
                tv_6_8.setBackgroundResource(R.drawable.ll_from_to);
                tv_8_10.setBackgroundResource(R.drawable.ll_from_to_selected);

                tv_12_2.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_2_4.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_4_6.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_6_8.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                tv_8_10.setTextColor(ContextCompat.getColor(activity,R.color.white));

                update_delivery_time_cost_ui(getString(R.string.from_8_00_to_10_00));

            }
        });



        //getDeliveryCost();

    }
    private void getDeliveryCost()
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService()
                .getDeliveryCost()
                .enqueue(new Callback<DeliveryCostModel>() {
                    @Override
                    public void onResponse(Call<DeliveryCostModel> call, Response<DeliveryCostModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            activity.dismissSnackBar();
                            if (response.body()!=null && response.body().getDelivery()!=null)
                            {

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<DeliveryCostModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void update_delivery_time_cost_ui(String date )
    {
        tv_date_time_details.setText(date);

    }

    private String getCurrentDate()
    {
        Calendar calendar = Calendar.getInstance(new Locale(current_lang));
        long timeInMillis = calendar.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE - yyyy/MM/dd -",new Locale(current_lang));
        current_date = dateFormat.format(new Date(timeInMillis));
        return current_date;
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
         activity = (HomeActivity) context;
        date_time_listener = activity;
    }
    public interface Date_Time_Listener
    {
        void onDate_Time_Set(int time_type);
    }
}
