package com.unique.daiyiming.ilovecollege.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.unique.daiyiming.ilovecollege.R;

/**
 * Created by daiyiming on 2015/6/10.
 */
public class ScaleChangeImageView extends ImageView {

    private Bitmap fromImage = null;
    private Bitmap toImage = null;

    private OnScaleChangeImageViewClickListener listener = null;
    private boolean isClicked = false;

    public interface OnScaleChangeImageViewClickListener {
        void OnScaleChangeImageViewClick(View view);
    }

    public ScaleChangeImageView(Context context) {
        super(context);
    }

    public ScaleChangeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChangeImageView);
        int fromImageId = typedArray.getResourceId(R.styleable.ChangeImageView_fromImage, -1);
        int toImageId = typedArray.getResourceId(R.styleable.ChangeImageView_toImage, -1);

        if (fromImageId != -1 && toImageId != -1) {
            fromImage = BitmapFactory.decodeResource(getContext().getResources(), fromImageId);
            toImage = BitmapFactory.decodeResource(getContext().getResources(), toImageId);

            this.setImageBitmap(fromImage);
        } else {
            throw new RuntimeException("未设置fromImage或toImage");
        }
    }

    public void setFromImage(Bitmap fromImage) {
        this.fromImage = fromImage;
    }

    public void setToImage(Bitmap toImage) {
        this.toImage = toImage;
    }

    public void setOnScaleChangeImageViewClickListener(OnScaleChangeImageViewClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("ClickableViewAccessibility") @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (isClicked) {
                    return true;
                }
                isClicked = true;
                this.setImageBitmap(toImage);
                ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1, 1.05f);
                animatorX.setInterpolator(new DecelerateInterpolator());
                animatorX.setDuration(200);
                animatorX.start();

                ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1, 1.05f);
                animatorY.setInterpolator(new DecelerateInterpolator());
                animatorY.setDuration(200);
                animatorY.start();
            }break;
            case MotionEvent.ACTION_CANCEL: { //焦点离开
                ScaleChangeImageView.this.setImageBitmap(fromImage);
                ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1.05f, 1);
                animatorX.setInterpolator(new DecelerateInterpolator());
                animatorX.setDuration(200);
                animatorX.start();

                ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1.05f, 1);
                animatorY.setInterpolator(new DecelerateInterpolator());
                animatorY.setDuration(200);
                animatorY.start();
            }break;
            case MotionEvent.ACTION_UP: {
                final int xUp = (int) event.getX();
                final int yUp = (int) event.getY();

                ScaleChangeImageView.this.setImageBitmap(fromImage);
                ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1.05f, 1);
                animatorX.setInterpolator(new DecelerateInterpolator());
                animatorX.setDuration(200);
                animatorX.start();

                ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1.05f, 1);
                animatorY.setInterpolator(new DecelerateInterpolator());
                animatorY.setDuration(200);
                animatorY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (xUp >= 0 && xUp <= ScaleChangeImageView.this.getWidth() && yUp >= 0 && yUp <= ScaleChangeImageView.this.getHeight() && listener != null) {
                            listener.OnScaleChangeImageViewClick(ScaleChangeImageView.this);
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
}























