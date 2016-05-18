package com.zzx.factorytest.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.zzx.factorytest.R;

public class SignalView extends View {

	private Bitmap bgGrid;
	private int maxDbm = -50;
	private int minDbm = -110;
	private int graphWidth = 300;
	private int padingLeft = 20;
	private int sigalBlockNumber = 100;
	private int[] singalBlock = new int[sigalBlockNumber];
	private int range_red = -95;
	private int range_yellow = -80;
	// private int range_blue=-95;
	private int blockIndex = 0;
	private Paint paint = new Paint();
	private int gridStartX;
	private int gridStartY;
	private int gridEndX;
	private int gridEndY;
	private int mWidth;
	private int mHeight;
	private int gridHeightSpace;
	private int gridWidthSpace;

	public SignalView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		/*graphWidth = (int)this.getResources().getDimension(R.dimen.SignagraphWidth);
		bgGrid = BitmapFactory.decodeResource(getResources(), R.drawable.graph);
		bgGrid = ImageUtil.zoomBitmap(bgGrid, graphWidth, (bgGrid.getHeight()
				* graphWidth / bgGrid.getWidth()));*/
		WindowManager wm = ((Activity)context).getWindowManager();
		Display display = wm.getDefaultDisplay();
		gridWidthSpace = (int) (display.getWidth() * 0.6f / 32);
		gridHeightSpace = (int) (display.getHeight() * 0.35f / 8);
		mWidth = gridWidthSpace * 32;
		mHeight = gridHeightSpace * 8;
	}

	public SignalView(Context context, int _gridHeight, int _gridHeiCount, int _gridWidth, int _gridWidCount) {
		super(context);
		gridHeightSpace	= _gridHeight / _gridHeiCount;
		gridWidthSpace	= _gridWidth / _gridWidCount;
		mWidth	= gridWidthSpace * _gridWidCount;
		mHeight	= gridHeightSpace * _gridHeiCount;
//		rectWidth = mWidth / blockIndex;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

//		drawBgGrid(canvas);
		drawText(canvas);
		drawGrid(canvas);
		drawSigal(canvas);
		super.onDraw(canvas);
	}
	private void drawText(Canvas canvas) {
		Resources res = getResources();
		paint.setStrokeWidth(1f);
		paint.setAntiAlias(true);
		Rect rect = new Rect();
		String str = res.getString(R.string.net_value_longest);
		paint.getTextBounds(str, 0, str.length(), rect);
		gridStartX = rect.width() + 5;
		gridStartY = rect.height() / 2;
		gridEndX = gridStartX + mWidth;
		gridEndY = gridStartY + mHeight;
		TypedArray signalIndex = res.obtainTypedArray(R.array.signal_index);
		int textStartY = 10;
		for (int i = 0; i < signalIndex.length(); i++) {
			canvas.drawText(signalIndex.getString(i), 0, textStartY, paint);
			textStartY += gridHeightSpace * 2;
		}
	}
	void drawGrid(Canvas canvas){
		paint.setStrokeWidth(1f);
		paint.setAntiAlias(true);
		for (int xStart = gridStartX; xStart <= gridEndX; xStart += gridWidthSpace) {
			if (xStart == gridStartX || xStart == gridEndX) {
				paint.setColor(Color.RED);
			} else {
				paint.setColor(Color.GRAY);
			}
			canvas.drawLine(xStart, gridStartY, xStart, gridEndY, paint);
		}
		for (int yStart = gridStartY; yStart <= gridEndY; yStart += gridHeightSpace) {
			if (yStart == gridStartY || yStart == gridEndY) {
				paint.setColor(Color.RED);
			} else {
				paint.setColor(Color.GRAY);
			}
			canvas.drawLine(gridStartX, yStart, gridEndX, yStart, paint);
		}
	}
	private void drawSigal(Canvas canvas) {
		canvas.translate(30, 0);

		paint.setColor(Color.BLACK);
//		float x = (bgGrid.getWidth() / sigalBlockNumber) * blockIndex+6;
		float x = (mWidth / sigalBlockNumber) * blockIndex+6;
//		canvas.drawLine(x, 0, x, bgGrid.getHeight(), paint);
		canvas.drawLine(x, gridStartY, x, mHeight + gridStartY, paint);

		// ��ɫ��
		paint.setStyle(Style.FILL);
		for (int i = 0; i <= blockIndex; i++) {
			if (singalBlock[i] <= range_red) {
				paint.setColor(Color.RED);
			} else if (singalBlock[i] <= range_yellow) {
				paint.setColor(Color.YELLOW);
			} else {
				paint.setColor(Color.GREEN);
			}
			/*float startX = (i == 0 ? 2
					: ((bgGrid.getWidth() / sigalBlockNumber) * (i)));
			float endX = (bgGrid.getWidth() / sigalBlockNumber) * (i + 1);
			float height = (bgGrid.getHeight() / (maxDbm - minDbm))
					* (singalBlock[i] - minDbm);
//			Log.i("test", "height=" + height + "  startX=" + startX + " endX="
//					+ endX + "singalBlock[" + i + "]" + singalBlock[i]);
			canvas.drawRect(startX, bgGrid.getHeight() - height, endX,
					bgGrid.getHeight()-2, paint);*/
			float startX = (i == 0 ? 2
					: ((mWidth / sigalBlockNumber) * (i)));
			float endX = (mWidth / sigalBlockNumber) * (i + 1);
			float height = (mHeight / (maxDbm - minDbm))
					* (singalBlock[i] - minDbm);
//			Log.i("test", "height=" + height + "  startX=" + startX + " endX="
//					+ endX + "singalBlock[" + i + "]" + singalBlock[i]);
			canvas.drawRect(startX, mHeight - height, endX,
					mHeight + gridStartY, paint);

		}
		// ���ź�ֵ
		paint.setColor(Color.BLUE);
		/*float y = (bgGrid.getHeight() / (maxDbm - minDbm))
				* (singalBlock[blockIndex] - minDbm);*/
		float y = (mHeight / (maxDbm - minDbm))
		 * (singalBlock[blockIndex] - minDbm);
		canvas.drawText(singalBlock[blockIndex] + "dbm", x, y, paint);

		blockIndex++;
		if (blockIndex >= sigalBlockNumber) {
			blockIndex = 0;
		}
	}

	/*private void drawBgGrid(Canvas canvas) {
		//
		padingLeft = (int)this.getResources().getDimension(R.dimen.SignapadingLeft);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		for (int i = 0; i < 5; i++) {
			int dbm = maxDbm - i * 15;
			float y = (bgGrid.getHeight()) * i / 4 + 10;
			canvas.drawText(dbm + "", 0, y, paint);
		}
		canvas.drawBitmap(bgGrid, padingLeft, 0, new Paint());

	}*/

	public void onSignalChanged(int strength) {

		if (blockIndex < sigalBlockNumber ) {
			if(strength==0){
				strength=minDbm;
			}
			singalBlock[blockIndex] = strength;
		}

		this.invalidate();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
//		this.setMeasuredDimension(this.getWidth(), bgGrid.getHeight());
		this.setMeasuredDimension(this.getWidth(), mHeight);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}

