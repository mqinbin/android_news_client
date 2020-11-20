package com.qinbin.lib_dsgl;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * 需要使用到的3个基础知识
 * 1 GridLayout
 *  android:columnCount="3"
 *
 * 2 布局动画
 *
 * android:animateLayoutChanges="true"
 *
 * 3 拖拽框架
 * startDrag ， 第二个参数 DragShadowBuilder
 * setOnDragListener ，onDrag 的返回值
 * DragEvent， getAction 类型 ，getX/Y 坐标
 *
 * 新的类 Rect
 */
public class DsglActivityMainActivity0 extends Activity {

    private GridLayout mGridLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsgl_activity_main0);
        mGridLayout = (GridLayout) findViewById(R.id.dsgl_main_gl);
        mGridLayout.setOnDragListener(mDragListener);
    }
    int mClickCount;

    public void addViewToGL(View view) {
        mClickCount ++;
        TextView tv = createTv(""+mClickCount);
        int marging = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,getResources().getDisplayMetrics());
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        lp.width =screenWidth / mGridLayout.getColumnCount() - 2 * marging;
        lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
        lp.setMargins(marging,marging,marging,marging);
        tv.setLayoutParams(lp);

        tv.setOnClickListener(removeOcl);
        tv.setOnLongClickListener(mLongListener);
        mGridLayout.addView(tv,0);
    }

    private TextView createTv(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(18); // 单位不是像素，是sp
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.dsgl_tv_bg_normal);
        return textView;
    }

    private View.OnClickListener removeOcl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGridLayout.removeView(v);
        }
    };

    private View.OnLongClickListener mLongListener = new View.OnLongClickListener() {
        // 返回值表示消费了触摸事件，false 还会触发 onClick，返回true就不会触发onClick
        @Override
        public boolean onLongClick(View v) {
            // 此方法会产生一个跟随手指移动的影子，出现后影子的中心点在手指上
            // 第二个参数是产生影子的，构造中传入的View是影子的原形； 影子是View的快照
            mDragView = v;
            v.startDrag(null, new View.DragShadowBuilder(v), null,0);
            v.setBackgroundResource(R.drawable.dsgl_tv_bg_disable);
            return true;
        }
    };
    View mDragView;
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
                break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    int index = findTouchIndex(event.getX(),event.getY());
                    if(index>=0&& mDragView !=null &&mDragView != mGridLayout.getChildAt(index)){
                        mGridLayout.removeView(mDragView);
                        mGridLayout.addView(mDragView,index);
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
            int childCount = mGridLayout.getChildCount();
            mRects = new Rect[childCount];
            for (int i = 0; i < childCount; i++) {
                View c = mGridLayout.getChildAt(i);
                mRects[i] = new Rect(c.getLeft(),c.getTop(),c.getRight(),c.getBottom()); // 拿到View 上下左右边 相对于 mGridLayout（父控件）的左上角位置
            }
        }
    };
    // SparseArray<String> 相当于 HashMap<Integer,String> 但更高效、谷歌官方推荐
    static SparseArray<String> dragEventType = new SparseArray<String>();
    static{
        dragEventType.put(DragEvent.ACTION_DRAG_STARTED, "STARTED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENDED, "ENDED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENTERED, "ENTERED");
        dragEventType.put(DragEvent.ACTION_DRAG_EXITED, "EXITED");
        dragEventType.put(DragEvent.ACTION_DRAG_LOCATION, "LOCATION");
        dragEventType.put(DragEvent.ACTION_DROP, "DROP");
    }
    public static String getDragEventAction(DragEvent de) {
        return dragEventType.get(de.getAction());
    }
}
