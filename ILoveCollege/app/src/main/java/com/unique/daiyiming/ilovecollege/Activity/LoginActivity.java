package com.unique.daiyiming.ilovecollege.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.unique.daiyiming.ilovecollege.Config.Config;
import com.unique.daiyiming.ilovecollege.MySharedPreference.LocalUserInfoSharedPreference;
import com.unique.daiyiming.ilovecollege.EncryptTool.EncryptTool;
import com.unique.daiyiming.ilovecollege.NetServiceTools.JsonPostHelper;
import com.unique.daiyiming.ilovecollege.R;
import com.unique.daiyiming.ilovecollege.View.ScaleImageView;
import com.unique.daiyiming.ilovecollege.View.WaitProgress;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements ScaleImageView.OnScaleImageViewClickListener {

    private final static String SEND_URL = Config.BASE_URL + "/account/login/";//登录发送的网站

    private final static String USER_NAME = "username";//用户名
    private final static String PASSWORD = "password";//密码

    private final static String GET_PARAMS_USER_INFO = "user_info"; //用户信息
    private final static String GET_PARAMS_NICKNAME = "nickname"; //昵称
    private final static String GET_PARAMS_SIGN = "sign"; //个性签名
    private final static String GET_PARAMS_STUDENT_ID = "student_id"; //学号
    private final static String GET_PARAMS_GENDER = "gender"; //性别
    private final static String GET_PARAMS_AVATAR = "avatar"; //用户头像地址
    private final static String GET_PARAMS_HOMETOWN = "hometown"; //家乡
    private final static String GET_PARAMS_BIRTHDAY = "birthday"; //生日
    private final static String GET_PARAMS_SEX_ORIENTATION = "sex_orientation"; //性取向
    private final static String GET_PARAMS_LOVE_STATUS = "love_status"; //恋爱状态
    private final static String GET_PARAMS_HOBBIES = "hobbies"; //爱好
    private final static String GET_PARAMS_SCHOOL = "school"; //学校
    private final static String GET_PARAMS_MAJOR = "major"; //专业
    private final static String GET_PARAMS_GRADE = "grade"; //年级
    private final static String GET_PARAMS_TOKEN = "token"; //Token

    private final static int HANDLER_MESSAGE_CLOSE_WAIT_VIEW = 1; //关闭等待页面

    private EditText edt_userName;
    private EditText edt_password;
    private Button btn_registNow;
    private Button btn_forgetPassword;
    private ScaleImageView simg_login;
    private WaitProgress wp_waitView = null; //等待界面

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE_CLOSE_WAIT_VIEW: {
                    closeWaitView();
                }
                break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buildStatusBar();

        edt_userName = (EditText) findViewById(R.id.edt_userName);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_registNow = (Button) findViewById(R.id.btn_registNow);
        btn_forgetPassword = (Button) findViewById(R.id.btn_forgetPassword);
        simg_login = (ScaleImageView) findViewById(R.id.simg_login);
        wp_waitView = (WaitProgress) findViewById(R.id.wp_waitView);

        //为立即注册按钮设置监听器
        btn_registNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
        //为忘记密码按钮设置监听器
        btn_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "跳入忘记密码界面", Toast.LENGTH_LONG).show();
            }
        });
        //为登录按钮设置监听器
        simg_login.setOnScaleImageViewClickListener(this);
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
        //检查输入密码与用户名
        if (!(checkUserName(edt_userName) && checkPassword(edt_password)))
            return;

        final String userName = edt_userName.getText().toString();
        String password = edt_password.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(USER_NAME, userName);
            jsonObject.put(PASSWORD, EncryptTool.encrypt(password));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //发送数据
        JsonPostHelper jsonPostHelper = new JsonPostHelper(this);
        jsonPostHelper.getJsonResponse(SEND_URL, jsonObject, null, new JsonPostHelper.OnJsonPostHelperResonseSuccessedListener() {
            @Override
            public void OnJsonPostHelperResonseSuccessed(JSONObject response) {
                String username = "";
                String nickname = "";
                String sign = "";
                String studentId = "";
                String gender = "";
                String avatar = "";
                String hometown = "";
                String birthday = "";
                String sexOrientation = "";
                String loveStatus = "";
                String hobbies = "";
                String school = "";
                String major = "";
                String grade = "";
                String token = "";
                try {
                    JSONObject userInfo = response.getJSONObject(GET_PARAMS_USER_INFO);
                    username = userName;
                    nickname = userInfo.getString(GET_PARAMS_NICKNAME);
                    sign = userInfo.getString(GET_PARAMS_SIGN);
                    studentId = userInfo.getString(GET_PARAMS_STUDENT_ID);
                    gender = userInfo.getString(GET_PARAMS_GENDER);
                    avatar = userInfo.getString(GET_PARAMS_AVATAR);
                    hometown = userInfo.getString(GET_PARAMS_HOMETOWN);
                    birthday = userInfo.getString(GET_PARAMS_BIRTHDAY);
                    sexOrientation = userInfo.getString(GET_PARAMS_SEX_ORIENTATION);
                    loveStatus = userInfo.getString(GET_PARAMS_LOVE_STATUS);
                    hobbies = userInfo.getString(GET_PARAMS_HOBBIES);
                    school = userInfo.getString(GET_PARAMS_SCHOOL);
                    major = userInfo.getString(GET_PARAMS_MAJOR);
                    grade = userInfo.getString(GET_PARAMS_GRADE);
                    token = userInfo.getString(GET_PARAMS_TOKEN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LocalUserInfoSharedPreference localUserInfoSharedPreference = new LocalUserInfoSharedPreference(LoginActivity.this);
                localUserInfoSharedPreference.putAll(username, nickname, sign, studentId, gender, avatar, hometown, birthday, sexOrientation, loveStatus, hobbies, school, major, grade, token);
                localUserInfoSharedPreference.commit();

                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        }, new JsonPostHelper.OnJsonPostHelperResonseFiledListener() {
            @Override
            public void OnJsonPostHelperResonseFiled(String cause) {
                Toast.makeText(LoginActivity.this, "登录失败" + (cause.equals("") ? cause : " " + cause), Toast.LENGTH_LONG).show();
                handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_CLOSE_WAIT_VIEW, 1000);
            }
        });
        openWaitView();
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

    //检查输入密码是否小于六位
    private boolean checkPassword(EditText v) {
        if (v.getText().toString().length() < 6) {
            Toast.makeText(this, "密码不能小于6位", Toast.LENGTH_LONG).show();
            viewNotification(findViewById(R.id.rl_password));
            return false;
        }
        return true;
    }

    //检查输入用户名是否正确
    private boolean checkUserName(EditText v) {
        if (v.getText().toString().length() != 11) {
            Toast.makeText(this, "请正确输入用户名(手机号)", Toast.LENGTH_LONG).show();
            viewNotification(findViewById(R.id.rl_userName));
            return false;
        }
        return true;
    }

    private void viewNotification(View view) {
        view.requestFocus();
        int offsetX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, offsetX, 0, -offsetX, 0, offsetX, 0, -offsetX, 0);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

    }
}