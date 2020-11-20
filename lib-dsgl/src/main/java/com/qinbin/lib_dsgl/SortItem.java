package com.qinbin.lib_dsgl;

/**
 * Created by Teacher on 2016/8/13.
 */
public class SortItem implements  ISortItem {

    private String mTitle;

    public SortItem(String title) {
        mTitle = title;
    }

    @Override
    public String getItemTitle() {
        return mTitle;
    }
}
