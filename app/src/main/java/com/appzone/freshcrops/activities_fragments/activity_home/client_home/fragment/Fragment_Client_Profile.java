package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.activities_fragments.terms_contact_us_activity.activity.Terms_Conditions_Activity;
import com.appzone.freshcrops.models.ResponseModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;
import com.appzone.freshcrops.singletone.UserSingleTone;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Client_Profile extends Fragment {
    private TextView tv_name,tv_phone,tv_alter_phone,tv_lang,tv_points;
    private ImageView image_arrow1,image_arrow2,image_arrow3,image_arrow4,image_arrow5;
    private LinearLayout ll_phone,ll_password,ll_language,ll_share,ll_logout,ll_data_container,ll_alter_phone;
    private FrameLayout fl_terms,fl_contact_us,fl_about_app;
    private ImageView image_twitter,image_facebook,image_instagram;
    private AppBarLayout app_bar;
    private String current_lang;
    private HomeActivity activity;
    private  AlertDialog dialogUpdatePhone,dialogUpdatePassword,dialogContactUs;
    private UserModel userModel;
    private UserSingleTone userSingleTone;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_profile,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Client_Profile newInstance()
    {
        return new Fragment_Client_Profile();
    }

    private void initView(View view)
    {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        preferences = Preferences.getInstance();
        activity = (HomeActivity) getActivity();
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_alter_phone = view.findViewById(R.id.tv_alter_phone);

        //////////////////////////////////////////////
        ll_data_container = view.findViewById(R.id.ll_data_container);
        app_bar = view.findViewById(R.id.app_bar);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int total_offset = appBarLayout.getTotalScrollRange();
                if ((total_offset+verticalOffset)>90)
                {
                    ll_data_container.setVisibility(View.VISIBLE);
                }else
                    {
                        ll_data_container.setVisibility(View.GONE);

                    }
            }
        });
        /////////////////////////////////////////////
        tv_points = view.findViewById(R.id.tv_points);

        tv_lang = view.findViewById(R.id.tv_lang);

        image_arrow1 = view.findViewById(R.id.image_arrow1);
        image_arrow2 = view.findViewById(R.id.image_arrow2);
        image_arrow3 = view.findViewById(R.id.image_arrow3);
        image_arrow4 = view.findViewById(R.id.image_arrow4);
        image_arrow5 = view.findViewById(R.id.image_arrow5);

        ll_phone = view.findViewById(R.id.ll_phone);
        ll_password = view.findViewById(R.id.ll_password);
        ll_alter_phone = view.findViewById(R.id.ll_alter_phone);
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
            image_arrow5.setImageResource(R.drawable.arrow_blue_left);

            tv_lang.setText(getString(R.string.lang)+" : " + "العربية");

        }else
            {
                image_arrow1.setImageResource(R.drawable.arrow_blue_right);
                image_arrow2.setImageResource(R.drawable.arrow_blue_right);
                image_arrow3.setImageResource(R.drawable.arrow_blue_right);
                image_arrow4.setImageResource(R.drawable.arrow_blue_right);
                image_arrow5.setImageResource(R.drawable.arrow_blue_right);


                tv_lang.setText(getString(R.string.lang)+" : " + "English");

            }

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

        ll_alter_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String alter_phone="";
                if (userModel.getUser().getAlternative_phone()!=null&&!TextUtils.isEmpty(userModel.getUser().getAlternative_phone()))
                {
                    alter_phone = userModel.getUser().getAlternative_phone().trim();
                }
                CreateUpdateAlterPhoneDialog(alter_phone);
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
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.appzone.freshcrops"));
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
                String uri = "whatsapp://send?phone=966551011284";
                CreateSocialMediaIntent(uri);
            }
        });

        UpdateUI(userModel);
    }


    private void UpdateUI(UserModel userModel)
    {
        if (userModel!=null)
        {
            tv_name.setText(userModel.getUser().getName());
            tv_phone.setText("00966"+userModel.getUser().getPhone());

            if (userModel.getUser().getAlternative_phone()!=null||!TextUtils.isEmpty(userModel.getUser().getAlternative_phone()))
            {
                tv_alter_phone.setText("00966"+userModel.getUser().getAlternative_phone());
            }else
                {
                    tv_alter_phone.setText(R.string.no_alternative_phone);

                }

                if (userModel.getUser().getPoints()>0)
                {
                    tv_points.setText(userModel.getUser().getPoints()+"");
                }else
                    {
                        tv_points.setText("0");

                    }
        }

    }


    private void CreateSocialMediaIntent(String uri)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
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
       // dialogUpdatePhone.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialogUpdatePhone.show();
    }

    private void CreateUpdateAlterPhoneDialog(String phone)
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

                    UpdateAlterPhone(m_phone);

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
        //dialogUpdatePhone.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialogUpdatePhone.show();
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

    private void UpdateAlterPhone(String m_phone)
    {


        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.updating_phone));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        String user_token = userModel.getToken();
        Api.getService()
                .updateAlterPhone(user_token,m_phone)
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
                                Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }


                        }else
                        {
                            dialog.dismiss();
                            if (response.code() == 422)
                            {
                                activity.CreateSnackBar(getString(R.string.incorrect_old_password));

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
    private void ContactUs(String m_name, String m_msg, String m_phone)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.show();
        String ph = m_phone;
        Api.getService()
                .sendContactUs(m_name,ph,m_msg)
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
