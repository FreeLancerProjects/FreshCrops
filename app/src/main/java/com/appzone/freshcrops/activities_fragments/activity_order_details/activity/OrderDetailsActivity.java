package com.appzone.freshcrops.activities_fragments.activity_order_details.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_chat.ChatActivity;
import com.appzone.freshcrops.activities_fragments.activity_order_details.fragments.Fragment_Client_Order_Details;
import com.appzone.freshcrops.activities_fragments.activity_order_details.fragments.Fragment_Client_Previous_Order_Details;
import com.appzone.freshcrops.activities_fragments.activity_order_details.fragments.Fragment_Delegate_Collecting_Order_Products;
import com.appzone.freshcrops.activities_fragments.activity_order_details.fragments.Fragment_Delegate_Current_Order_Details;
import com.appzone.freshcrops.activities_fragments.activity_order_details.fragments.Fragment_Delegate_New_Order_Details;
import com.appzone.freshcrops.activities_fragments.activity_order_details.fragments.Fragment_Delegate_Order_Products_Details;
import com.appzone.freshcrops.activities_fragments.activity_order_details.fragments.Fragment_Map_Order_Details;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.models.OrderItem;
import com.appzone.freshcrops.models.OrderStatusModel;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.models.UserChatModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment_Client_Order_Details fragment_client_order_details;
    private Fragment_Client_Previous_Order_Details fragment_client_previous_order_details;

    private Fragment_Delegate_New_Order_Details fragment_delegate_new_order_details;
    private Fragment_Delegate_Current_Order_Details fragment_delegate_Current_order_details;
    private Fragment_Map_Order_Details fragment_map_order_details;
    private Fragment_Delegate_Collecting_Order_Products fragment_DelegateCollecting_order_products;
    private Fragment_Delegate_Order_Products_Details fragment_delegate_order_products_details;
    private OrdersModel.Order order;
    private String order_type ="",user_type;
    private String current_lang;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private Snackbar snackbar;
    private View root;
    //////////////////////////////////////
    private View fl_root;
    private ImageView image;
    private TextView tv_name,tv_price,tv_amount;
    private Button btn_add,btn_cancel;
    private BottomSheetBehavior behavior;
    private OrdersModel.Products product;

    private boolean isOrderStatusChanged=false;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        this.user_type = userModel.getUser().getRole();
        Paper.init(this);
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(this,current_lang);
        fragmentManager = getSupportFragmentManager();
        root = findViewById(R.id.root);
        ///////////////////////////////////
        fl_root = findViewById(R.id.fl_root);
        image = findViewById(R.id.image);
        tv_name = findViewById(R.id.tv_name);
        tv_price = findViewById(R.id.tv_price);
        tv_amount = findViewById(R.id.tv_amount);
        btn_add = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment_DelegateCollecting_order_products!=null && fragment_DelegateCollecting_order_products.isAdded())
                {
                    fragment_DelegateCollecting_order_products.updateOrderCost(product, Tags.product_alternative);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        behavior = BottomSheetBehavior.from(fl_root);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateOrderStatus(final OrderStatusModel orderStatusModel)
    {
        if (fragment_client_order_details!=null && fragment_client_order_details.isAdded())
        {
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_client_order_details.updateStepView(orderStatusModel.getOrder_status());
                        }
                    },1000);
        }
    }

    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            this.order = (OrdersModel.Order) intent.getSerializableExtra("order");
            this.order_type = intent.getStringExtra("order_type");

            UpdateUI(order, order_type);
        }
    }
    private void UpdateUI(OrdersModel.Order order, String order_type)
    {
        if (user_type.equals(Tags.user_client))
        {
            if (order_type.equals(Tags.order_old))
            {
                DisplayFragment_Client_Previous_Order_Details(order);

            }else if (order_type.equals(Tags.order_new)||order_type.equals(Tags.order_current))
                {
                    DisplayFragment_Client_Order_Details(order);

                }
        }else if (user_type.equals(Tags.user_delegate))
            {
                if (order_type.equals(Tags.order_new))
                {
                    DisplayFragment_Delegate_New_Order_Details(order);
                }else if (order_type.equals(Tags.order_current))
                {
                    DisplayFragment_Delegate_Current_Order_Details(false);
                }
            }
    }
    public void UpdateBottomSheetUI(OrdersModel.Products product)
    {
        if (product!=null)
        {
            this.product = product;

            if (product.getProduct().getImage().size()>0)
            {
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+product.getProduct().getImage().get(0))).fit().into(image);

            }

            if (current_lang.equals("ar"))
            {
                tv_name.setText(product.getProduct().getName_ar()+" "+product.getProduct_price().getSize_ar());
            }else
                {
                    tv_name.setText(product.getProduct().getName_en()+" "+product.getProduct_price().getSize_en());

                }

                tv_amount.setText(product.getQuantity()+"");
            double total_item_cost;

            if (product.getFeature()!=null)
            {
                total_item_cost = product.getFeature().getDiscount();
            }else
                {
                    total_item_cost = product.getProduct_price().getNet_price();

                }

                tv_price.setText(new DecimalFormat("##.##").format(total_item_cost)+" "+getString(R.string.rsa));

            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }



    }
    public void DisplayFragment_Client_Order_Details(OrdersModel.Order order)
    {
        fragment_client_order_details = Fragment_Client_Order_Details.newInstance(order);
        fragmentManager.beginTransaction().replace(R.id.fragment_order_details_container,fragment_client_order_details).commit();

    }
    public void DisplayFragment_Client_Previous_Order_Details(OrdersModel.Order order)
    {
        fragment_client_previous_order_details = Fragment_Client_Previous_Order_Details.newInstance(order);
        fragmentManager.beginTransaction().replace(R.id.fragment_order_details_container,fragment_client_previous_order_details).commit();

    }
    public void DisplayFragment_Delegate_New_Order_Details(OrdersModel.Order order)
    {
        if (fragment_map_order_details!=null && fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_map_order_details).commit();
        }
        if (fragment_delegate_order_products_details!=null && fragment_delegate_order_products_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_order_products_details).commit();
        }
        if (fragment_DelegateCollecting_order_products !=null && fragment_DelegateCollecting_order_products.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_DelegateCollecting_order_products).commit();
        }

        if (fragment_delegate_new_order_details==null)
        {
            fragment_delegate_new_order_details = Fragment_Delegate_New_Order_Details.newInstance(order);
        }

        if (!fragment_delegate_new_order_details.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fragment_order_details_container,fragment_delegate_new_order_details,"fragment_delegate_new_order_details").addToBackStack("fragment_delegate_new_order_details").commit();
        }else
            {
                fragmentManager.beginTransaction().show(fragment_delegate_new_order_details).commit();
            }


    }
    public void DisplayFragment_Delegate_Current_Order_Details(boolean isOrderCollected)
    {
        this.isOrderStatusChanged = isOrderCollected;
        if (fragment_map_order_details!=null && fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_map_order_details).commit();
        }

        if (fragment_DelegateCollecting_order_products!=null && fragment_DelegateCollecting_order_products.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_DelegateCollecting_order_products).commit();
        }


        if (fragment_delegate_Current_order_details ==null)
        {
            fragment_delegate_Current_order_details = Fragment_Delegate_Current_Order_Details.newInstance(order);
        }


        if (!fragment_delegate_Current_order_details.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fragment_order_details_container, fragment_delegate_Current_order_details,"fragment_delegate_Current_order_details").addToBackStack("fragment_delegate_Current_order_details").commit();
            if (isOrderCollected)
            {
                if (fragment_DelegateCollecting_order_products !=null && fragment_DelegateCollecting_order_products.isAdded())
                {
                    fragment_DelegateCollecting_order_products = null;
                    fragmentManager.popBackStack("fragment_DelegateCollecting_order_products",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else
                    {
                        fragmentManager.beginTransaction().hide(fragment_DelegateCollecting_order_products).commit();
                    }
                fragment_delegate_Current_order_details.setOrderCollected();
            }
        }else
        {
            fragmentManager.beginTransaction().show(fragment_delegate_Current_order_details).commit();
            if (isOrderCollected)
            {
                if (fragment_DelegateCollecting_order_products !=null && fragment_DelegateCollecting_order_products.isAdded())
                {
                    fragment_DelegateCollecting_order_products = null;
                    fragmentManager.popBackStack("fragment_DelegateCollecting_order_products",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }else
                {
                    fragmentManager.beginTransaction().hide(fragment_DelegateCollecting_order_products).commit();
                }
                fragment_delegate_Current_order_details.setOrderCollected();
            }
        }


    }
    public void DisplayFragment_Delegate_Show_Order_Products()
    {
        if (fragment_map_order_details!=null && fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_map_order_details).commit();
        }
        if (fragment_delegate_new_order_details!=null && fragment_delegate_new_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_new_order_details).commit();
        }
        if (fragment_delegate_Current_order_details !=null && fragment_delegate_Current_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_Current_order_details).commit();
        }

        if (fragment_delegate_order_products_details == null)
        {
            fragment_delegate_order_products_details = Fragment_Delegate_Order_Products_Details.newInstance(order);
        }

        if (!fragment_delegate_order_products_details.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fragment_order_details_container, fragment_delegate_order_products_details,"fragment_delegate_order_products_details").addToBackStack("fragment_delegate_order_products_details").commit();
        }else
        {
            fragmentManager.beginTransaction().show(fragment_delegate_order_products_details).commit();
        }

    }
    public void DisplayFragment_Delegate_Collecting_Order_Products()
    {
        if (fragment_map_order_details!=null && fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_map_order_details).commit();
        }

        if (fragment_delegate_Current_order_details !=null && fragment_delegate_Current_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_Current_order_details).commit();
        }

        if (fragment_DelegateCollecting_order_products == null)
        {
            fragment_DelegateCollecting_order_products = Fragment_Delegate_Collecting_Order_Products.newInstance(order);
        }

        if (!fragment_DelegateCollecting_order_products.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fragment_order_details_container, fragment_DelegateCollecting_order_products,"fragment_DelegateCollecting_order_products").addToBackStack("fragment_DelegateCollecting_order_products").commit();
        }else
        {
            fragmentManager.beginTransaction().show(fragment_DelegateCollecting_order_products).commit();
        }

    }
    public void DisplayFragment_Map_Order_Details()
    {
        if (fragment_delegate_new_order_details!=null && fragment_delegate_new_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_new_order_details).commit();
        }
        if (fragment_delegate_Current_order_details !=null && fragment_delegate_Current_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_Current_order_details).commit();
        }

        if (fragment_map_order_details==null)
        {
            fragment_map_order_details = Fragment_Map_Order_Details.newInstance(order.getLat(),order.getLng());

        }

        if (fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_map_order_details).commit();

        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_order_details_container,fragment_map_order_details,"fragment_map_order_details").addToBackStack("fragment_map_order_details").commit();

            }

    }

    public void setDeliveryStarted()
    {
        this.isOrderStatusChanged = true;
    }
    public void setDeliveryFinished()
    {
        Intent intent =  getIntent();
        setResult(RESULT_OK,intent);
        finish();    }
    public void AcceptOrder()
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .Accept_Refuse_order(order.getId(),userModel.getToken(),Tags.order_accepted)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dismissSnackBar();
                            dialog.dismiss();
                            Toast.makeText(OrderDetailsActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            intent.putExtra("status",1);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else
                            {
                                if (response.code()==404)
                                {
                                    CreateAlertDialogString(getString(R.string.the_order_accepted_by_another_delegate));
                                }else
                                    {
                                        dismissSnackBar();
                                        dialog.dismiss();
                                        Toast.makeText(OrderDetailsActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                                    }


                            }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    public void RefuseOrder()
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .Accept_Refuse_order(order.getId(),userModel.getToken(),Tags.order_refused)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dismissSnackBar();
                            dialog.dismiss();
                            Toast.makeText(OrderDetailsActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            intent.putExtra("status",0);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else
                        {
                            try {
                                Log.e("error",response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dismissSnackBar();
                            dialog.dismiss();
                            Toast.makeText(OrderDetailsActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });



    }

    public void CancelOrder()
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .updateOrderStatus(order.getId(),userModel.getToken(),Tags.status_cancel_order)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dismissSnackBar();
                            dialog.dismiss();
                            Toast.makeText(OrderDetailsActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            setResult(RESULT_OK,intent);
                            finish();
                        }else
                        {
                            if (response.code()==404)
                            {
                                CreateAlertDialogString(getString(R.string.the_order_accepted_by_another_delegate));
                            }else
                            {
                                dismissSnackBar();
                                dialog.dismiss();
                                Toast.makeText(OrderDetailsActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    public void SendOrderAgain(List<OrderItem> orderItemList)
    {
        Intent intent = getIntent();
        intent.putExtra("data", (Serializable) orderItemList);
        setResult(RESULT_OK,intent);
        finish();

    }
    public void UpdateOrder(OrdersModel.Order order)
    {
        Intent intent = getIntent();
        intent.putExtra("order",order);
        setResult(RESULT_OK,intent);
        finish();
    }


    public void NavigateToChatActivity()
    {
        UserChatModel userChatModel = null;
        if (user_type.equals(Tags.user_client))
        {
             userChatModel = new UserChatModel(order.getDelegate().getId(),order.getChat_room_id(),order.getDelegate().getName(),order.getDelegate().getPhone(),order.getDelegate().getAvatar(),Tags.user_delegate,order.getDelegate().getRate());
        }else if (user_type.equals(Tags.user_delegate))
        {

             userChatModel = new UserChatModel(order.getClient().getId(),order.getChat_room_id(),order.getClient().getName(),order.getClient_phone(),"",Tags.user_client,0);
             userChatModel.setAlter_phone(order.getClient_alternative_phone());

        }

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user_chat_data",userChatModel);
        startActivity(intent);
    }

    public void CreateSnackBar(String msg)
    {
        snackbar = Common.CreateSnackBar(this,root,msg);
        snackbar.show();
    }

    public void dismissSnackBar()
    {
        if (snackbar!=null)
        {
            snackbar.dismiss();

        }
    }

    public void CreateAlertDialogString (String msg)
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_sign,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = getIntent();
                intent.putExtra("accepted",true);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back()
    {
        if (behavior.getState()== BottomSheetBehavior.STATE_EXPANDED)
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else if (fragment_client_order_details!=null && fragment_client_order_details.isVisible())
        {
            finish();
        }else if (fragment_delegate_order_products_details!=null && fragment_delegate_order_products_details.isVisible())
        {
            DisplayFragment_Delegate_New_Order_Details(order);
        }else if (fragment_delegate_new_order_details!=null && fragment_delegate_new_order_details.isVisible())
        {
            finish();
        }else if (fragment_delegate_Current_order_details !=null && fragment_delegate_Current_order_details.isVisible())
        {
            if (isOrderStatusChanged)
            {
                Intent intent =  getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }

            else
                {
                    finish();

                }
        }
        else if (fragment_client_previous_order_details !=null && fragment_client_previous_order_details.isVisible())
        {
            finish();
        }
        else
            {
                if (user_type.equals(Tags.user_delegate)&&order_type.equals(Tags.order_new))
                {
                    DisplayFragment_Delegate_New_Order_Details(order);
                }else if (user_type.equals(Tags.user_delegate)&&order_type.equals(Tags.order_current))
                {
                    DisplayFragment_Delegate_Current_Order_Details(false);
                }
            }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}

