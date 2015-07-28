package com.unique.daiyiming.ilovecollege.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unique.daiyiming.ilovecollege.ScreenParams.ScreenParams;
import com.unique.daiyiming.ilovecollege.R;

/**
 * Created by daiyiming on 2015/6/20.
 */
public class ViewPagerTabBar extends RelativeLayout implements View.OnClickListener {

    public final static int TAB_ONE = 0;
    public final static int TAB_TWO = 1;
    public final static int TAB_THREE = 2;

    private TextView[] tv_tabs = new TextView[3];
    private View slide = null;

    private int normalColor = Color.parseColor("#ffffff");
    private int selectColor = Color.parseColor("#ff0000");

    private int currentPosition = 0;

    private OnViewPagerTabBarChangeListener listener = null;

    public interface OnViewPagerTabBarChangeListener {
        void OnViewPagerTabBarChanged(int currentPosition);
    }

    public ViewPagerTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerTabBar);

        normalColor = typedArray.getColor(R.styleable.ViewPagerTabBar_normalColor, normalColor);
        selectColor = typedArray.getColor(R.styleable.ViewPagerTabBar_selectColor, selectColor);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.ViewPagerTabBar_tabTextSize, 0);

        ScreenParams screenParams = new ScreenParams(context);

        LinearLayout ll_tabContainer = new LinearLayout(context);
        ll_tabContainer.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams containerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ll_tabContainer.setLayoutParams(containerParams);
        this.addView(ll_tabContainer);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        tvParams.weight = 1;

        tv_tabs[TAB_ONE] = new TextView(context);
        tv_tabs[TAB_ONE].setLayoutParams(tvParams);
        tv_tabs[TAB_ONE].setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tv_tabs[TAB_ONE].setText(typedArray.getString(R.styleable.ViewPagerTabBar_tabText1));
        tv_tabs[TAB_ONE].setTextColor(selectColor);
        tv_tabs[TAB_ONE].setGravity(Gravity.CENTER);
        tv_tabs[TAB_ONE].setOnClickListener(this);
        tv_tabs[TAB_ONE].setTag(TAB_ONE);
        ll_tabContainer.addView(tv_tabs[TAB_ONE]);

        tv_tabs[TAB_TWO] = new TextView(context);
        tv_tabs[TAB_TWO].setLayoutParams(tvParams);
        tv_tabs[TAB_TWO].setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tv_tabs[TAB_TWO].setText(typedArray.getString(R.styleable.ViewPagerTabBar_tabText2));
        tv_tabs[TAB_TWO].setTextColor(normalColor);
        tv_tabs[TAB_TWO].setGravity(Gravity.CENTER);
        tv_tabs[TAB_TWO].setOnClickListener(this);
        tv_tabs[TAB_TWO].setTag(TAB_TWO);
        ll_tabContainer.addView(tv_tabs[TAB_TWO]);

        tv_tabs[TAB_THREE] = new TextView(context);
        tv_tabs[TAB_THREE].setLayoutParams(tvParams);
        tv_tabs[TAB_THREE].setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tv_tabs[TAB_THREE].setText(typedArray.getString(R.styleable.ViewPagerTabBar_tabText3));
        tv_tabs[TAB_THREE].setTextColor(normalColor);
        tv_tabs[TAB_THREE].setGravity(Gravity.CENTER);
        tv_tabs[TAB_THREE].setOnClickListener(this);
        tv_tabs[TAB_THREE].setTag(TAB_THREE);
        ll_tabContainer.addView(tv_tabs[TAB_THREE]);

        slide = new View(context);
        slide.setBackgroundColor(selectColor);
        LayoutParams slideParams = new LayoutParams(screenParams.getScreenWidth() / 3, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()));
        slideParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        slide.setLayoutParams(slideParams);
        this.addView(slide);
    }

    public void setCurrentPosition(int position) {
        if (position == currentPosition || position < 0 || position > 2) {
            return;
        }
        //改变文字颜色
        tv_tabs[currentPosition].setTextColor(normalColor);
        tv_tabs[position].setTextColor(selectColor);
        //滑动滑块
        ValueAnimator animator = ValueAnimator.ofInt(slide.getWidth() * currentPosition, slide.getWidth() * position);
        animator.setDuration(300);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams slideParams = (LayoutParams) slide.getLayoutParams();
                slideParams.leftMargin = (int) animation.getAnimatedValue();
                slide.setLayoutParams(slideParams);
            }
        });
        animator.start();
        currentPosition = position;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public void setOnViewPagerTabBarChangeListener(OnViewPagerTabBarChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        int positon = (Integer) v.getTag();
        setCurrentPosition(positon);
        if (listener != null) {
            listener.OnViewPagerTabBarChanged(positon);
        }
    }

}





























