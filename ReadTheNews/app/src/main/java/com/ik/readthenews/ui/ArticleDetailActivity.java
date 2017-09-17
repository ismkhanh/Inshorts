package com.ik.readthenews.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ik.readthenews.R;
import com.ik.readthenews.repository.database.entity.Article;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailActivity extends AppCompatActivity {

    public static String KEY_SELECTED_ARTICLE = "selected_article";

    @BindView(R.id.web_view) WebView mWebView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(KEY_SELECTED_ARTICLE)){
            Article article = bundle.getParcelable(KEY_SELECTED_ARTICLE);
            if (article != null) {
                ButterKnife.bind(this);
                progressBar.setVisibility(View.VISIBLE);
                mWebView.setWebViewClient(new MyBrowser());
                mWebView.loadUrl(article.getURL());
            } else {
                showMsg("Invalid Article");
                finish();
            }
        }else{
            showMsg("Invalid Intent Passed");
            finish();
        }


    }

    private void showMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class MyBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}
