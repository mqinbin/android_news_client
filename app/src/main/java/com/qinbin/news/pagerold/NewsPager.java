package com.qinbin.news.pagerold;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import com.qinbin.news.R;
import com.qinbin.news.bean.NewsCategory;
import com.qinbin.news.bean.NewsListInfo;
import com.qinbin.news.bean.NewsListResp;
import com.qinbin.news.bean.SingleNews;
import com.qinbin.news.constant.NetConstant;
import com.qinbin.news.utils.CommonViewHolder;
import com.qinbin.lib_arl.AutoRollLayout;
import com.qinbin.lib_arl.IRollItem;
import com.qinbin.lib_arl.RollItem;
import me.maxwin.view.XListView;

/**
 * * 封装： 把相关的东西放在一起
 * <p/>
 * 抽取： 通过具体的类型，得到父类
 * <p/>
 * MVC
 * <p/>
 * List<NewsPager>
 * NewsPager = ListView +  BaseAdapter + List<DATA> + 下载逻辑
 * <p/>
 * List<ListView>
 * List<BaseAdapter>
 * List<List<DATA>>
 * List<下载逻辑>
 * <p/>
 * M
 * List<Student>
 * Student = String  +  Integer + Boolean
 * <p/>
 * List<String>
 * List<Integer>
 * List<Boolean>
 * <p/>
 * 其他做法：
 * Fragment： 会出现Fragment嵌套使用的问题， 现在还没讲
 * 自定义控件 ：View层东西不应该涉及业务逻辑
 * <p/>
 * <p/>
 * <p/>
 * 下拉刷新和上拉加载 的区别
 * 下拉刷新： 获取最新的数据
 * 上拉加载： 获取历史数据
 * <p/>
 * 下拉刷新的业务逻辑：
 * 下拉刷新：把客户端已有的最新的新闻的id，发送给服务端，服务端返回比这个id更新的 新闻（数量小于等于分页大小）
 * 上拉加载亦然
 */
public class NewsPager {


    private View mRootView;

    private NewsCategory mNewsCategory;
    private Context mContext;
    private BitmapUtils mBitmapUtils;

//    private List<SingleNews> mNewsInListView;
//    private List<SingleNews> mNewsInAutoRollLayout;
    private AutoRollLayout mAutoRollLayout;
    private XListView mXlistView;
    private HttpUtils mHttpUtils;
    private NewsListInfo mNewsListInfo;


    public NewsPager(Context context, NewsCategory newsCategory) {
        mContext = context;
        mNewsCategory = newsCategory;
        mBitmapUtils = new BitmapUtils(context);
//        TextView textView = new TextView(context);
//        textView.setText(newsCategory.title);
//        mRootView = textView;


        initView();
        initData();
    }

    private void initView() {
        mRootView = View.inflate(mContext, R.layout.pager_news, null);
        mXlistView = (XListView) mRootView.findViewById(R.id.pager_news_lv);
        mXlistView.setXListViewListener(mXListener);
        mXlistView.setPullLoadEnable(true);

        View headerView = View.inflate(mContext, R.layout.pager_news_header, null);
        mAutoRollLayout = (AutoRollLayout) headerView.findViewById(R.id.news_pager_header_arl);
        mXlistView.addHeaderView(headerView);


    }

    private void initData() {

        mXlistView.setAdapter(mBaseAdapter);
        mHttpUtils = new HttpUtils();
        mHttpUtils.send(HttpRequest.HttpMethod.GET, NetConstant.formatUrl(mNewsCategory.url), mCallback);
    }


