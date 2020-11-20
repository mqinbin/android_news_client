package com.qinbin.news.pager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.qinbin.news.R;
import com.qinbin.news.bean.NewsCategory;
import com.qinbin.news.bean.NewsListInfo;
import com.qinbin.news.bean.SingleNews;
import com.qinbin.news.constant.NetConstant;
import com.qinbin.news.utils.CommonViewHolder;
import com.qinbin.lib_arl.AutoRollLayout;

public class NewsPager extends BasePager {

    private AutoRollLayout mAutoRollLayout;
    private int mArlHeaderIndex;

    public NewsPager(Context context, NewsCategory newsCategory) {
        super(context, newsCategory);

    }

    /**
     * 添加头的操作必须在setAdapter之前调用，所以提取了initView方法和initData方法，在initData中设置适配器
     * NewsPager 需要重写initView方法，在调用完super.initView之后，去添加头
     */
    @Override
    protected void initView() {
        super.initView();

        // 添加头之前，头的数量就是添加后的角标
        mArlHeaderIndex = mXlistView.getHeaderViewsCount();

        View headerView = View.inflate(mContext, R.layout.pager_news_header, null);
        mAutoRollLayout = (AutoRollLayout) headerView.findViewById(R.id.news_pager_header_arl);
        mXlistView.addHeaderView(headerView);
        mAutoRollLayout.setOnItemClickListener(new AutoRollLayout.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

//        mXlistView.setAdapter(setAdatper);
//        getAdapter = mXlistView.getAdapter();
        // 1 setAdatper  == getAdapter // 如果ListVIew有头或者脚的话 是不相等的
        // 2 setAdatper.getCount()  == getAdapter.getCount() ;  get - set  =  头 + 脚 的数量

        AbsListView.OnScrollListener scrollListener  = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            // 1 在ListView显示出来后， 头和脚是算item的
            // 2 求轮播图的头的角标
            // 3 onScroll方法并没有如我们想象地那样调用 ，当外面的VIewPager 或内部的轮播图在滚动的时候，也会被调用
            // 4 还要结合之前的需求3

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("onScroll " ,"" + firstVisibleItem);
//                if(firstVisibleItem<= mArlHeaderIndex){
//                    mAutoRollLayout.setAutoRoll(true);
//                }else{
//                    mAutoRollLayout.setAutoRoll(false);
//                }
                // 优化小技巧（实用）： 如果没有变化 就不要调用
                if(mFirstVisibleItem == firstVisibleItem){
                    return;
                }

                mFirstVisibleItem =firstVisibleItem;
                startOrStopAutoRoll();
            }


        };
        mXlistView.setOnScrollListener(scrollListener);
    }
    int mFirstVisibleItem ;
    boolean mShown;

    private void startOrStopAutoRoll() {
        if(mShown && mFirstVisibleItem<=mArlHeaderIndex){
            mAutoRollLayout.setAutoRoll(true);
        }else{
            mAutoRollLayout.setAutoRoll(false);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mShown = true;
//        mAutoRollLayout.setAutoRoll(true);
        startOrStopAutoRoll();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShown = false;
//        mAutoRollLayout.setAutoRoll(false);
        startOrStopAutoRoll();
    }
    @Override
    protected int getItemLayout() {
        return R.layout.item_news;
    }

    @Override
    protected void bindView(SingleNews singleNews, CommonViewHolder cvh) {
        cvh.getTv(R.id.item_news_title_tv).setText(singleNews.title);
        cvh.getTv(R.id.item_news_pubdate_tv).setText(singleNews.pubdate);
        sBitmapUtils.display(cvh.getIv(R.id.item_news_iv), NetConstant.formatUrl(singleNews.listimage));

    }

    NewsListInfo mNewsListInfo;

    @Override
    protected void onDownloadData(NewsListInfo newsListInfo, Boolean isRefreshing) {
        super.onDownloadData(newsListInfo, isRefreshing);
        if ( isRefreshing == null || isRefreshing ) {
            mNewsListInfo = newsListInfo;
            updateAutoRollLayout();
        }
    }

    private void updateAutoRollLayout() {


//        if (mNewsListInfo.topnews == null) {
//            mAutoRollLayout.setItems(null);
//            return;
//        }
//
//        List<IRollItem> items = new ArrayList<>();
////        List<SingleNews> -->  List<RollItem>
//        for (SingleNews singleNews : mNewsListInfo.topnews) {
//            items.add(new RollItem(NetConstant.formatUrl(singleNews.topimage), singleNews.title));
//        }
//        mAutoRollLayout.setItems(items);
//        mAutoRollLayout.setAutoRoll(true);

        mAutoRollLayout.setItems(mNewsListInfo.topnews);
    }
}
