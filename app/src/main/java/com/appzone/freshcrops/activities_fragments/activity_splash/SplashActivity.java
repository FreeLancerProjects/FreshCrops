package com.appzone.freshcrops.activities_fragments.activity_splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.DelegateHomeActivity;
import com.appzone.freshcrops.activities_fragments.activity_sign_in.SignInActivity;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;

public class SplashActivity extends AppCompatActivity {

    private ImageView image;
    //private ProgressBar prgBar;
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();

        image = findViewById(R.id.image);
       /* prgBar = findViewById(R.id.progBar);
        prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
*/
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade);
        image.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String session = preferences.getSession(SplashActivity.this);
                if (session.equals(Tags.session_login))
                {
                    userModel = preferences.getUserData(SplashActivity.this);
                    userSingleTone.setUserModel(userModel);

                    if (userModel.getUser().getRole().equals(Tags.user_client))
                    {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (userModel.getUser().getRole().equals(Tags.user_delegate))
                    {
                        Intent intent = new Intent(SplashActivity.this, DelegateHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }else
                    {


                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
