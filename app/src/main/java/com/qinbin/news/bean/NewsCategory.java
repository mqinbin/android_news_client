package com.qinbin.news.bean;

import com.qinbin.lib_dsgl.ISortItem;

/**
 * Created by Teacher on 2016/8/7.
 */
public class NewsCategory implements ISortItem {
    // 唯一标识
    public int id;
    // 名称，给人看的
    public String title;
    // TODO
    public int type;
    // ！ 这个新闻栏目下新闻列表的url
    public String url;

    @Override
    public String getItemTitle() {
        return title;
    }
    // 没用
//    public String url1;
}
