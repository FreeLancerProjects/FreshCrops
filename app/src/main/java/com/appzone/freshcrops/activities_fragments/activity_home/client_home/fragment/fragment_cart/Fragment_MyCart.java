package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_MyCart extends Fragment{

    private ImageView image_basket,image_car,image_payment,image_arrow1,image_arrow2;
    private TextView tv_basket,tv_car,tv_payment;
    private HomeActivity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_cart,container,false);
        initView(view);
        return view;
    }

    public static Fragment_MyCart newInstance()
    {
        return new Fragment_MyCart();
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();


        image_arrow1 = view.findViewById(R.id.image_arrow1);
        image_arrow2 = view.findViewById(R.id.image_arrow2);

        Paper.init(getActivity());
        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_arrow1.setImageResource(R.drawable.arrow_gray_right);
            image_arrow2.setImageResource(R.drawable.arrow_gray_right);

        }else
        {
            image_arrow1.setImageResource(R.drawable.arrow_gray_left);
            image_arrow2.setImageResource(R.drawable.arrow_gray_left);
        }


        image_basket = view.findViewById(R.id.image_basket);
        image_car = view.findViewById(R.id.image_car);
        image_payment = view.findViewById(R.id.image_payment);
        tv_basket = view.findViewById(R.id.tv_basket);
        tv_car = view.findViewById(R.id.tv_car);
        tv_payment = view.findViewById(R.id.tv_payment);



    }

    public void UpdateBasketUI()
    {
        image_basket.setImageResource(R.drawable.basket2);
        image_basket.setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary));
        tv_basket.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
        clearCar();

    }

    public void UpdateCarUI()
    {
        image_car.setImageResource(R.drawable.car_address_selected);
        image_car.setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary));

        tv_car.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
        clearConfirm();
    }

    public void UpdatePaymentUI()
    {
        image_payment.setImageResource(R.drawable.payment_selected);
        image_payment.setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary));

        tv_payment.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
    }



    public void clearBasket()
    {
        image_basket.setImageResource(R.drawable.basket);
        tv_basket.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

    }

    private void clearCar()
    {
        image_car.setImageResource(R.drawable.car_address_unselected);
        tv_car.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

    }

    private void clearConfirm()
    {
        image_payment.setImageResource(R.drawable.payment_unselected);
        tv_payment.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));


    }




}
