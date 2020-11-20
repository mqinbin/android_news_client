package com.qinbin.news.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinbin.news.R;

/**

 */
public class SettingsFragment extends BaseFragment {


    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initView(ImageView imageView, TextView textView, View childView) {
        imageView.setVisibility(View.GONE);
        textView.setText("设置");

    }
}