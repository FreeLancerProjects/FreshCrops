package com.appzone.freshcrops.activities_fragments.activity_sign_up;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.activities_fragments.activity_sign_in.SignInActivity;
import com.appzone.freshcrops.activities_fragments.activity_sign_up.fragment_sign_up.Fragment_Delegate_SignUp;
import com.appzone.freshcrops.activities_fragments.activity_sign_up.fragment_sign_up.Fragment_Terms_Conditions;
import com.appzone.freshcrops.activities_fragments.activity_sign_up.fragment_sign_up.Fragment_User_SignUp;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.share.Common;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class SignUpActivity extends AppCompatActivity implements Fragment_Terms_Conditions.ListenForTermsAndCondition{

    private LinearLayout ll_back;
    private ImageView image_back;
    private TextView tv_title;
    private String current_lang = "";
    private FragmentManager fragmentManager;
    private Fragment_User_SignUp fragment_user_signUp;
    private Fragment_Delegate_SignUp fragment_delegate_signUp;
    private Fragment_Terms_Conditions fragment_terms_conditions;
    private String type="";
    private View root;
    private Snackbar snackbar;
    private boolean isFromHome = false;
    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        getDataFromIntent();
    }


    private void initView()
    {
        Paper.init(this);
        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(this,current_lang);

        root = findViewById(R.id.root);
        fragmentManager = getSupportFragmentManager();
        image_back = findViewById(R.id.image_back);
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);

        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);

        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
    }
    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            if (intent.hasExtra("from"))
            {
                if (intent.getStringExtra("from").equals("home"))
                {
                    isFromHome=true;
                }
            }
            type = intent.getStringExtra("type");

            UpdateUI(type);

        }
    }
    private void UpdateUI(String type)
    {
       DisplayFragmentsSignUp(type);
    }

    public void DisplayFragmentTerms_Conditions()
    {
        tv_title.setText(R.string.terms_and_conditions);

        if (fragment_terms_conditions==null)
        {
            fragment_terms_conditions = Fragment_Terms_Conditions.newInstance();
        }

        if (fragment_terms_conditions.isAdded())
        {
            if (fragment_user_signUp!=null)
            {
                fragmentManager.beginTransaction().hide(fragment_user_signUp).commit();
            }else if (fragment_delegate_signUp!=null)
            {
                fragmentManager.beginTransaction().hide(fragment_delegate_signUp).commit();

            }

            fragmentManager.beginTransaction().show(fragment_terms_conditions).commit();



        }else
        {
            if (fragment_user_signUp!=null)
            {
                fragmentManager.beginTransaction().hide(fragment_user_signUp).commit();
            }else if (fragment_delegate_signUp!=null)
            {
                fragmentManager.beginTransaction().hide(fragment_delegate_signUp).commit();

            }

            fragment_terms_conditions = Fragment_Terms_Conditions.newInstance();
            fragmentManager.beginTransaction().add(R.id.sign_up_fragment_container, fragment_terms_conditions,"fragment_terms_conditions").addToBackStack("fragment_terms_conditions").commit();

        }
    }

    public void DisplayFragmentsSignUp(String type)
    {
        switch (type)
        {
            case "1":
                tv_title.setText(getString(R.string.new_account));

                if (fragment_user_signUp==null)
                {
                    fragment_user_signUp = Fragment_User_SignUp.newInstance();

                }

                if (fragment_terms_conditions==null)
                {
                    if (fragment_user_signUp.isAdded())
                    {
                        fragmentManager.beginTransaction().show(fragment_user_signUp).commit();
                    }else
                        {
                            fragmentManager.beginTransaction().add(R.id.sign_up_fragment_container, fragment_user_signUp,"fragment_user_signUp").addToBackStack("fragment_user_signUp").commit();

                        }
                }else
                    {
                        if (fragment_terms_conditions.isVisible())
                        {
                            fragmentManager.beginTransaction().hide(fragment_terms_conditions).commit();
                        }

                        fragmentManager.beginTransaction().show(fragment_user_signUp).commit();


                    }

                break;

            case "2" :
                tv_title.setText(R.string.delg_su);

                if (fragment_delegate_signUp==null)
                {
                    fragment_delegate_signUp = Fragment_Delegate_SignUp.newInstance();

                }

                if (fragment_terms_conditions==null)
                {
                    if (fragment_delegate_signUp.isAdded())
                    {
                        fragmentManager.beginTransaction().show(fragment_delegate_signUp).commit();
                    }else
                    {
                        fragmentManager.beginTransaction().add(R.id.sign_up_fragment_container, fragment_delegate_signUp,"fragment_delegate_signUp").addToBackStack("fragment_delegate_signUp").commit();

                    }
                }else
                {
                    if (fragment_terms_conditions.isVisible())
                    {
                        fragmentManager.beginTransaction().hide(fragment_terms_conditions).commit();
                    }

                    fragmentManager.beginTransaction().show(fragment_delegate_signUp).commit();


                }
                break;
        }
    }

    public void Back()
    {
        if (fragment_terms_conditions!=null&&fragment_terms_conditions.isVisible())
        {
            if (fragment_user_signUp!=null&&fragment_user_signUp.isAdded())
            {
                fragment_user_signUp.update_checkbox(false);
            }else if (fragment_delegate_signUp!=null&&fragment_delegate_signUp.isAdded())
            {
                fragment_delegate_signUp.update_checkbox(false);
            }
            DisplayFragmentsSignUp(type);

        }else
            {
                if (isFromHome)
                {
                    NavigateToHome();
                }else
                    {
                        NavigateToSignInActivity();

                    }
            }
    }

    private void NavigateToSignInActivity()
    {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void NavigateToHome()
    {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment :fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment :fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed()
    {
        Back();
    }

    @Override
    public void onChecked(boolean isChecked)
    {
        if (fragment_user_signUp!=null&&fragment_user_signUp.isAdded())
        {
            fragment_user_signUp.update_checkbox(true);
        }else if (fragment_delegate_signUp!=null&&fragment_delegate_signUp.isAdded())
        {
            fragment_delegate_signUp.update_checkbox(true);
        }
        DisplayFragmentsSignUp(type);

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
}
