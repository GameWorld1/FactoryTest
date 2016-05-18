package com.zzx.factorytest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zzx.factorytest.R;

/**
 * �������ײ�����ж�view
 * ��ΰ��
 * 2013-4-12
 */
public class JudgeView extends LinearLayout implements OnClickListener {

    private ImageButton ib_right;
    private ImageButton ib_wrong;
    private OnResultSelected onResultSelected;

    public JudgeView(Context context) {
        this(context, null);
    }

    public JudgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.judge_view_layout,
                this);

        ib_right = (ImageButton) findViewById(R.id.ib_right);
        ib_wrong = (ImageButton) findViewById(R.id.ib_wrong);
        ib_right.setOnClickListener(this);
        ib_wrong.setOnClickListener(this);
    }

    public interface OnResultSelected {
        void onSelectResult(boolean success);
    }

    public void setOnResultSelectedListener(OnResultSelected onResultSelected) {
        this.onResultSelected = onResultSelected;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ib_right.getId()) {
            if (null == onResultSelected)
                Log.d("judgeview", "onResultSelected is null");
            else
                Log.d("judgeview", "onResultSelected is ok");
            if (null != onResultSelected)
                onResultSelected.onSelectResult(true);
        } else if (v.getId() == ib_wrong.getId()) {
            if (null != onResultSelected)
                onResultSelected.onSelectResult(false);

        }
    }
}
