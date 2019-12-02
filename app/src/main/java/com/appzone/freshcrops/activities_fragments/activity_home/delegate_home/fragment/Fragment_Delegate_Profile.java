package com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.DelegateHomeActivity;
import com.appzone.freshcrops.activities_fragments.terms_contact_us_activity.activity.Terms_Conditions_Activity;
import com.appzone.freshcrops.models.GainModel;
import com.appzone.freshcrops.models.ResponseModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Delegate_Profile extends Fragment {
    private DelegateHomeActivity activity;
    private Toolbar toolBar;
    private AppBarLayout app_bar;
    private TextView tv_name,tv_phone,tv_lang,tv_rate,tv_total,tv_refused_order,tv_done_order,tv_credit_limit,tv_dukkan_receivable,tv_my_receivable,tv_member_ship;
    private ImageView image_arrow1,image_arrow2,image_arrow3,image_arrow4;
    private LinearLayout ll_phone,ll_password,ll_language,ll_share,ll_driver_container,ll_logout;
    private FrameLayout fl_terms,fl_contact_us,fl_about_app;
    private ImageView image_twitter,image_facebook,image_instagram;
    private CircleImageView image;
    private SimpleRatingBar rateBar;
    private String current_lang;
    private AlertDialog dialogUpdatePhone,dialogUpdatePassword,dialogContactUs;
    private UserModel userModel;
    private UserSingleTone userSingleTone;
    private Preferences preferences;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private int img_req=1;
    private Uri uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_delegate_profile, container, false);
       initView(view);
       return view;
    }

    public static Fragment_Delegate_Profile newInstance() {

        return new Fragment_Delegate_Profile();
    }

    private void initView(View view)
    {

        activity = (DelegateHomeActivity) getActivity();
        toolBar = view.findViewById(R.id.toolBar);
        app_bar = view.findViewById(R.id.app_bar);
        ll_driver_container = view.findViewById(R.id.ll_driver_container);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int constant =220;
                if ((appBarLayout.getTotalScrollRange()+verticalOffset) <= constant)
                {
                    ll_driver_container.setVisibility(View.GONE);
                    toolBar.setBackgroundResource(R.color.colorPrimary);
                }else
                    {
                        ll_driver_container.setVisibility(View.VISIBLE);

                        toolBar.setBackgroundResource(R.color.transparent);
                    }
            }
        });

        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        preferences = Preferences.getInstance();
        tv_name = view.findViewById(R.id.tv_name);
        tv_rate = view.findViewById(R.id.tv_rate);
        tv_member_ship = view.findViewById(R.id.tv_member_ship);
        tv_total = view.findViewById(R.id.tv_total);
        tv_refused_order = view.findViewById(R.id.tv_refused_order);
        tv_done_order = view.findViewById(R.id.tv_done_order);
        tv_credit_limit = view.findViewById(R.id.tv_credit_limit);
        tv_dukkan_receivable = view.findViewById(R.id.tv_dukkan_receivable);
        tv_my_receivable = view.findViewById(R.id.tv_my_receivable);



        tv_phone = view.findViewById(R.id.tv_phone);
        tv_lang = view.findViewById(R.id.tv_lang);
        rateBar = view.findViewById(R.id.rateBar);
        image = view.findViewById(R.id.image);

        image_arrow1 = view.findViewById(R.id.image_arrow1);
        image_arrow2 = view.findViewById(R.id.image_arrow2);
        image_arrow3 = view.findViewById(R.id.image_arrow3);
        image_arrow4 = view.findViewById(R.id.image_arrow4);

        ll_phone = view.findViewById(R.id.ll_phone);
        ll_password = view.findViewById(R.id.ll_password);
        ll_language = view.findViewById(R.id.ll_language);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_share = view.findViewById(R.id.ll_share);
        image_twitter = view.findViewById(R.id.image_twitter);
        image_facebook = view.findViewById(R.id.image_facebook);
        image_instagram = view.findViewById(R.id.image_instagram);

        fl_terms = view.findViewById(R.id.fl_terms);
        fl_contact_us = view.findViewById(R.id.fl_contact_us);
        fl_about_app = view.findViewById(R.id.fl_about_app);

        Paper.init(getActivity());
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        if (current_lang.equals("ar"))
        {
            image_arrow1.setImageResource(R.drawable.arrow_blue_left);
            image_arrow2.setImageResource(R.drawable.arrow_blue_left);
            image_arrow3.setImageResource(R.drawable.arrow_blue_left);
            image_arrow4.setImageResource(R.drawable.arrow_blue_left);

            tv_lang.setText(getString(R.string.lang)+" : " + "العربية");

        }else
        {
            image_arrow1.setImageResource(R.drawable.arrow_blue_right);
            image_arrow2.setImageResource(R.drawable.arrow_blue_right);
            image_arrow3.setImageResource(R.drawable.arrow_blue_right);
            image_arrow4.setImageResource(R.drawable.arrow_blue_right);


            tv_lang.setText(getString(R.string.lang)+" : " + "English");

        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(img_req);
            }
        });

        ll_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLanguageDialog(current_lang);
            }
        });

        ll_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = userModel.getUser().getPhone();
                CreateUpdatePhoneDialog(phone);
            }
        });

        ll_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUpdatePasswordDialog();
            }
        });


        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.SignOut();

            }
        });

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share();
            }
        });

        fl_about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.appzone.freshcrops"));
                startActivity(intent);

            }
        });

        fl_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Terms_Conditions_Activity.class);
                startActivity(intent);
            }
        });

        image_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String uri = "whatsapp://send?phone=966551011284";
                CreateSocialMediaIntent(uri);*/
            }
        });

        image_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "https://www.facebook.com/dukkan.app.5";
                CreateSocialMediaIntent(uri);
            }
        });

        image_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "https://instagram.com/dukkanapp?utm_source=ig_profile_share&igshid=1a8okmwc5pex7";
                CreateSocialMediaIntent(uri);
            }
        });


        fl_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "https://t.me/DukkanApp";
                CreateSocialMediaIntent(uri);
            }
        });

        UpdateUI(userModel);
    }

    private void UpdateUI(UserModel userModel)
    {
        if (userModel!=null)
        {
            Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser().getAvatar())).fit().into(image);
            tv_name.setText(userModel.getUser().getName());
            tv_phone.setText("00966"+userModel.getUser().getPhone());
            tv_rate.setText("("+userModel.getUser().getRate()+")");
            tv_member_ship.setText(userModel.getUser().getMembership()+"");
            SimpleRatingBar.AnimationBuilder builder = rateBar.getAnimationBuilder();
            builder.setDuration(1500);
            builder.setRepeatCount(0);
            builder.setRatingTarget((float) userModel.getUser().getRate());
            builder.setInterpolator(new AccelerateInterpolator());
            builder.start();

            tv_total.setText(userModel.getUser().getTotal_gain()+" "+getString(R.string.rsa));
            tv_refused_order.setText(userModel.getUser().getEscaped_orders_count()+"");
            tv_done_order.setText(userModel.getUser().getUser_orders_count()+"");
            tv_my_receivable.setText(userModel.getUser().getMy_gain()+" "+getString(R.string.rsa));
            tv_dukkan_receivable.setText(userModel.getUser().getDukkan_gain()+" "+getString(R.string.rsa));
            getMaxGain();

        }

    }

    private void getMaxGain() {
        Api.getService()
                .getMaxGain().enqueue(new Callback<GainModel>() {
            @Override
            public void onResponse(Call<GainModel> call, Response<GainModel> response) {
                if (response.isSuccessful())
                {
                    tv_credit_limit.setText(response.body().getDukkan_max_gain()+" "+getString(R.string.rsa));
                }else
                    {
                        try {
                            Log.e("Error",response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
            }

            @Override
            public void onFailure(Call<GainModel> call, Throwable t) {
                try {
                    Log.e("Error",t.getMessage());
                }catch (Exception e){}
            }
        });
    }

    private void CreateSocialMediaIntent(String uri)
    {
        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);*/
    }

    private void Share()
    {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"تطبيق محاصيل طازجة رابط التحميل : https://play.google.com/store/apps/details?id=com.appzone.freshcrops");
        startActivity(intent);
    }

    private void CreateLanguageDialog(final String defLang)
    {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_language,null);
        final RadioButton rb_ar = view.findViewById(R.id.rb_ar);
        final RadioButton rb_en = view.findViewById(R.id.rb_en);
        CardView cardView_ar = view.findViewById(R.id.cardView_ar);
        CardView cardView_en = view.findViewById(R.id.cardView_en);

        if (defLang.equals("ar"))
        {
            rb_ar.setChecked(true);
            rb_en.setChecked(false);
        }else
        {
            rb_ar.setChecked(false);
            rb_en.setChecked(true);
        }

        rb_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_en.setChecked(false);
                if (!defLang.equals("ar"))
                {
                    dialog.dismiss();
                    activity.ChangeLanguage("ar");
                }

            }
        });

        rb_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_ar.setChecked(false);
                if (defLang.equals("ar"))
                {
                    dialog.dismiss();

                    activity.ChangeLanguage("en");
                }
            }
        });

        cardView_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_en.setChecked(false);
                if (!defLang.equals("ar"))
                {
                    dialog.dismiss();

                    activity.ChangeLanguage("ar");
                }

            }
        });

        cardView_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_ar.setChecked(false);
                if (defLang.equals("ar"))
                {
                    dialog.dismiss();

                    activity.ChangeLanguage("en");
                }

            }
        });

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialog.show();
    }

    private void CreateUpdatePhoneDialog(String phone)
    {
        dialogUpdatePhone = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_phone,null);
        final EditText edt_phone = view.findViewById(R.id.edt_phone);
        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        edt_phone.setText(phone);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.CloseKeyBoard(getActivity(),edt_phone);
                dialogUpdatePhone.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m_phone = edt_phone.getText().toString();
                if (!TextUtils.isEmpty(m_phone) && m_phone.length()==9)
                {
                    edt_phone.setError(null);
                    Common.CloseKeyBoard(getActivity(),edt_phone);
                    dialogUpdatePhone.dismiss();

                    UpdatePhone(m_phone);

                }else
                {
                    if (TextUtils.isEmpty(m_phone))
                    {
                        edt_phone.setError(getString(R.string.field_req));
                    }else if (m_phone.length()!=9)
                    {
                        edt_phone.setError(getString(R.string.inv_phone));

                    }
                }



            }
        });

        dialogUpdatePhone.setView(view);
        dialogUpdatePhone.setCanceledOnTouchOutside(true);
        dialogUpdatePhone.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialogUpdatePhone.show();
    }


    private void Check_ReadPermission(int img_req)
    {
        if (ContextCompat.checkSelfPermission(getActivity(),read_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{read_permission},img_req);
        }else
        {
            select_photo(img_req);
        }
    }


    private void select_photo(int img_req) {
        Intent intent ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);

        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent,img_req);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == img_req && resultCode == Activity.RESULT_OK && data!=null)
        {
            uri = data.getData();
            Log.e("fdsfsd","mmmmmmmmmmmm");
            UpdatePhoto(uri);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == img_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(img_req);
                }else
                {
                    Toast.makeText(activity,getString(R.string.access_image_denied), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void CreateUpdatePasswordDialog()
    {
        dialogUpdatePassword = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_password,null);
        final EditText edt_current_password = view.findViewById(R.id.edt_current_password);
        final EditText edt_new_password = view.findViewById(R.id.edt_new_password);

        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        //edt_phone.setText(userModel.);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.CloseKeyBoard(getActivity(),edt_current_password);
                dialogUpdatePassword.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m_current_password = edt_current_password.getText().toString();
                String m_new_password = edt_new_password.getText().toString();

                if (!TextUtils.isEmpty(m_current_password) && !TextUtils.isEmpty(m_new_password))
                {
                    edt_current_password.setError(null);
                    edt_new_password.setError(null);

                    Common.CloseKeyBoard(getActivity(),edt_current_password);
                    dialogUpdatePassword.dismiss();

                    UpdatePassword(m_current_password,m_new_password);
                }else
                {
                    if (TextUtils.isEmpty(m_current_password))
                    {
                        edt_current_password.setError(getString(R.string.field_req));
                    }

                    if (TextUtils.isEmpty(m_new_password))
                    {
                        edt_new_password.setError(getString(R.string.field_req));
                    }
                }



            }
        });

        dialogUpdatePassword.setView(view);
        dialogUpdatePassword.setCanceledOnTouchOutside(true);
        //dialogUpdatePassword.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialogUpdatePassword.show();
    }

    private void CreateContactUsDialog()
    {
        dialogContactUs = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_contact_us,null);
        final EditText edt_name = view.findViewById(R.id.edt_name);
        final EditText edt_msg = view.findViewById(R.id.edt_msg);
        final EditText edt_phone = view.findViewById(R.id.edt_phone);
        Button btn_send = view.findViewById(R.id.btn_send);



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m_name = edt_name.getText().toString().trim();
                String m_msg = edt_msg.getText().toString().trim();
                String m_phone = edt_phone.getText().toString().trim();
                if (!TextUtils.isEmpty(m_name) && !TextUtils.isEmpty(m_msg)&&!TextUtils.isEmpty(m_phone)&&m_phone.length()==9)
                {
                    edt_name.setText("");
                    edt_phone.setText("");
                    edt_msg.setText("");

                    edt_name.setError(null);
                    edt_msg.setError(null);
                    edt_phone.setError(null);

                    Common.CloseKeyBoard(getActivity(),edt_name);
                    dialogContactUs.dismiss();

                    ContactUs(m_name,m_msg,m_phone);
                }else
                {
                    if (TextUtils.isEmpty(m_name))
                    {
                        edt_name.setError(getString(R.string.field_req));
                    }

                    if (TextUtils.isEmpty(m_msg))
                    {
                        edt_msg.setError(getString(R.string.field_req));
                    }

                    if (TextUtils.isEmpty(m_phone))
                    {
                        edt_phone.setError(getString(R.string.field_req));
                    }else if (m_phone.length() != 9 )
                    {
                        edt_phone.setError(getString(R.string.inv_phone));
                    }
                }



            }
        });

        dialogContactUs.setView(view);
        dialogContactUs.setCanceledOnTouchOutside(true);
        //dialogContactUs.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialogContactUs.show();
    }

    private void UpdatePhoto(Uri uri)
    {


        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        RequestBody user_token_part = Common.getRequestBodyText(userModel.getToken());
        MultipartBody.Part image_part = Common.getMultiPart(getActivity(),uri,"avatar");
        Api.getService()
                .updateImage(user_token_part,image_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        Log.e("error",response.code()+"_");

                        if (response.isSuccessful())
                        {

                            if (response.body()!=null && response.body().getUser()!=null)
                            {
                                Log.e("avatar",response.body().getUser().getAvatar()+"_");
                                activity.dismissSnackBar();
                                dialog.dismiss();
                                Toast.makeText(activity,getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                UpdateUserData(response.body());
                            }else
                            {
                                Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }else
                        {
                            dialog.dismiss();

                            try {
                                Log.e("Error",response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 422)
                            {
                                activity.CreateSnackBar(getString(R.string.phone_number_exists));

                            }else
                            {
                                activity.CreateSnackBar(getString(R.string.failed));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void UpdatePhone(String m_phone)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updating_phone));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        String user_token = userModel.getToken();
        Api.getService()
                .updatePhone(user_token,m_phone)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        Log.e("error",response.code()+"_");

                        if (response.isSuccessful())
                        {
                            if (response.body()!=null && response.body().getUser()!=null)
                            {
                                activity.dismissSnackBar();
                                dialog.dismiss();
                                Toast.makeText(activity,getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                UpdateUserData(response.body());
                            }else
                            {
                                Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }else
                        {
                            dialog.dismiss();

                            if (response.code() == 422)
                            {
                                activity.CreateSnackBar(getString(R.string.phone_number_exists));

                            }else
                                {
                                    activity.CreateSnackBar(getString(R.string.failed));

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdatePassword(String m_current_password, String m_new_password)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updating_phone));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        String user_token = userModel.getToken();
        Api.getService()
                .updatePassword(user_token,m_current_password,m_new_password)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful())
                        {
                            if (response.body()!=null && response.body().getUser()!=null)
                            {
                                activity.dismissSnackBar();
                                dialog.dismiss();
                                Toast.makeText(activity,getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                UpdateUserData(response.body());
                            }else
                            {
                                dialog.dismiss();
                                if (response.code() == 422)
                                {
                                    activity.CreateSnackBar(getString(R.string.incorrect_old_password));

                                }else
                                {
                                    activity.CreateSnackBar(getString(R.string.failed));

                                }                            }


                        }else
                        {

                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.failed));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void ContactUs(String m_name, String m_msg, String m_phone)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.show();
        Api.getService()
                .sendContactUs(m_name,m_phone,m_msg)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getCode()==200)
                            {
                                dialog.dismiss();
                                Toast.makeText(activity, R.string.succ, Toast.LENGTH_LONG).show();
                            }else
                            {
                                Toast.makeText(activity, R.string.failed, Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_LONG).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    public void UpdateProfile()
    {
        Api.getService()
                .getUserData(userModel.getToken())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {
                                Log.e("skip",response.body().getUser().getEscaped_orders_count()+"");
                                Log.e("skip2",response.body().getUser().getUser_orders_count()+"");

                                Log.e("avatar",response.body().getUser().getAvatar()+"_");
                                UpdateUserData(response.body());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {

                    }
                });
    }
    private void UpdateUserData(UserModel userModel)
    {
        UpdateUI(userModel);
        this.userModel = userModel;
        userSingleTone.setUserModel(userModel);
        preferences.create_update_userData(getActivity(),userModel);
        activity.UpdateUserData(userModel);

    }


}
