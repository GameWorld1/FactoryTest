package com.zzx.factorytest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zzx.factorytest.R;
import com.zzx.factorytest.bean.WifiStrength;

import org.achartengine.model.Point;

import java.util.ArrayList;
import java.util.List;

public class WifiStrengthView extends View {

	private float graphWidth;
	private float graphHeigth;
	private float padingLeft = 90;
	private float padingRight = 20;
	private float padingTop = 10;
	private float padingBottom = 80;
	private float textPadingLeft = 50;
	private int maxStrength = -30;
	private int minStrength = -90;
	private int maxchannel = 14;
	private int yLineNumber = 7;// y轴刻度数
	private int axisXSpan = 2; // x轴跨度
	private RectF rect;
	private List<WifiStrength> wifiList = new ArrayList<WifiStrength>();
	private int[] colorList = new int[] { Color.RED, Color.BLUE, Color.GREEN,
			Color.YELLOW, Color.GRAY };

	public WifiStrengthView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setBackgroundColor(Color.BLACK);
	}

	public void updateData(List<WifiStrength> wifiList) {
		this.wifiList = wifiList;
		this.invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// graphWidth = this.getWidth();
		// graphHeigth = this.getHeight();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		graphWidth = this.getWidth();
		graphHeigth = this.getHeight();
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawGridBg(canvas);
		drawStrengthView(canvas);
		super.onDraw(canvas);
	}

	private Point p1 = new Point();

	private Point p2 = new Point();

	private Point p3 = new Point();
	private float firstMultiplier;

	private float secondMultiplier;

	// 画信号曲线
	private void drawStrengthView(Canvas canvas) {

		// 模拟数据
		for (int j = 0; j < wifiList.size() && j < colorList.length; j++) {
			WifiStrength wifiStrength = wifiList.get(j);
			wifiStrength.color = colorList[j];
			// wifiStrength.channel = 8;
			//
			// wifiStrength.strength = -50;
			// wifiStrength.label = "zzx[-50]";

			// if (wifiStrength.channel - axisXSpan < 0) {
			// wifiStrength.channel = axisXSpan;
			// }

			firstMultiplier = 0.3f;
			secondMultiplier = 1 - firstMultiplier;
			List<Float> points = new ArrayList<Float>();
			float xTop = rect.left + getBottomChanelGap()
					* (wifiStrength.channel + axisXSpan - 1);
			float yTop = rect.bottom
					- ((rect.bottom - rect.top) / (yLineNumber + axisXSpan))
					* (wifiStrength.strength - minStrength + ((maxStrength - minStrength) / (yLineNumber + 1)))
					/ ((maxStrength - minStrength) / (yLineNumber + 1));
			// float x2
			float x1 = xTop - axisXSpan * getBottomChanelGap();

			float x2 = xTop + axisXSpan * getBottomChanelGap();

			points.add(x1);// x
			points.add(rect.bottom);// y

			points.add(xTop);// x
			points.add(yTop);// y

			points.add(x2);// x
			points.add(rect.bottom);// y
			Path p = new Path();
			float x = points.get(0);
			float y = points.get(1);
			p.moveTo(x, y);

			int length = points.size();

			for (int i = 0; i < length; i += 2) {
				int nextIndex = i + 2 < length ? i + 2 : i;
				int nextNextIndex = i + 4 < length ? i + 4 : nextIndex;
				calc(points, p1, i, nextIndex, secondMultiplier);
				p2.setX(points.get(nextIndex));
				p2.setY(points.get(nextIndex + 1));
				calc(points, p3, nextIndex, nextNextIndex, firstMultiplier);
				// From last point, approaching x1/y1 and x2/y2 and ends up at
				// x3/y3

				p.cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY());
			}

			Paint paint = new Paint();
			paint.setColor(wifiStrength.color);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(3);
			paint.setAntiAlias(true);
			canvas.drawPath(p, paint);

			// 画标签
			paint.setStrokeWidth(2);
			paint.setTextSize(this.getResources().getDimension(
					R.dimen.wifitextsizes));
			paint.setStyle(Style.STROKE);
			paint.setStyle(Style.FILL);
			String text = wifiStrength.label + "(" + wifiStrength.strength
					+ "," + wifiStrength.channel + ")";
			float textWidth = paint.measureText(text);
			canvas.drawText(text, xTop - textWidth / 2, yTop, paint);
			canvas.drawCircle(xTop, yTop, 2, paint);
		}
	}

	private void calc(List<Float> points, Point result, int index1, int index2,
			final float multiplier) {
		float p1x = points.get(index1);
		float p1y = points.get(index1 + 1);
		float p2x = points.get(index2);
		float p2y = points.get(index2 + 1);

		float diffX = p2x - p1x; // p2.x - p1.x;
		float diffY = p2y - p1y; // p2.y - p1.y;
		result.setX(p1x + (diffX * multiplier));
		result.setY(p1y + (diffY * multiplier));
	}

	private void drawGridBg(Canvas canvas) {
		padingLeft = this.getResources().getDimension(R.dimen.wifipadingleft);
		padingBottom = this.getResources().getDimension(
				R.dimen.wifipadingbottom);
		textPadingLeft = this.getResources().getDimension(
				R.dimen.wifitextpadingleft);
		// 画外框
		rect = new RectF(padingLeft, padingTop, graphWidth - padingRight,
				graphHeigth - padingBottom);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(rect, paint);

		// 画内线
		float gap = rect.height() / (yLineNumber + 1);
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		paint.setPathEffect(effects);
		for (int i = 0; i < yLineNumber; i++) {
			Path path = new Path();
			path.moveTo(rect.left, rect.top + (i + 1) * gap);
			path.lineTo(rect.right, rect.top + (i + 1) * gap);
			canvas.drawPath(path, paint);
		}
		// 画标尺

		int numberGap = (maxStrength - minStrength) / (yLineNumber - 1);
		paint.setStyle(Style.FILL);
		paint.setTextSize(this.getResources().getDimension(
				R.dimen.wifitextsizes));
		paint.setPathEffect(null);
		for (int i = 0; i < yLineNumber; i++) {
			canvas.drawText((maxStrength - i * numberGap) + "", textPadingLeft,
					rect.top + (i + 1) * gap, paint);
		}
		gap = getBottomChanelGap();
		for (int i = 1; i <= maxchannel; i++) {
			float textWidth = paint.measureText(i + "");

			canvas.drawText(i + "", rect.left + (i + axisXSpan - 1) * gap
					- textWidth / 2, rect.bottom + 20, paint);
		}
		// 画文字说明
		paint.setTextSize(this.getResources().getDimension(
				R.dimen.wifitextsizeg));
		canvas.drawText(
				"WIFI频道",
				(rect.right - rect.left) / 2,
				this.getBottom()
						- this.getResources().getDimension(
								R.dimen.wificanvastext), paint);

		drawText(canvas, "信号强度[dBm]",
				this.getResources().getDimension(R.dimen.wifidrawtext),
				this.getHeight() / 2, paint, 270);

	}

	/**
	 * 取得底部频道标尺间距
	 * 
	 * @return
	 */
	private float getBottomChanelGap() {
		return rect.width() / (maxchannel + 1);

	}

	void drawText(Canvas canvas, String text, float x, float y, Paint paint,
			float angle) {
		if (angle != 0) {
			canvas.rotate(angle, x, y);
		}
		canvas.drawText(text, x, y, paint);
		if (angle != 0) {
			canvas.rotate(-angle, x, y);
		}
	}
}
