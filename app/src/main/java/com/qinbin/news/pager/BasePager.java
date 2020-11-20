package com.qinbin.news.pager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import com.qinbin.news.NewsDetailActivity;
import com.qinbin.news.R;
import com.qinbin.news.bean.NewsCategory;
import com.qinbin.news.bean.NewsListInfo;
import com.qinbin.news.bean.NewsListResp;
import com.qinbin.news.bean.SingleNews;
import com.qinbin.news.constant.NetConstant;
import com.qinbin.news.utils.CommonViewHolder;
import me.maxwin.view.XListView;

/**
 * 抽取父类的原因：
 * <p/>
 * 一 共同点
 * 1 都ListView
 * 2 都需要Adapter
 * 3 都需要数据
 * 4 都需要下载数据
 * 5 都需要下拉刷新或加载更多
 * <p/>
 * 二 不同点
 * 1 条目布局不一样
 * 2 NewsPager有轮播图的头，但picturePage 没有
 */
public abstract class BasePager {


    private View mRootView;

    protected NewsCategory mNewsCategory;
    protected Context mContext;
    protected static BitmapUtils sBitmapUtils;

    protected XListView mXlistView;
    private HttpUtils mHttpUtils;
    private NewsListInfo mNewsListInfo;


    public BasePager(Context context, NewsCategory newsCategory) {
        mContext = context;
        mNewsCategory = newsCategory;
        if (sBitmapUtils == null) {
            sBitmapUtils = new BitmapUtils(context);
        }


        initView();
        initData();
    }

    public void onResume() {
    }

    public void onPause() {
    }

    protected void initView() {
        mRootView = View.inflate(mContext, R.layout.pager_news, null);
        mXlistView = (XListView) mRootView.findViewById(R.id.pager_news_lv);
        mXlistView.setXListViewListener(mXListener);
        mXlistView.setPullLoadEnable(true);
        mXlistView.setOnItemClickListener(mOnItemClickListener);


    }

    private void initData() {

        mXlistView.setAdapter(mBaseAdapter);
        mHttpUtils = new HttpUtils();
        mHttpUtils.send(HttpRequest.HttpMethod.GET, NetConstant.formatUrl(mNewsCategory.url), mCallback);
    }


    private XListView.IXListViewListener mXListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            if (TextUtils.isEmpty(mNewsListInfo.more)) {
                Toast.makeText(mContext, "木有更新数据拉", Toast.LENGTH_SHORT).show();
                mXlistView.stopRefresh();
            } else {
                mHttpUtils.send(HttpRequest.HttpMethod.GET, NetConstant.formatUrl(mNewsListInfo.more), mRefreshCallback);
            }
        }

        @Override
        public void onLoadMore() {
            if (TextUtils.isEmpty(mNewsListInfo.more)) {
                Toast.makeText(mContext, "木有历史数据拉", Toast.LENGTH_SHORT).show();
                mXlistView.stopLoadMore();
            } else {
                mHttpUtils.send(HttpRequest.HttpMethod.GET, NetConstant.formatUrl(mNewsListInfo.more), mLoadMoreCallback);
            }
        }
    };
    private RequestCallBack<String> mCallback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            NewsListResp newsListResp = new Gson().fromJson(json, NewsListResp.class);
            mNewsListInfo = newsListResp.data;
            onDownloadData(mNewsListInfo, null);

            // 更新界面
            mBaseAdapter.notifyDataSetChanged();

        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    private RequestCallBack<String> mLoadMoreCallback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            NewsListResp loadMoreNewsListResp = new Gson().fromJson(json, NewsListResp.class);
            mNewsListInfo.merge(loadMoreNewsListResp.data, false);

            onDownloadData(mNewsListInfo, false);
            // 更新界面
            mBaseAdapter.notifyDataSetChanged();
            // 关闭加载更多的动画
            mXlistView.stopLoadMore();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mXlistView.stopLoadMore();
        }
    };
    private RequestCallBack<String> mRefreshCallback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            NewsListResp refreshNewsListResp = new Gson().fromJson(json, NewsListResp.class);
            mNewsListInfo.merge(refreshNewsListResp.data, true);
            onDownloadData(mNewsListInfo, true);
            // 更新界面
            mBaseAdapter.notifyDataSetChanged();
            // 关闭加载更多的动画
            mXlistView.stopRefresh();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mXlistView.stopRefresh();
        }
    };

    /**
     * 用来对外界提供所维护的View， 需要在newsFragment的 PagerAdapter的 instantiateItem 用
     *
     * @return
     */
    public View getView() {
        return mRootView;
    }

    protected abstract int getItemLayout();

    protected abstract void bindView(SingleNews singleNews, CommonViewHolder cvh);

    protected void onDownloadData(NewsListInfo newsListInfo, Boolean isRefreshing) {

    }

    ;

    private BaseAdapter mBaseAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return (mNewsListInfo == null || mNewsListInfo.news == null) ? 0 : mNewsListInfo.news.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 替换为抽象的
            CommonViewHolder cvh = CommonViewHolder.createCVH(convertView, getItemLayout(), parent);

            SingleNews singleNews = mNewsListInfo.news.get(position);
            bindView(singleNews, cvh);

            return cvh.convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    };
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SingleNews singleNews = mNewsListInfo.news.get(position);
            Intent intent = new Intent(mContext, NewsDetailActivity.class);
            intent.putExtra("SingleNews", singleNews);
            mContext.startActivity(intent);
        }
    };

}
