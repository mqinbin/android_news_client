package com.qinbin.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Teacher on 2016/8/7.
 */
public class WelcomeActivity extends Activity {

    @ViewInject(R.id.welcome_iv)
    ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ViewUtils.inject(this);
//        playAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        playAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sHandler.removeCallbacks(mGoNextActivityRunnable);
    }

    private void playAnimation() {

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setAnimationListener(mAnimationListener);

        ScaleAnimation scaleAnimation
                = new ScaleAnimation(
                0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1000);
        animationSet.addAnimation(scaleAnimation);


        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        animationSet.addAnimation(alphaAnimation);

        mImageView.startAnimation(animationSet);
    }

    static Handler sHandler = new Handler();

    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            sHandler.postDelayed(mGoNextActivityRunnable, 1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    Runnable mGoNextActivityRunnable = new Runnable() {
        @Override
        public void run() {
            goNextActivity();
        }
    };
    private void goNextActivity() {
        startActivity(new Intent(this,GuideActivity.class));
        finish();
    }
}
