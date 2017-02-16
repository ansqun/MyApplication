package com.ansqun.example.clock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ansqun.example.clock.R;
import com.ansqun.example.clock.util.SizeUtil;

import java.util.Calendar;

/**
 * Created by ansqun on 2017/2/15.
 */
public class WatchBoard extends View {
    private static final String TAG = "WatchBoard";

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
        obtainStyleAttrs(attrs);
        initPaint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //给初始化宽度设置一个很大的值,当宽度或者高度确定时取最小值,因为宽高必定有一个为确定值,所以这样过后会得到宽高的最小值
        int width = 1000; //设定一个最小值

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED ||
                heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            try {
                String exception = "宽度高度至少有一个确定的值,不能同时为wrap_content";
                throw new NoDetermineSizeException(exception);
            } catch (NoDetermineSizeException e) {
                e.printStackTrace();
            }
        } else {
            if (widthMode == MeasureSpec.EXACTLY) {
                width = Math.min(widthSize, width);
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                width = Math.min(heightSize, width);
            }
        }
        Log.d(TAG, "width is " + width);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = (Math.min(w, h) - mPadding) / 2;
        mPointEndLength = mRadius / 6;
        Log.d(TAG, "mRadius is " + mRadius);
        Log.d(TAG, "mPointEndLength is " + mPointEndLength);
    }

    /**
     * 获取属性并且设置默认值,添加异常情况的处理(一旦出现异常,使用全部默认值)
     *
     * @param attrs
     */
    private void obtainStyleAttrs(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.WatchBoard);
            mPadding = array.getDimension(R.styleable.WatchBoard_wb_padding, DptoPx(10));
            mTextSize = array.getDimension(R.styleable.WatchBoard_wb_text_size, DptoPx(16));
            mHourPointWidth = array.getDimension(R.styleable.WatchBoard_wb_hour_point_width, DptoPx(5));
            mMinutePointWidth = array.getDimension(R.styleable.WatchBoard_wb_minute_point_width, DptoPx(3));
            mSecondPointWidth = array.getDimension(R.styleable.WatchBoard_wb_second_point_width, DptoPx(2));
            mPointRadius = (int) array.getDimension(R.styleable.WatchBoard_wb_point_cornor_radius, DptoPx(10));
            mPointEndLength = array.getDimension(R.styleable.WatchBoard_wb_point_end_length, DptoPx(10));

            mColorLong = array.getColor(R.styleable.WatchBoard_wb_scale_long_color, Color.argb(255, 0, 0, 0));
            mColorShort = array.getColor(R.styleable.WatchBoard_wb_scale_short_color, Color.argb(125, 0, 0, 0));
            mHourPointColor = array.getColor(R.styleable.WatchBoard_wb_minute_point_color, Color.BLACK);
            mMinutePointColor = array.getColor(R.styleable.WatchBoard_wb_minute_point_color, Color.BLACK);
            mSecondPointColor = array.getColor(R.styleable.WatchBoard_wb_second_point_color, Color.RED);

        } catch (Exception e) {
            mPadding = DptoPx(10);
            mTextSize = DptoPx(16);
            mHourPointWidth = DptoPx(5);
            mMinutePointWidth = DptoPx(3);
            mSecondPointWidth = DptoPx(2);
            mPointRadius = (int) DptoPx(10);
            mPointEndLength = DptoPx(10);

            mColorLong = Color.argb(255, 0, 0, 0);
            mColorShort = Color.argb(125, 0, 0, 0);
            mHourPointColor = Color.BLACK;
            mMinutePointColor = Color.BLACK;
            mSecondPointColor = Color.RED;

        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2); //移动画布中心到View中间

        drawCircle(canvas);
        drawScale(canvas);
        drawPointer(canvas);
        canvas.restore();

        postInvalidateDelayed(1000);
    }

    /**
     * 绘制表盘
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mRadius, mPaint);

    }

    /**
     * 绘制刻度与文字
     *
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(), 1));
        int lineWidth = 0;
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                mPaint.setStrokeWidth(DptoPx(1.5f));
                mPaint.setColor(mColorLong);
                lineWidth = 40;

                mPaint.setTextSize(mTextSize);
                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";
                Rect textBound = new Rect();
                mPaint.getTextBounds(text, 0, text.length(), textBound);
                mPaint.setColor(Color.BLACK);

                canvas.save();
                canvas.translate(0, -mRadius + DptoPx(10) + lineWidth + textBound.bottom - textBound.top);  //移动画布中心到刻度前
                canvas.rotate(-6 * i);//使文字水平显示
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(text, -(textBound.right - textBound.left) / 2, textBound.bottom + DptoPx(5), mPaint);
                canvas.restore();

            } else {
                mPaint.setStrokeWidth(DptoPx(1));
                mPaint.setColor(mColorShort);
                lineWidth = 30;
            }
            canvas.drawLine(0, -mRadius + mPadding, 0, -mRadius + DptoPx(10) + lineWidth, mPaint);
            canvas.rotate(6);
        }
    }

    /**
     * 绘制表针
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int angleHour = (hour % 12) * 360 / 12;
        int angleMinute = minute * 360 / 60;
        int angleSecond = second * 360 / 60;
        Log.d(TAG, "hour is " + hour + ", minute is " + minute + ", second is " + second);

        RectF rectF = new RectF();
        //绘制时针
        canvas.save();
        canvas.rotate(angleHour);
        rectF.set(-mHourPointWidth / 2, -mRadius * 3 / 5, mHourPointWidth / 2, mPointEndLength);
        mPaint.setColor(mHourPointColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointWidth);
        canvas.drawRoundRect(rectF, mPointRadius, mPointRadius,  mPaint);
        canvas.restore();

        //绘制分针
        canvas.save();
        canvas.rotate(angleMinute);
        rectF.set(-mMinutePointWidth / 2, -mRadius * 3.5f / 5, mMinutePointWidth / 2, mPointEndLength);
        mPaint.setColor(mMinutePointColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mMinutePointWidth);
        canvas.drawRoundRect(rectF, mPointRadius, mPointRadius,  mPaint);
        canvas.restore();

        //绘制秒针
        canvas.save();
        canvas.rotate(angleSecond);
        rectF.set(-mSecondPointWidth / 2, -mRadius + 15, mSecondPointWidth / 2, mPointEndLength);
        mPaint.setColor(mSecondPointColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mSecondPointWidth);
        canvas.drawRoundRect(rectF, mPointRadius, mPointRadius,  mPaint);
        canvas.restore();

        //绘制中心小圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSecondPointColor);
        canvas.drawCircle(0, 0, mSecondPointWidth * 4, mPaint);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    private float DptoPx(float value) {
        return SizeUtil.Dp2Px(getContext(), value);
    }

    private float SptoPx(float value) {
        return SizeUtil.Sp2Px(getContext(), value);
    }

    /**
     * 当宽高均为wrap_content的时候抛出异常,因为这样的操作对于这个组件来说是不合理的
     */
    class NoDetermineSizeException extends Exception {
        public NoDetermineSizeException(String message) {
            super(message);
        }
    }

}
