package com.unique.daiyiming.ilovecollege.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.unique.daiyiming.ilovecollege.R;

public class ScaleTextView extends TextView {

	private OnScaleTextViewClickListener listener = null;
	private boolean isClicked = false;
	private float scale = 1.05f;
	
	public interface OnScaleTextViewClickListener {
		void OnScaleTextViewClick(View view);
	}
	
	public ScaleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleTextView);
		scale = typedArray.getFloat(R.styleable.ScaleTextView_textScale, 1.05f);
	}

	public ScaleTextView(Context context) {
		super(context);
	}
	
	
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				if (isClicked) {
					return true;
				}
				isClicked = true;
				ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1, scale);
				animatorX.setInterpolator(new DecelerateInterpolator());
				animatorX.setDuration(200);
				animatorX.start();
				
				ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1, scale);
				animatorY.setInterpolator(new DecelerateInterpolator());
				animatorY.setDuration(200);
				animatorY.start();
			}break;
			case MotionEvent.ACTION_CANCEL: { //焦点离开
				ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", scale, 1);
				animatorX.setInterpolator(new DecelerateInterpolator());
				animatorX.setDuration(200);
				animatorX.start();

				ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", scale, 1);
				animatorY.setInterpolator(new DecelerateInterpolator());
				animatorY.setDuration(200);
				animatorY.start();
			}break;
			case MotionEvent.ACTION_UP: {
				final int xUp = (int) event.getX();
				final int yUp = (int) event.getY();

				ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", scale, 1);
				animatorX.setInterpolator(new DecelerateInterpolator());
				animatorX.setDuration(200);
				animatorX.start();
				
				ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", scale, 1);
				animatorY.setInterpolator(new DecelerateInterpolator());
				animatorY.setDuration(200);
				animatorY.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						if (xUp >= 0 && xUp <= ScaleTextView.this.getWidth() && yUp >= 0 && yUp <= ScaleTextView.this.getHeight() && listener != null) {
							listener.OnScaleTextViewClick(ScaleTextView.this);
						}
						isClicked = false;
						super.onAnimationEnd(animation);
					}
				});
				animatorY.start();
			}break;
		}
		return true;
	}
	
	public void setOnScaleTextViewClickListener(OnScaleTextViewClickListener listener) {
		this.listener = listener;
	}
	
}





















