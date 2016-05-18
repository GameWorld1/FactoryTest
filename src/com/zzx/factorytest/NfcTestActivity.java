package com.zzx.factorytest;

import org.xml.sax.XMLReader;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zzx.factorytest.nfc.card.CardManager;
import com.zzx.factorytest.view.JudgeView;

public final class NfcTestActivity extends TestItemBaseActivity implements
		OnClickListener, Html.TagHandler, ImageGetter {
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	private Resources res;
	private TextView board;

	private enum ContentType {
		HINT, DATA, MSG
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_activity);

		final Resources res = getResources();
		this.res = res;
		((JudgeView) findViewById(R.id.judgeview))
				.setOnResultSelectedListener(this);
		this.board = (TextView) findViewById(R.id.board);

		findViewById(R.id.btnNfc).setOnClickListener(this);

		board.setMovementMethod(LinkMovementMethod.getInstance());
		board.setFocusable(false);
		board.setClickable(false);
		board.setLongClickable(false);

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		onNewIntent(getIntent());
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (nfcAdapter != null)
			nfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (nfcAdapter != null)
			nfcAdapter.enableForegroundDispatch(this, pendingIntent,
					CardManager.FILTERS, CardManager.TECHLISTS);

		refreshStatus();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		final Parcelable p = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Log.d("NFCTAG", intent.getAction() + "");
		showData((p != null) ? CardManager.load(p, res) : null);
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {

		case R.id.btnNfc: {
			startActivityForResult(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
			break;
		}

		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		refreshStatus();
	}

	private void refreshStatus() {
		final Resources r = this.res;

		final String tip;
		if (nfcAdapter == null)
			tip = r.getString(R.string.tip_nfc_notfound);
		else if (nfcAdapter.isEnabled())
			tip = r.getString(R.string.tip_nfc_enabled);
		else
			tip = r.getString(R.string.tip_nfc_disabled);

		final StringBuilder s = new StringBuilder(
				r.getString(R.string.app_name));

		s.append("  --  ").append(tip);
		setTitle(s);

		final CharSequence text = board.getText();
		if (text == null || board.getTag() == ContentType.HINT)
			showHint();
	}

	private void copyData() {
		final CharSequence text = board.getText();
		if (text == null || board.getTag() != ContentType.DATA)
			return;

		((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE))
				.setText(text);

		final String msg = res.getString(R.string.msg_copied);
		final Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private void showData(String data) {
		if (data == null || data.length() == 0) {
			showHint();
			return;
		}

		final TextView board = this.board;
		final Resources res = this.res;

		final int padding = res.getDimensionPixelSize(R.dimen.pnl_margin);

		board.setPadding(padding, padding, padding, padding);
		board.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
		board.setTextSize(res.getDimension(R.dimen.text_small));
		board.setTextColor(res.getColor(R.color.text_default));
		board.setGravity(Gravity.NO_GRAVITY);
		board.setTag(ContentType.DATA);
		board.setText(Html.fromHtml(data));
	}

	private void showHelp(int id) {
		final TextView board = this.board;
		final Resources res = this.res;

		final int padding = res.getDimensionPixelSize(R.dimen.pnl_margin);

		board.setPadding(padding, padding, padding, padding);
		board.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
		board.setTextSize(res.getDimension(R.dimen.text_small));
		board.setTextColor(res.getColor(R.color.text_default));
		board.setGravity(Gravity.NO_GRAVITY);
		board.setTag(ContentType.MSG);
		board.setText(Html.fromHtml(res.getString(id), this, this));
	}

	private void showHint() {
		final TextView board = this.board;
		final Resources res = this.res;
		final String hint;

		if (nfcAdapter == null)
			hint = res.getString(R.string.msg_nonfc);
		else if (nfcAdapter.isEnabled())
			hint = res.getString(R.string.msg_nocard);
		else
			hint = res.getString(R.string.msg_nfcdisabled);

		final int padding = res.getDimensionPixelSize(R.dimen.text_middle);

		board.setPadding(padding, padding, padding, padding);
		board.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
		board.setTextSize(res.getDimension(R.dimen.text_middle));
		board.setTextColor(res.getColor(R.color.text_tip));
		board.setGravity(Gravity.CENTER_VERTICAL);
		board.setTag(ContentType.HINT);
		board.setText(Html.fromHtml(hint));
	}

	@Override
	public void handleTag(boolean opening, String tag, Editable output,
			XMLReader xmlReader) {
		if (!opening && "version".equals(tag)) {
			try {
				output.append(getPackageManager().getPackageInfo(
						getPackageName(), 0).versionName);
			} catch (NameNotFoundException e) {
			}
		}
	}

	@Override
	public Drawable getDrawable(String source) {

		return null;
	}

	// @Override
	// public Drawable getDrawable(String source) {
	// final Resources r = getResources();
	//
	// final Drawable ret;
	// final String[] params = source.split(",");
	// if ("icon_main".equals(params[0])) {
	// ret = r.getDrawable(R.drawable.ic_app_main);
	// } else {
	// ret = null;
	// }
	//
	// if (ret != null) {
	// final float f = r.getDisplayMetrics().densityDpi / 72f;
	// final int w = (int) (Util.parseInt(params[1], 10, 16) * f + 0.5f);
	// final int h = (int) (Util.parseInt(params[2], 10, 16) * f + 0.5f);
	// ret.setBounds(0, 0, w, h);
	// }
	//
	// return ret;
	// }
}
