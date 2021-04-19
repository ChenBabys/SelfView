package com.home.selfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
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
        //设置渐变色填充
        LinearGradient linearGradient = new LinearGradient(0, 0, 100, 100
                , Color.parseColor("#FF563A"), Color.parseColor("#FF212A")
                , Shader.TileMode.MIRROR);
        fontPaint.setShader(linearGradient);
        fontPaint.setStyle(Paint.Style.FILL);


        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(ContextCompat.getColor(context, R.color.color_FFD748));
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
        Rect src = new Rect(0, 0, mLeftIconSrc.getWidth(), mLeftIconSrc.getHeight()); // 指定图片在屏幕上显示的区域  图片 >>原矩形
        //绘制他们左上右下的坐标位置（注意！：这个绘制是左上角的是0,0开始的。）
        RectF dst = new RectF(height * 0.25f, height * 0.18f, height * 0.75f, height * 0.82f); // 绘制图片  屏幕 >>目标矩形
        canvas.drawBitmap(mLeftIconSrc, src, dst, circlePaint);
    }

    private void drawCircle(Canvas canvas) {
        //前面的参数分别是x轴和y轴的圆心坐标
        canvas.drawCircle(height / 2, height / 2, radius, circlePaint);
    }


    //绘制背景
    private void drawBg(Canvas canvas) {
        canvas.drawRoundRect(bgRectF, radius, radius, srcPaint);
    }

    //绘制纯颜色的进度条
    private void drawFg(Canvas canvas) {
        //如果当前的比例小于圆形图表则不绘制。为了避免开头的绘制被挤压出边缘
        if (width * scale < height) return;
        Log.d("测试", String.valueOf(scale) + "," + height + "," + width * scale);
        canvas.drawRoundRect(
                new RectF(0, 0, width * scale, height),
                radius, radius, fontPaint);
    }

    //绘制填充图片的进度条,弃用
//    private void drawFg(Canvas canvas) {
//        if (scale == 0.0f) {
//            return;
//        }
//        Bitmap fgBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas fgCanvas = new Canvas(fgBitmap);
//        //可以填充图片
//        Bitmap fgSrc = BitmapFactory.decodeResource(getResources(), R.mipmap.fg);
//        fgCanvas.drawRoundRect(
//                new RectF(0, 0, width * scale, height),
//                radius, radius, srcPaint);
//
//        srcPaint.setXfermode(mPorterDuffXfermode);
//        fgCanvas.drawBitmap(fgSrc, null, bgRectF, srcPaint);
//
//        canvas.drawBitmap(fgBitmap, 0, 0, null);
//        srcPaint.setXfermode(null);
//    }

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