    private XListView.IXListViewListener mXListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            //            Toast.makeText(mContext,"onRefresh",Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(mNewsListInfo.more)) {
                Toast.makeText(mContext, "木有更新数据拉", Toast.LENGTH_SHORT).show();
                mXlistView.stopRefresh();
            } else {
                mHttpUtils.send(HttpRequest.HttpMethod.GET, NetConstant.formatUrl(mNewsListInfo.more), mRefreshCallback);
            }
        }

        @Override
        public void onLoadMore() {
//            Toast.makeText(mContext,"onLoadMore",Toast.LENGTH_SHORT).show();
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
//            mNewsInListView = mNewsListInfo.news;
//
//            mNewsInAutoRollLayout = mNewsListInfo.topnews;
            // 更新界面
            mBaseAdapter.notifyDataSetChanged();
            updateAutoRollLayout();

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

//            List<SingleNews> loadNewsInListView = loadMoreNewsListResp.data.news;
//            mNewsInListView.addAll(loadNewsInListView);
//            // 改More的数据
//            mNewsListInfo.more = loadMoreNewsListResp.data.more;
            mNewsListInfo.merge(loadMoreNewsListResp.data,false);

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

//            List<SingleNews> refreshNewsInListView = refreshNewsListResp.data.news;
//            mNewsInListView.addAll(0, refreshNewsInListView);
//            mNewsInAutoRollLayout = refreshNewsListResp.data.topnews;
            mNewsListInfo.merge(refreshNewsListResp.data,true);

            // 更新界面
            mBaseAdapter.notifyDataSetChanged();

            updateAutoRollLayout();

            // 关闭加载更多的动画
            mXlistView.stopRefresh();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mXlistView.stopRefresh();
        }
    };

    private void updateAutoRollLayout() {
        if (mNewsListInfo.topnews == null) {
            mAutoRollLayout.setItems(null);
            return;
        }

        List<IRollItem> items = new ArrayList<>();
//        List<SingleNews> -->  List<RollItem>
        for (SingleNews singleNews : mNewsListInfo.topnews) {
            items.add(new RollItem(NetConstant.formatUrl(singleNews.topimage), singleNews.title));
        }
        mAutoRollLayout.setItems(items);
        mAutoRollLayout.setAutoRoll(true);
    }

    /**
     * 用来对外界提供所维护的View， 需要在newsFragment的 PagerAdapter的 instantiateItem 用
     *
     * @return
     */
    public View getView() {
        return mRootView;
    }

    private BaseAdapter mBaseAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return (mNewsListInfo == null ||  mNewsListInfo.news == null) ? 0 : mNewsListInfo.news.size();
        }


        //        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = View.inflate(mContext, R.layout.item_news, null);
//                ViewHolder viewHolder = new ViewHolder();
//                viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.item_news_iv);
//                viewHolder.mTitleTv = (TextView) convertView.findViewById(R.id.item_news_title_tv);
//                viewHolder.mPubdateTv = (TextView) convertView.findViewById(R.id.item_news_pubdate_tv);
//                convertView.setTag(viewHolder);
//            }
//            SingleNews singleNews = mNewsInListView.get(position);
//            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
//
//            viewHolder.mTitleTv.setText(singleNews.title);
//            viewHolder.mPubdateTv.setText(singleNews.pubdate);
////            viewHolder.mImageView.
//            sBitmapUtils.display(viewHolder.mImageView, NetConstant.formatUrl(singleNews.listimage));
//            return convertView;
//        }

        private void oo() {
            int i = 10;
            xx(i);
            System.out.println(i);
        }

        private void xx(int i) {
            i = 100;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommonViewHolder cvh = CommonViewHolder.createCVH(convertView, R.layout.item_news, parent);

            SingleNews singleNews = mNewsListInfo.news.get(position);
            cvh.getTv(R.id.item_news_title_tv).setText(singleNews.title);
            cvh.getTv(R.id.item_news_pubdate_tv).setText(singleNews.pubdate);
            mBitmapUtils.display(cvh.getIv(R.id.item_news_iv), NetConstant.formatUrl(singleNews.listimage));

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

    /**
     * ViewHolder的作用： 减少FindViewById的次数
     * 如何实现的： 把找好的View放在成员变量中
     * <p/>
     * 通用的解决： 使用集合代替多个成员变量
     */
//    public static class ViewHolder {
//        ImageView mImageView;
//        TextView mTitleTv;
//        TextView mPubdateTv;
//    }


}
