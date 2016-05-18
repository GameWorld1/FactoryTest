package com.zzx.factorytest.view;

import com.zzx.factorytest.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class PowerView extends View {
    private int mGridHeight = 0;
    private boolean DEBUG = true;
	private final String MIC_DEBUG = "micdebug";
	private final int HISTORY_POWER_COUNT = 32;
    private final int GRID_COUNT = 10;
	private double currentPower	= 0;
	private double prePower		= 0;
	private double mAveragePower = 0;
	private double[] historyPowers = new double[HISTORY_POWER_COUNT];
	private int historyIndex = 0;
    private int mViewHeight = 0;
    private int mViewWidth  = 0;
    private int mGridMarginVer = 0;
    private int mGridMarginHor = 0;
    private int mGridStartX = 0;
    private int mGridStartY = 0;
    private int baseValue   = -100;
    private int baseAddvalue = 10;
    private int valueCount  = 10;
    private int mGridSpace = 0;
    private int textHeight  = 0;
    private int textBaseLingY = 0;
    private float mTextStrokeWidth	= 0;
    private float mTextSize = 0;
    private float mTextScaleX = 0;
    private Paint paint = new Paint();
    private Rect rect   = new Rect();
	private Resources mRes = null;
	public PowerView(Context context, int _viewHeight, int _viewWidth) {
		super(context);
		mRes = context.getResources();
//        this.setBackgroundColor(Color.BLUE);
        mViewHeight = _viewHeight;
        mViewWidth  = _viewWidth;
        mGridMarginVer = Integer.valueOf(mRes.getString(R.string.power_view_mGridMarginVer));
        mTextStrokeWidth = Float.valueOf(mRes .getString(R.string.power_view_paint_stroke_width));
        mTextSize = Float.valueOf(mRes.getString(R.string.power_view_paint_text_size));
        mTextScaleX = Float.valueOf(mRes.getString(R.string.power_view_paint_text_scalex));
        paint.setStrokeWidth(mTextStrokeWidth);
        paint.setTextSize(mTextSize);
        paint.setTextScaleX(mTextScaleX);
        paint.getTextBounds(String.valueOf(baseValue), 0, String.valueOf(baseValue).length(), rect);
        mGridMarginHor  = rect.width() / 2;
        textHeight      = rect.height();
        mGridSpace = (mViewWidth - mGridMarginHor * 2) / valueCount;
        mGridHeight = Integer.valueOf(context.getResources().getString(R.string.power_view_grid_height));
        Log.d(MIC_DEBUG, "mViewHeight = " + mViewHeight + " ; mViewWidth = " + mViewWidth + " ; mGridMarginHor = " + mGridMarginHor + " ; mGridSpace = " + mGridSpace);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGridAndText(canvas);
        drawPower(canvas);
        drawDbAndPeak(canvas);
    }

    private void drawPower(Canvas canvas) {
        drawCurrentPower(canvas);
        drawHigestPower(canvas);
    }

    private void drawHigestPower(Canvas canvas) {

    }

    private void drawCurrentPower(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20.f);
        float progressScale = (float) (Math.abs(currentPower) / 100.0);
        float progressWidth = mGridSpace * valueCount * (1 - progressScale);
        int startY = mGridMarginVer + mGridHeight / 2;
        canvas.drawLine(mGridStartX, startY, mGridStartX + progressWidth, startY, paint);
    }


    private void drawGridAndText(Canvas canvas) {
        drawGrid(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mTextStrokeWidth);
        paint.setTextSize(mTextSize);
        paint.setTextScaleX(mTextScaleX);
        int startX = 0;
        textBaseLingY = mGridMarginVer + mGridHeight + textHeight + 5;
        for (int i = baseValue; i <= 0; i += 10) {
            canvas.drawText(String.valueOf(i), startX, textBaseLingY, paint);
//            Log.d(MIC_DEBUG, i + "");
            if (-10 == i) {
                startX += mGridSpace * 1.5;
            } else
                startX += mGridSpace;
        }
    }

    private void drawDbAndPeak(Canvas canvas) {
        String currentDb = currentPower + "";
        paint.setStrokeWidth(Float.valueOf(mRes.getString(R.string.power_view_paint_db_stroke_width)));
        paint.setTextSize(Float.valueOf(mRes.getString(R.string.power_view_paint_db_text_size)));
        paint.setColor(Color.BLUE);
        paint.setTextScaleX(Float.valueOf(mRes.getString(R.string.power_view_paint_db_text_scalex)));
        paint.setAntiAlias(true);
        paint.getTextBounds(currentDb, 0, currentDb.length(), rect);
        if (currentDb.length() >= 6)
            currentDb = currentDb.substring(0, 6);
        int marginTop = Integer.valueOf(mRes.getString(R.string.db_text_margin_top));
        canvas.drawText(currentDb + " dB", 10, textBaseLingY + rect.height() + marginTop, paint);
    }

    private void drawGrid(Canvas canvas) {
        mGridStartY = mGridMarginVer;
        mGridStartX = mGridMarginHor;
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1.0f);
        for (int i = 0; i <= valueCount; i++) {
            canvas.drawLine(mGridStartX, mGridStartY, mGridStartX, mGridStartY + mGridHeight, paint);
            mGridStartX += mGridSpace;
        }
        mGridStartX = mGridMarginHor;
        for (int i = 0; i <= 1; i++) {
            canvas.drawLine(mGridStartX, mGridStartY, mGridStartX + valueCount * mGridSpace, mGridStartY, paint);
            mGridStartY += mGridHeight;
        }
    }

    public void update(double _currentPower) {
		if (_currentPower < -100.0)
            _currentPower = -100.0;
        else if (_currentPower > 0.0)
            _currentPower = 0.0;
        currentPower = (float) _currentPower;
//        Log.d(MainActivity.TAG_D, "PowerView.currentPower --------> " + currentPower);
        // Get the previous power value, and add the new value into the
        // history buffer.  Re-calculate the rolling average power value.
        if (++historyIndex >= historyPowers.length)
            historyIndex = 0;
        prePower = historyPowers[historyIndex];
        historyPowers[historyIndex] = (float) _currentPower;
        mAveragePower -= prePower / HISTORY_POWER_COUNT;
        mAveragePower += (float) _currentPower / HISTORY_POWER_COUNT;
        /*if (DEBUG) {
        	Log.d(MIC_DEBUG, "CurrentPower ---> " + currentPower + "mAveragePower ---> " + mAveragePower);
        }*/
        this.invalidate();
	}

}
