package com.qinbin.news.bean;

import java.io.Serializable;

import com.qinbin.news.constant.NetConstant;
import com.qinbin.lib_arl.IRollItem;

/**
 * 是一条新闻的简要情况
 */
public class SingleNews implements Serializable,IRollItem{
    // 新闻的唯一标识
    public int id;
    // !! 条目左侧图片的网址
    public String listimage;
    // !!  轮播图的图片网址
    public String topimage;
    // ！！新闻的出版时间
    public String pubdate;
    // ！！新闻的标题
    public String title;
    // 新闻详情的地址
    public String url;

    @Override
    public String getPicUrl() {
        return NetConstant.formatUrl(topimage);
    }

    @Override
    public String getTitle() {
        return title;
    }
}
