package com.appzone.freshcrops.activities_fragments.activity_sign_up.fragment_sign_up;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_sign_up.SignUpActivity;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.models.Terms_Condition_Model;
import com.appzone.freshcrops.remote.Api;

import java.util.Locale;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Terms_Conditions extends Fragment {

    private TextView tv_content;
    private FrameLayout fl_accept;
    private LinearLayout ll;
    private SmoothProgressBar smooth_progress;
    private ListenForTermsAndCondition listener;
    private SignUpActivity activity;
    private String current_lang;
    private ImageView image_arrow;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_condition,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Terms_Conditions newInstance()
    {
        return new Fragment_Terms_Conditions();
    }

    private void initView(View view) {
        Paper.init(getActivity());
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(getActivity(),current_lang);
        image_arrow = view.findViewById(R.id.image_arrow);
        if (current_lang.equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.arrow_right);
        }else
            {
                image_arrow.setImageResource(R.drawable.arrow_left);

            }
        tv_content = view.findViewById(R.id.tv_content);
        fl_accept = view.findViewById(R.id.fl_accept);
        smooth_progress = view.findViewById(R.id.smooth_progress);
        ll = view.findViewById(R.id.ll);

        fl_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChecked(true);
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
                            activity.dismissSnackBar();
                            smooth_progress.setVisibility(View.GONE);
                            if (current_lang.equals("ar"))
                            {
                                tv_content.setText(response.body().getSite_terms_conditions().getAr());
                            }else
                            {
                                tv_content.setText(response.body().getSite_terms_conditions().getEn());

                            }

                            ll.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Terms_Condition_Model> call, Throwable t) {
                        try {
                            smooth_progress.setVisibility(View.GONE);
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Erorr",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SignUpActivity) context;
        listener = activity;
    }

    public interface ListenForTermsAndCondition
    {
        void onChecked(boolean isChecked);
    }


}
