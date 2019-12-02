package com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.Fragment_Client_Profile;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.Fragment_Offers;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.Fragment_Order_Finish_Congratulation;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.Fragment_Search;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.Fragment_SubCategory;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart.Fragment_Date_Time;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart.Fragment_Delivery_Address;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart.Fragment_Map;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart.Fragment_MyCart;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart.Fragment_Payment_Confirmation;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart.Fragment_Review_Purchases;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_home.Fragment_Home;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_home.sub_fragments.Fragment_Charging_Cards;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_home.sub_fragments.Fragment_Food_Department;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_my_order.Fragment_Client_Orders;
import com.appzone.freshcrops.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.freshcrops.activities_fragments.activity_sign_in.SignInActivity;
import com.appzone.freshcrops.activities_fragments.product_details.activity.ProductDetailsActivity;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.models.CouponModel;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.models.NotificationRateModel;
import com.appzone.freshcrops.models.OrderItem;
import com.appzone.freshcrops.models.OrderItemListModel;
import com.appzone.freshcrops.models.OrderToUploadModel;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.models.PageModel;
import com.appzone.freshcrops.models.ResponseModel;
import com.appzone.freshcrops.models.SimilarProductModel;
import com.appzone.freshcrops.models.UpdateOrderStatusModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.services.ServiceUpdateLocation;
import com.appzone.freshcrops.share.Common;
import com.appzone.freshcrops.singletone.OrderItemsSingleTone;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements Fragment_Date_Time.Date_Time_Listener, Fragment_Map.AddressListener{

    private FragmentManager fragmentManager;
    private String current_lang = "";
    private AHBottomNavigation ah_bottom_nav;
    private Fragment_Home fragment_home;
    private Fragment_Offers fragment_offers;
    private Fragment_SubCategory fragment_subCategory;
    private Fragment_Client_Orders fragment_client_orders;
    private Fragment_Client_Profile fragment_client_profile;
    private Fragment_Search fragment_search;
    private Fragment_Order_Finish_Congratulation fragment_order_finish_congratulation;

    ////////////////////////////////////////

    private Fragment_MyCart fragment_myCart;
    private Fragment_Review_Purchases fragment_review_purchases;
    private Fragment_Delivery_Address fragment_delivery_address;
    private Fragment_Payment_Confirmation fragment_payment_confirmation;
    private Fragment_Date_Time fragment_date_time;

    ///////////////////////////////////////

    private Fragment_Food_Department fragment_food_department;
    private Fragment_Charging_Cards fragment_charging_cards;

    ///////////////////////////////////////

    private Fragment_Map fragment_map;
    private final String fineLoc = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 11;
    private final int gps_req = 12;
    private OrderItemsSingleTone orderItemsSingleTone;

    ////////////////////////////////////////

    private MainCategory mainCategory;

    ////////////////////////////////////////
    private Snackbar snackbar;
    private Intent intentService;
    private AlertDialog gpsDialog;
    private LocationManager locationManager;
    public  View root;
    private UserSingleTone userSingleTone;
    private Preferences preferences;
    public UserModel userModel;
    private int time_type=-1;
    private double order_lat=0.0,order_lng=0.0;
    private String order_address="";
    private OrderToUploadModel orderToUploadModel = null;
    public String payment_method="",delivery_cost="0.0";
    public CouponModel couponModel;
    public String address="";
    public double total_order_cost_after_tax = 0.0;
    private String last_selected_fragment="";
    private MainCategory.MainCategoryItems mainCategoryItems;
    public  String coupon_code="";
    public boolean isCouponActive = false;
    ////////////////////for update order///////////////////
    public int order_id_for_update = -1;
    public OrdersModel.Order order_for_update;
    private double rate=0.0;
    private String comment="";

    @Override
    protected void attachBaseContext(Context base)
    {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        getDataFromIntent();
    }
    private void initView()
    {
        Paper.init(this);
        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(this,current_lang);

        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }

        userSingleTone =  UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        preferences = Preferences.getInstance();
        root = findViewById(R.id.root);
        fragmentManager = getSupportFragmentManager();
        orderItemsSingleTone = OrderItemsSingleTone.newInstance();
        ah_bottom_nav = findViewById(R.id.ah_bottom_nav);

        ah_bottom_nav.setDefaultBackgroundColor(ContextCompat.getColor(this,R.color.white));
        ah_bottom_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom_nav.setTitleTextSizeInSp(13,14);
        ah_bottom_nav.setForceTint(true);
        ah_bottom_nav.setColored(false);
        ah_bottom_nav.setAccentColor(ContextCompat.getColor(this,R.color.colorPrimary));
        ah_bottom_nav.setInactiveColor(ContextCompat.getColor(this,R.color.gray_text));

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home),R.drawable.bottom_nav_home,R.color.gray_text);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.offers),R.drawable.bottom_nav_offer,R.color.gray_text);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.cart),R.drawable.bottom_nav_cart,R.color.gray_text);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.my_order),R.drawable.bootom_nav_list,R.color.gray_text);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.me),R.drawable.bottom_nav_user,R.color.gray_text);

        ah_bottom_nav.addItem(item1);
        ah_bottom_nav.addItem(item2);
        ah_bottom_nav.addItem(item3);
        ah_bottom_nav.addItem(item4);
        ah_bottom_nav.addItem(item5);

        ah_bottom_nav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position)
                {
                    case 0:
                        if (fragment_order_finish_congratulation ==null)
                        {
                            UpdateBottomNavPos(position);
                            DisplayFragmentHome();
                        }

                        break;
                    case 1:
                        if (userModel!=null)
                        {
                            if (fragment_order_finish_congratulation ==null)
                            {
                                UpdateBottomNavPos(position);
                                DisplayFragmentOffer();
                            }


                        }else
                            {
                                if (orderItemsSingleTone.getOrderItemList().size()>0)
                                {
                                    Common.CreateUserNotSignInAlertDialog(HomeActivity.this,getString(R.string.si_su),orderItemsSingleTone.getOrderItemList());

                                }else
                                    {
                                        Common.CreateUserNotSignInAlertDialog(HomeActivity.this,getString(R.string.si_su),new ArrayList<OrderItem>());

                                    }


                            }
                        break;
                    case 2:
                        if (fragment_order_finish_congratulation ==null)
                        {
                            DisplayFragmentMyCart();

                        }

                        break;
                    case 3:
                        if (userModel!=null)
                        {
                            if (fragment_order_finish_congratulation ==null)
                            {
                                UpdateBottomNavPos(position);
                                DisplayFragmentClientOrders();

                            }

                        }else
                            {
                                if (orderItemsSingleTone.getOrderItemList().size()>0)
                                {
                                    Common.CreateUserNotSignInAlertDialog(HomeActivity.this,getString(R.string.si_su),orderItemsSingleTone.getOrderItemList());

                                }else
                                {
                                    Common.CreateUserNotSignInAlertDialog(HomeActivity.this,getString(R.string.si_su),new ArrayList<OrderItem>());

                                }
                            }
                        break;
                    case 4:
                        if (userModel!=null)
                        {
                            if (fragment_order_finish_congratulation ==null)
                            {
                                UpdateBottomNavPos(position);
                                DisplayFragmentClientProfile();
                            }


                        }else
                            {
                                if (orderItemsSingleTone.getOrderItemList().size()>0)
                                {
                                    Common.CreateUserNotSignInAlertDialog(HomeActivity.this,getString(R.string.si_su),orderItemsSingleTone.getOrderItemList());

                                }else
                                {
                                    Common.CreateUserNotSignInAlertDialog(HomeActivity.this,getString(R.string.si_su),new ArrayList<OrderItem>());

                                }
                            }
                        break;
                }
                return false;
            }
        });

        DisplayFragmentHome();
        updateUserFireBaseToken();

        List<OrderItem> orderItemList = preferences.getCartItems(this);
        if (orderItemList.size()>0)
        {
            UpdateCartNotification(orderItemList.size());
            orderItemsSingleTone.AddListOrderItems(orderItemList);
        }

    }
    public void UpdateUserData(UserModel userModel)
    {
        this.userModel = userModel;
    }

    private void getDataFromIntent()
    {

        Intent intent = getIntent();
        if (intent!=null)
        {
            if (intent.hasExtra("signup"))
            {
                int signup = intent.getIntExtra("signup",0);
                if (signup==1)
                {
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    CreateWelcomeNotification();
                                }
                            },3000);
                }
            }else if (intent.hasExtra("status"))
            {
                if (intent.getIntExtra("status",0) == 2)
                {
                    DisplayFragmentClientOrders();
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fragment_client_orders.setPage(1);

                                }
                            },100);

                }else if (intent.getIntExtra("status",0) == 3)
                {

                    DisplayFragmentClientOrders();
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    fragment_client_orders.setPage(2);

                                }
                            },100);

                    if (intent.hasExtra("rate_data"))
                    {
                        NotificationRateModel notificationRateModel = (NotificationRateModel) intent.getSerializableExtra("rate_data");
                        CreateAddRateAlertDialog(notificationRateModel);
                    }
                }
                else if (intent.getIntExtra("status",0) == 4)
                {
                    DisplayFragmentClientOrders();
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fragment_client_orders.setPage(0);

                                }
                            },100);
                }


            }
        }
    }
    private void CreateWelcomeNotification()
    {
        String sound_path = "android.resource://"+getPackageName()+"/"+R.raw.client;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String CHANNEL_ID = "my_channel_01";
            CharSequence CHANNEL_NAME = "channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,IMPORTANCE);
            channel.setShowBadge(true);
            channel.setSound(Uri.parse(sound_path),new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build()
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_path));
            builder.setContentTitle(getString(R.string.notifications));
            builder.setContentText(getString(R.string.welcome_to_Dukkan));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager!=null)
            {
                manager.createNotificationChannel(channel);
                manager.notify(1,builder.build());

            }


        }else

        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSound(Uri.parse(sound_path));
            builder.setContentTitle(getString(R.string.notifications));
            builder.setContentText(getString(R.string.welcome_to_Dukkan));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager!=null)
            {
                manager.notify(1,builder.build());

            }
        }
    }
    private void updateUserFireBaseToken()
    {
        if (userModel!=null)
        {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful())
                            {
                                String fireBaseToken = task.getResult().getToken();
                                String user_token = userModel.getToken();
                                Api.getService()
                                        .updateFireBaseToken(user_token,fireBaseToken)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                if (response.isSuccessful())
                                                {
                                                    Log.e("user_token_update","success");



                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                try {
                                                    Log.e("Error",t.getMessage());
                                                }catch (Exception e){}
                                            }
                                        });
                            }
                        }
                    });
        }
    }
    public void UpdateCartNotification(int count)
    {
        if (count > 0 )
        {
            AHNotification.Builder builder = new AHNotification.Builder();
            builder.setBackgroundColor(ContextCompat.getColor(this,R.color.green_text));
            builder.setTextColor(ContextCompat.getColor(this,R.color.white));
            builder.setText(String.valueOf(count));

            ah_bottom_nav.setNotification(builder.build(),2);


        }else
            {
                AHNotification.Builder builder = new AHNotification.Builder();
                builder.setBackgroundColor(ContextCompat.getColor(this,R.color.green_text));
                builder.setTextColor(ContextCompat.getColor(this,R.color.white));
                builder.setText("");
                ah_bottom_nav.setNotification(builder.build(),2);
            }
    }
    private void UpdateBottomNavPos(int pos)
    {
        ah_bottom_nav.setCurrentItem(pos,false);
    }

    public void DisplayFragmentHome()
    {


        if (fragment_order_finish_congratulation!=null&&fragment_order_finish_congratulation.isAdded())
        {

            fragmentManager.popBackStack("fragment_order_finish_congratulation",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragment_order_finish_congratulation = null;
        }
        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
        {
            fragmentManager.popBackStack("fragment_payment_confirmation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragmentManager.popBackStack("fragment_review_purchases",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.popBackStack("fragment_subCategory",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_search!=null&&fragment_search.isAdded())
        {
            fragmentManager.popBackStack("fragment_search",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_map!=null&&fragment_map.isAdded())
        {
            fragmentManager.popBackStack("fragment_map",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_date_time!=null&&fragment_date_time.isAdded())
        {
            fragmentManager.popBackStack("fragment_date_time",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_home==null)
        {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded())
        {
            if (!fragment_home.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_home).commit();
                DisplayFragmentFood_Department();
                UpdateBottomNavPos(0);
            }
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_home,"fragment_home").addToBackStack("fragment_home").commit();
                DisplayFragmentFood_Department();
                UpdateBottomNavPos(0);
            }


            if (fragment_offers!=null&&fragment_offers.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }

        if (fragment_myCart!=null&&fragment_myCart.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myCart).commit();
        }

        if (fragment_client_orders!=null&&fragment_client_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_orders).commit();
        }

        if (fragment_client_profile!=null&&fragment_client_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_profile).commit();
        }









    }
    private void DisplayFragmentOffer()
    {

        if (fragment_order_finish_congratulation!=null&&fragment_order_finish_congratulation.isAdded())
        {
            fragmentManager.popBackStack("fragment_order_finish_congratulation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragmentManager.popBackStack("fragment_review_purchases",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
        {
            fragmentManager.popBackStack("fragment_payment_confirmation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.popBackStack("fragment_subCategory",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_search!=null&&fragment_search.isAdded())
        {
            fragmentManager.popBackStack("fragment_search",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_map!=null&&fragment_map.isAdded())
        {
            fragmentManager.popBackStack("fragment_map",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_date_time!=null&&fragment_date_time.isAdded())
        {
            fragmentManager.popBackStack("fragment_date_time",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_offers==null)
        {
            fragment_offers = Fragment_Offers.newInstance();
        }

        if (fragment_offers.isAdded())
        {
            if (!fragment_offers.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_offers).commit();
                UpdateBottomNavPos(1);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_offers,"fragment_offers").addToBackStack("fragment_offers").commit();
            UpdateBottomNavPos(1);
        }


        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
        if (fragment_myCart!=null&&fragment_myCart.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myCart).commit();
        }
        if (fragment_client_orders!=null&&fragment_client_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_orders).commit();
        }
        if (fragment_client_profile!=null&&fragment_client_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_profile).commit();
        }




    }
    public void DisplayFragmentClientOrders()
    {
        if (fragment_order_finish_congratulation!=null&&fragment_order_finish_congratulation.isAdded())
        {
            fragmentManager.popBackStack("fragment_order_finish_congratulation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragmentManager.popBackStack("fragment_review_purchases",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
        {
            fragmentManager.popBackStack("fragment_payment_confirmation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.popBackStack("fragment_subCategory",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_search!=null&&fragment_search.isAdded())
        {
            fragmentManager.popBackStack("fragment_search",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_map!=null&&fragment_map.isAdded())
        {
            fragmentManager.popBackStack("fragment_map",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_date_time!=null&&fragment_date_time.isAdded())
        {
            fragmentManager.popBackStack("fragment_date_time",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_client_orders==null)
        {
            fragment_client_orders = Fragment_Client_Orders.newInstance();
        }

        if (fragment_client_orders.isAdded())
        {
            if (!fragment_client_orders.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_client_orders).commit();
                UpdateBottomNavPos(3);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_client_orders,"fragment_client_orders").addToBackStack("fragment_client_orders").commit();
            UpdateBottomNavPos(3);
        }

        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_offers!=null&&fragment_offers.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }

        if (fragment_myCart!=null&&fragment_myCart.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myCart).commit();
        }
        if (fragment_client_profile!=null&&fragment_client_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_profile).commit();
        }

    }
    public void DisplayFragmentClientProfile()
    {
        if (fragment_order_finish_congratulation!=null&&fragment_order_finish_congratulation.isAdded())
        {
            fragmentManager.popBackStack("fragment_order_finish_congratulation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragmentManager.popBackStack("fragment_review_purchases",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
        {
            fragmentManager.popBackStack("fragment_payment_confirmation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.popBackStack("fragment_subCategory",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_search!=null&&fragment_search.isAdded())
        {
            fragmentManager.popBackStack("fragment_search",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_map!=null&&fragment_map.isAdded())
        {
            fragmentManager.popBackStack("fragment_map",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_date_time!=null&&fragment_date_time.isAdded())
        {
            fragmentManager.popBackStack("fragment_date_time",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_client_profile==null)
        {
            fragment_client_profile = Fragment_Client_Profile.newInstance();
        }

        if (fragment_client_profile.isAdded())
        {
            if (!fragment_client_profile.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_client_profile).commit();
                UpdateBottomNavPos(4);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_client_profile,"fragment_client_profile").addToBackStack("fragment_client_profile").commit();
            UpdateBottomNavPos(4);
        }


        if (fragment_offers!=null&&fragment_offers.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }

        if (fragment_myCart!=null&&fragment_myCart.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myCart).commit();
        }

        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
        if (fragment_client_orders!=null&&fragment_client_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_orders).commit();
        }

    }
    public void DisplayFragmentSearch()
    {

        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.popBackStack("fragment_subCategory",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        fragment_search = Fragment_Search.newInstance();


        if (fragment_search.isAdded())
        {
            if (!fragment_search.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_search).commit();
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_search,"fragment_search").addToBackStack("fragment_search").commit();
        }


        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
        if (fragment_myCart!=null&&fragment_myCart.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myCart).commit();
        }
        if (fragment_client_orders!=null&&fragment_client_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_orders).commit();
        }
        if (fragment_client_profile!=null&&fragment_client_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_profile).commit();
        }
        if (fragment_offers!=null&&fragment_offers.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }
    }
    ////////////////////////////////////
    private void DisplayFragmentMyCart()
    {

        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragmentManager.popBackStack("fragment_review_purchases",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded()&&!fragment_payment_confirmation.isVisible())
        {
            fragmentManager.popBackStack("fragment_payment_confirmation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.popBackStack("fragment_subCategory",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_search!=null&&fragment_search.isAdded())
        {
            fragmentManager.popBackStack("fragment_search",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_myCart==null)
        {
            fragment_myCart = Fragment_MyCart.newInstance();

        }

        if (fragment_myCart.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_myCart).commit();
            DisplayFragmentReview_Purchases();
            UpdateBottomNavPos(2);

        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_myCart,"fragment_myCart").addToBackStack("fragment_myCart").commit();
            DisplayFragmentReview_Purchases();

            UpdateBottomNavPos(2);
        }


        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_offers!=null&&fragment_offers.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }

        if (fragment_client_orders!=null&&fragment_client_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_orders).commit();
        }
        if (fragment_client_profile!=null&&fragment_client_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_client_profile).commit();
        }




    }
    public void DisplayFragmentReview_Purchases()
    {
        fragment_review_purchases = Fragment_Review_Purchases.newInstance(orderItemsSingleTone.getOrderItemList());

        if (fragment_review_purchases.isAdded())
        {
            if (!fragment_review_purchases.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_review_purchases).commit();
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_my_cart_container,fragment_review_purchases,"fragment_review_purchases").addToBackStack("fragment_review_purchases").commit();
        }



        if (fragment_delivery_address!=null&&fragment_delivery_address.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delivery_address).commit();
        }

        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_payment_confirmation).commit();
        }

    }
    public void DisplayFragmentDelivery_Address(final double total_order_cost)
    {
        if (userModel==null)
        {
            Common.CreateUserNotSignInAlertDialog(HomeActivity.this,getString(R.string.si_su),new ArrayList<OrderItem>());

        }else
            {
                if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
                {
                    fragmentManager.popBackStack("fragment_payment_confirmation",FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }
                if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
                {
                    fragmentManager.beginTransaction().hide(fragment_review_purchases).commit();
                }


                if (fragment_delivery_address==null)
                {
                    fragment_delivery_address = Fragment_Delivery_Address.newInstance(total_order_cost);

                }

                if (fragment_delivery_address.isAdded())
                {
                    if (!fragment_delivery_address.isVisible())
                    {
                        fragmentManager.beginTransaction().show(fragment_delivery_address).commit();
                        fragment_delivery_address.setTotal_order(total_order_cost);
                        new Handler()
                                .postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        fragment_delivery_address.getCouponData(total_order_cost);

                                        if (!TextUtils.isEmpty(address))
                                        {
                                            fragment_delivery_address.UpdateAddress(address);

                                        }
                                    }
                                },1);
                    }
                }else
                {
                    fragmentManager.beginTransaction().add(R.id.fragment_my_cart_container,fragment_delivery_address,"fragment_delivery_address").addToBackStack("fragment_delivery_address").commit();
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fragment_delivery_address.getCouponData(total_order_cost);

                                    if (!TextUtils.isEmpty(address))
                                    {
                                        fragment_delivery_address.UpdateAddress(address);

                                    }
                                }
                            },1);
                }






                updateUIToolBarFragmentCart(2);
            }





    }
    public void DisplayFragmentPayment_Confirmation(String payment_method,CouponModel couponModel)
    {
        this.payment_method = payment_method;
        this.couponModel = couponModel;

        fragment_payment_confirmation = Fragment_Payment_Confirmation.newInstance();

        if (fragment_payment_confirmation.isAdded())
        {
            if (!fragment_payment_confirmation.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_payment_confirmation).commit();
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_my_cart_container,fragment_payment_confirmation,"fragment_payment_confirmation").addToBackStack("fragment_payment_confirmation").commit();
        }


        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_review_purchases).commit();
        }
        if (fragment_delivery_address!=null&&fragment_delivery_address.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delivery_address).commit();
        }

        updateUIToolBarFragmentCart(3);

    }
    public void DisplayFragmentFood_Department()
    {
        if (fragment_food_department==null)
        {
            fragment_food_department = Fragment_Food_Department.newInstance();
        }
        if (fragment_food_department.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_food_department).commit();
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_home_sub_fragment_container,fragment_food_department,"fragment_food_department").addToBackStack("fragment_food_department").commit();
            }

            if (fragment_charging_cards!=null && fragment_charging_cards.isAdded() && fragment_charging_cards.isVisible())
            {
                fragmentManager.beginTransaction().hide(fragment_charging_cards).commit();
            }
    }
    public void DisplayFragmentCharging_Cards()
    {
        if (fragment_charging_cards==null)
        {
            fragment_charging_cards = Fragment_Charging_Cards.newInstance();
        }
        if (fragment_charging_cards.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_charging_cards).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_sub_fragment_container,fragment_charging_cards,"fragment_charging_cards").addToBackStack("fragment_charging_cards").commit();
        }

        if (fragment_food_department!=null && fragment_food_department.isAdded() && fragment_food_department.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_food_department).commit();
        }
    }
    public void DisplayFragmentMap()
    {
        fragment_map = Fragment_Map.newInstance();

        fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_map,"fragment_map").addToBackStack("fragment_map").commit();

        if (fragment_delivery_address!=null&&fragment_delivery_address.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delivery_address).commit();
        }

        checkLocationPermission();

    }
    public void DisplayFragmentDateTime()
    {
        fragment_date_time = Fragment_Date_Time.newInstance();

        fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_date_time,"fragment_date_time").addToBackStack("fragment_date_time").commit();

        if (fragment_delivery_address!=null&&fragment_delivery_address.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delivery_address).commit();
        }


    }
    public void DisplayFragment_Order_Finish_Congratulation(OrdersModel.Order order)
    {
        orderItemsSingleTone.ClearCart();
        orderToUploadModel = null;
        UpdateCartNotification(0);

        if (fragment_myCart!=null&&fragment_myCart.isAdded())
        {
            fragment_myCart = null;
            fragmentManager.popBackStack("fragment_myCart",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragment_review_purchases = null;
            fragmentManager.popBackStack("fragment_review_purchases",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_delivery_address!=null&&fragment_delivery_address.isAdded())
        {
            fragment_delivery_address=null;
            fragmentManager.popBackStack("fragment_delivery_address",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
        {
            fragmentManager.popBackStack("fragment_payment_confirmation",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (fragment_search!=null&&fragment_search.isAdded())
        {
            fragmentManager.popBackStack("fragment_search",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.popBackStack("fragment_subCategory",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }



        fragment_order_finish_congratulation = Fragment_Order_Finish_Congratulation.newInstance(order);

        fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_order_finish_congratulation,"fragment_order_finish_congratulation").addToBackStack("fragment_order_finish_congratulation").commit();




    }
    ///////////////////////////////////////////////////
    public void DisplayFragmentSubCategory(MainCategory.MainCategoryItems mainCategoryItems)
    {

        this.mainCategoryItems = mainCategoryItems;

        if (fragment_search!=null&&fragment_search.isAdded())
        {
            fragmentManager.popBackStack("fragment_search",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragment_subCategory = Fragment_SubCategory.newInstance(mainCategoryItems);


        if (fragment_subCategory.isAdded())
            {
                fragmentManager.beginTransaction().show(fragment_subCategory).commit();
            }else
                {
                    fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_subCategory,"fragment_subCategory").addToBackStack("fragment_subCategory").commit();

                }

                if (fragment_home!=null&&fragment_home.isAdded())
                {
                    fragmentManager.beginTransaction().hide(fragment_home).commit();
                }

    }
    public void updateUIToolBarFragmentCart(int pos)
    {
        switch (pos)
        {
            case 0:
                if (fragment_myCart!=null&&fragment_myCart.isAdded())
                {
                    fragment_myCart.clearBasket();
                }
                break;
            case 1:
                if (fragment_myCart!=null&&fragment_myCart.isAdded())
                {
                    fragment_myCart.UpdateBasketUI();
                }
                break;
            case 2:
                if (fragment_myCart!=null&&fragment_myCart.isAdded())
                {
                    fragment_myCart.UpdateCarUI();

                }
                break;
            case 3:
                if (fragment_myCart!=null&&fragment_myCart.isAdded())
                {
                    fragment_myCart.UpdatePaymentUI();
                }
                break;

        }
    }
    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,fineLoc)== PackageManager.PERMISSION_GRANTED)
        {

            if (isGpsOpen())
            {
                StartLocationUpdate();

            }else
                {
                    CreateGpsDialog();
                }

        }else
            {
                String [] perm = {fineLoc};

                ActivityCompat.requestPermissions(this,perm,loc_req);
            }

    }
    private void StartLocationUpdate()
    {
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }

        intentService = new Intent(this, ServiceUpdateLocation.class);
        startService(intentService);
    }
    private void StopLocationUpdate()
    {
        if (intentService!=null)
        {
            stopService(intentService);
        }
    }
    ///////////////////////////////////
    public void setMainCategory (MainCategory mainCategory)
    {
        this.mainCategory = mainCategory;

    }
    public List<MainCategory.Products> getSimilarProducts(int main_category_id,int sub_category_id,int product_id)
    {
        final List<MainCategory.Products> productsList = new ArrayList<>();

        if (mainCategory!=null&&mainCategory.getData().size()>0)
        {
            for (int i=0;i<10;i++)
            {
                MainCategory.MainCategoryItems mainCategoryItems = mainCategory.getData().get(i);
                if (mainCategoryItems!=null)
                {
                    if (mainCategoryItems.getId()==main_category_id)
                    {
                        for (MainCategory.SubCategory subCategory : mainCategoryItems.getSub_categories())
                        {
                            if (sub_category_id!=0)
                            {
                                if (subCategory.getId()==sub_category_id)
                                {
                                    for (MainCategory.Products products : subCategory.getProducts())
                                    {

                                        if (products!=null)
                                        {
                                            if (products.getId()!=product_id)
                                            {
                                                productsList.add(products);

                                            }

                                        }
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }

                }
            }
        }else
        {
            Api.getService()
                    .getSimilarProducts(1,product_id,main_category_id,sub_category_id)
                    .enqueue(new Callback<SimilarProductModel>() {
                        @Override
                        public void onResponse(Call<SimilarProductModel> call, Response<SimilarProductModel> response) {
                            if (response.isSuccessful())
                            {
                                productsList.addAll(response.body().getData());
                            }
                        }

                        @Override
                        public void onFailure(Call<SimilarProductModel> call, Throwable t) {
                            try {
                                CreateSnackBar(getString(R.string.something));
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){

                            }
                        }
                    });
        }

        return productsList;
    }

    ///////////////////////////////////

    private void CreateGpsDialog()
    {
        gpsDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(this).inflate(R.layout.gps_layout,null);
        Button btn_allow = view.findViewById(R.id.btn_allow);
        Button btn_deny = view.findViewById(R.id.btn_deny);
        btn_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsDialog.dismiss();
                openGps();

            }
        });

        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsDialog.dismiss();
            }
        });

        gpsDialog.setView(view);
        gpsDialog.setCanceledOnTouchOutside(false);
        gpsDialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        gpsDialog.show();
    }
    private void openGps()
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,gps_req);
    }
    private boolean isGpsOpen()
    {
        if (locationManager == null)
        {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }
    ///////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToLocationUpdate(Location location)
    {
        if (fragment_map != null && fragment_map.isAdded())
        {
            if (fragment_map.isMapReady)
            {
               fragment_map.UpdateLocation(location);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenForNotification(PageModel pageModel)
    {
        if (pageModel.getStatus() == 1||pageModel.getStatus() == 0)
        {
            RefreshFragmentClient_New_Current_Order();

        }else if (pageModel.getStatus() == 2)
        {

            RefreshFragmentClient_Current_Previous_Order();

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  listenForRate(NotificationRateModel notificationRateModel)
    {
        CreateAddRateAlertDialog(notificationRateModel);
    }
    ///////////////////////////////////
    public void NavigateToProductDetailsActivity(MainCategory.Products product, List<MainCategory.Products> similarProducts)
    {


        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product",product);
        intent.putExtra("similar_products", (Serializable) similarProducts);
        startActivityForResult(intent,1122);

    }
    private void NavigateToSignInActivity()
    {
        Intent intent  = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
    public void NavigateToOrderDetailsActivity(OrdersModel.Order order,String order_type)
    {


        if (fragment_order_finish_congratulation!=null && fragment_order_finish_congratulation.isVisible())
        {
            DisplayFragmentHome();
            Intent intent = new Intent(this, OrderDetailsActivity.class);
            intent.putExtra("order",order);
            intent.putExtra("order_type",order_type);
            startActivityForResult(intent,4);

        }else
        {
            fragment_order_finish_congratulation = null;

            if (order_type.equals(Tags.order_old))
            {
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra("order",order);
                intent.putExtra("order_type",order_type);
                startActivityForResult(intent,2);
            }else
            {
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra("order",order);
                intent.putExtra("order_type",order_type);
                startActivityForResult(intent,4);
            }

        }

    }
    private void RefreshFragmentClient_Previous_New_Order()
    {
        if (fragment_client_orders!=null)
        {
            fragment_client_orders.RefreshFragment_Previous_New_Order();
        }
    }
    private void RefreshFragmentClient_Current_Previous_Order()
    {
        if (fragment_client_orders!=null && fragment_client_orders.isAdded())
        {
            fragment_client_orders.RefreshFragment_Current_Previous_Order();
        }

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_client_profile!=null&&fragment_client_profile.isAdded())
                        {
                            fragment_client_profile.UpdateProfile();
                        }
                    }
                },50);
    }
    private void RefreshFragmentClient_New_Current_Order()
    {
        if (fragment_client_orders!=null&&fragment_client_orders.isAdded())
        {
            fragment_client_orders.RefreshFragment_New_Current_Order();
        }
    }
    ////////////////////////////////////
    public void setLast_selected_fragment(String fragment)
    {
        this.last_selected_fragment = fragment;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == gps_req)
        {
            if (isGpsOpen())
            {
                StartLocationUpdate();
            }else
            {
                CreateGpsDialog();
            }
        }else if (requestCode == 1122 && resultCode == RESULT_OK)
        {
            UpdateCartNotification(orderItemsSingleTone.getItemsCount());
        }else if (requestCode == 2 && resultCode==RESULT_OK)
        {
            if (data!=null)
            {
                if (data.hasExtra("data"))
                {
                    List<OrderItem> orderItemList = (List<OrderItem>) data.getSerializableExtra("data");
                    UpdateFragmentCart(orderItemList);
                }else
                    {
                        RefreshFragmentClient_Previous_New_Order();

                    }
            }else
                {
                    RefreshFragmentClient_Previous_New_Order();

                }
        }else if (requestCode ==4 && resultCode ==RESULT_OK && data!=null)
        {

            if (data.hasExtra("order"))
            {
                OrdersModel.Order order = (OrdersModel.Order) data.getSerializableExtra("order");
                if (order!=null)
                {
                    getProductsForUpdateOrder(order);
                }

                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DisplayFragmentMyCart();

                            }
                        },1);
            }


        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == loc_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (isGpsOpen())
                    {
                        StartLocationUpdate();
                    }else
                    {
                        CreateGpsDialog();
                    }
                }else
                    {
                        CreateToast(getString(R.string.gps_perm_denied));
                    }


            }
        }
    }
    public void getProductsForUpdateOrder(final OrdersModel.Order order)
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService()
                .getProductsToSendAgain(order.getId())
                .enqueue(new Callback<OrderItemListModel>() {
                    @Override
                    public void onResponse(Call<OrderItemListModel> call, Response<OrderItemListModel> response) {
                        if (response.isSuccessful())
                        {
                            dismissSnackBar();
                            dialog.dismiss();
                            if (response.body()!=null)
                            {
                                HomeActivity.this.order_for_update = order;
                                HomeActivity.this.order_id_for_update = order.getPayment_method();
                                UpdateFragmentCart(response.body().getData());
                            }
                        }else
                        {


                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderItemListModel> call, Throwable t) {

                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void UpdateFragmentCart(List<OrderItem> orderItemList)
    {

        if (fragment_order_finish_congratulation!=null&&fragment_order_finish_congratulation.isVisible())
        {
            UpdateCartNotification(orderItemList.size());
            if (orderItemsSingleTone==null)
            {
                orderItemsSingleTone = OrderItemsSingleTone.newInstance();
            }
            orderItemsSingleTone.AddListOrderItems(orderItemList);

            fragmentManager.popBackStack("fragment_order_finish_congratulation",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragment_order_finish_congratulation = null;

            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DisplayFragmentMyCart();

                        }
                    },1);
        }else
            {
                UpdateCartNotification(orderItemList.size());
                if (orderItemsSingleTone==null)
                {
                    orderItemsSingleTone = OrderItemsSingleTone.newInstance();
                }
                orderItemsSingleTone.AddListOrderItems(orderItemList);

                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DisplayFragmentMyCart();

                            }
                        },1);
            }


    }
    public void ChangeLanguage(String lang)
    {
        Paper.book().write("lang",lang);
        current_lang = lang;
        LanguageHelper.setLocality(this,lang);
        refreshActivity();
    }
    private void refreshActivity()
    {

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    @Override
    public void onDate_Time_Set(int time_type )
    {

        this.time_type = time_type;

        fragmentManager.popBackStack("fragment_date_time", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().show(fragment_myCart).commit();
        fragment_delivery_address.UpdateDate_Time(time_type);
        fragmentManager.beginTransaction().show(fragment_delivery_address).commit();


    }
    @Override
    public void onAddressSet(String address, double lat, double lng)
    {
        this.order_lat = lat;
        this.order_lng = lng;
        this.order_address = address;
        StopLocationUpdate();
        fragmentManager.popBackStack("fragment_map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().show(fragment_myCart).commit();
        fragment_delivery_address.UpdateAddress(address);
        fragmentManager.beginTransaction().show(fragment_delivery_address).commit();
    }
    public void SaveListOf_Order_Order_Total_Cost(List<OrderItem> orderItemList, double net_total_order_price, double total_order_cost_after_tax, double tax)
    {
        if (orderToUploadModel==null)
        {
            orderToUploadModel = new OrderToUploadModel();
        }
        this.total_order_cost_after_tax = total_order_cost_after_tax;
        orderToUploadModel.setOrder_total_price_net(net_total_order_price);
        orderToUploadModel.setTax(tax);
        orderToUploadModel.setOrderItemList(orderItemList);
    }
    public void Save_Order_Data(String name, String phone, String alter_phone, String street_name, String feedback, CouponModel couponModel, String payment_method, String coupon_code,int time_type)
    {
        this.coupon_code = coupon_code;
        if (orderToUploadModel==null)
        {
            orderToUploadModel = new OrderToUploadModel();
        }

        orderToUploadModel.setClient_id(userModel.getUser().getId());
        orderToUploadModel.setClient_name(name);
        orderToUploadModel.setClient_Phone(phone);
        orderToUploadModel.setClient_street(street_name);
        orderToUploadModel.setNotes(feedback);
        orderToUploadModel.setTime_type(time_type);
        orderToUploadModel.setDelivery_cost(Double.parseDouble(delivery_cost));
        orderToUploadModel.setClient_alternative_phone(alter_phone);


        //orderToUploadModel.setOrder_total_price(final_total_order_cost);

        if (couponModel!=null)
        {
            orderToUploadModel.setCoupon_value(couponModel.getCoupon_value());
            if (couponModel.getCoupon_codes()!=null)
            {
                isCouponActive = true;
                orderToUploadModel.setCoupon_code(couponModel.getCoupon_codes().getAr());

            }

        }else
            {
                isCouponActive = false;

                orderToUploadModel.setCoupon_value(0);
                orderToUploadModel.setCoupon_code("");

            }

        orderToUploadModel.setPayment_method(payment_method);
        orderToUploadModel.setClient_address(order_address);
        orderToUploadModel.setLat(order_lat);
        orderToUploadModel.setLng(order_lng);

    }
    public void UploadOrder(int discount_by_use, double discount_point, double discount_points_cost, double final_total_order_price)
    {
        Log.e("final_total_order_price",final_total_order_price+"");
        orderToUploadModel.setOrder_total_price(final_total_order_price);
        orderToUploadModel.setDiscount_by_use(discount_by_use);
        orderToUploadModel.setDiscount_point(discount_point);
        orderToUploadModel.setTotal_discount(discount_points_cost);

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Api.getService()
                .uploadOrder(orderToUploadModel)
                .enqueue(new Callback<OrdersModel.Order>() {
                    @Override
                    public void onResponse(Call<OrdersModel.Order> call, Response<OrdersModel.Order> response) {

                        if (response.isSuccessful())
                        {

                            dialog.dismiss();
                            dismissSnackBar();
                            Toast.makeText(HomeActivity.this, R.string.order_sent_successfully, Toast.LENGTH_SHORT).show();
                            if (response.body()!=null)
                            {
                                DisplayFragment_Order_Finish_Congratulation(response.body());

                                new Handler()
                                        .postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (fragment_client_orders!=null && fragment_client_orders.isAdded())
                                                {
                                                    fragment_client_orders.RefreshFragment_New_Current_Order();
                                                }
                                            }
                                        },1);


                            }

                        }else
                        {
                            try {

                                Log.e("error",response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss();
                            if (response.code()==404)
                            {
                                Toast.makeText(HomeActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrdersModel.Order> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());

                        }catch (Exception e){}
                    }
                });



    }
    public void UploadUpdatedOrder(List<OrderItem> orderItemList, double net_total_order_price, double total_order_cost_after_tax, double tax)
    {
        if (orderToUploadModel==null)
        {
            orderToUploadModel = new OrderToUploadModel();
        }
        orderToUploadModel.setOrder_total_price_net(net_total_order_price);
        orderToUploadModel.setTax(tax);
        orderToUploadModel.setOrderItemList(orderItemList);

        orderToUploadModel.setClient_id(order_for_update.getClient().getId());
        orderToUploadModel.setClient_name(order_for_update.getClient().getName());
        orderToUploadModel.setClient_Phone(order_for_update.getClient().getPhone());
        orderToUploadModel.setClient_street(order_for_update.getStreet());
        orderToUploadModel.setNotes(order_for_update.getNote());
        orderToUploadModel.setTime_type(order_for_update.getTime_type());
        orderToUploadModel.setDelivery_cost(order_for_update.getDelivery_cost());
        orderToUploadModel.setClient_alternative_phone(order_for_update.getClient_alternative_phone());

        orderToUploadModel.setPayment_method(String.valueOf(order_for_update.getPayment_method()));
        orderToUploadModel.setClient_address(order_for_update.getAddress());
        orderToUploadModel.setLat(order_for_update.getLat());
        orderToUploadModel.setLng(order_for_update.getLng());

        double total = 0.0;
        if (order_for_update.getDiscount_by_use()==0)
        {
            total = total_order_cost_after_tax+order_for_update.getDelivery_cost();

        }else if (order_for_update.getDiscount_by_use()== Tags.discount_by_use_coupon)
        {
            total = (total_order_cost_after_tax+order_for_update.getDelivery_cost())-order_for_update.getCoupon_value();

        }else if (order_for_update.getDiscount_by_use() == Tags.discount_by_use_points)
        {
            if ((total_order_cost_after_tax+order_for_update.getDelivery_cost())>order_for_update.getDiscount_point())
            {
                total = (total_order_cost_after_tax+order_for_update.getDelivery_cost())-order_for_update.getDiscount_point();

            }else
                {
                    total = order_for_update.getDiscount_point()-(total_order_cost_after_tax+0.0);

                }

        }


        orderToUploadModel.setOrder_total_price(total);
        orderToUploadModel.setDiscount_by_use(order_for_update.getDiscount_by_use());
        orderToUploadModel.setDiscount_point(order_for_update.getDiscount_point());
        orderToUploadModel.setTotal_discount(order_for_update.getTotal_discount());

        orderToUploadModel.setCoupon_value(order_for_update.getCoupon_value());
        orderToUploadModel.setCoupon_code(order_for_update.getCoupon_code());

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .updateOrder(order_for_update.getId(),orderToUploadModel)
                .enqueue(new Callback<UpdateOrderStatusModel>() {
                    @Override
                    public void onResponse(Call<UpdateOrderStatusModel> call, Response<UpdateOrderStatusModel> response) {
                        if (response.isSuccessful())
                        {
                            dismissSnackBar();
                            dialog.dismiss();

                            if (response.body()!=null)
                            {
                                Toast.makeText(HomeActivity.this, R.string.order_updated_successfully, Toast.LENGTH_LONG).show();
                                orderItemsSingleTone.ClearCart();
                                orderToUploadModel = null;
                                UpdateCartNotification(0);
                                order_for_update = null;
                                order_id_for_update = -1;
                                DisplayFragmentHome();
                                new Handler()
                                        .postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (fragment_client_orders!=null && fragment_client_orders.isAdded())
                                                {
                                                    fragment_client_orders.RefreshFragment_New_Current_Order();
                                                }
                                            }
                                        },1);

                            }
                        }else
                            {

                                try {
                                    Log.e("error",response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                dismissSnackBar();
                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                if (response.code() == 404)
                                {
                                    /*if (response.body()!=null)
                                    {
                                        if (response.body().getStatus() == Tags.status_delegate_collect_order)
                                        {
                                            Common.CreateSignAlertDialog(HomeActivity.this,getString(R.string.cannot_update_order)+" "+getString(R.string.collecting_order));
                                        }else if (response.body().getStatus() == Tags.status_delegate_already_collect_order)
                                        {
                                            Common.CreateSignAlertDialog(HomeActivity.this,getString(R.string.cannot_update_order)+" "+getString(R.string.order_collected));

                                        }
                                        else if (response.body().getStatus() == Tags.status_delegate_delivering_order)
                                        {
                                            Common.CreateSignAlertDialog(HomeActivity.this,getString(R.string.cannot_update_order)+" "+getString(R.string.delivering_order));

                                        }
                                    }*/

                                    Common.CreateSignAlertDialog(HomeActivity.this,getString(R.string.cannot_update_order));


                                }

                                }
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderStatusModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                        }catch (Exception e){}
                    }
                });

    }
    public void Clear_Order_Object()
    {
        if (orderToUploadModel!=null)
        {
            orderToUploadModel = null;
        }
    }
    public void CreateCartAlertDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_remove_cart_item,null);
        FrameLayout fl_delete = view.findViewById(R.id.fl_delete);
        FrameLayout fl_cancel = view.findViewById(R.id.fl_cancel);

        TextView tv_not_dialog = view.findViewById(R.id.tv_not_dialog);
        TextView tv_content = view.findViewById(R.id.tv_content);
        tv_not_dialog.setText(String.valueOf(orderItemsSingleTone.getItemsCount()));
        tv_content.setText(getString(R.string.the_cart_contains)+" "+orderItemsSingleTone.getItemsCount()+" "+getString(R.string.item)+" "+getString(R.string.delete_items));

        fl_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.clearCart(HomeActivity.this);
                orderItemsSingleTone.ClearCart();
                Clear_Order_Object();
                UpdateCartNotification(0);
                order_for_update = null;
                order_id_for_update = -1;
                dialog.dismiss();
            }
        });
        fl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
    public void CreateAddRateAlertDialog(final NotificationRateModel notificationRateModel)
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rate,null);
        ImageView  img_close = view.findViewById(R.id.img_close);
        CircleImageView image = view.findViewById(R.id.image);
        TextView tv_name = view.findViewById(R.id.tv_name);
        final ImageView image_love = view.findViewById(R.id.image_love);
        final ImageView image_smile = view.findViewById(R.id.image_smile);
        final ImageView image_angry = view.findViewById(R.id.image_angry);
        final LinearLayout ll_comment = view.findViewById(R.id.ll_comment);
        final EditText edt_comment = view.findViewById(R.id.edt_comment);
        final TextView tv_rate = view.findViewById(R.id.tv_rate);
        final Button btn_rate = view.findViewById(R.id.btn_rate);
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+notificationRateModel.getDelegate_avatar())).fit().into(image);
        tv_name.setText(notificationRateModel.getDelegate_name());




        image_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate = 5.0;
                image_love.setImageResource(R.drawable.emoji_love_sel);
                image_smile.setImageResource(R.drawable.emoji_smile_unsel);
                image_angry.setImageResource(R.drawable.emoji_ang_unsel);
                tv_rate.setText(R.string.excellent);
                ll_comment.setVisibility(View.GONE);
                btn_rate.setVisibility(View.VISIBLE);
            }
        });

        image_smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate = 3.0;

                image_love.setImageResource(R.drawable.emoji_love_unsel);
                image_smile.setImageResource(R.drawable.emoji_smile_sel);
                image_angry.setImageResource(R.drawable.emoji_ang_unsel);
                tv_rate.setText(R.string.moderate);
                ll_comment.setVisibility(View.VISIBLE);
                btn_rate.setVisibility(View.VISIBLE);
            }
        });

        image_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate = 1.5;

                image_love.setImageResource(R.drawable.emoji_love_unsel);
                image_smile.setImageResource(R.drawable.emoji_smile_unsel);
                image_angry.setImageResource(R.drawable.emoji_ang_sel);
                tv_rate.setText(R.string.bad);
                ll_comment.setVisibility(View.VISIBLE);
                btn_rate.setVisibility(View.VISIBLE);
            }
        });

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = edt_comment.getText().toString().trim();
                AddRate(dialog,notificationRateModel.getReceiver_id(),notificationRateModel.getDelegate_id(),rate,comment);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void AddRate(final AlertDialog alertDialog, int client_id, int delegate_id, double rate, String comment) {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .addRate(client_id,delegate_id,rate,comment)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            alertDialog.dismiss();
                            dialog.dismiss();
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            if (manager!=null)
                            {
                                manager.cancelAll();
                            }
                        }else
                            {
                                try {
                                    Log.e("error_code",response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {

                        try {
                            dialog.dismiss();
                            Log.e("Error",t.getMessage());
                            Toast.makeText(HomeActivity.this,getString(R.string.something), Toast.LENGTH_SHORT).show();
                        }catch (Exception re){}
                    }
                });
    }

    private void CreateToast(String msg)
    {
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
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
    public void Back()
    {

        if (fragment_home!=null && fragment_home.isAdded()&& fragment_home.isVisible())
        {

            if (orderItemsSingleTone.getItemsCount()>0)
            {
                CreateCartAlertDialog();
            }else
            {
                if (userModel!=null)
                {


                    finish();
                }else
                {
                    NavigateToSignInActivity();
                }
            }


        }else if (fragment_map!=null&&fragment_map.isVisible()) {
            fragmentManager.popBackStack("fragment_map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().show(fragment_myCart).commit();
            fragmentManager.beginTransaction().show(fragment_delivery_address).commit();

        }else if (fragment_date_time!=null&&fragment_date_time.isVisible()) {
            fragmentManager.popBackStack("fragment_date_time", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().show(fragment_myCart).commit();
            fragmentManager.beginTransaction().show(fragment_delivery_address).commit();

        }
        else if (fragment_delivery_address!=null&&fragment_delivery_address.isVisible()) {

            DisplayFragmentReview_Purchases();

        }
        else if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isVisible()) {

            DisplayFragmentDelivery_Address(total_order_cost_after_tax);

        }
        else if (fragment_subCategory!=null&&fragment_subCategory.isVisible()) {
            fragmentManager.popBackStack("fragment_subCategory", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            DisplayFragmentHome();

        }else if ((fragment_offers!=null&&fragment_offers.isVisible())||(fragment_myCart!=null&&fragment_myCart.isVisible())||(fragment_client_orders!=null&&fragment_client_orders.isVisible())||(fragment_client_profile!=null&&fragment_client_profile.isVisible()))
        {

            DisplayFragmentHome();

        }
        else
        {
            if (last_selected_fragment.equals("fragment_home"))
            {
                DisplayFragmentHome();

            }else if (last_selected_fragment.equals("fragment_sub_category"))
            {
                DisplayFragmentSubCategory(mainCategoryItems);
            }
        }
    }
    public void SignOut()
    {
        if (userModel!=null)
        {
            final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.sgin_out));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            String user_token = userModel.getToken();
            Api.getService()
                    .logout(user_token)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            Log.e("Error",response.code()+"_");
                            if (response.isSuccessful())
                            {
                                dismissSnackBar();
                                dialog.dismiss();
                                clearData();


                            }else
                            {
                                dialog.dismiss();

                                if (response.code() == 401)
                                {
                                    CreateSnackBar(getString(R.string.failed));
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
    }
    private void clearData()
    {
        fragmentManager.popBackStack();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager!=null)
        {
            manager.cancelAll();
        }
        preferences.ClearData(this);
        userSingleTone.clear();
        userModel = null;
        NavigateToSignInActivity();
    }

    @Override
    public void onBackPressed()
    {
        Back();
    }
    @Override
    protected void onDestroy()
    {
        if (intentService!=null)
        {
            StopLocationUpdate();
        }

        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }

        orderItemsSingleTone.ClearCart();

        super.onDestroy();

    }

}
