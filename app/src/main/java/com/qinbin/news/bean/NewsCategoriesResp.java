package com.qinbin.news.bean;

import java.util.List;

/**
 * Created by Teacher on 2016/8/7.
 */
public class NewsCategoriesResp {
    // 服务上的所有的新闻栏目
    public List<NewsCategory> data;
    // 需要显示的新闻栏目的id
    public List<Integer> extend;
    // 响应码
    public int retcode;

}
