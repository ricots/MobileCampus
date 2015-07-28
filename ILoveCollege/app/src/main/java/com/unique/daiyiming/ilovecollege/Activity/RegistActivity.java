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
import android.os.Message;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unique.daiyiming.ilovecollege.Config.Config;
import com.unique.daiyiming.ilovecollege.MySharedPreference.LocalUserInfoSharedPreference;
import com.unique.daiyiming.ilovecollege.EncryptTool.EncryptTool;
import com.unique.daiyiming.ilovecollege.ScreenParams.ScreenParams;
import com.unique.daiyiming.ilovecollege.NetServiceTools.JsonPostHelper;
import com.unique.daiyiming.ilovecollege.R;
import com.unique.daiyiming.ilovecollege.View.ScaleChangeImageView;
import com.unique.daiyiming.ilovecollege.View.ScaleImageView;
import com.unique.daiyiming.ilovecollege.View.ScaleTextView;
import com.unique.daiyiming.ilovecollege.View.WaitProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 作者：代一鸣
 */
public class RegistActivity extends Activity implements ScaleImageView.OnScaleImageViewClickListener, ScaleTextView.OnScaleTextViewClickListener, View.OnClickListener, ScaleChangeImageView.OnScaleChangeImageViewClickListener {

    private final static String SEND_URL = Config.BASE_URL + "/account/register/";

    private final static String SEND_PARAMS_USERNAME = "username"; //用户名
    private final static String SEND_PARAMS_CODE = "if_code"; //验证码
    private final static String SEND_PARAMS_GENDER = "gender"; //性别
    private final static String SEND_PARAMS_PASSWORD = "password"; //密码

    private final static String GET_PARAMS_TOKEN = "token"; //Token

    private final static int HANDLER_MESSAGE_ANIMATION_STOP = 1; //动画停止
    private final static int HANDLER_MESSAGE_CLOSE_WAIT_VIEW = 2; //关闭等待页面

    private TextView tv_genderTitle = null; //选择性别标题
    private LinearLayout ll_form = null; //表单父元素
    private EditText edt_phoneNumber = null; //电话
    private EditText edt_password = null; //密码
    private EditText edt_code = null; //验证码
    private ScaleTextView stv_getCode = null; //获取验证码
    private LinearLayout ll_backToLogin = null; //回到注册界面
    private ScaleImageView simg_go = null; //注册
    private ScaleChangeImageView scimg_man = null; //男
    private ScaleChangeImageView scimg_woman = null; //女
    private WaitProgress wp_waitView = null; //等待界面

    private String username = "";
    private String code = "";
    private String password = "";
    private String gender = "";

    private ScreenParams screenParams = null; //屏幕参数

    private boolean isAnimation = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE_ANIMATION_STOP: {
                    isAnimation = false;
                }break;
                case HANDLER_MESSAGE_CLOSE_WAIT_VIEW: {
                    closeWaitView();
                }break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        buildStatusBar();

