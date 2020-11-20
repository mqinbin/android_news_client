package com.qinbin.news.fragment;

import android.graphics.Color;
import android.os.Handler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.widget.SwipeRefreshLayout;
import com.qinbin.news.MainActivity;
import com.qinbin.news.R;
import com.qinbin.news.constant.NetConstant;
import com.qinbin.lib_arl.AutoRollLayout;
import com.qinbin.lib_arl.IRollItem;
import com.qinbin.lib_arl.RollItem;

/**
 * Created by Teacher on 2016/8/7.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoRollLayout mAutoRollLayout;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(ImageView imageView, TextView textView, View childView) {
        imageView.setVisibility(View.VISIBLE);
        textView.setText("首页");

        mAutoRollLayout = (AutoRollLayout) childView.findViewById(R.id.home_arl);


        List<IRollItem> items = new ArrayList<IRollItem>();
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU91.jpg","吃饭"));
        items.add( new RollItem(NetConstant.HOST+"/10007/1452327318UU92.jpg","睡觉"));
        items.add(new RollItem(NetConstant.HOST+"/10007/1452327318UU93.jpg", "打豆豆"));
        items.add(new RollItem(NetConstant.HOST+"/10007/1452327318UU94.png", "吃豆豆"));
        mAutoRollLayout.setItems(items);
//        mAutoRollLayout.setAutoRoll(true);

        childView.findViewById(R.id.home_weather_iv).setOnClickListener(this);


        mSwipeRefreshLayout = (SwipeRefreshLayout) childView.findViewById(R.id.home_srl);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        // 设置圆圈背景色
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.YELLOW);
        // 设置箭头颜色，可以指定多个，转一圈换一个颜色，如果到了最后一个，就回到第一个
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE,Color.GREEN);

        // 19.1.0 第一次出现 SwipeRefreshLayout ，它是 横线
//        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_dark,android.R.color.holo_orange_dark,android.R.color.holo_red_dark);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAutoRollLayout.setAutoRoll(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAutoRollLayout.setAutoRoll(false);
    }

    static Handler sHandler = new Handler();
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(getActivity(), "onRefresh", Toast.LENGTH_SHORT).show();
            // 延迟5秒消失
            sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 5000);
        }
    };

    @Override
    public void onClick(View v) {
        // 显示菜单
        // =  拿到菜单 + 调用方法让它显示
        SlidingMenu slidingMenu = ((MainActivity) getActivity()).mSlidingMenu;
        slidingMenu.showMenu();
    }
}
