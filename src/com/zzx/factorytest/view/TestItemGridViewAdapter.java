package com.zzx.factorytest.view;

import java.util.List;

import com.zzx.factorytest.bean.TestItem;
import com.zzx.factorytest.manager.FactoryTestManager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TestItemGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<TestItem> list;
    private FactoryTestManager factoryTestManager;
    private boolean isShowResult;
    private boolean itemClickEnable = true;

    public TestItemGridViewAdapter(Context context) {
        this.mContext = context;
        factoryTestManager = FactoryTestManager.getInstance(context);
        list = factoryTestManager.getTestList();
    }

    public void setItemClickEnable(boolean enable) {
        itemClickEnable = enable;
    }

    public void isShowResult(boolean isShow) {
        isShowResult = isShow;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestItem item = list.get(position);
        TestResultView resultView = new TestResultView(mContext, item);
        if (isShowResult) {
            resultView.setResult(factoryTestManager.getResult(item.itemName));
        }

        return resultView;
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return itemClickEnable;
    }

}
