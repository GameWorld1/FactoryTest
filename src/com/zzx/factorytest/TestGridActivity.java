package com.zzx.factorytest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzx.factorytest.bean.TestItem;
import com.zzx.factorytest.manager.FactoryTestManager;
import com.zzx.factorytest.view.TestItemGridViewAdapter;

public class TestGridActivity extends Activity implements OnItemClickListener {

    private TestItemGridViewAdapter adapter;
    private GridView gridView;
    private FactoryTestManager factoryTestManager;
    private Handler mHandler;
    private Class currentTestingCls;
    private Button btn_return;
    public static final int RESULT_CODE_TESTOK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_item_grid);
        gridView = (GridView) findViewById(R.id.item_grid);
        btn_return = (Button) findViewById(R.id.btn_return);
        adapter = new TestItemGridViewAdapter(this);
        mHandler = new Handler();
        gridView.setOnItemClickListener(this);
        factoryTestManager = FactoryTestManager.getInstance(this);

        if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_AUTO_TEST) {
            factoryTestManager.clearTestResult();

            TestItem firstTestItem = factoryTestManager.getTestList().get(0);
            jumpActivity(firstTestItem.activityCls);
        }

        gridView.setAdapter(adapter);
        adapter.isShowResult(true);
        this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.water_logo));

        super.onCreate(savedInstanceState);

    }


    public void jumpActivity(final Class activityCls) {
        currentTestingCls = activityCls;
//		startActivity(new Intent(this, WifiTestActivity.class));
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
            Log.i("test", "------------next activity=" + nextActivity);
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
            adapter.setItemClickEnable(false);

            btn_return.setEnabled(false);
        } else if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_SINGAL_TEST) {
            // 单项
            adapter.setItemClickEnable(true);
            btn_return.setEnabled(true);
        } else if (FactoryTestManager.currentTestMode == FactoryTestManager.TestMode.MODE_RESULT_TEST) {
            // 查看报表
            adapter.setItemClickEnable(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        startActivity(new Intent(this,
                ((TestItem) adapter.getItem(position)).activityCls));

    }
}
