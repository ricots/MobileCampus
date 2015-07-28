package com.unique.daiyiming.ilovecollege.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unique.daiyiming.ilovecollege.Fragment.HLYFragment;
import com.unique.daiyiming.ilovecollege.Fragment.WDDPFragment;
import com.unique.daiyiming.ilovecollege.Fragment.XYTFragment;
import com.unique.daiyiming.ilovecollege.R;
import com.unique.daiyiming.ilovecollege.View.ImageWithTagView;
import com.unique.daiyiming.ilovecollege.View.NewsBar;
import com.unique.daiyiming.ilovecollege.View.ScaleImageView;


public class MainActivity extends FragmentActivity implements ImageWithTagView.OnImageWithTagViewClickListener, ScaleImageView.OnScaleImageViewClickListener, View.OnClickListener {

    //页面号
    private final static int FRAGMENT_ONE = 0;
    private final static int FRAGMENT_TWO = 1;
    private final static int FRAGMENT_THREE = 2;

    private final static String BUNDLE_CURRENT_POSITION = "currentposition";

    private ImageWithTagView iwtv_wddp = null;
    private ImageWithTagView iwtv_hly = null;
    private ImageWithTagView iwtv_xyt = null;
    private TextView tv_mainTitle = null;

    private ScaleImageView simg_slideMenu = null;
    private ScaleImageView simg_publish = null;

    private NewsBar nb_newsBar = null;

    private RelativeLayout rl_slideMenu = null;
    private RelativeLayout rl_shield = null;
    private ScaleImageView simg_slideMenuBack = null;
    private LinearLayout ll_popupMenu = null;
    private Fragment[] fm_pages = new Fragment[3];
    private FragmentManager fragmentManager = null;

    private LinearLayout ll_personMessage = null;
    private LinearLayout ll_myCollege = null;
    private LinearLayout ll_callMe = null;
    private LinearLayout ll_settings = null;

    private int currentPosition = 0;

    private boolean isSureQuit = false; //是否要退出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildStatusBar();

