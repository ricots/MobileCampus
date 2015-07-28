package com.unique.daiyiming.ilovecollege.View;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unique.daiyiming.ilovecollege.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daiyiming on 2015/6/26.
 */
public class NewsBar extends LinearLayout {

    private ImageView img_icon = null; //图标
    private TextView tv_news = null; //新闻内容
    private List<String> newsList = null;
    private boolean isShow = false; //没有显示

    public NewsBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        img_icon = new ImageView(context);
        tv_news = new TextView(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewsBar);
        int iconSrc = typedArray.getResourceId(R.styleable.NewsBar_iconSrc, R.drawable.icon_news);
        int iconSize = typedArray.getDimensionPixelSize(R.styleable.NewsBar_iconSize, 0);
        int newsTextColor = typedArray.getColor(R.styleable.NewsBar_newsTextColor, Color.parseColor("#333333"));
        int newsTextSize = typedArray.getDimensionPixelSize(R.styleable.NewsBar_newsTextSize, 0);

        this.setOrientation(HORIZONTAL);

        LayoutParams imgParams = new LayoutParams(iconSize, iconSize);
        imgParams.gravity = Gravity.CENTER_VERTICAL;
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        imgParams.leftMargin = margin;
        imgParams.rightMargin = margin;
        img_icon.setLayoutParams(imgParams);
        img_icon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), iconSrc));
        this.addView(img_icon);

        LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        tv_news.setLayoutParams(textParams);
        tv_news.setTextColor(newsTextColor);
        tv_news.setTextSize(TypedValue.COMPLEX_UNIT_PX, newsTextSize);
        tv_news.setSingleLine(true);
        tv_news.setFocusable(true);
        tv_news.setFocusableInTouchMode(true);
        tv_news.setHorizontallyScrolling(true);
        tv_news.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv_news.setMarqueeRepeatLimit(-1); //无限循环
        this.addView(tv_news);

        newsList = new ArrayList<>();
    }

    /**
     * 添加新闻
     * @param news 要添加的新闻
     */
    public void addNews(String news) {
        news = news.replaceAll("\\s*", ""); //排除所有非字符
        newsList.add(news);
    }

    /**
     * 清空所有新闻
     */
    public void clearNews() {
        newsList.clear();
    }

    /**
     * 获取新闻数目
     * @return 数目
     */
    public int getNewsCount() {
        return newsList.size();
    }

    /**
     * 提交
     */
    public void commit() {
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < newsList.size(); i ++) {
            buffer.append(newsList.get(i) + "\t\t");
        }
        tv_news.setText(buffer.toString());
    }

    public void show() {
        isShow = true;
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
        alphaAnimator.setDuration(200);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getContext().getResources().getDisplayMetrics()));
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = NewsBar.this.getLayoutParams();
                params.height = (int) animation.getAnimatedValue();
                NewsBar.this.setLayoutParams(params);
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.playTogether(alphaAnimator, valueAnimator);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
    }

    public void hide() {
        isShow = false;
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        alphaAnimator.setDuration(200);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getContext().getResources().getDisplayMetrics()), 0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = NewsBar.this.getLayoutParams();
                params.height = (int) animation.getAnimatedValue();
                NewsBar.this.setLayoutParams(params);
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.playTogether(alphaAnimator, valueAnimator);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
    }

    public boolean isShow() {
        return isShow;
    }

}




















