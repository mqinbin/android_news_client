package com.qinbin.news.fragment;

import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qinbin.news.R;
import com.qinbin.news.bean.NewsCategoriesResp;
import com.qinbin.news.bean.NewsCategory;
import com.qinbin.news.constant.NetConstant;
import com.qinbin.news.pager.BasePager;
import com.qinbin.news.pager.NewsPager;
import com.qinbin.news.pager.PicturePager;
import com.qinbin.news.widget.EditDialog;

/**
 * 界面
 * 构成  ViewPager  +  ImageView  + TPI
 * <p/>
 * 1 关联TPI和ViewPager ，需要在setViewPager之前 调用setAdapter
 * <p/>
 * 2 修改主题
 * ① 修改MainActivity的主题为  @style/Theme.PageIndicatorDefaults
 * ② 修改 @style/Theme.PageIndicatorDefaults  --> Widget.TabPageIndicator ->@style/TextAppearance.TabPageIndicator ->  @color/vpi__dark_theme 选择器，selected 的状态
 * ③ 修改 @style/Theme.PageIndicatorDefaults  --> Widget.TabPageIndicator -> @drawable/vpi__tab_indicator   选择器，selected 的状态
 * <p/>
 * <p/>
 * 3 从服务器获取所有的新闻栏目信息，并展示
 * 软件都是处理数据的：代码的整体原则：
 * <p/>
 * ① 拿到数据
 * 数据的来源  服务器 可能是 内容提供者， 传感器，文件，蓝牙、wifi 、服务器
 * 可能需要加工（业务逻辑）
 * ② 显示数据
 * 系统控件、自定义控件、第三方控件、动画、
 * <p/>
 * <p/>
 * <p/>
 */
public class NewsFragment extends BaseFragment {


    private TabPageIndicator mTabPageIndicator;
    private ViewPager mViewPager;
    private List<NewsCategory> mAllNewsCategories;


    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView(ImageView imageView, TextView textView, View childView) {
        imageView.setVisibility(View.VISIBLE);
        textView.setText("新闻");

        ImageView editImageView = (ImageView) childView.findViewById(R.id.news_iv);
        editImageView.setOnClickListener(mShowDialogOcl);

        mViewPager = (ViewPager) childView.findViewById(R.id.news_vp);
        mTabPageIndicator = (TabPageIndicator) childView.findViewById(R.id.news_tpi);

        mViewPager.setAdapter(mPageAdapter);
        mTabPageIndicator.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(mPageListener);

        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        BasePager pager = getCurrentPager();
        if (pager != null) {
            pager.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BasePager pager = getCurrentPager();
        if (pager != null) {
            pager.onPause();
        }
    }

    private void initData() {
        HttpUtils httpUtils = new HttpUtils();
        // 模拟器用 10.0.2.2
        // genymotion 10.0.3.2
        // 真机 用 23.83.240.120
        httpUtils.send(HttpRequest.HttpMethod.GET, NetConstant.HOST + "/categories.json", mCallback);

    }

    private RequestCallBack<String> mCallback = new RequestCallBack<String>() {


        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            NewsCategoriesResp newsCategoriesResp = new Gson().fromJson(json, NewsCategoriesResp.class);

            //  处理数据
//            mShowNewsCategories = newsCategoriesResp.data;
            mAllNewsCategories = newsCategoriesResp.data;
            List<Integer> showNewsCategoriesId = newsCategoriesResp.extend;

            mShowNewsCategories = new ArrayList<NewsCategory>();
            for (Integer id : showNewsCategoriesId) {
                for (NewsCategory newsCategory : mAllNewsCategories) {
                    if (newsCategory.id == id.intValue()) { // 可能因为环境问题直接写等号是ok的，代码中要注意
                        mShowNewsCategories.add(newsCategory);
                        break; // 性能优化
                    }
                }
            }
            //更新界面

            mPageAdapter.notifyDataSetChanged();
            mTabPageIndicator.notifyDataSetChanged();
            // 进行初始化
            mPageListener.onPageSelected(0);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Toast.makeText(getActivity(), "联网出错了" + s, Toast.LENGTH_LONG).show();
        }
    };
    List<NewsCategory> mShowNewsCategories;
    String[] pageNames = new String[]{"女性", "两性", "男性", "足球", "里约奥运", "死库水"};

    private Map<NewsCategory, BasePager> pagers = new HashMap<>();

    private PagerAdapter mPageAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
//            return pageNames.length;
            return mShowNewsCategories == null ? 0 : mShowNewsCategories.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return pageNames[position];
            return mShowNewsCategories.get(position).title;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsCategory newsCategory = mShowNewsCategories.get(position);

            if (pagers.get(newsCategory) == null) {
                if (newsCategory.type == 1) {
                    pagers.put(newsCategory, new NewsPager(container.getContext(), newsCategory));
                } else if (newsCategory.type == 2) {
                    pagers.put(newsCategory, new PicturePager(container.getContext(), newsCategory));
                }
            }
            BasePager pager = pagers.get(newsCategory);
            container.addView(pager.getView());
            return pager.getView();

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    };

    private ViewPager.OnPageChangeListener mPageListener = new ViewPager.SimpleOnPageChangeListener() {
        BasePager currentPager;

        @Override
        public void onPageSelected(int position) {
            if (currentPager != null) {
                currentPager.onPause();
            }

            BasePager pager = pagers.get(mShowNewsCategories.get(position));
            if (pager != null) {
                pager.onResume();
            }
            currentPager = pager;
        }
    };

    public BasePager getCurrentPager() {
        if (mShowNewsCategories == null) {
            return null;
        }

        int position = mViewPager.getCurrentItem();
        return pagers.get(mShowNewsCategories.get(position));
    }

    private View.OnClickListener mShowDialogOcl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final EditDialog editDialog = new EditDialog(getActivity());

            List<NewsCategory> hideNewsCategories = new ArrayList<>();
            hideNewsCategories.addAll(mAllNewsCategories);
            hideNewsCategories.removeAll(mShowNewsCategories);

            editDialog.setShowAndHideNewsCategories(mShowNewsCategories, hideNewsCategories);
            editDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    List<NewsCategory> showNewsCategories = editDialog.getShowNewsCategories();
//                    Toast.makeText(getActivity(),"" + showNewsCategories.size(),Toast.LENGTH_SHORT).show();
                    updateUi(showNewsCategories);
                }
            });
            editDialog.show();
        }
    };

    private void updateUi(List<NewsCategory> showNewsCategories) {
        // 要知道用户更新前看的是啥
        NewsCategory userWatchNewsCategories = mShowNewsCategories.get(mViewPager.getCurrentItem());

        mShowNewsCategories = showNewsCategories;
//        mPageAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mPageAdapter);
        mTabPageIndicator.notifyDataSetChanged();

        // 让ViewPager滑动到指定位置
        int currentIndex = mShowNewsCategories.indexOf(userWatchNewsCategories);
        // 对于原来看的，隐藏了，ViewPager的 setCurrentItem方法有容错处理
        mViewPager.setCurrentItem(currentIndex);

    }

    //TODO 记录并使用用户对新闻栏目的偏好:
    // 对于 不是特别细致的需求，我们用 “5W1H”方法来细化需求，细化需求了就会做了
    // Why  :  用户体验
    // What :  保存 mShowNewsCategories 的 id
    // Who  :
    // When :  保存：用户编辑结束就包括  恢复：下载好所有的新闻栏目，使用本地存的id，而不是服务器规定的id
    // Where:  sp、db
    // How  :  sp: {10007,10006} ->"10007,10006"

}