        init();
    }

    private void buildStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //4.4以上的沉浸式
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View statusBar = findViewById(R.id.statusBar);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android")));
            statusBar.setLayoutParams(params);
        }
    }

    private void init() {
        iwtv_wddp = (ImageWithTagView) findViewById(R.id.iwtv_wddp);
        iwtv_hly = (ImageWithTagView) findViewById(R.id.iwtv_hly);
        iwtv_xyt = (ImageWithTagView) findViewById(R.id.iwtv_xyt);
        tv_mainTitle = (TextView) findViewById(R.id.tv_mainTitle);

        simg_slideMenu = (ScaleImageView) findViewById(R.id.simg_slideMenu);
        simg_publish = (ScaleImageView) findViewById(R.id.simg_publish);

        nb_newsBar = (NewsBar) findViewById(R.id.nb_newsBar);

        //-------------------新闻测试----------------------------

        for (int i = 0; i < 10; i ++) {
            nb_newsBar.addNews(i + "daiyiming----jindun");
        }
        nb_newsBar.commit();

        //--------------------END-------------------------------

        rl_slideMenu = (RelativeLayout) findViewById(R.id.rl_slideMenu);
        simg_slideMenuBack = (ScaleImageView) findViewById(R.id.simg_slideMenuBack);
        rl_shield = (RelativeLayout) findViewById(R.id.rl_shield);
        ll_popupMenu = (LinearLayout) findViewById(R.id.ll_popupMenu);

        ll_personMessage = (LinearLayout) findViewById(R.id.ll_personMessage);
        ll_myCollege = (LinearLayout) findViewById(R.id.ll_myCollege);
        ll_callMe = (LinearLayout) findViewById(R.id.ll_callMe);
        ll_settings = (LinearLayout) findViewById(R.id.ll_settings);

        fm_pages[0] = new WDDPFragment();
        fm_pages[1] = new HLYFragment();
        fm_pages[2] = new XYTFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fm_pages.length; i ++) {
            transaction.add(R.id.fl_fragmentPlaceHolder, fm_pages[i]);
        }
        transaction.commit();
        showPage(FRAGMENT_ONE);

        iwtv_wddp.setOnImageWithTagViewClickListener(this);
        iwtv_hly.setOnImageWithTagViewClickListener(this);
        iwtv_xyt.setOnImageWithTagViewClickListener(this);

        simg_slideMenu.setOnScaleImageViewClickListener(this);
        simg_publish.setOnScaleImageViewClickListener(this);
        simg_slideMenuBack.setOnScaleImageViewClickListener(this);

        ll_personMessage.setOnClickListener(this);
        ll_myCollege.setOnClickListener(this);
        ll_callMe.setOnClickListener(this);
        ll_settings.setOnClickListener(this);

        nb_newsBar.hide(); //默认隐藏

        rl_slideMenu.setTag(false); //未在动画
        ll_popupMenu.setTag(false); //未在动画
    }

    @Override
    public void OnImageWithTagViewClicked(View view) {
        switch (view.getId()){
            case R.id.iwtv_wddp: { //第一页
                if (! iwtv_wddp.isSelected()) {
                    iwtv_wddp.setSelected(true);
                    iwtv_hly.setSelected(false);
                    iwtv_xyt.setSelected(false);
                    showPage(FRAGMENT_ONE);
                    simg_publish.setVisibility(View.VISIBLE);
                    nb_newsBar.hide();
                    tv_mainTitle.setText("我的地盘");
                }
            }break;
            case R.id.iwtv_hly: { //第二页
                if (! iwtv_hly.isSelected()) {
                    iwtv_wddp.setSelected(false);
                    iwtv_hly.setSelected(true);
                    iwtv_xyt.setSelected(false);
                    showPage(FRAGMENT_TWO);
                    simg_publish.setVisibility(View.GONE);
                    if (! nb_newsBar.isShow()) {
                        nb_newsBar.show();
                    }
                    tv_mainTitle.setText("欢乐园");
                }
            }break;
            case R.id.iwtv_xyt: { //第三页
                if (! iwtv_xyt.isSelected()) {
                    iwtv_wddp.setSelected(false);
                    iwtv_hly.setSelected(false);
                    iwtv_xyt.setSelected(true);
                    showPage(FRAGMENT_THREE);
                    simg_publish.setVisibility(View.GONE);
                    if (! nb_newsBar.isShow()) {
                        nb_newsBar.show();
                    }
                    tv_mainTitle.setText("校园通");
                }
            }break;
        }
    }

    /**
     * 显示页面
     */
    private void showPage(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fm_pages.length; i ++) {
            if (i == position) { //显示
                transaction.show(fm_pages[i]);
            } else { //隐藏
                transaction.hide(fm_pages[i]);
            }
        }
        currentPosition = position;
        transaction.commit();
    }

    @Override
    public void OnScaleImageViewClick(View view) {
        switch (view.getId()) {
            case R.id.simg_slideMenu: {
                if (! (Boolean) rl_slideMenu.getTag()) {
                    openSlideMenu();
                }
            }break;
            case R.id.simg_slideMenuBack: {
                if (! (Boolean) rl_slideMenu.getTag()) {
                    closeSlideMenu();
                }
            }break;
            case R.id.simg_publish: {
                if (! (boolean) ll_popupMenu.getTag()) {
                    openPopupMenu();
                }
            }break;
        }
    }

    /**
     * 打开滑动菜单
     */
    private void openSlideMenu() {
        rl_shield.setOnTouchListener(new View.OnTouchListener() { //屏蔽点击击穿
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        ValueAnimator animator = ValueAnimator.ofInt(-rl_slideMenu.getWidth(), 0);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                rl_slideMenu.setTag(true);
                super.onAnimationStart(animation);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                rl_slideMenu.setTag(false);
                super.onAnimationEnd(animation);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_slideMenu.getLayoutParams();
                params.leftMargin = (Integer) animation.getAnimatedValue();
                rl_slideMenu.setLayoutParams(params);
            }
        });
        animator.start();
    }

    /**
     * 关闭滑动菜单
     */
    private void closeSlideMenu() {
        ValueAnimator animator = ValueAnimator.ofInt(0, -rl_slideMenu.getWidth());
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                rl_slideMenu.setTag(true);
                super.onAnimationStart(animation);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                rl_shield.setOnTouchListener(null);
                rl_slideMenu.setTag(false);
                super.onAnimationEnd(animation);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_slideMenu.getLayoutParams();
                params.leftMargin = (Integer) animation.getAnimatedValue();
                rl_slideMenu.setLayoutParams(params);
            }
        });
        animator.start();
    }

    /**
     * 打开PopupMenu
     */
    private void openPopupMenu() {
        rl_shield.setOnTouchListener(new View.OnTouchListener() { //屏蔽点击击穿
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!(Boolean) ll_popupMenu.getTag()) { //如果没在动画
                    closePopupMenu();
                }
                return true;
            }
        });

        ScaleAnimation shapeAnimation = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.8f, ScaleAnimation.RELATIVE_TO_SELF, 0);
        shapeAnimation.setDuration(300);
        shapeAnimation.setInterpolator(new DecelerateInterpolator());
        ll_popupMenu.startAnimation(shapeAnimation);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ll_popupMenu, "alpha", 0, 1);
        alphaAnimator.setDuration(300);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_popupMenu.setVisibility(View.VISIBLE);
                ll_popupMenu.setTag(true); //在动画
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ll_popupMenu.setTag(false); //不在动画
                super.onAnimationEnd(animation);
            }
        });
        alphaAnimator.start();
    }

    private void closePopupMenu() {
        ScaleAnimation shapeAnimation = new ScaleAnimation(1, 0, 1, 0, ScaleAnimation.RELATIVE_TO_SELF, 0.8f, ScaleAnimation.RELATIVE_TO_SELF, 0);
        shapeAnimation.setDuration(300);
        shapeAnimation.setInterpolator(new DecelerateInterpolator());
        shapeAnimation.setFillAfter(true);
        ll_popupMenu.startAnimation(shapeAnimation);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ll_popupMenu, "alpha", 1, 0);
        alphaAnimator.setDuration(300);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_popupMenu.setTag(true);
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ll_popupMenu.setVisibility(View.GONE);
                rl_shield.setOnTouchListener(null);
                ll_popupMenu.setTag(false);
                super.onAnimationStart(animation);
            }
        });
        alphaAnimator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_personMessage: {

            }break;
            case R.id.ll_myCollege: {

            }break;
            case R.id.ll_callMe: {

            }break;
            case R.id.ll_settings: {

            }break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isSureQuit) {
            isSureQuit = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (! isSureQuit) {
                    isSureQuit = true;
                    Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}




























