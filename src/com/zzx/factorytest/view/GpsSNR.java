package com.zzx.factorytest.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class GpsSNR extends View {

    private Paint mPaint;
    private float mDis;
    private RectF mRectF_1;
    private RectF mRectF_2;
    private RectF mRectF_3;
    private RectF mRectF_4;
    private RectF mRectF_5;
    private float mText;

    public GpsSNR(Context context) {
        this(context, null);
    }

    public GpsSNR(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GpsSNR(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mDis = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

        mText = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());

        mRectF_1 = new RectF();
        mRectF_2 = new RectF();
        mRectF_3 = new RectF();
        mRectF_4 = new RectF();
        mRectF_5 = new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int width = MeasureSpec.makeMeasureSpec((int) (mDis * 2), MeasureSpec.EXACTLY);
        super.onMeasure(width, heightMeasureSpec);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GREEN);

        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        canvas.drawRect(mDis, getPaddingTop(), mDis * 2, height / 5 * 3, mPaint);

        mPaint.setColor(Color.rgb(217, 255, 0));
        canvas.drawRect(mDis, height / 5 * 3, mDis * 2, height / 10 * 7, mPaint);


        mPaint.setColor(Color.rgb(255, 255, 0));
        canvas.drawRect(mDis, height / 10 * 7, mDis * 2, height / 5 * 4, mPaint);

        mPaint.setColor(Color.rgb(255, 128, 0));
        canvas.drawRect(mDis, height / 5 * 4, mDis * 2, height / 10 * 9, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawRect(mDis, height / 10 * 9, mDis * 2, height, mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawText("99", 3, mText + getPaddingTop(), mPaint);
        canvas.drawText("40", 3, height / 5 * 3 + getPaddingTop(), mPaint);
        canvas.drawText("30", 3, height / 10 * 7 + getPaddingTop(), mPaint);
        canvas.drawText("20", 3, height / 5 * 4 + getPaddingTop(), mPaint);
        canvas.drawText("10", 3, height / 10 * 9 + getPaddingTop(), mPaint);
        canvas.drawText("00", 3, height, mPaint);
    }

}
