package com.qinbin.lib_arl;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图的方法
 * <p/>
 * 对外暴露的方法：
 * 一 让外界指定数据，此控件负责显示数据
 * // public void setItems(List<String> ,List<String> )
 * public void setItems(List<RollItem>  )
 * <p/>
 * 关心三个View
 * ViewPager、TextView  点（3件事情）
 * <p/>
 * <p/>
 * 二 开始或停止滚动
 * public void  setAutoRoll(boolean )
 * <p/>
 * hander.postDelay  removeCallbacks 、 避免重复调用
 * <p/>
 * <p/>
 * 需求：当用户手指触摸时停止滚动，以备用户看清楚，送手时恢复滚动
 * 做法：
 * ① 对ViewPager 设置触摸监听
 * ② 在监听的onTouch方法中 返回false
 * ③ 判断触摸事件的类型，关注down 和 up
 * ④ 如果 之前是滚动的，在down的时候 removeCallbacks
 * ⑤ 如果 之前是滚动的，在up的时候 postDelay
 * <p/>
 * 触摸事件相关
 * 一
 * 如果要拿到触摸事件，通常做法：重写View的 三个方法（ dispathTouchEvent ，onIntercepterTouchEvent ,onTouchEvent ）中的一个
 * 在控件的外部拿到触摸事件 (这个触摸事件是消费前的)View.setOnTouchListener
 *
 * 二 Cancel类型的事件
 *  原来的触摸事件是由子控件消费的
 *  但父控件通过判断，发现事件是自己想要的，onInterceptTouchEvent方法会返回true，会把触摸事件拦截掉
 *  子控件就无法得到后续的触摸事件了，
 *  这种现象称为触摸事件的抢夺
 *
 *  在发生抢夺之后，父控件会制造一个cancel类型的事件，发送给子控件
 *  cancel类型的事件约等于 up类型的事件，代表着一组触摸事件在这个View的结束
 *
 * 三 requestDisallowInterceptTouchEvent
 *  ①此方法会用位运算在 成员变量中存储boolean值信息， 在分发方法中会去使用到它的值 去决定是否调用onIntercepter方法
 *  ② 此方法会递归调用父控件的此方法，把信号传递上去
 *  ③ 有些控件比如说 SwipeRefreshLayout 会重写此方法，不去理会子控件的请求
 *
 *  四 OnClickListener的 onClick方法是如何触发的
 *   在View的 onTouchEvent 的 UP类型的判断中 调用了 performClick 方法触发的
 *  performClick 可以模拟点击
 *
 *  五  手势识别器  GestureDetector
 *  作用是简化触摸事件的分析
 *  使用步骤：
 *  1 创建对象
 *  2 把触摸事件交给它去分析
 *  3 使用分析的结果
 *
 *  类比间谍
 *
 *
 *
 */
public class AutoRollLayout extends FrameLayout implements View.OnClickListener {

    private ViewPager mViewPager;
    private TextView mTextView;
    private LinearLayout mLinearLayout;
    private OnItemClickListener mOnItemClickListener;
    private GestureDetector mGestureDetector;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface  OnItemClickListener{
        void onItemClick(View view, int position);
    }


    public AutoRollLayout(Context context) {
        this(context, null);
    }

