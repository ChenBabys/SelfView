package com.home.selfview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

/**
 * Created by ChenYesheng On 2021/4/6 15:43
 * Desc:
 */
public class SlefProgressBar extends ConstraintLayout {
    private int mBackgroundColor, mFrontGroundColor, mFrontTextColor;
    private float mFrontTextSize;
    private float scale = 0.2f;
    private String mFrontText;
    private ImageView leftIcon;
    private TextView barScale;
    private ConstraintLayout barBg;


    public SlefProgressBar(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public SlefProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SlefProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @SuppressLint("RestrictedApi")
    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TintTypedArray tintTypedArray = TintTypedArray
                    .obtainStyledAttributes(context, attrs, R.styleable.SlefProgressBar,
                            defStyleAttr, 0);
            mBackgroundColor = tintTypedArray.getColor(R.styleable.SlefProgressBar_mBackgroundColor,
                    ContextCompat.getColor(context, R.color.color_F5F5F5));
            mFrontGroundColor = tintTypedArray.getColor(R.styleable.SlefProgressBar_mFrontGroundColor,
                    ContextCompat.getColor(context, R.color.color_FF392B));
            mFrontTextColor = tintTypedArray.getColor(R.styleable.SlefProgressBar_mFrontTextColor,
                    ContextCompat.getColor(context, R.color.white));
            mFrontTextSize = tintTypedArray.getDimension(R.styleable.SlefProgressBar_mFrontTextSize, 20);
            mFrontText = tintTypedArray.getString(R.styleable.SlefProgressBar_mFrontText);
            tintTypedArray.recycle();
        }
        initView(context);
    }

    //改用xml这届布局，代码实现有瑕疵~
    private void initView(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_sale_progress_bar, this, false);
        barBg = view.findViewById(R.id.bar_bg);
        leftIcon = view.findViewById(R.id.bar_image);
        barScale = view.findViewById(R.id.bar_text);
        setTextBackgroundColor(mFrontGroundColor);
        setParentBackgroundColor(mBackgroundColor);
        setTextColor(mFrontTextColor);
        setText(mFrontText);
        setTextSize(mFrontTextSize);
        //设置百分比颜色
        setScale(scale);
        addView(view);
    }

    /**
     * 设置百分比占比
     *
     * @param scale
     */
    private void setScale(float scale) {
        ConstraintLayout.LayoutParams parentLayoutParams = (LayoutParams) barBg.getLayoutParams();
        ConstraintLayout.LayoutParams TextLayoutParams = (LayoutParams) barScale.getLayoutParams();
        TextLayoutParams.width = (int) (parentLayoutParams.width* 30);
        barScale.setLayoutParams(TextLayoutParams);
    }

    private void setTextSize(float size) {
        if (size != 0.0f) {
            barScale.setTextSize(size);
        }
    }

    private void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            barScale.setText(text);
        }
    }

    private void setParentBackgroundColor(int color) {
        if (color != 0) {
            barBg.setBackgroundColor(color);
        }
    }

    private void setTextColor(int color) {
        if (color != 0) {
            barScale.setTextColor(color);
        }
    }

    private void setTextBackgroundColor(int color) {
        if (color != 0) {
            barScale.setBackgroundColor(color);
        }
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
