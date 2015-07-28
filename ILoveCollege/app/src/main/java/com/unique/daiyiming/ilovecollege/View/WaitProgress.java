package com.unique.daiyiming.ilovecollege.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.unique.daiyiming.ilovecollege.R;
import com.unique.daiyiming.ilovecollege.ScreenParams.ScreenParams;

/**
 * daiyiming
 */
public class WaitProgress extends RelativeLayout{

	private Context context = null;
	private ScreenParams screenParams = null;
	private DotView[] dots = null;
	private int radius = 0;
	
	private class DotView extends View {

		public DotView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public DotView(Context context) {
			super(context);
		}
		
		@SuppressLint("DrawAllocation") @Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			
			canvas.drawColor(Color.parseColor("#00fafafa"));
			
			Paint paint = new Paint();
			paint.setColor(Color.parseColor("#fafafa"));
			paint.setStyle(Style.FILL);
			paint.setAntiAlias(true);
			
			canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, paint);
		}
		
	}
	
	public WaitProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaitProgress);
		radius = (int) typedArray.getDimensionPixelSize(R.styleable.WaitProgress_dotRadius, 0);
		init();
	}

	public WaitProgress(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	private void init() {
		screenParams = new ScreenParams(context);
		LinearLayout dotsContainer = new LinearLayout(getContext());
		dotsContainer.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams dotsContainerParams = new LayoutParams(LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		dotsContainerParams.addRule(CENTER_IN_PARENT, TRUE);
		dotsContainer.setLayoutParams(dotsContainerParams);
		int dotHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, getResources().getDisplayMetrics());;
		int dotWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, getResources().getDisplayMetrics());;
		int dotMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());;
		dotsContainer.setGravity(Gravity.CENTER);
		this.addView(dotsContainer);
		dots = new DotView[4];
		for (int i = 0; i < dots.length; i ++) {
			dots[i] = new DotView(context);
			LinearLayout.LayoutParams dotLayoutParams = new LinearLayout.LayoutParams(dotWidth, dotHeight);
			dotLayoutParams.setMargins(dotMargin, dotMargin, dotMargin, dotMargin);
			dots[i].setLayoutParams(dotLayoutParams);
			dotsContainer.addView(dots[i]);
		}
		
		moveOut();
	}
	
	private void moveOut() {
		for (int i = dots.length - 1, j = 1; i >= 0; i --, j ++) {
			ObjectAnimator animatorSlideOut = ObjectAnimator.ofFloat(dots[i], "translationX", 0, screenParams.getScreenWidth());
			animatorSlideOut.setInterpolator(new AccelerateInterpolator());
			animatorSlideOut.setDuration(500);
			animatorSlideOut.setStartDelay(j * 200);
			if (i == 0) {
				animatorSlideOut.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						moveIn();
					}
				});
			}
			
			ObjectAnimator animatorHide = ObjectAnimator.ofFloat(dots[i], "alpha", 1, 0);
			animatorHide.setInterpolator(new AccelerateInterpolator());
			animatorHide.setDuration(500);
			animatorHide.setStartDelay(j * 200);
			
			animatorSlideOut.start();
			animatorHide.start();
		}
	}
	
	private void moveIn() {
		for (int i = dots.length - 1, j = 1; i >= 0; i --, j ++) {
			ObjectAnimator animatorSlideOut = ObjectAnimator.ofFloat(dots[i], "translationX", 0 - screenParams.getScreenWidth(), 0);
			animatorSlideOut.setInterpolator(new DecelerateInterpolator());
			animatorSlideOut.setDuration(500);
			animatorSlideOut.setStartDelay(j * 200);
			if (i == 0) {
				animatorSlideOut.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						moveOut();
					}
				});
			}
			
			ObjectAnimator animatorShow = ObjectAnimator.ofFloat(dots[i], "alpha", 0, 1);
			animatorShow.setInterpolator(new AccelerateInterpolator());
			animatorShow.setDuration(500);
			animatorShow.setStartDelay(j * 200);
			
			animatorSlideOut.start();
			animatorShow.start();
		}
	}
	
	public void show() {
		setVisibility(View.VISIBLE);
		ObjectAnimator animatorShow = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
		animatorShow.setInterpolator(new DecelerateInterpolator());
		animatorShow.setDuration(300);
		animatorShow.start();
	}
	
	public void hide() {
		ObjectAnimator animatorHide = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
		animatorHide.setInterpolator(new DecelerateInterpolator());
		animatorHide.setDuration(300);
		animatorHide.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				WaitProgress.this.setVisibility(View.GONE);
				super.onAnimationEnd(animation);
			}
		});
		animatorHide.start();
	}
	
}
