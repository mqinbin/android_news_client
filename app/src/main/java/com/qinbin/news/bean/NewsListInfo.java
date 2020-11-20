package com.qinbin.news.bean;

import java.util.List;

/**
 * Created by Teacher on 2016/8/9.
 */
public class NewsListInfo {
    // 加载更多的请求地址
    public String more;
    // !! 显示在ListVIew中的数据
    public List<SingleNews> news;
    // 冗余数据，次栏目的标题
    public String title;
    // !! 显示在轮播图中的数据
    public List<SingleNews> topnews;

    public void merge(NewsListInfo other, boolean isRefresh) {
        if (isRefresh) {
            news.addAll(0, other.news);
            topnews = other.topnews;
        } else {
            news.addAll(other.news);
            more = other.more;
        }

    }
}
