package com.qinbin.test_arl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.qinbin.lib_arl.AutoRollLayout;
import com.qinbin.lib_arl.IRollItem;
import com.qinbin.lib_arl.RollItem;

public class MainActivity extends Activity {

    private AutoRollLayout mAutoRollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoRollLayout = (AutoRollLayout) findViewById(R.id.main_arl);
        List<IRollItem> items = new ArrayList<>();
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU91.jpg","吃饭"));
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU92.jpg","睡觉"));
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU93.jpg","打豆豆"));
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU94.png","吃豆豆"));
        mAutoRollLayout.setItems(items);
    }

    public void show4(View view) {
        List<IRollItem> items = new ArrayList<>();
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU91.jpg","吃饭"));
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU92.jpg","睡觉"));
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU93.jpg","打豆豆"));
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU94.png","吃豆豆"));
        mAutoRollLayout.setItems(items);
    }
    public void show2(View view) {
        List<IRollItem> items = new ArrayList<>();
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU91.jpg","吃饭"));
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU94.png","吃豆豆"));
        mAutoRollLayout.setItems(items);
    }
    public void show1(View view) {
        List<IRollItem> items = new ArrayList<>();
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU94.png","吃豆豆"));
        mAutoRollLayout.setItems(items);
    }
    public void show0(View view) {
        mAutoRollLayout.setItems(null);
    }

    public void on(View view) {
        mAutoRollLayout.setAutoRoll(true);
    }
    public void off(View view) {
        mAutoRollLayout.setAutoRoll(false);
    }
}
