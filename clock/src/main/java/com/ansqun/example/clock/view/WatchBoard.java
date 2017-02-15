package com.ansqun.example.clock.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ansqun on 2017/2/15.
 */
public class WatchBoard extends View {

    private float mRadius;          //外圆半径
    private float mPadding;         //边距
    private float mTextSize;        //文字大小
    private float mHourPointWidth;      //时针宽度
    private float mMinutePointWidth;    //分针宽度
    private float mSecondPointWidth;    //秒针宽度
    private int mPointRadius;           //指针圆角
    private float mPointEndLength;      //指针末尾的长度

    private int mColorLong;
    private int mColorShort;
    private int mHourPointColor;
    private int mMinutePointColor;
    private int mSecondPointColor;

    private Paint mPaint;


    public WatchBoard(Context context) {
        this(context, null);
    }

    public WatchBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
}
