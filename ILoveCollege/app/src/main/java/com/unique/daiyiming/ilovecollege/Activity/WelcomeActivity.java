package com.unique.daiyiming.ilovecollege.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.unique.daiyiming.ilovecollege.MySharedPreference.LocalUserInfoSharedPreference;
import com.unique.daiyiming.ilovecollege.R;


public class WelcomeActivity extends Activity {

    private View mainView = null;
    private ImageView img_logo = null;
    private ImageView img_text = null;

    private Runnable callBackRunnable = new Runnable() {
        @Override
        public void run() {
            LocalUserInfoSharedPreference localUserInfoSharedPreference = new LocalUserInfoSharedPreference(WelcomeActivity.this);
            Intent intent = null;
            if (localUserInfoSharedPreference.isEnable()) {
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
            } else {
                intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            WelcomeActivity.this.finish();
        }
    };

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        mainView = findViewById(R.id.mainView);
        img_logo = (ImageView) findViewById(R.id.img_logo);
        img_text = (ImageView) findViewById(R.id.img_text);

        buildStatusBar();

        AnimatorSet animatorSet = new AnimatorSet();
        //主背景动画
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mainView, "scaleX", 1.2f, 1);
        scaleXAnimator.setDuration(2000);
        scaleXAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mainView, "scaleY", 1.2f, 1);
        scaleYAnimator.setDuration(2000);
        scaleYAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator alphaAnimator1 = ObjectAnimator.ofFloat(mainView, "alpha", 0.6f, 1);
        alphaAnimator1.setDuration(1200);
        alphaAnimator1.setInterpolator(new DecelerateInterpolator());
        //文字动画
        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(img_text, "translationY", 0, 0 - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()));
        translateAnimator.setDuration(2000);
        translateAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator alphaAnimator2 = ObjectAnimator.ofFloat(img_text, "alpha", 0, 1);
        alphaAnimator2.setDuration(1200);
        alphaAnimator2.setInterpolator(new DecelerateInterpolator());
        //播放动画
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator1, translateAnimator, alphaAnimator2);
        animatorSet.setDuration(2000);
        animatorSet.setStartDelay(500);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
            @Override
            public void onAnimationStart(Animator animation) {
                mainView.setVisibility(View.VISIBLE);
                img_logo.setVisibility(View.VISIBLE);
                img_text.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        animatorSet.start();
        handler.postDelayed(callBackRunnable, 2800);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(callBackRunnable);
        super.onDestroy();
    }

    private void buildStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //4.4以上的沉浸式
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusbarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));
            RelativeLayout.LayoutParams logoParams = (RelativeLayout.LayoutParams) img_logo.getLayoutParams();
            logoParams.topMargin = logoParams.topMargin + statusbarHeight;
            img_logo.setLayoutParams(logoParams);
            RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) img_text.getLayoutParams();
            textParams.bottomMargin = textParams.bottomMargin - statusbarHeight;
            img_text.setLayoutParams(textParams);
        }
    }

}
