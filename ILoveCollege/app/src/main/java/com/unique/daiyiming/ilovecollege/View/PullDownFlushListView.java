package com.unique.daiyiming.ilovecollege.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unique.daiyiming.ilovecollege.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daiyiming on 2015/6/22.
 */
public class PullDownFlushListView extends ListView implements View.OnTouchListener, AbsListView.OnScrollListener {

    private final static int HANDLER_MESSAGE_CLOSE_WAIT_VIEW = 0;

    private RelativeLayout header = null; //头部控件
    private TextView tv_pullDownTag = null;
    private TextView tv_lastTime = null;
    private ImageView img_flushImageView = null;
    private boolean canFlush = false; //可以刷新
    private OnPullDownFlushListViewFlushListener listener = null;
    private LinearLayout ll_flushViewContainer = null; //刷新的界面
    private LinearLayout ll_waitViewContainer = null; //等待界面
    private TextView footer = null; //尾部控件
    private ObjectAnimator circleViewRotation = null;
    private TextView tv_waitTextView = null;
    private ImageView img_waitImageView = null;
    private boolean isAnimation = false;

    private int downX = 0, downY = 0;

    private boolean isFlushLocked = false; //组件是否上锁

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE_CLOSE_WAIT_VIEW: {
                    ValueAnimator animator = ValueAnimator.ofInt(header.getPaddingTop(), 0 - ll_flushViewContainer.getHeight());
                    animator.setDuration(200);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ll_waitViewContainer.setVisibility(View.INVISIBLE);
                            ll_flushViewContainer.setVisibility(View.VISIBLE);
                            ll_waitViewContainer.setTag(false);
                            isAnimation = false;
                            super.onAnimationEnd(animation);
                        }
                    });
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            header.setPadding(0, (Integer) animation.getAnimatedValue(), 0, 0);
                        }
                    });
                    animator.start();
                }break;
            }
            super.handleMessage(msg);
        }
    };

    public interface OnPullDownFlushListViewFlushListener {
        void OnPullDownFlushListViewFlush(); //刷新
        void OnPullDownFlushListViewLoadMore(); //加载更多
    }

    public PullDownFlushListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //填充下拉刷新界面
        header = new RelativeLayout(context);
        int headerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
        LayoutParams headViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(headViewParams);
        header.setPadding(0, 0 - headerHeight, 0, 0);

        //刷新界面
        ll_flushViewContainer = new LinearLayout(context);
        RelativeLayout.LayoutParams flushViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        flushViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        flushViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        ll_flushViewContainer.setLayoutParams(flushViewParams);
        ll_flushViewContainer.setOrientation(LinearLayout.HORIZONTAL);
        int flushViewContainerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        ll_flushViewContainer.setPadding(0, flushViewContainerPadding, 0, flushViewContainerPadding);
        header.addView(ll_flushViewContainer);

        img_flushImageView = new ImageView(context);
        int flushImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams flushImageParams = new LinearLayout.LayoutParams(flushImageWidth, flushImageWidth);
        flushImageParams.gravity = Gravity.CENTER_VERTICAL;
        img_flushImageView.setLayoutParams(flushImageParams);
        img_flushImageView.setImageResource(R.drawable.icon_pull_down);
        ll_flushViewContainer.addView(img_flushImageView);

        LinearLayout flushTextContainer = new LinearLayout(context);
        LinearLayout.LayoutParams flushTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics()));
        flushTextParams.gravity = Gravity.CENTER_VERTICAL;
        flushTextContainer.setLayoutParams(flushTextParams);
        flushTextContainer.setOrientation(LinearLayout.VERTICAL);
        ll_flushViewContainer.addView(flushTextContainer);

        tv_pullDownTag = new TextView(context);
        LinearLayout.LayoutParams tagParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        tagParams.weight = 1;
        tv_pullDownTag.setLayoutParams(tagParams);
        tv_pullDownTag.setTextColor(Color.parseColor("#9E9E9E"));
        tv_pullDownTag.setText("向下拖动 要你好看");
        tv_pullDownTag.setTextSize(16);
        tv_pullDownTag.setGravity(Gravity.TOP);
        flushTextContainer.addView(tv_pullDownTag);

        tv_lastTime = new TextView(context);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        timeParams.weight = 1;
        tv_lastTime.setLayoutParams(timeParams);
        tv_lastTime.setTextColor(Color.parseColor("#9E9E9E"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        tv_lastTime.setText("最后刷新：" + dateFormat.format(new Date(System.currentTimeMillis())));
        tv_lastTime.setTextSize(14);
        tv_lastTime.setGravity(Gravity.BOTTOM);
        flushTextContainer.addView(tv_lastTime);

        //填充等待界面
        ll_waitViewContainer = new LinearLayout(context);
        RelativeLayout.LayoutParams ll_waitViewContainerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ll_waitViewContainerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        ll_waitViewContainerParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        ll_waitViewContainer.setLayoutParams(ll_waitViewContainerParams);
        ll_waitViewContainer.setOrientation(LinearLayout.HORIZONTAL);
        int waitViewContainerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        ll_waitViewContainer.setPadding(0, waitViewContainerPadding, 0, waitViewContainerPadding);
        header.addView(ll_waitViewContainer);

        img_waitImageView = new ImageView(context);
        int waitImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams waitImageViewParams = new LinearLayout.LayoutParams(waitImageWidth, waitImageWidth);
        waitImageViewParams.gravity = Gravity.CENTER_VERTICAL;
        img_waitImageView.setLayoutParams(waitImageViewParams);
        img_waitImageView.setImageResource(R.drawable.icon_circle_wait_progress);
        ll_waitViewContainer.addView(img_waitImageView);

        tv_waitTextView = new TextView(context);
        tv_waitTextView.setText("正在刷新");
        tv_waitTextView.setTextColor(Color.parseColor("#9E9E9E"));
        tv_waitTextView.setTextSize(18);
        LinearLayout.LayoutParams waitTextViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        waitTextViewParams.gravity = Gravity.CENTER_VERTICAL;
        waitTextViewParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        tv_waitTextView.setLayoutParams(waitTextViewParams);
        ll_waitViewContainer.addView(tv_waitTextView);

        //填充尾部
        footer = new TextView(context);
        LayoutParams footerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
        footer.setLayoutParams(footerParams);
        footer.setText("内容加载中...");
        footer.setTextColor(Color.parseColor("#9E9E9E"));
        footer.setTextSize(18);
        footer.setGravity(Gravity.CENTER);

        this.setDividerHeight(0);
        this.addHeaderView(header);
        this.addFooterView(footer);
        this.setOnTouchListener(this);
        this.setOnScrollListener(this);

        circleViewRotation = ObjectAnimator.ofFloat(img_waitImageView, "rotation", 0, -360);
        circleViewRotation.setDuration(1000);
        circleViewRotation.setRepeatCount(-1);
        circleViewRotation.setRepeatMode(ObjectAnimator.INFINITE);
        circleViewRotation.setInterpolator(new LinearInterpolator());

        //默认尾部隐藏
        footer.setVisibility(View.GONE);
        ll_waitViewContainer.setVisibility(View.INVISIBLE);
        ll_waitViewContainer.setTag(false); //未显示
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (this.getFirstVisiblePosition() == 0 && !isAnimation) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = (int) event.getX();
                    downY = (int) event.getY();
                }break;
                case MotionEvent.ACTION_MOVE: {
                    int moveX = (int) event.getX();
                    int moveY = (int) event.getY();
                    if (moveY >= downY && moveY - downY >= Math.abs(moveX - downX)) {
                        if (! (Boolean) ll_waitViewContainer.getTag()) {
                            header.setPadding(0, (moveY - downY) / 2 - ll_flushViewContainer.getHeight(), 0, 0);
                        } else {
                            header.setPadding(0, (moveY - downY) / 2 - ll_flushViewContainer.getHeight() + ll_waitViewContainer.getHeight(), 0, 0);
                        }
                    }
                    if (header.getPaddingTop() >= 0) {
                        tv_pullDownTag.setText("松开手指 立即刷新");
                        img_flushImageView.setRotation(180);
                    } else {
                        tv_pullDownTag.setText("向下拖动 要你好看");
                        img_flushImageView.setRotation(0);
                    }
                    if (header.getPaddingTop() > 0 - ll_flushViewContainer.getHeight()) {
                        return true;
                    }
                }break;
                case MotionEvent.ACTION_UP: {
                    int endPadding = 0 - ll_flushViewContainer.getHeight();
                    if (header.getPaddingTop() >= 0) {
                        canFlush = true;
                        endPadding = ll_waitViewContainer.getHeight() - ll_flushViewContainer.getHeight();
                    }
                    if ((Boolean) ll_waitViewContainer.getTag()) {
                        endPadding = ll_waitViewContainer.getHeight() - ll_flushViewContainer.getHeight();
                    }
                    ValueAnimator animator = ValueAnimator.ofInt(header.getPaddingTop(), endPadding);
                    animator.setDuration(200);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            if (canFlush) {
                                ll_waitViewContainer.setVisibility(View.VISIBLE);
                                ll_waitViewContainer.setTag(true);
                                circleViewRotation.start();
                                ll_flushViewContainer.setVisibility(View.INVISIBLE);
                                isAnimation = true;
                            }
                            super.onAnimationStart(animation);
                        }
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (canFlush && listener != null && !isFlushLocked) { //刷新
                                listener.OnPullDownFlushListViewFlush();
                            }
                            canFlush = false;
                            isAnimation = false;
                            super.onAnimationEnd(animation);
                        }
                    });
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            header.setPadding(0, (Integer) animation.getAnimatedValue(), 0, 0);
                        }
                    });
                    animator.start();
                }break;
            }
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && listener != null && !isFlushLocked) { //加载更多
            listener.OnPullDownFlushListViewLoadMore();
        }
    }

    public void setOnPullDownFlushListViewFlushListener(OnPullDownFlushListViewFlushListener listener) {
        this.listener = listener;
    }

    public void setLastFlushTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        tv_lastTime.setText("最后刷新：" + dateFormat.format(new Date(time)));
    }

    public void showSuccessWaitView() {
        isAnimation = true;
        circleViewRotation.end();
        img_waitImageView.setImageResource(R.drawable.icon_flush_success);
        tv_waitTextView.setText("刷新成功");
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_CLOSE_WAIT_VIEW, 1000);
    }

    public void showFailWaieView() {
        isAnimation = true;
        circleViewRotation.end();
        img_waitImageView.setImageResource(R.drawable.icon_flush_fail);
        tv_waitTextView.setText("刷新失败");
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_CLOSE_WAIT_VIEW, 1000);
    }

    public void showFootView() {
        footer.setVisibility(View.VISIBLE);
    }

    public void hideFootView() {
        footer.setVisibility(View.GONE);
    }

    public void lockFlush() {
        isFlushLocked = true;
    }
    
    public void unLockFlush() {
        isFlushLocked = false;
    }

}
