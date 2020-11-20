package com.qinbin.news.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qinbin.news.R;
import com.qinbin.news.bean.NewsCategory;
import com.qinbin.lib_dsgl.DragSortGridLayout;
import com.qinbin.lib_dsgl.ISortItem;

/**
 * Created by Teacher on 2016/8/11.
 */
public class EditDialog extends Dialog {

    private final DragSortGridLayout mShowDsgl;
    private final DragSortGridLayout mHideDsgl;

    public EditDialog(Context context) {
//        Dialog dialog = new Dialog(getActivity(), R.style.EditDialog);
        super(context, R.style.EditDialog);

        setContentView(R.layout.dialog_edit);

        // 布局
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;

        // 动画，见样式

        // 背景
        setCanceledOnTouchOutside(true);

//        dialog.show();

        mShowDsgl = (DragSortGridLayout) findViewById(R.id.dialog_show_dsgl);
        mHideDsgl = (DragSortGridLayout) findViewById(R.id.dialog_hide_dsgl);

        mShowDsgl.setAllowDrag(true);
        mShowDsgl.setOnItemClickListener(new DragSortGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ISortItem item) {
                mShowDsgl.removeView(view);
//                hideDsgl.addView(view);
                mHideDsgl.addItem(item);
            }
        });

        mHideDsgl.setAllowDrag(false);

        mHideDsgl.setOnItemClickListener(new DragSortGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ISortItem item) {
                mHideDsgl.removeView(view);
//                showDsgl.addView(view);
                mShowDsgl.addItem(item);
            }
        });
    }

    Map<String, NewsCategory> mAllCategories = new HashMap<>();
    public void setShowAndHideNewsCategories(List<NewsCategory> showNewsCategories, List<NewsCategory> hideNewsCategories) {

//        List<String> showItems = new ArrayList<>();
//        for (NewsCategory newsCategory : showNewsCategories) {
//            showItems.add(newsCategory.title);
//            mAllCategories.put(newsCategory.title,newsCategory);
//        }
//        mShowDsgl.setItems(showItems);
        mShowDsgl.setItems(showNewsCategories);

//        List<String> hideItems= new ArrayList<>();
//        for (NewsCategory newsCategory : hideNewsCategories) {
//            hideItems.add(newsCategory.title);
//            mAllCategories.put(newsCategory.title, newsCategory);
//        }
//        mHideDsgl.setItems(hideItems);
        mHideDsgl.setItems(hideNewsCategories);

    }

    public List<NewsCategory> getShowNewsCategories() {
//        List<String> sortedItems = mShowDsgl.getSortedItems();
//
//        List<NewsCategory> result = new ArrayList<>();
//        for (String title : sortedItems) {
//            result.add(mAllCategories.get(title));
//        }
//        return result;
        return mShowDsgl.getSortedItems();
    }
}
