package com.home.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * author : CYS
 * e-mail : 1584935420@qq.com
 * date : 2021/4/5 15:21
 * desc :抢购进度条
 * version : 1.0
 */
public class QgPgBar extends View {
    private Paint mBackPaint, mFrontPaint, mTextPaint;
    private float mStrokeWidth = 50;
    private float mHalfStrokeWidth = mStrokeWidth / 2;
    private float mRadius = 200;
    private RectF mRect;
    private int mProgress = 0;
    //private int mTargetProgress = 90;
    private int mTargetProgress = 100;
    private int mMax = 100;
    private int mWidth, mHeight;


    public QgPgBar(Context context) {
        super(context);
        init();
    }

    public QgPgBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QgPgBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


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
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(80);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    //重写测量大小的方法onMeasure绘制view的核心onDraw();
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getRealSize(widthMeasureSpec);
        mHeight = getRealSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initRect();
        float angle = (float) mProgress / mMax * 360;
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBackPaint);
        canvas.drawArc(mRect, -90, angle, false, mFrontPaint);
        canvas.drawText(mProgress + "%",
                mWidth / 2 + mHalfStrokeWidth,
                mHeight / 2 + mHalfStrokeWidth, mTextPaint);
        if (mProgress < mTargetProgress) {
            mProgress += 1;
            invalidate();
        }
    }

    /**
     * 获取真实尺寸
     *
     * @param mMeasureSpec
     * @return
     */
    private int getRealSize(int mMeasureSpec) {
        int result = 1;
        int mode = MeasureSpec.getMode(mMeasureSpec);
        int size = MeasureSpec.getSize(mMeasureSpec);
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            //自己计算
            result = (int) (mRadius * 2 + mStrokeWidth);
        } else {
            result = size;
        }
        return result;
    }

    /**
     * 初始化矩形
     */
    private void initRect() {
        if (mRect == null) {
            mRect = new RectF();
            int viewSize = (int) (mRadius * 2);
            int left = (mWidth - viewSize) / 2;
            int top = (mHeight - viewSize) / 2;
            int right = left + viewSize;
            int bottom = top + viewSize;
            mRect.set(left, top, right, bottom);
        }
    }
}
