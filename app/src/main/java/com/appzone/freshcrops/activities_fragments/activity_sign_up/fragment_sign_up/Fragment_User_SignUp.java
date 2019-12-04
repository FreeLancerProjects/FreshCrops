package com.appzone.freshcrops.activities_fragments.activity_sign_up.fragment_sign_up;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.activities_fragments.activity_sign_up.SignUpActivity;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_User_SignUp extends Fragment {
    private ImageView image_arrow;
    private EditText edt_name,edt_phone,edt_password;
    private Button btn_terms_conditions;
    private CheckBox checkbox_rule;
    private FrameLayout fl_sign_up;
    private boolean accept_rule = false;
    private SignUpActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_signup,container,false);
        initView(view);
        return view;
    }

    public static Fragment_User_SignUp newInstance()
    {
        return new Fragment_User_SignUp();
    }

    private void initView(View view) {

        activity = (SignUpActivity) getActivity();
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_password = view.findViewById(R.id.edt_password);
        btn_terms_conditions = view.findViewById(R.id.btn_terms_conditions);
        checkbox_rule = view.findViewById(R.id.checkbox_rule);
        fl_sign_up = view.findViewById(R.id.fl_sign_up);
        image_arrow = view.findViewById(R.id.image_arrow);

        Paper.init(getActivity());
        String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (lang.equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_arrow.setImageResource(R.drawable.arrow_left);

        }

        checkbox_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_rule.isChecked())
                {
                    accept_rule = true;
                }else
                    {
                        accept_rule = false;
                    }
            }
        });

        btn_terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentTerms_Conditions();
            }
        });
        fl_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
    }


    private void CheckData() {
        String m_name = edt_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_phone)&&
                m_phone.length()==9&&
                !TextUtils.isEmpty(m_password)&&
                accept_rule


                )
        {
            Common.CloseKeyBoard(getActivity(),edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_password.setError(null);
            Sign_Up(m_name,m_phone,m_password);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.field_req));
                }else
                    {
                        edt_name.setError(null);

                    }
                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (m_phone.length()!=9)
                {
                    edt_phone.setError(getString(R.string.inv_phone));
                }
                else
                {
                    edt_phone.setError(null);

                }

                if (TextUtils.isEmpty(m_password))
                {
                    edt_password.setError(getString(R.string.field_req));
                }else
                {
                    edt_password.setError(null);

                }

                if (!accept_rule)
                {
                    activity.CreateSnackBar(getString(R.string.cnt_sign_up));
                }

            }

    }

    private void Sign_Up(String m_name, String m_phone, String m_password) {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.signingup));
        dialog.show();
        Api.getService()
                .SignUp_Client(m_name,m_phone,m_password, Tags.user_client)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            activity.dismissSnackBar();
                            dialog.dismiss();
                            if (response.body()!=null && response.body().getUser()!=null)
                            {
                                UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                Preferences preferences = Preferences.getInstance();
                                UserModel userModel = response.body();
                                userSingleTone.setUserModel(userModel);
                                preferences.create_update_userData(getActivity(),userModel);

                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                intent.putExtra("signup",1);
                                startActivity(intent);
                                getActivity().finish();

                            }else
                                {
                                    Common.CreateSignAlertDialog(getActivity(),getString(R.string.something));
                                }
                        }else
                            {
                                activity.dismissSnackBar();
                                dialog.dismiss();

                                if (response.code()==422)
                                {
                                    Common.CreateSignAlertDialog(getActivity(),getString(R.string.phone_number_exists));

                                }
                                try {
                                    Log.e("Erorr",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
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

    public void update_checkbox(boolean isChecked)
    {
        activity.dismissSnackBar();
        accept_rule = isChecked;
        checkbox_rule.setChecked(isChecked);
    }





}
