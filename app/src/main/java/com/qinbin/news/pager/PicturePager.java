package com.qinbin.news.pager;

import android.content.Context;

import com.qinbin.news.R;
import com.qinbin.news.bean.NewsCategory;
import com.qinbin.news.bean.SingleNews;
import com.qinbin.news.constant.NetConstant;
import com.qinbin.news.utils.CommonViewHolder;


public class PicturePager extends BasePager {
    public PicturePager(Context context, NewsCategory newsCategory) {
        super(context, newsCategory);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_picture;
    }

    @Override
    protected void bindView(SingleNews singleNews, CommonViewHolder cvh) {
        cvh.getTv(R.id.item_picture_tv).setText(singleNews.title);
        sBitmapUtils.display(cvh.getIv(R.id.item_picture_iv) , NetConstant.formatUrl(singleNews.listimage));
    }
}
