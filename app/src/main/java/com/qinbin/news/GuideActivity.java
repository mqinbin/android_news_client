package com.qinbin.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Teacher on 2016/8/7.
 */
public class GuideActivity extends Activity {


    @ViewInject(R.id.guide_vp)
    ViewPager mViewPager;

    @ViewInject(R.id.guide_btn)
    Button mButton;

    @ViewInject(R.id.guide_cpi)
    CirclePageIndicator mCirclePageIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ViewUtils.inject(this);

        mViewPager.setAdapter(mPageAdapter);
//        mViewPager.setOnPageChangeListener(mPageListener);
        mViewPager.addOnPageChangeListener(mPageListener);


        mCirclePageIndicator.setViewPager(mViewPager);
        // 设置圆圈颜色
        mCirclePageIndicator.setFillColor(Color.RED);
        mCirclePageIndicator.setPageColor(Color.GRAY);
        // 设置半径大小 ，单位是像素
        int pxFor7Dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,7,getResources().getDisplayMetrics());
        mCirclePageIndicator.setRadius(pxFor7Dp);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });

        // 初始状态有问题： 按钮可见
//        mButton.setVisibility(View.GONE);
        mPageListener.onPageSelected(0);
    }
    int[] mPicRes = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private PagerAdapter mPageAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mPicRes.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return  view == object;
        }

        // 预加载页面用到的方法
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(mPicRes[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return  imageView;
        }

        // 销毁页面用到的方法
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

    private ViewPager.OnPageChangeListener mPageListener =new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            if(position == mPageAdapter.getCount() -1){
                mButton.setVisibility(View.VISIBLE);
            }else{
                mButton.setVisibility(View.GONE);
            }
        }
    };
}
