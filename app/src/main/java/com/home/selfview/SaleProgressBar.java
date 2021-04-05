package com.home.selfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

import androidx.annotation.Nullable;

/**
 * author : CYS
 * e-mail : 1584935420@qq.com
 * date : 2021/4/5 16:03
 * desc :
 * version : 1.0
 */
public class SaleProgressBar extends View {
    private Paint mBackPaint, mFrontPaint, mTextPaint;
    private float mStrokeWidth = 5;
    private float mHalfStrokeWidth = mStrokeWidth / 2;
    private RectF mRect;
    private int mProgress = 50;
    private int mTargetProgress = 100;
    private int mMax = 100;
    private int mWidth, mHeight;
    private float mRadius;
    //售出比例
    private float scale;
    private float nearOverTextWidth;
    private float overTextWidth;
    private float baseLineY;

    private String nearOverText = "1000";
    private String overText = "132";

    public SaleProgressBar(Context context) {
        super(context);
        //initAttrs(context, null);
        init();
    }

    public SaleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //initAttrs(context, attrs);
        init();
    }

    public SaleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initAttrs(context, attrs);
        init();
    }

//    private void initAttrs(Context context, AttributeSet attrs) {
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SaleProgressView);
//        sideColor = ta.getColor(R.styleable.SaleProgressView_sideColor, 0xffff3c32);
//        textColor = ta.getColor(R.styleable.SaleProgressView_textColor, 0xffff3c32);
//        sideWidth = ta.getDimension(R.styleable.SaleProgressView_sideWidth, dp2px(2));
//        overText = ta.getString(R.styleable.SaleProgressView_overText);
//        nearOverText = ta.getString(R.styleable.SaleProgressView_nearOverText);
//        textSize = ta.getDimension(R.styleable.SaleProgressView_textSize, sp2px(16));
//        //isNeedAnim = ta.getBoolean(R.styleable.SaleProgressView_isNeedAnim,true);
//        ta.recycle();
//    }


    /**
     * 初始化画笔和画板
     */
    private void init() {
        mBackPaint = new Paint();
        mBackPaint.setColor(Color.GRAY);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackPaint.setStrokeWidth(mStrokeWidth);

        mFrontPaint = new Paint();
        mFrontPaint.setColor(Color.RED);
        mFrontPaint.setAntiAlias(true);
        mFrontPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mFrontPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(dp2px(12));
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        nearOverTextWidth = mTextPaint.measureText(nearOverText);
        overTextWidth = mTextPaint.measureText(overText);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取view的宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        //圆角半径
        mRadius = mHeight / 2.0f;
        //留出一定的间隙，避免边框被切掉一部分,mStrokeWidth边框厚度
        if (mRect == null) {
            mRect = new RectF(mStrokeWidth, mStrokeWidth, mWidth - mStrokeWidth,
                    mHeight - mStrokeWidth);
        }
        if (baseLineY == 0.0f) {
            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
            baseLineY = mHeight / 2 - (fm.descent / 2 + fm.ascent / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMax == 0) {
            scale = 0.0f;
        } else {
            scale = Float.parseFloat(new DecimalFormat("0.00").format((float) mProgress / (float) mMax));
        }
        drawBg(canvas);
        drawFg(canvas);
        drawText(canvas);
    }

    //文本
    private void drawText(Canvas canvas) {
        String scaleText = new DecimalFormat("#%").format(scale);
        String saleText = String.format("已抢%s件", mProgress);
        float scaleTextWidth = mTextPaint.measureText(scaleText);

        if (scale < 0.8f) {
            canvas.drawText(saleText, dp2px(10), baseLineY, mTextPaint);
            canvas.drawText(scaleText, mWidth - scaleTextWidth - dp2px(10), baseLineY, mTextPaint);
        } else if (scale < 1.0f) {
            canvas.drawText(nearOverText, mWidth / 2 - nearOverTextWidth / 2, baseLineY, mTextPaint);
            canvas.drawText(scaleText, mWidth - scaleTextWidth - dp2px(10), baseLineY, mTextPaint);
        } else {
            canvas.drawText(overText, mWidth / 2 - overTextWidth / 2, baseLineY, mTextPaint);
        }

//        canvas.drawRoundRect(
//                new RectF(mStrokeWidth, mStrokeWidth,
//                        (mWidth - mStrokeWidth) * scale, mHeight - mStrokeWidth),
//                mRadius, mRadius, mFrontPaint);
    }

    //画背景
    private void drawBg(Canvas canvas) {
        canvas.drawRoundRect(mRect, mRadius, mRadius, mBackPaint);
    }

    //进度条填充
    private void drawFg(Canvas canvas) {
        if (scale == 0) {
            return;
        }
        canvas.drawRoundRect(new RectF(mStrokeWidth,
                        mStrokeWidth, (mWidth - mStrokeWidth) * scale, mHeight - mStrokeWidth),
                mRadius, mRadius, mFrontPaint);
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
