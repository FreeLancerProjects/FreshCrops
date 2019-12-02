package com.appzone.freshcrops.activities_fragments.activity_show_image;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;


public class Activity_Show_Image extends AppCompatActivity {

    private ImageView image,image_back;
    private LinearLayout ll_back;
    private String current_lang = "";
    private String image_url="";

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__show__image);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        Paper.init(this);
        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(this,current_lang);

        image_back = findViewById(R.id.image_back);
        ll_back = findViewById(R.id.ll_back);
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
                supportFinishAfterTransition();
            }
        });
        image = findViewById(R.id.image);

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            image_url = Tags.IMAGE_URL+intent.getStringExtra("url");

            Picasso.with(this).load(Uri.parse(image_url)).fit().into(image);

        }
    }






}