        init();
    }

    private void init() {
        //获取组件
        tv_genderTitle = (TextView) findViewById(R.id.tv_genderTitle);
        ll_form = (LinearLayout) findViewById(R.id.ll_form);
        edt_phoneNumber = (EditText) findViewById(R.id.edt_phoneNumber);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_code = (EditText) findViewById(R.id.edt_code);
        stv_getCode = (ScaleTextView) findViewById(R.id.stv_getCode);
        ll_backToLogin = (LinearLayout) findViewById(R.id.ll_backToLogin);
        simg_go = (ScaleImageView) findViewById(R.id.simg_go);
        scimg_man = (ScaleChangeImageView) findViewById(R.id.scimg_man);
        scimg_woman = (ScaleChangeImageView) findViewById(R.id.scimg_woman);
        wp_waitView = (WaitProgress) findViewById(R.id.wp_waitView);

        screenParams = new ScreenParams(this);

        simg_go.setTag(false); //默认未进入性别选择

        simg_go.setOnScaleImageViewClickListener(this);
        stv_getCode.setOnScaleTextViewClickListener(this);
        ll_backToLogin.setOnClickListener(this);
        scimg_man.setOnScaleChangeImageViewClickListener(this);
        scimg_woman.setOnScaleChangeImageViewClickListener(this);
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

    @Override
    public void OnScaleImageViewClick(View view) {
        switch (view.getId()) {
            case R.id.simg_go: { //点击红色箭头按钮
                if (isAnimation) { //如果正在动画则屏蔽
                    return;
                }
                if (! (Boolean) simg_go.getTag()) {
                    username = edt_phoneNumber.getText().toString();
                    password = edt_password.getText().toString();
                    code = edt_code.getText().toString();
                    //检查输入值
                    if (!checkInputValue(username, password, code)) { //如果检查失败则直接返回
                        return;
                    }
                    //进入选择性别形式
                    simg_go.setTag(true);
                    changeView();
                } else {
                    simg_go.setTag(false);
                    restoreView();
                }
            }break;
        }
    }

    /**
     * 变为性别选择状态
     */
    private void changeView() {
        isAnimation = true; //开始动画
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ANIMATION_STOP, 1000);
        //表单
        ObjectAnimator formAnimator = ObjectAnimator.ofFloat(ll_form, "translationX", 0, 0 - screenParams.getScreenWidth());
        formAnimator.setDuration(300);
        formAnimator.setInterpolator(new DecelerateInterpolator());
        //标题
        ObjectAnimator titleAnimator = ObjectAnimator.ofFloat(tv_genderTitle, "translationX", screenParams.getScreenWidth(), 0);
        titleAnimator.setDuration(300);
        titleAnimator.setInterpolator(new DecelerateInterpolator());
        //执行改变界面动画
        AnimatorSet changeBodyAnimatorSet = new AnimatorSet();
        changeBodyAnimatorSet.playTogether(formAnimator, titleAnimator);
        changeBodyAnimatorSet.setDuration(300);
        changeBodyAnimatorSet.setInterpolator(new DecelerateInterpolator());
        changeBodyAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                tv_genderTitle.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        changeBodyAnimatorSet.start();

        //显示性别选择按钮
        //旋转Go按钮
        ObjectAnimator goAnimator = ObjectAnimator.ofFloat(simg_go, "rotation", 0, -180);
        goAnimator.setDuration(600);
        goAnimator.setStartDelay(300);
        goAnimator.setInterpolator(new OvershootInterpolator(2));
        goAnimator.start();

        int xOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int yOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

        //显示男女图标
        //女XY移动动画
        ObjectAnimator womanXAnimator = ObjectAnimator.ofFloat(scimg_woman, "translationX", 0, xOffset);
        womanXAnimator.setDuration(100);
        womanXAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator womanYAnimator = ObjectAnimator.ofFloat(scimg_woman, "translationY", 0, -yOffset);
        womanYAnimator.setDuration(100);
        womanYAnimator.setInterpolator(new DecelerateInterpolator());
        //女移动动画组合
        AnimatorSet womanTranslationAnimatorSet = new AnimatorSet();
        womanTranslationAnimatorSet.setDuration(100);
        womanTranslationAnimatorSet.setInterpolator(new DecelerateInterpolator());
        womanTranslationAnimatorSet.playTogether(womanXAnimator, womanYAnimator);
        //女颤抖动画
        ObjectAnimator shakeAnimator = ObjectAnimator.ofFloat(scimg_woman, "rotation", 0, -10, 10, 0);
        shakeAnimator.setDuration(100);
        shakeAnimator.setInterpolator(new DecelerateInterpolator());
        //女动画
        AnimatorSet womanAnimatorSet = new AnimatorSet();
        womanAnimatorSet.setDuration(200);
        womanAnimatorSet.setStartDelay(200);
        womanAnimatorSet.setInterpolator(new DecelerateInterpolator());
        womanAnimatorSet.playSequentially(womanTranslationAnimatorSet, shakeAnimator);
        womanAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                scimg_woman.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        womanAnimatorSet.start();
        //男XY移动动画
        ObjectAnimator manXAnimator = ObjectAnimator.ofFloat(scimg_man, "translationX", 0, -xOffset);
        manXAnimator.setDuration(100);
        manXAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator manYAnimator = ObjectAnimator.ofFloat(scimg_man, "translationY", 0, -yOffset);
        manYAnimator.setDuration(100);
        manYAnimator.setInterpolator(new DecelerateInterpolator());
        //男移动动画组合
        AnimatorSet manTranslationAnimatorSet = new AnimatorSet();
        manTranslationAnimatorSet.setDuration(100);
        manTranslationAnimatorSet.setInterpolator(new DecelerateInterpolator());
        manTranslationAnimatorSet.playTogether(manXAnimator, manYAnimator);
        //男颤抖动画
        ObjectAnimator manShakeAnimator = ObjectAnimator.ofFloat(scimg_man, "rotation", 0, -10, 10, 0);
        manShakeAnimator.setDuration(100);
        manShakeAnimator.setInterpolator(new DecelerateInterpolator());
        //男动画
        AnimatorSet manAnimatorSet = new AnimatorSet();
        manAnimatorSet.setDuration(200);
        manAnimatorSet.setStartDelay(400);
        manAnimatorSet.setInterpolator(new DecelerateInterpolator());
        manAnimatorSet.playSequentially(manTranslationAnimatorSet, manShakeAnimator);
        manAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                scimg_man.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        manAnimatorSet.start();
    }

    /**
     * 复原界面
     */
    private void restoreView() {
        isAnimation = true; //开始动画
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ANIMATION_STOP, 1000);
        //隐藏性别选择按钮
        //旋转Go按钮
        ObjectAnimator goAnimator = ObjectAnimator.ofFloat(simg_go, "rotation", -180, 0);
        goAnimator.setDuration(600);
        goAnimator.setInterpolator(new OvershootInterpolator(2));
        goAnimator.start();

        int xOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int yOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

        //显示男女图标
        //女XY移动动画
        ObjectAnimator womanXAnimator = ObjectAnimator.ofFloat(scimg_woman, "translationX", xOffset, 0);
        womanXAnimator.setDuration(200);
        womanXAnimator.setInterpolator(new AnticipateInterpolator());
        ObjectAnimator womanYAnimator = ObjectAnimator.ofFloat(scimg_woman, "translationY", -yOffset, 0);
        womanYAnimator.setDuration(200);
        womanYAnimator.setInterpolator(new AnticipateInterpolator());
        //女移动动画组合
        AnimatorSet womanTranslationAnimatorSet = new AnimatorSet();
        womanTranslationAnimatorSet.setDuration(200);
        womanTranslationAnimatorSet.setStartDelay(400);
        womanTranslationAnimatorSet.setInterpolator(new AnticipateInterpolator());
        womanTranslationAnimatorSet.playTogether(womanXAnimator, womanYAnimator);
        womanTranslationAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scimg_woman.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });
        womanTranslationAnimatorSet.start();
        //男XY移动动画
        ObjectAnimator manXAnimator = ObjectAnimator.ofFloat(scimg_man, "translationX", -xOffset, 0);
        manXAnimator.setDuration(100);
        manXAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator manYAnimator = ObjectAnimator.ofFloat(scimg_man, "translationY", -yOffset, 0);
        manYAnimator.setDuration(100);
        manYAnimator.setInterpolator(new DecelerateInterpolator());
        //男移动动画组合
        AnimatorSet manTranslationAnimatorSet = new AnimatorSet();
        manTranslationAnimatorSet.setDuration(200);
        manTranslationAnimatorSet.setStartDelay(200);
        manTranslationAnimatorSet.setInterpolator(new AnticipateInterpolator());
        manTranslationAnimatorSet.playTogether(manXAnimator, manYAnimator);
        manTranslationAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scimg_man.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });
        manTranslationAnimatorSet.start();

        //改变界面
        AnimatorSet changeBodyAnimatorSet = new AnimatorSet();
        //表单
        ObjectAnimator formAnimator = ObjectAnimator.ofFloat(ll_form, "translationX", 0 - screenParams.getScreenWidth(), 0);
        formAnimator.setDuration(300);
        formAnimator.setInterpolator(new DecelerateInterpolator());
        //标题
        ObjectAnimator titleAnimator = ObjectAnimator.ofFloat(tv_genderTitle, "translationX", 0, screenParams.getScreenWidth());
        titleAnimator.setDuration(300);
        titleAnimator.setInterpolator(new DecelerateInterpolator());
        //执行改变界面动画
        changeBodyAnimatorSet.playTogether(formAnimator, titleAnimator);
        changeBodyAnimatorSet.setDuration(300);
        changeBodyAnimatorSet.setStartDelay(600);
        changeBodyAnimatorSet.setInterpolator(new DecelerateInterpolator());
        changeBodyAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tv_genderTitle.setVisibility(View.GONE);
                super.onAnimationStart(animation);
            }
        });
        changeBodyAnimatorSet.start();
    }

    /**
     * 打开等待界面
     */
    private void openWaitView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(wp_waitView, "alpha", 0, 1);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                wp_waitView.setVisibility(View.VISIBLE);
                wp_waitView.setOnTouchListener(new View.OnTouchListener() { //屏蔽穿透点击事件
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                super.onAnimationStart(animation);
            }
        });
        animator.start();
    }

    /**
     * 关闭等待界面
     */
    private void closeWaitView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(wp_waitView, "alpha", 1, 0);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                wp_waitView.setVisibility(View.GONE);
                wp_waitView.setOnTouchListener(null); //取消屏蔽
                super.onAnimationStart(animation);
            }
        });
        animator.start();
    }

    @Override
    public void OnScaleTextViewClick(View view) {
        switch (view.getId()) {
            case R.id.stv_getCode: { //获取验证码
                if (edt_phoneNumber.getText().toString().length() != 11) {
                    Toast.makeText(this, "请输入正确手机号后重试(11位)", Toast.LENGTH_LONG).show();
                    viewNotification(findViewById(R.id.rl_phoneNumberContainer));
                    return;
                }
            }break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_backToLogin: { //返回到登录界面
                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                RegistActivity.this.finish();
            }break;
        }
    }

    /**
     * 检查输入值
     */
    private boolean checkInputValue(String phoneNumber, String password, String code) {
        //检查手机号
        if (phoneNumber.length() != 11) {
            Toast.makeText(this, "请输入正确手机号(11位)", Toast.LENGTH_LONG).show();
            viewNotification(findViewById(R.id.rl_phoneNumberContainer));
            return false;
        }
        //检查密码
        if (password.length() < 6) {
            Toast.makeText(this, "密码不能小于6位", Toast.LENGTH_LONG).show();
            viewNotification(findViewById(R.id.rl_passwordContainer));
            return false;
        }
        //检查验证码
        if (code.length() < 6) {
            Toast.makeText(this, "验证码不能小于六位", Toast.LENGTH_LONG).show();
            viewNotification(edt_code);
            return false;
        }
        return true;
    }

    /**
     * 控件提醒
     */
    private void viewNotification(View view) {
        view.requestFocus();
        int offsetX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, offsetX, 0, - offsetX, 0, offsetX, 0, - offsetX, 0);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    @Override
    public void OnScaleChangeImageViewClick(View view) {
        gender = "0";
        switch (view.getId()) {
            case R.id.scimg_man: { //点击了男
                gender = "0";
            }break;
            case R.id.scimg_woman: { //点击了女
                gender = "1";
            }break;
        }

        //定义发送参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(SEND_PARAMS_USERNAME, username);
        params.put(SEND_PARAMS_CODE, EncryptTool.encrypt(code));
        params.put(SEND_PARAMS_PASSWORD, EncryptTool.encrypt(password));
        params.put(SEND_PARAMS_GENDER, gender);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SEND_PARAMS_USERNAME, username);
            jsonObject.put(SEND_PARAMS_CODE, EncryptTool.encrypt(code));
            jsonObject.put(SEND_PARAMS_PASSWORD, EncryptTool.encrypt(password));
            jsonObject.put(SEND_PARAMS_GENDER, gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //发送数据
        JsonPostHelper jsonPostHelper = new JsonPostHelper(this);
        jsonPostHelper.getJsonResponse(SEND_URL, jsonObject, null, new JsonPostHelper.OnJsonPostHelperResonseSuccessedListener() {
            @Override
            public void OnJsonPostHelperResonseSuccessed(JSONObject response) {
                String token  = "";
                try {
                    token = response.getString(GET_PARAMS_TOKEN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LocalUserInfoSharedPreference localUserInfoSharedPreference = new LocalUserInfoSharedPreference(RegistActivity.this);
                localUserInfoSharedPreference.putAll(username, "", "", gender, "", "", "", "", "", "", "", "", "", "", token);
                localUserInfoSharedPreference.commit();

                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                RegistActivity.this.finish();
            }
        }, new JsonPostHelper.OnJsonPostHelperResonseFiledListener() {
            @Override
            public void OnJsonPostHelperResonseFiled(String cause) {
                Toast.makeText(getApplicationContext(), "注册失败" + (cause.equals("") ? cause : " " + cause), Toast.LENGTH_LONG).show();
                handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_CLOSE_WAIT_VIEW, 1000);
            }
        });
        openWaitView();
    }
}























