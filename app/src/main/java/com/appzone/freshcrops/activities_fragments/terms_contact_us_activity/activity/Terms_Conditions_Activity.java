package com.appzone.freshcrops.activities_fragments.terms_contact_us_activity.activity;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.models.Terms_Condition_Model;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.share.Common;

import java.util.Locale;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Terms_Conditions_Activity extends AppCompatActivity {

    private FrameLayout fl;
    private LinearLayout ll_back;
    private SmoothProgressBar smooth_progress;
    private TextView tv_content;
    private ImageView image_back;
    private String current_lang = "";
    private View root;
    private Snackbar snackbar;
    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        initView();
    }

    private void initView() {

        Paper.init(this);
        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(this,current_lang);

        root = findViewById(R.id.root);
        image_back = findViewById(R.id.image_back);
        ll_back = findViewById(R.id.ll_back);
        smooth_progress = findViewById(R.id.smooth_progress);
        fl = findViewById(R.id.fl);
        tv_content = findViewById(R.id.tv_content);
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
                finish();
            }
        });

        getTerms_Condition();


    }

    private void getTerms_Condition() {
        Api.getService()
                .getTermsConditions()
                .enqueue(new Callback<Terms_Condition_Model>() {
                    @Override
                    public void onResponse(Call<Terms_Condition_Model> call, Response<Terms_Condition_Model> response) {
                        if (response.isSuccessful())
                        {
                            smooth_progress.setVisibility(View.GONE);
                            if (current_lang.equals("ar"))
                            {
                                tv_content.setText(response.body().getSite_terms_conditions().getAr());
                            }else
                                {
                                    tv_content.setText(response.body().getSite_terms_conditions().getEn());

                                }

                                fl.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Terms_Condition_Model> call, Throwable t) {
                        try {
                            smooth_progress.setVisibility(View.GONE);
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Erorr",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void CreateSnackBar(String msg)
    {
        snackbar = Common.CreateSnackBar(this,root,msg);
        snackbar.show();
    }
}
