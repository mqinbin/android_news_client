package com.qinbin.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import com.qinbin.news.bean.SingleNews;
import com.qinbin.news.constant.NetConstant;

/**
 * Created by Teacher on 2016/8/11.
 */
public class NewsDetailActivity extends Activity {

    @ViewInject(R.id.detail_wv)
    WebView mWebView;

    @ViewInject(R.id.detail_pb)
    ProgressBar mProgressBar;
    private WebSettings mWebViewSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);

        SingleNews singleNews = (SingleNews) getIntent().getSerializableExtra("SingleNews");

        // webview的简单使用：
//        1  显示网页, 不需要考虑子线程的问题
        mWebView.loadUrl(NetConstant.formatUrl(singleNews.url));
//        2 设置常用的监听 ，有2类，简单的 和高级
//        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);

//       3 拿到webView的设置器
        mWebViewSettings = mWebView.getSettings();
        // 开启js功能
        mWebViewSettings.setJavaScriptEnabled(true);
        // 设置字体大小
        mWebViewSettings.setTextSize(WebSettings.TextSize.SMALLEST);
//
    }
    private WebViewClient mWebViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d("onProgressChanged", "" + newProgress);
            if(newProgress == 100){
                mProgressBar.setVisibility(View.GONE);
            }
        }
    };
    private CharSequence[] items =new String[]{"最小","较小","中等","较大","最大"};
    @OnClick(R.id.detail_textsize_iv)
    public void changeTextSize(View v){

       final  int currentIndex = mWebViewSettings.getTextSize().ordinal();

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            int mChooseItem = currentIndex;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 当选项被点击了，whitch 就是角标
                // 当按钮被点击了，就是常量DialogInterface。
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        mWebViewSettings.setTextSize(WebSettings.TextSize.values()[mChooseItem]);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    default:
                        mChooseItem = which;
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置字体大小");

        builder.setSingleChoiceItems(items, currentIndex, onClickListener);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消",onClickListener);
        builder.show();
    }

    // TODO , 记录用户的字体大小的偏好 5W1H
}
