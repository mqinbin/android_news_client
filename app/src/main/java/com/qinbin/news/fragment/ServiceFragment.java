package com.qinbin.news.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinbin.news.R;

/**
 * Created by Teacher on 2016/8/7.
 */
public class ServiceFragment extends BaseFragment {


    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_service;
    }

    @Override
    protected void initView(ImageView imageView, TextView textView, View childView) {
        imageView.setVisibility(View.GONE);
        textView.setText("服务");

    }
}