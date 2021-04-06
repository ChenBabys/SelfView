package com.home.selfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;

/**
 * Created by zh on 2017/8/19.
 */
public class SaleProgressView extends View {

    //商品总数
    private int totalCount;
    //当前卖出数
    private int currentCount;
    //动画需要的
    private int progressCount;
    //售出比例
    private float scale;
    //文字颜色
    private int textColor;
    //背景矩形
    private RectF bgRectF;
    private float radius;
    private int width;
    private int height;
    private PorterDuffXfermode mPorterDuffXfermode;
    private Paint srcPaint, fontPaint;
    private Bitmap mLeftIconSrc;
    private String nearOverText;
    private String overText;
    private float textSize;
    private Paint textPaint;
    private float nearOverTextWidth;
    private float overTextWidth;
    private float baseLineY;
    private boolean isNeedAnim;

    private Paint circlePaint;

    public SaleProgressView(Context context) {
        this(context, null);
    }

    public SaleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SaleProgressView);
        textColor = ta.getColor(R.styleable.SaleProgressView_textColor, 0xffff3c32);
        overText = ta.getString(R.styleable.SaleProgressView_overText);
        nearOverText = ta.getString(R.styleable.SaleProgressView_nearOverText);
        textSize = ta.getDimension(R.styleable.SaleProgressView_textSize, sp2px(11));
        isNeedAnim = ta.getBoolean(R.styleable.SaleProgressView_isNeedAnim, true);
        ta.recycle();
    }

    private void initPaint(Context context) {

        srcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        srcPaint.setColor(ContextCompat.getColor(context, R.color.color_F5F5F5));
        srcPaint.setStyle(Paint.Style.FILL);


        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setColor(ContextCompat.getColor(context, R.color.color_FF392B));
        fontPaint.setStyle(Paint.Style.FILL);


        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(ContextCompat.getColor(context, R.color.color_FFD748));
        //circlePaint.setStrokeWidth(sideWidth);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);

        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        nearOverTextWidth = textPaint.measureText(nearOverText);
        overTextWidth = textPaint.measureText(overText);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取View的宽高
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        //圆角半径
        radius = height / 2.0f;
        //留出一定的间隙，避免边框被切掉一部分
        if (bgRectF == null) {
            bgRectF = new RectF(0, 0, width, height);
        }

        if (baseLineY == 0.0f) {
            Paint.FontMetricsInt fm = textPaint.getFontMetricsInt();
            baseLineY = height / 2 - (fm.descent / 2 + fm.ascent / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isNeedAnim) {
            progressCount = currentCount;
        }

        if (totalCount == 0) {
            scale = 0.0f;
        } else {
            scale = Float.parseFloat(new DecimalFormat("0.00").format((float) progressCount / (float) totalCount));
        }

        drawBg(canvas);
        drawFg(canvas);
        drawCircle(canvas);
        drawImage(canvas);
        drawText(canvas);

        //这里是为了演示动画方便，实际开发中进度只会增加
        if (progressCount != currentCount) {
            if (progressCount < currentCount) {
                progressCount++;
            } else {
                progressCount--;
            }
            postInvalidate();
        }

    }

    private void drawImage(Canvas canvas) {
        if (mLeftIconSrc == null) {
            mLeftIconSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.aec_kill_fire);
        }
        RectF f = new RectF();
        Rect src = new Rect(0, 0, mLeftIconSrc.getWidth(), mLeftIconSrc.getHeight()); // 指定图片在屏幕上显示的区域  图片 >>原矩形
        RectF dst = new RectF(dp2px(4), dp2px(2), height - dp2px(5), height - dp2px(3)); // 绘制图片  屏幕 >>目标矩形
        canvas.drawBitmap(mLeftIconSrc, src, dst, circlePaint);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(height - dp2px(9), height / 2, radius, circlePaint);
    }



    //绘制背景
    private void drawBg(Canvas canvas) {
        canvas.drawRoundRect(bgRectF, radius, radius, srcPaint);
    }

    //绘制进度条
    private void drawFg(Canvas canvas) {
        canvas.drawRoundRect(
                new RectF(0, 0, width * scale, height),
                radius, radius, fontPaint);
    }

    //绘制文字信息
    private void drawText(Canvas canvas) {
        //String scaleText = new DecimalFormat("#%").format(scale);
        String saleText = String.format("已抢%s", progressCount);

        //float scaleTextWidth = textPaint.measureText(scaleText);

        Bitmap textBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textPaint.setColor(textColor);

        if (scale < 0.8f) {
            textCanvas.drawText(saleText + "%", dp2px(40), baseLineY, textPaint);
            //textCanvas.drawText(scaleText, width - scaleTextWidth - dp2px(10), baseLineY, textPaint);
        } else if (scale < 1.0f) {
            textCanvas.drawText(nearOverText, width / 2 - nearOverTextWidth / 2, baseLineY, textPaint);
            //textCanvas.drawText(scaleText, width - scaleTextWidth - dp2px(10), baseLineY, textPaint);
        } else {
            textCanvas.drawText(overText, width / 2 - overTextWidth / 2, baseLineY, textPaint);
        }

        textPaint.setXfermode(mPorterDuffXfermode);
        textPaint.setColor(Color.WHITE);
        textCanvas.drawRoundRect(
                new RectF(0, 0, (width) * scale, height),
                radius, radius, textPaint);
        canvas.drawBitmap(textBitmap, 0, 0, null);
        textPaint.setXfermode(null);
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public void setTotalAndCurrentCount(int totalCount, int currentCount) {
        this.totalCount = totalCount;
        if (currentCount > totalCount) {
            currentCount = totalCount;
        }
        this.currentCount = currentCount;
        postInvalidate();
    }
}