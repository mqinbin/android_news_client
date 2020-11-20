package com.qinbin.news;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import com.qinbin.news.fragment.HomeFragment;
import com.qinbin.news.fragment.NewsFragment;
import com.qinbin.news.fragment.ServiceFragment;
import com.qinbin.news.fragment.SettingsFragment;
/*

轮播图定时滚动导致的内存泄漏的解决：
总体： 当轮播图不可见时就停止

1 HomeFragment中的轮播图,在切换到其他Fragment 停，切换回来继续
2 当Activity不可见时，HomeFragment中的轮播图 停
3 NewsPager中的轮播图 切换到其他Pager时 停，切换回来继续
4 从NewsFragment切换到其他Fragment 或 关闭Activity时 ,NewsPager的轮播图要停下来

[5] 当ListView滚动导致轮播图不可见，停，滑动下来，继续

解决：
1 在HomeFragment的生命周期方法（onResume  onPause）中， 控制轮播图的转动

2  同1

3
① 在BasePager上添加 生命周期方法（onResume  onPause） ，不实现
② 在NewsPager中利用生命周期方法去控制轮播图的滚动
③ 去调用BasePager的生命周期方法： 对ViewPager设置页面变化监听，在onPageSelect方法中调用onResume 和onPause
④ 在下载好新闻栏目数据，更新了ViewPager之后 。调用PageChangeListener的onPageSelect 传入0，进行初始化

4  在NewsFragment的生命周期方法中调用当前显示的Pager的生命周期方法

5
 对ListView设置滚动监听，在监听中得到第一个可见的孩子，与轮播图头进行比较，处理滚动
 ① 对ListView设置滚动监听，在onScroll方法中，把成员变量mFirstVisibleItem 赋值为firstVisibleItem
 ② 修改需求3 在生命周期方法中 修改mShown的变量
 ③ 添加startOrStop的方法，去判断 mShown mFirstVisibleItem 两个条件，来决定轮播图是否滚动
 ④ 在 步骤① ②中 修改了成员变量后调用  startOrStop
 注意 对 mFirstVisibleItem 判断是需要获得轮播图在ListVIew的角标的， 角标的求法：在添加之前获得ListView的头的数量，添加后就是角标
  优化小技巧（实用）： 如果没有变化 就不要调用


消防员（划归思想）
数学家去做消防员

爱因斯坦：逻辑 让你从A的B，想象力 让你到任何地方；




 */
public class MainActivity extends Activity {

    @ViewInject(R.id.main_rg)
    RadioGroup mRadioGroup;

    List<Fragment> mFragments = new ArrayList<Fragment>();
    public SlidingMenu mSlidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        mFragments.add(new HomeFragment());
        mFragments.add(new NewsFragment());
        mFragments.add(new ServiceFragment());
        mFragments.add(new SettingsFragment());

        mRadioGroup.setOnCheckedChangeListener(mCheckedListener);

        //1

//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_fragment_container,mFragments.get(0))
//                .commit();
        //2
//        mCheckedListener.onCheckedChanged(mRadioGroup,R.id.main_news_rb);

        //3
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);

        initSlidingMenu();
    }

    private void initSlidingMenu() {
        // 四来定
        mSlidingMenu = new SlidingMenu(this);
        // 布局
        mSlidingMenu.setMenu(R.layout.menu);
        // 宽度
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        mSlidingMenu.setBehindWidth(screenWidth / 3);
        // 位置
        mSlidingMenu.setMode(SlidingMenu.RIGHT);
        // 触摸模式
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);


        // 添加到Activity上,构造方法也可以实现
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    }

    private RadioGroup.OnCheckedChangeListener mCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        /**
         *
         * @param group
         * @param checkedId  被勾选的RadioButton的id
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // id -> View -> index -> Fragment;
            View child = group.findViewById(checkedId);
            int index = group.indexOfChild(child);
            Fragment fragment = mFragments.get(index);

            replaceFragment(fragment);
//            replaceWithBackStack(fragment);
//            addRemoveFragment(fragment);
//            addHideShowFragment(fragment);
        }
    };


    // hide  和  show 操作，不会对生命周期方法产生影响
    Fragment mCurrentFragment;
    List<Fragment> mHasAddFragments = new ArrayList<Fragment>();
    private void addHideShowFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        // 如果fragment没有添加过，就添加，
        // 如果fragment添加过，就显示
        // 如果原来的fragment存在，就隐藏
        if(!mHasAddFragments.contains(fragment)){
            fragmentTransaction.add(R.id.main_fragment_container, fragment);
            mHasAddFragments.add(fragment);
        }else{
           fragmentTransaction.show(fragment);
        }
        if (mCurrentFragment != null) {
            fragmentTransaction.hide(mCurrentFragment);
        }
        mCurrentFragment = fragment;
        fragmentTransaction.commit();

    }


    // replace 操作和 add remove
    private void addRemoveFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_container, fragment);
        if (mCurrentFragment != null) {
            fragmentTransaction.remove(mCurrentFragment);
        }
        fragmentTransaction.commit();
        mCurrentFragment = fragment;
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }

    // 后退栈 会导致  onAttach  onDetach 、 onCreate、onDestory不被调用
    // 而且点击back按钮 不会退出Activity，返回上一次Fragment的操作
    private void replaceWithBackStack(Fragment fragment) {

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

}
