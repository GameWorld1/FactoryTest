package com.zzx.factorytest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.Arrays;

public class LightView extends View {
    public static final String TAG = "LightView.cla";
    private Paint mPaintBackground;
    private RectF mRectFBackground;
    private float[] mLines;
    private int mIntervalNumber = 10;
    private int mLineNumer = mIntervalNumber + 1;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;

    public static int defaultPadding = 16;
    public int mX_Value = 100;
    private Paint mX_ValuePaint;

    public LightView(Context context) {
        this(context, null);
    }

    public LightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d(TAG, " onCreate ");
        init();
    }

    private void init() {
        mPaintBackground = new Paint();
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setColor(Color.WHITE);

        mX_ValuePaint = new Paint(mPaintBackground);
        mX_ValuePaint.setTextAlign(Paint.Align.CENTER);

        mRectFBackground = new RectF();
        mLines = new float[mLineNumer << 2];
        defaultPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultPadding, getResources().getDisplayMetrics());
    }


    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, " onAttachedToWindow ->");
        super.onAttachedToWindow();
        Log.d(TAG, " onAttachedToWindow <-");
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, " onDetachedFromWindow ->");
        super.onDetachedFromWindow();
        Log.d(TAG, " onDetachedFromWindow <-");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, " onMeasure ->");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, " onMeasure <-");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > 0 && height > 0) {

            mPaddingLeft = getPaddingLeft() > 0 ? getPaddingLeft() : defaultPadding;
            mPaddingTop = getPaddingTop() > 0 ? getPaddingTop() : defaultPadding;
            mPaddingRight = getPaddingRight() > 0 ? getPaddingRight() : defaultPadding;
            mPaddingBottom = getPaddingBottom() > 0 ? getPaddingBottom() : defaultPadding;

            mRectFBackground.set(mPaddingLeft, mPaddingTop, width - mPaddingRight, height - mPaddingBottom);
            int interval = measureInterval(mRectFBackground.bottom - mRectFBackground.top, mIntervalNumber);
            for (int i = 0; i < mLineNumer; i++) {
                int index = i << 2;

                mLines[index] = mRectFBackground.left;
                if (i == mIntervalNumber) {
                    mLines[index + 1] = mLines[index + 3] = mRectFBackground.bottom;
                } else {
                    mLines[index + 1] = mLines[index + 3] = interval * i + mRectFBackground.top;
                }

                mLines[index + 2] = mRectFBackground.right;

            }

            Log.d(TAG, Arrays.toString(mLines));


        }


    }

    private int measureInterval(float height, int number) {

        return Math.round(height / number);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, " onDraw ->");
        super.onDraw(canvas);
        Log.d(TAG, " onDraw <-");
        drawBackground(canvas);
        drawX_Value(canvas);


    }

    private void drawX_Value(Canvas canvas) {
        int interval = mX_Value / 10;
        for (int i = 0; i < mLineNumer; i++) {



        }

    }

    private void drawBackground(Canvas canvas) {

        canvas.drawColor(Color.GRAY);
//        canvas.drawRect(mRectFBackground, mPaintBackground);
        canvas.drawLines(mLines, mPaintBackground);

    }

}
