package com.zzx.factorytest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzx.factorytest.R;
import com.zzx.factorytest.bean.TestItem;
import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.utils.ImageUtil;

public class TestResultView extends LinearLayout {

	private TestItem testItem;
	private ImageView iv_icon;
	private ImageView iv_result;
	private TextView tv_label;
	private FactoryTestManager factoryTestManager;
	private float textSize = 25f;
	private float textPadding = 0f;
	private int pading = 50;
	private Boolean setResult = null;
	private ImageView imageResult;
	Bitmap successbitmap;
	Bitmap failbitmap;

	private int result_icon_width = 50;
	private int result_icon_height = 50;

	public TestResultView(Context context, TestItem testItem) {
		super(context);

		this.testItem = testItem;
		LayoutInflater.from(context).inflate(R.layout.test_result_list_item,
				this);
		tv_label = (TextView) this.findViewById(R.id.tv_label);
		iv_icon = (ImageView) this.findViewById(R.id.iv_icon);
		iv_result = (ImageView) this.findViewById(R.id.iv_result);
		imageResult = new ImageView(getContext());
		factoryTestManager = FactoryTestManager.getInstance(context);
		initContent();
		// this.setWillNotDraw(false);
		successbitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.check_success);
		failbitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.check_fail);
	}

	public void setResult(Boolean result) {
		setResult = result;
		initContent();
		this.invalidate();
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// super.onDraw(canvas);
	// Paint paint = new Paint();
	//
	// if (setResult != null) {
	// if (setResult) {
	//
	// canvas.drawBitmap(bitmap,
	// iv_icon.getRight() - bitmap.getWidth(),
	// iv_icon.getBottom() - bitmap.getHeight(), paint);
	// } else {
	// Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.check_fail);
	// canvas.drawBitmap(bitmap,
	// iv_icon.getRight() - bitmap.getWidth(),
	// iv_icon.getBottom() - bitmap.getHeight(), paint);
	// // this.setBackgroundColor(Color.RED);
	// }
	// }
	// }

	private void initContent() {
		tv_label.setText(testItem.label);
		Bitmap bitmap=null;
		if (setResult != null) {
			if (setResult) {
				 bitmap = ImageUtil.createBitmap(BitmapFactory.decodeResource(getResources(), testItem.iconId),successbitmap);
			}else{
				 bitmap = ImageUtil.createBitmap(BitmapFactory.decodeResource(getResources(), testItem.iconId),failbitmap);
			}
			iv_icon.setImageBitmap(bitmap);
		}else{
			iv_icon.setImageResource(testItem.iconId);
			
		}
		// boolean result = factoryTestManager.getResult(testItem.itemName);
		// iv_result.setImageResource(result?R.drawable.right:R.drawable.wrong);
	}

}
