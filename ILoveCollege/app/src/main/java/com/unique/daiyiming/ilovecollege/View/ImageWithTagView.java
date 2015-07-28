package com.unique.daiyiming.ilovecollege.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unique.daiyiming.ilovecollege.R;

/**
 * Created by daiyiming on 2015/6/16.
 */
public class ImageWithTagView extends RelativeLayout implements View.OnClickListener {

    private ImageView imageView = null;
    private TextView textView = null;
    private int imgSize = 0;
    private float textSize = 0;

    private boolean selected = false;

    private Bitmap normalBitmap = null;
    private Bitmap selectedBitmap = null;
    private String text = "";

    private OnImageWithTagViewClickListener listener = null;

    public interface OnImageWithTagViewClickListener {
        void OnImageWithTagViewClicked(View view);
    }

    public ImageWithTagView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageWithTagView);
        imgSize = typedArray.getDimensionPixelSize(R.styleable.ImageWithTagView_imgSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics()));
        textSize = typedArray.getDimension(R.styleable.ImageWithTagView_textSize, 16);
        text = typedArray.getString(R.styleable.ImageWithTagView_text);
        int srcNormalId = typedArray.getResourceId(R.styleable.ImageWithTagView_imgNormalSrc, -1);
        if (srcNormalId == -1) {
            normalBitmap = null;
        } else {
            normalBitmap = BitmapFactory.decodeResource(context.getResources(), srcNormalId);
        }
        int srcSelectedId = typedArray.getResourceId(R.styleable.ImageWithTagView_imgSelectedSrc, -1);
        if (srcSelectedId == -1) {
            selectedBitmap = null;
        } else {
            selectedBitmap = BitmapFactory.decodeResource(context.getResources(), srcSelectedId);
        }
        selected = typedArray.getBoolean(R.styleable.ImageWithTagView_selected, false);
        init();
    }

    private void init() {
        LinearLayout ll_container = new LinearLayout(getContext());
        LayoutParams containerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        containerParams.addRule(CENTER_IN_PARENT, TRUE);
        ll_container.setLayoutParams(containerParams);
        ll_container.setOrientation(LinearLayout.VERTICAL);
        this.addView(ll_container);

        imageView = new ImageView(getContext());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imgSize, imgSize);
        imageParams.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(imageParams);
        if (selected) {
            imageView.setImageBitmap(selectedBitmap);
        } else {
            imageView.setImageBitmap(normalBitmap);
        }
        ll_container.addView(imageView);

        textView = new TextView(getContext());
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setLayoutParams(textParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setText(text);
        if (selected) {
            textView.setTextColor(Color.parseColor("#FF0000"));
            imageView.setImageBitmap(selectedBitmap);
        } else {
            textView.setTextColor(Color.parseColor("#6B6B6B"));
            imageView.setImageBitmap(normalBitmap);
        }
        ll_container.addView(textView);

        ll_container.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.OnImageWithTagViewClicked(this);
        }
    }

    public void setOnImageWithTagViewClickListener(OnImageWithTagViewClickListener listener) {
        this.listener = listener;
    }

    public void setSelected(boolean tag) {
        if (tag) {
            textView.setTextColor(Color.parseColor("#FF0000"));
            imageView.setImageBitmap(selectedBitmap);
        } else {
            textView.setTextColor(Color.parseColor("#6B6B6B"));
            imageView.setImageBitmap(normalBitmap);
        }
        this.selected = tag;
    }

    public boolean isSelected() {
        return selected;
    }
}























