package com.home.selfview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ChenYesheng On 2021/4/20 9:23
 * Desc: 倒计时TextView封装（提供设置倒计时的方法，但是不提供属性）
 */
public class CountDownTimerTextView extends androidx.appcompat.widget.AppCompatTextView {
    private String textPrefix;
    private long millisCount;
    private long millisOneTime = 1000;//默认是1秒每次


    public CountDownTimerTextView(@NonNull Context context) {
        super(context);
    }

    public CountDownTimerTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTimerTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param textPrefix    文本前缀
     * @param millisCount   总毫秒数
     * @param millisOneTime 单次毫秒数
     * @return 设置参数
     */
    public CountDownTimerTextView initMillsCountAndOneTimeText(String textPrefix, long millisCount, long millisOneTime) {
        this.textPrefix = textPrefix;
        this.millisCount = millisCount;
        this.millisOneTime = millisOneTime;
        return this;
    }

    /**
     * 开始倒计时~
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public CountDownTimerTextView startTimer() {
        new CountDownTimer(millisCount, millisOneTime) {

            @Override
            public void onTick(long millisUntilFinished) {
                //setText(getCurrentTime(millisUntilFinished));
                setTimeViews(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                //setText(getCurrentTime(0));
                setTimeViews(0);
            }
        }.start();
        return this;
    }


    //Android/java时间戳转时分秒
    private String getCurrentTime(long value) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = format.format(new Date(value));
        return time;
    }

    /**
     * 不再设置天数统计
     *
     * @param timeMs
     */
    private void setTimeViews(long timeMs) {
        //long dayNum = timeMs / 1000 / 60 / 60 / 24;
        //long hourNum = timeMs / 1000 / 60 / 60 % 24;//超过24小时直接清零的方式，适合于显示天数的格式
        long hourNum = timeMs / 1000 / 60 / 60;
        long minNum = timeMs / 1000 / 60 % 60;
        long secNum = timeMs / 1000 % 60;
        //是否有天数
        //boolean isHaveDay = dayNum > 0;
        //String timeStr = (isHaveDay ? dayNum + "天:" : "")
        String timeStr = getNumStr((int) hourNum)
                + ":"
                + getNumStr((int) minNum)
                + ":"
                + getNumStr((int) secNum);
        setText(String.format("%s %s", textPrefix, timeStr));
    }

    private String getNumStr(int num) {
        return (num < 10 ? "0" : "") + num;
    }

}
