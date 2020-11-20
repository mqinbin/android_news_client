package com.qinbin.lib_arl;

/**
 * Created by Teacher on 2016/8/7.
 */
public class RollItem implements IRollItem {
    String picUrl;
    String title;

    @Override
    public String getPicUrl() {
        return picUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public RollItem(String picUrl, String title) {

        this.picUrl = picUrl;
        this.title = title;
    }
}
