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
import android.widget.ImageView;

import com.unique.daiyiming.ilovecollege.R;

public class ScaleImageView extends ImageView {

	private OnScaleImageViewClickListener listener = null;
	private boolean isClicked = false;
	private float scale = 1.05f;

	public interface OnScaleImageViewClickListener {
		void OnScaleImageViewClick(View view);
	}

	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView);
		scale = typedArray.getFloat(R.styleable.ScaleImageView_imageScale, 1.05f);
	}

	public ScaleImageView(Context context) {
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
						if (xUp >= 0 && xUp <= ScaleImageView.this.getWidth() && yUp >= 0 && yUp <= ScaleImageView.this.getHeight() && listener != null) {
							listener.OnScaleImageViewClick(ScaleImageView.this);
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
	
	public void setOnScaleImageViewClickListener(OnScaleImageViewClickListener listener) {
		this.listener = listener;
	}
	
}





















