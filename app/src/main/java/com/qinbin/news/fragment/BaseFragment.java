package com.qinbin.news.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qinbin.news.R;

/**
 * 抽取父类的原因：
 * 一 有共同点： 代码（xml 和 java ）都在父类中完成
 * 1 顶部的标题区域一样
 * 2 标题文字样式一样
 * 3 标题中右侧图片
 * 4 都在中间
 * 二 有不同点  让子类去实现抽象方法！！
 * 1
 * 2 文字内容不一样
 * 3 图标的可见性不同
 * 4 中间的内容布局不一样
 * <p/>
 * 遵循模板的方式
 * <p/>
 * BaseFragment 完成的其他事情：
 * ①打印生命周期方法
 * ②复用mRootView，避免浪费性能，及数据丢失
 */
public abstract class BaseFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private View mRootView;

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "1 onAttach");
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "2 onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "3 onCreateView");
        // !!!! 这个判断很重要
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_base, container, false);
            ImageView imageView = (ImageView) mRootView.findViewById(R.id.fragment_base_title_iv);
            TextView textView = (TextView) mRootView.findViewById(R.id.fragment_base_title_tv);
            ViewGroup childContainer = (ViewGroup) mRootView.findViewById(R.id.fragment_base_child_container);
            int childLayout = getContentLayoutRes();
            View childView = inflater.inflate(childLayout, childContainer, true);
            initView(imageView, textView, childView);
        }


        return mRootView;
    }

    /**
     * 让子类去实现，返回一个中间区域所需要的布局资源，父类在 onCreateView 会把这个资源填充出来并添加到 R.id.fragment_base_child_container 中间去
     *
     * @return
     */
    protected abstract int getContentLayoutRes();

    /**
     * @param imageView 给子类去设置可见性
     * @param textView  给子类去设置文字
     * @param childView findViewById ，也就是  getContentLayoutRes 对应的布局
     */
    protected abstract void initView(ImageView imageView, TextView textView, View childView);


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "4 onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "5 onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "6 onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "六 onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "五 onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "三 onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "二 onDestroy");
        super.onDestroy();
    }


    @Override
    public void onDetach() {
        Log.d(TAG, "一 onDetach");
        super.onDetach();
    }


}