    public AutoRollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.arl_arl_layout, this);
        mViewPager = (ViewPager) findViewById(R.id.arl_arl_vp);
        mTextView = (TextView) findViewById(R.id.arl_arl_tv);
        mLinearLayout = (LinearLayout) findViewById(R.id.arl_arl_ll);
        mViewPager.setOnPageChangeListener(mPageListener);
        mViewPager.setOnTouchListener(mTouchListener);
        // 1 创建对象

        mGestureDetector = new GestureDetector(getContext(),mGestureListener);
    }
    //  3 使用分析的结果
    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.OnGestureListener() {
        // 所有方法的返回值 会传到 GestureDetector.onTouchEvent
        // 手指按下
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("OnGestureListener","onDown");
            return false;
        }

        // 按下后在很短的时间内（约200ms ），没有移动
        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("OnGestureListener","onShowPress");
        }
        // 长按
        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("OnGestureListener","onLongPress");

        }
        // 单击
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("OnGestureListener","onSingleTapUp");
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(null,mViewPager.getCurrentItem());
            }
            return false;
        }
        // 移动
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("OnGestureListener","onScroll");
            return false;
        }

        // 手指离开屏幕时还具有速度
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("OnGestureListener","onFling");
            return false;
        }
    };
    List<? extends IRollItem> mItems;

    public <T extends IRollItem> void setItems(List<T> items) {
        this.mItems = items;
        // 拿到数据，显示到界面上

//        ViewPager
        mViewPager.setAdapter(mPageAdapter);
//        TextView
        mTextView.setText(null);
        // 见mPageListener
//        点  若干个点； 当前选中的是红的； 点了点，切换到对应的页面
        mLinearLayout.removeAllViews();
        addDots();

        if (mItems == null) {
            return;
        }
        mPageListener.onPageSelected(0);
    }

    static Handler sHandler = new Handler();
    boolean mAutoRoll;

    public void setAutoRoll(boolean autoRoll) {
        Log.e("ARL", "setAutoRoll" + autoRoll);
        if (mAutoRoll == autoRoll) {
            return;
        }
        this.mAutoRoll = autoRoll;

        if (autoRoll) {
            sHandler.postDelayed(goNextPageRunnable, 1000);
        } else {
            sHandler.removeCallbacks(goNextPageRunnable);
        }
    }

    Runnable goNextPageRunnable = new Runnable() {
        @Override
        public void run() {
            goNextPage();
            sHandler.postDelayed(this, 1000);
        }
    };

    boolean mFromLeftToRight = true;

    private void goNextPage() {
        // 虽然ViewPager的setCurrentItem 有容错处理,但我们应该也对自己产生的错误数据负责
        if (mPageAdapter.getCount() == 1) {
            return;
        }

        int currentIndex = mViewPager.getCurrentItem();
        if (currentIndex == 0) {
            mFromLeftToRight = true;
        }
        if (currentIndex == mPageAdapter.getCount() - 1) {
            mFromLeftToRight = false;
        }

        int targetIndex = 0;
        if (mFromLeftToRight) {
            targetIndex = currentIndex + 1;
        } else {
            targetIndex = currentIndex - 1;
        }
        mViewPager.setCurrentItem(targetIndex);
        Log.e("ARL", "goNextPage" + mItems.get(targetIndex).getTitle());
    }

    private void addDots() {
        if (mItems == null) {
            return;
        }


        int pxFor10Dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        for (IRollItem item : mItems) {
            View dot = new View(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pxFor10Dp, pxFor10Dp);
            lp.setMargins(0, 0, pxFor10Dp, 0);
            dot.setLayoutParams(lp);
            dot.setBackgroundResource(R.drawable.arl_dot_selector);

            mLinearLayout.addView(dot);
            dot.setOnClickListener(this);
        }
    }
    //


    private OnTouchListener mTouchListener = new OnTouchListener() {

        /**
         *
         * @param v      v 代表设置了监听的View
         * @param event  event View的分发时收到的触摸事件 ， 其实就是在View onTouchEvent事件前的时候
         * @return 返回值代表消费了触摸事件; 如果返回true，View的 onTouchEvent方法就不会被调用了， 如果返回false了 onTouchEvent方法会调用
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//              2 把触摸事件交给它去分析
            boolean match = mGestureDetector.onTouchEvent(event);
            Log.d("onTouch" ,"match" + match);


            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    Log.e("onTouch", "DOWN");
                    if (mAutoRoll) {
                        sHandler.removeCallbacks(goNextPageRunnable);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e("onTouch", "CANCEL");
//                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("onTouch", "UP");
                    if (mAutoRoll) {
                        sHandler.postDelayed(goNextPageRunnable, 1000);
                    }
                    break;
            }

            return false;
        }
    };
//    private OnClickListener mImageViewOcl = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(mOnItemClickListener!= null){
//                mOnItemClickListener.onItemClick(v,mViewPager.getCurrentItem());
//            }
//        }
//    };
    private PagerAdapter mPageAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mItems == null ? 0 : mItems.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        List<ImageView> cache = new ArrayList<ImageView>();

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (cache.isEmpty()) {
                ImageView imageView = new ImageView(container.getContext());
//                imageView.setOnClickListener(mImageViewOcl);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
// 不能给ImageView设置触摸监听，因为左右滑动时 ，ViewPager会把触摸事件拦截，在这种情况下无法收到UP类型的事件
//                imageView.setOnTouchListener(mTouchListener);
                cache.add(imageView);
            }
            ImageView imageView = cache.remove(0);
//            Picasso
            Picasso.with(container.getContext())  // 拿到单例对象
                    .load(mItems.get(position).getPicUrl())
                    .fit() // 缩放图片
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imageView = (ImageView) object;
            cache.add(imageView);
            container.removeView(imageView);
        }
    };

    private ViewPager.OnPageChangeListener mPageListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mTextView.setText(mItems.get(position).getTitle());

            for (int i = 0; i < mPageAdapter.getCount(); i++) {
//                if(i == position){
//                    mLinearLayout.getChildAt(i).setEnabled(false);
//                }else{
//                    mLinearLayout.getChildAt(i).setEnabled(true);
//                }
                mLinearLayout.getChildAt(i).setEnabled(i != position);
            }

        }
    };

    @Override
    public void onClick(View v) {
        int position = mLinearLayout.indexOfChild(v);
        Log.e("onClick", "" + position);
        mViewPager.setCurrentItem(position);
    }
}
