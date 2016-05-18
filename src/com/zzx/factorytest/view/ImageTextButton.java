package com.zzx.factorytest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.zzx.factorytest.R;



public class ImageTextButton extends ImageButton {
	private String text = "";
	private int color = 0;
	private float textSize = 25f;
	private float textPadding = 0f;
	private Context mContext;
	private int pading = 50;
	private Boolean setResult = null;

	public ImageTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.imageTextButton);
		this.text = a.getString(R.styleable.imageTextButton_text);//
		this.color = a.getInteger(R.styleable.imageTextButton_textColor,
				android.R.color.white);
		// this.textSize = a.getDimension(R.styleable.imageTextButton_textSize,
		// 25);
		this.setPadding(0, 0, 0, pading);
		this.setFocusable(false);
		
		
		
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setTextSize(int textsize) {
		this.textSize = textsize;
	}

	public void setResult(boolean result) {
		setResult = result;
		if(!setResult){
			this.setBackgroundColor(Color.RED);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setTextAlign(Align.CENTER);
		paint.setColor(mContext.getResources().getColor(android.R.color.black));
		paint.setTextSize(textSize);
		if (text != null) {
			canvas.drawText(text, this.getWidth() / 2,
					(this.getHeight() - pading / 2), paint);
		}
		if (setResult != null) {
			if (setResult) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.check_success);
				canvas.drawBitmap(bitmap, 10,
						10, paint);
			} else {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.check_fail);
				canvas.drawBitmap(bitmap, 10,
						10, paint);
			}
		}
	}
}