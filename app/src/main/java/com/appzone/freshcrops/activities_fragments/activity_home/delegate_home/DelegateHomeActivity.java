package com.appzone.freshcrops.activities_fragments.activity_home.delegate_home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment.Fragment_Delegate_Profile;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment.fragment_delegate_orders.Fragment_Delegate_New_Order;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment.fragment_delegate_orders.Fragment_Delegate_Orders;
import com.appzone.freshcrops.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.freshcrops.activities_fragments.activity_sign_in.SignInActivity;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelegateHomeActivity extends AppCompatActivity implements Fragment_Delegate_New_Order.ListenerUpdateFragmentDelegateCurrentOrder{

    private FragmentManager fragmentManager;
    private AHBottomNavigation ahBottomNavigation;
    //private Fragment_Delegate_Notification fragment_delegate_notification;
    private Fragment_Delegate_Orders fragment_delegate_orders;
    private Fragment_Delegate_Profile fragment_delegate_profile;
    private String current_lang = "";
    private View root;
    private Snackbar snackbar;
    private UserSingleTone userSingleTone;
    private Preferences preferences;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        current_lang =Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(newBase,current_lang));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_home);
        initView();
        getDataFromIntent();

    }



    private void initView()
    {
        userSingleTone =  UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        preferences = Preferences.getInstance();
        root = findViewById(R.id.root);
        fragmentManager=getSupportFragmentManager();
        ahBottomNavigation = findViewById(R.id.ah_bottom_nav);

        ahBottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(this,R.color.white));
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setTitleTextSizeInSp(13,14);
        ahBottomNavigation.setForceTint(true);
        ahBottomNavigation.setColored(false);
        ahBottomNavigation.setAccentColor(ContextCompat.getColor(this,R.color.colorPrimary));
        ahBottomNavigation.setInactiveColor(ContextCompat.getColor(this,R.color.gray_text));

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.me),R.drawable.bottom_nav_user,R.color.gray_text);
        //AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.my_notification),R.drawable.nav_bottom_notfication,R.color.gray_text);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.my_order),R.drawable.bottom_nav_cart,R.color.gray_text);

        ahBottomNavigation.addItem(item1);
       // ahBottomNavigation.addItem(item2);
        ahBottomNavigation.addItem(item3);

        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                UpdateBottomNavPos(position);

                switch (position)
                {
                    case 0:
                        DisplayFragmentDriverProfile();


                        break;
                    case 1:
                        DisplayFragmentDriverOrders();

                        //DisplayFragmentDriverNotification();

                        break;

                }
                return false;
            }
        });

        DisplayFragmentDriverProfile();
        updateUserFireBaseToken();

    }
    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("signup"))
        {
            if (intent.getIntExtra("signup",0)==1)
            {
                CreateWelcomeNotification();
            }
        }else if (intent!=null && intent.hasExtra("status"))
        {
            if (intent.getIntExtra("status",0) == 1)
            {
                DisplayFragmentDriverOrders();
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fragment_delegate_orders.setPage(0);

                            }
                        },100);

            }

        }
    }

    private void CreateWelcomeNotification()
    {
        String sound_path = "android.resource://"+getPackageName()+"/"+R.raw.delegate;
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
            builder.setContentText(getString(R.string.welcome_delegate_not));
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
                builder.setContentText(getString(R.string.welcome_delegate_not));
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
    public void UpdateUserData(UserModel userModel)
    {
        this.userModel = userModel;
    }

    private void UpdateBottomNavPos(int pos)
    {
        ahBottomNavigation.setCurrentItem(pos,false);
    }

    private void DisplayFragmentDriverProfile()
    {
       /* if (fragment_delegate_notification !=null&& fragment_delegate_notification.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_delegate_notification).commit();
        }*/
        if (fragment_delegate_orders !=null&& fragment_delegate_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_orders).commit();
        }

        if (fragment_delegate_profile ==null)
        {
            fragment_delegate_profile = Fragment_Delegate_Profile.newInstance();
        }

        if (fragment_delegate_profile.isAdded())
        {
            if (!fragment_delegate_profile.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_delegate_profile).commit();
                UpdateBottomNavPos(0);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container, fragment_delegate_profile,"fragment_delegate_profile").addToBackStack("fragment_delegate_profile").commit();
            UpdateBottomNavPos(0);
        }




    }
    private void DisplayFragmentDriverNotification()
    {
        if (fragment_delegate_profile !=null&& fragment_delegate_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_delegate_profile).commit();
        }

        if (fragment_delegate_orders !=null&& fragment_delegate_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_orders).commit();
        }


        /*if (fragment_delegate_notification ==null)
        {
            fragment_delegate_notification = Fragment_Delegate_Notification.newInstance();
        }
        if (fragment_delegate_notification.isAdded())
        {
            if (!fragment_delegate_notification.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_delegate_notification).commit();
                UpdateBottomNavPos(1);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container, fragment_delegate_notification,"fragment_delegate_notification").addToBackStack("fragment_delegate_notification").commit();
            UpdateBottomNavPos(1);
        }
*/


    }
    public void DisplayFragmentDriverOrders()
    {
        if (fragment_delegate_profile !=null&& fragment_delegate_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_profile).commit();
        }

      /*  if (fragment_delegate_notification !=null&& fragment_delegate_notification.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_notification).commit();
        }*/

        if (fragment_delegate_orders == null)
        {
            fragment_delegate_orders = Fragment_Delegate_Orders.newInstance();
        }

        if (fragment_delegate_orders.isAdded())
        {
            if (!fragment_delegate_orders.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_delegate_orders).commit();
                UpdateBottomNavPos(2);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container, fragment_delegate_orders,"fragment_delegate_orders").addToBackStack("fragment_delegate_orders").commit();

            UpdateBottomNavPos(2);
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
    public void UpdateNotificationCount(int count)
    {
        if (count > 0 )
        {
            AHNotification.Builder builder = new AHNotification.Builder();
            builder.setBackgroundColor(ContextCompat.getColor(this,R.color.green_text));
            builder.setTextColor(ContextCompat.getColor(this,R.color.white));
            builder.setText(String.valueOf(count));

            ahBottomNavigation.setNotification(builder.build(),2);


        }else
        {
            AHNotification.Builder builder = new AHNotification.Builder();
            builder.setBackgroundColor(ContextCompat.getColor(this,R.color.green_text));
            builder.setTextColor(ContextCompat.getColor(this,R.color.white));
            builder.setText("");
            ahBottomNavigation.setNotification(builder.build(),2);
        }
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
    private void NavigateToSignInActivity()
    {
        Intent intent  = new Intent(DelegateHomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void NavigateToOrderDetailsActivity(OrdersModel.Order order)
    {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("order",order);
        intent.putExtra("order_type", Tags.order_current);
        startActivityForResult(intent,3);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode ==3 && resultCode == RESULT_OK)
        {
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (fragment_delegate_orders!=null && fragment_delegate_orders.isAdded())
                            {
                                fragment_delegate_orders.RefreshFragment();
                            }

                            if (fragment_delegate_profile!=null && fragment_delegate_profile.isAdded())
                            {
                                fragment_delegate_profile.UpdateProfile();
                            }
                        }
                    },1);

        }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
    public void onBackPressed() {
        Back();
    }

    private void Back()
    {
        if (fragment_delegate_profile !=null&& fragment_delegate_profile.isAdded()&& fragment_delegate_profile.isVisible())
        {
            if (userModel!=null)
            {
                fragmentManager.popBackStack();
                finish();
            }else
            {
                fragmentManager.popBackStack();
                NavigateToSignInActivity();
            }
        }else
            {
                DisplayFragmentDriverProfile();

            }
    }

    @Override
    public void onUpdated() {

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("ddd","ddd");
                        if (fragment_delegate_orders!=null && fragment_delegate_orders.isAdded())
                        {

                            Log.e("emaaaaaad","emad");
                            fragment_delegate_orders.RefreshFragment();


                        }




                        if (fragment_delegate_profile!=null && fragment_delegate_profile.isAdded())
                        {
                            fragment_delegate_profile.UpdateProfile();
                        }

                    }
                },1);

    }
}
