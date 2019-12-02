package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.models.CouponModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;

import java.text.DecimalFormat;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Payment_Confirmation extends Fragment {

    private ImageView image_arrow_back,image_arrow_continue;
    private TextView tv_total,tv_product_cost,tv_delivery_cost,tv_coupon_cost,tv_point,tv_point_cost,tv_coupon_value;
    private CardView card_visa,card_cash,card_mada;
    private EditText edt_card_number,edt_expire,edt_password;
    private LinearLayout ll_coupon,ll_point;
    private FrameLayout fl_continue,fl_back;
    private CardView card_coupon,card_point,card_nothing;
    private Button btn_coupon,btn_point,btn_nothing;
    private HomeActivity activity;
    private double total_order_cost=0.0,coupon_value=0.0,delivery_cost=0.0;
    private String payment_method="";
    private CouponModel couponModel = null;
    private int discount_by_use=0;
    private double discount_point=0.0;
    private double discount_coupon_points_cost =0.0;
    private double final_total_order_price=0.0;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_confirmation,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Payment_Confirmation newInstance()
    {
        return new Fragment_Payment_Confirmation();
    }
    private void initView(View view)
    {

        activity = (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        this.total_order_cost = activity.total_order_cost_after_tax;
        this.payment_method = activity.payment_method;
        this.couponModel = activity.couponModel;
        if (couponModel!=null&&couponModel.getCoupon_codes()!=null)
        {
            coupon_value = couponModel.getCoupon_value();
        }
        try {

            if (!TextUtils.isEmpty(activity.delivery_cost))
            {
                this.delivery_cost = Double.parseDouble(activity.delivery_cost);

            }
        }catch (NumberFormatException e)
        {
            Log.e("error",e.getMessage()+"_");
        }
        image_arrow_back = view.findViewById(R.id.image_arrow_back);
        image_arrow_continue = view.findViewById(R.id.image_arrow_continue);

        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_arrow_back.setImageResource(R.drawable.arrow_right);
            image_arrow_continue.setImageResource(R.drawable.arrow_left);
        }else
        {
            image_arrow_back.setImageResource(R.drawable.arrow_left);
            image_arrow_continue.setImageResource(R.drawable.arrow_right);
        }

        card_visa = view.findViewById(R.id.card_visa);
        card_cash = view.findViewById(R.id.card_cash);
        card_mada = view.findViewById(R.id.card_mada);

        edt_card_number = view.findViewById(R.id.edt_card_number);
        edt_expire = view.findViewById(R.id.edt_expire);
        edt_password = view.findViewById(R.id.edt_password);

        card_coupon = view.findViewById(R.id.card_coupon);
        card_point = view.findViewById(R.id.card_point);
        card_nothing = view.findViewById(R.id.card_nothing);

        btn_coupon = view.findViewById(R.id.btn_coupon);
        btn_point = view.findViewById(R.id.btn_point);
        btn_nothing = view.findViewById(R.id.btn_nothing);

        tv_point = view.findViewById(R.id.tv_point);
        tv_point_cost = view.findViewById(R.id.tv_point_cost);
        tv_coupon_value = view.findViewById(R.id.tv_coupon_value);

        tv_delivery_cost = view.findViewById(R.id.tv_delivery_cost);
        tv_product_cost = view.findViewById(R.id.tv_product_cost);
        tv_total = view.findViewById(R.id.tv_total);
        tv_coupon_cost = view.findViewById(R.id.tv_coupon_cost);
        ll_coupon = view.findViewById(R.id.ll_coupon);
        ll_point = view.findViewById(R.id.ll_point);

        fl_continue = view.findViewById(R.id.fl_continue);
        fl_back = view.findViewById(R.id.fl_back);


        fl_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.UploadOrder(discount_by_use,discount_point, discount_coupon_points_cost,final_total_order_price);
            }
        });

        fl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentDelivery_Address(activity.total_order_cost_after_tax);
            }
        });

        btn_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCouponUI(coupon_value);
            }
        });

        btn_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePointUI();
            }
        });
        btn_nothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNothingUI();
            }
        });

        updateUI();


    }
    private void updateUI()
    {
        if (delivery_cost == 0.0)
        {
            tv_delivery_cost.setText(R.string.del_for_free);
        }
        if (payment_method.equals(Tags.payment_cash))
        {
            card_cash.setVisibility(View.VISIBLE);
            card_visa.setVisibility(View.GONE);
            card_mada.setVisibility(View.GONE);

        }else if (payment_method.equals(Tags.payment_visa))
        {
            card_visa.setVisibility(View.VISIBLE);
            card_cash.setVisibility(View.GONE);
            card_mada.setVisibility(View.GONE);

        }else if (payment_method.equals(Tags.payment_mada))
        {
            card_visa.setVisibility(View.GONE);
            card_cash.setVisibility(View.GONE);
            card_mada.setVisibility(View.VISIBLE);
        }


        if (couponModel!=null&&couponModel.getCoupon_codes()!=null)
        {
            card_coupon.setVisibility(View.VISIBLE);
            card_point.setVisibility(View.GONE);
            card_nothing.setVisibility(View.GONE);
            UpdateCouponUI(coupon_value);
        }else
            {
                if (userModel.getUser().getPoints()>0)
                {
                    card_coupon.setVisibility(View.GONE);
                    card_point.setVisibility(View.VISIBLE);
                    card_nothing.setVisibility(View.VISIBLE);
                    updateNothingUI();
                }else
                    {
                        card_coupon.setVisibility(View.GONE);
                        card_point.setVisibility(View.GONE);
                        card_nothing.setVisibility(View.GONE);
                        updateNothingUI();
                    }
            }

        /*updateNothingUI();

        if (userModel.getUser().getCoupon()==0 && userModel.getUser().getPoints()>0.0)
        {
            card_coupon.setVisibility(View.VISIBLE);
            card_point.setVisibility(View.VISIBLE);
        }else if (userModel.getUser().getCoupon()==0 && userModel.getUser().getPoints()==0.0)
        {
            card_coupon.setVisibility(View.VISIBLE);
            card_point.setVisibility(View.GONE);
        }
        else if (userModel.getUser().getCoupon()==1 && userModel.getUser().getPoints()>0)
        {
            card_coupon.setVisibility(View.GONE);
            card_point.setVisibility(View.VISIBLE);
            updateNothingUI();

        }else if (userModel.getUser().getCoupon()==1&&userModel.getUser().getPoints()==0)
        {
            card_point.setVisibility(View.GONE);
            card_coupon.setVisibility(View.GONE);


        }*/





    }

    private void updateNothingUI() {
        discount_by_use = 0;
        card_nothing.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
        card_coupon.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray2));
        card_point.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray2));
        ll_coupon.setVisibility(View.GONE);
        ll_point.setVisibility(View.GONE);

        if (delivery_cost == 0.0)
        {
            tv_delivery_cost.setText(R.string.del_for_free);

        }else
            {
                tv_delivery_cost.setText(new DecimalFormat("##.##").format(delivery_cost)+" "+getString(R.string.rsa));

            }
        tv_product_cost.setText(new DecimalFormat("##.##").format(total_order_cost)+" "+getString(R.string.rsa));
        final_total_order_price = delivery_cost+total_order_cost;
        tv_total.setText(new DecimalFormat("##.##").format(final_total_order_price)+" "+getString(R.string.rsa));

    }

    private void UpdateCouponUI(double coupon_value)
    {
        discount_by_use = Tags.discount_by_use_coupon;

        ll_coupon.setVisibility(View.VISIBLE);
        ll_point.setVisibility(View.GONE);
        tv_coupon_value.setText(new DecimalFormat("##.##").format(coupon_value)+" %");
        double order_cost_after_discount_coupon = (coupon_value/100)*total_order_cost;
        discount_coupon_points_cost = order_cost_after_discount_coupon;

        tv_coupon_cost.setText(new DecimalFormat("##.##").format(order_cost_after_discount_coupon)+" "+getString(R.string.rsa));
        card_coupon.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));
        card_point.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray2));
        card_nothing.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray2));


        if (delivery_cost == 0.0)
        {
            tv_delivery_cost.setText(R.string.del_for_free);

        }else
        {
            tv_delivery_cost.setText(new DecimalFormat("##.##").format(delivery_cost)+" "+getString(R.string.rsa));

        }

        tv_product_cost.setText(new DecimalFormat("##.##").format(total_order_cost)+" "+getString(R.string.rsa));
        double order_cost_after_discount = total_order_cost-order_cost_after_discount_coupon;
        final_total_order_price = order_cost_after_discount+delivery_cost;

        tv_total.setText(new DecimalFormat("##.##").format(final_total_order_price)+" "+getString(R.string.rsa));

    }
    private void UpdatePointUI()
    {
        discount_by_use = Tags.discount_by_use_points;

        ll_point.setVisibility(View.VISIBLE);
        ll_coupon.setVisibility(View.GONE);
        double point_cost = userModel.getUser().getPoints()*couponModel.getClient_point_cost();
        tv_point_cost.setText(new DecimalFormat("##.##").format(point_cost)+" "+getString(R.string.rsa));

        card_nothing.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray2));
        card_coupon.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray2));
        card_point.setCardBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_text));

        double delivery_order_cost = delivery_cost+total_order_cost;
        if (point_cost>=delivery_order_cost)
        {
            discount_point = Math.round(delivery_order_cost/couponModel.getClient_point_cost());
            double point_remain = userModel.getUser().getPoints()-discount_point;
            tv_point.setText(String.valueOf(point_remain));
            double remain_point_cost = point_remain * couponModel.getClient_point_cost();

            discount_coupon_points_cost = discount_point * couponModel.getClient_point_cost();

            tv_point_cost.setText(new DecimalFormat("##.##").format(remain_point_cost)+" "+getString(R.string.rsa));

            tv_product_cost.setText(new DecimalFormat("##.##").format(total_order_cost)+" "+getString(R.string.rsa));

            if (delivery_cost == 0.0)
            {
                tv_delivery_cost.setText(R.string.del_for_free);

            }else
            {
                tv_delivery_cost.setText(new DecimalFormat("##.##").format(delivery_cost)+" "+getString(R.string.rsa));

            }
            tv_total.setText("0"+getString(R.string.rsa));
            final_total_order_price = 0;

        }else
            {
                discount_point = userModel.getUser().getPoints();
                discount_coupon_points_cost = point_cost;

                if (delivery_cost == 0.0)
                {
                    tv_delivery_cost.setText(R.string.del_for_free);

                }else
                {
                    tv_delivery_cost.setText(new DecimalFormat("##.##").format(delivery_cost)+" "+getString(R.string.rsa));

                }

                tv_product_cost.setText(new DecimalFormat("##.##").format(total_order_cost)+" "+getString(R.string.rsa));

                double order_cost_after_discount = total_order_cost-point_cost;
                final_total_order_price = order_cost_after_discount+delivery_cost;

                tv_total.setText(new DecimalFormat("##.##").format(final_total_order_price)+" "+getString(R.string.rsa));

            }





    }


}
