package com.qinbin.lib_dsgl;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 对外提供的方法：
 * 一  设置数据   public void setItems(List<String> items)
 * <p/>
 * <p/>
 * 二 是否能够拖拽概念顺序  public void setAllowDrag(boolean allow)
 *
 * 三  让外界处理点击条目的事件
 *  接口回调 OnItemClickListener
 *
 * 四 让外界通过数据的形式添加一个View  ；public void addItem(String item)
 *
 * 五 获得数据  public List<String>  getSortedItems()
 */
public class DragSortGridLayout extends GridLayout {
    public static final int COLUMN_COUNT = 4;
    private OnItemClickListener mOnItemClickListener;



    public static interface  OnItemClickListener <T extends  ISortItem>{
        void onItemClick(View view,T text);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    public DragSortGridLayout(Context context) {
        this(context, null);
    }

    public DragSortGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragSortGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setColumnCount(COLUMN_COUNT);
        setOnDragListener(mDragListener);
        // 添加布局动画
        LayoutTransition transition = new LayoutTransition();
//        transition.setDuration(500); // 指定动画的事件
        setLayoutTransition(transition);
    }


    public <T extends ISortItem>void setItems(List<T> items) {
        removeAllViews();
        if (items == null) {
            return;
        }
        for (ISortItem item : items) {
            addItem(item);
        }
    }
    public <T extends ISortItem> List<T>  getSortedItems(){
        List<T> result = new ArrayList<>();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView tv = (TextView) getChildAt(i);
            result.add((T) tv.getTag());
        }

        return result;
    }

    boolean mAllowDrag;

    public void setAllowDrag(boolean allow) {
        mAllowDrag = allow;
    }

    public void addItem(ISortItem item) {
        TextView tv = createTv(item.getItemTitle());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        lp.width = screenWidth / getColumnCount() - 2 * margin;
        lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
        lp.setMargins(margin, margin, margin, margin);
        tv.setLayoutParams(lp);
        tv.setTag(item);
        tv.setOnClickListener(mOcl);
        tv.setOnLongClickListener(mLongListener);
        addView(tv);
    }

    private TextView createTv(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(18); // 单位不是像素，是sp
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.dsgl_tv_bg_normal);
        return textView;
    }

    View mDragView;
    private View.OnLongClickListener mLongListener = new View.OnLongClickListener() {
        // 返回值表示消费了触摸事件，false 还会触发 onClick，返回true就不会触发onClick
        @Override
        public boolean onLongClick(View v) {
            if (!mAllowDrag) {
                return true;
            }
            // 此方法会产生一个跟随手指移动的影子，出现后影子的中心点在手指上
            // 第二个参数是产生影子的，构造中传入的View是影子的原形； 影子是View的快照
            mDragView = v;
            v.startDrag(null, new View.DragShadowBuilder(v), null, 0);
//            v.setBackgroundResource(R.drawable.dsgl_tv_bg_disable);
            return true;
        }
    };

    private View.OnDragListener mDragListener = new View.OnDragListener() {
        /**
         *
         * @param v         设置了监听的View
         * @param event     DragEvent
         * @return 返回值表示对 DragEvent 才会持续收到
         *
         * x y 坐标是 相对于设置了监听的View的左上角的位置
         */
        @Override
        public boolean onDrag(View v, DragEvent event) {

//            Log.d("onDrag",getDragEventAction(event));
//            Log.d("xy ",event.getX() + "  " + event.getY());

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    initRects();
                    if(mDragView!=null){
                        mDragView.setBackgroundResource(R.drawable.dsgl_tv_bg_disable);
                    }
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    int index = findTouchIndex(event.getX(), event.getY());
                    if (index >= 0 && mDragView != null && mDragView != getChildAt(index)) {
                        removeView(mDragView);
                        addView(mDragView, index);
                    }
                    break;
//                case DragEvent.ACTION_DROP:
                case DragEvent.ACTION_DRAG_ENDED:
                    if(mDragView!=null){
                        mDragView.setBackgroundResource(R.drawable.dsgl_tv_bg_normal);
                    }
                    break;
            }

            return true;
        }

        private int findTouchIndex(float x, float y) {
            for (int i = 0; i < mRects.length; i++) {
                Rect rect = mRects[i];
                if (rect.contains((int) x, (int) y)) {
                    return i;
                }
            }

            return -1;
        }

        Rect[] mRects;

        private void initRects() {
            int childCount = getChildCount();
            mRects = new Rect[childCount];
            for (int i = 0; i < childCount; i++) {
                View c = getChildAt(i);
                mRects[i] = new Rect(c.getLeft(), c.getTop(), c.getRight(), c.getBottom()); // 拿到View 上下左右边 相对于 mGridLayout（父控件）的左上角位置
            }
        }
    };

    private OnClickListener mOcl = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(v, (ISortItem) v.getTag());
            }
        }
    };
}
