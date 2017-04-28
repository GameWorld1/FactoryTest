package com.zzx.factorytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.zzx.factorytest.bean.TestItem;
import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.view.TestItemGridViewAdapter;

public class TestGridActivity extends Activity implements OnItemClickListener {

    private TestItemGridViewAdapter mAdapter;
    private GridView mGridView;
    private FactoryTestManager factoryTestManager;
    private Handler mHandler;
    private Class currentTestingCls;
    private Button mReturnBtn;
    public static final int RESULT_CODE_TESTOK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_item_grid);
        findView();
        initializeView();
        setListener();
        mHandler = new Handler();
        factoryTestManager = FactoryTestManager.getInstance(this);

        if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_AUTO_TEST) {
            factoryTestManager.clearTestResult();
            jumpActivity(factoryTestManager.getNextActivityClass(null));
        }
    }

    private void setListener() {
        mGridView.setOnItemClickListener(this);
    }

    private void initializeView() {
        mAdapter = new TestItemGridViewAdapter(this);
        mAdapter.isShowResult(true);
        mGridView.setAdapter(mAdapter);

    }

    private void findView() {
        mGridView = (GridView) findViewById(R.id.item_grid);
        mReturnBtn = (Button) findViewById(R.id.btn_return);
    }


    public void jumpActivity(final Class activityCls) {
        currentTestingCls = activityCls;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(new Intent(TestGridActivity.this,
                        activityCls), 0);
            }
        }, 1500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CODE_TESTOK) {
            Class nextActivity = factoryTestManager.getNextActivityClass(currentTestingCls);
            if (nextActivity == null) {// 测试完成
                Toast.makeText(this, "测试完成!", Toast.LENGTH_SHORT).show();
            } else {
                jumpActivity(nextActivity);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void btnReturn(View view) {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_AUTO_TEST) {
            // 自动测试
//			factoryTestManager.clearTestResult();
            mAdapter.setItemClickEnable(false);
            mReturnBtn.setEnabled(false);
        } else if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_SINGAL_TEST) {
            // 单项
            mAdapter.setItemClickEnable(true);
            mReturnBtn.setEnabled(true);
        } else if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_RESULT_TEST) {
            // 查看报表
            mAdapter.setItemClickEnable(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        startActivity(new Intent(this,
                ((TestItem) mAdapter.getItem(position)).activityCls));

    }
}
