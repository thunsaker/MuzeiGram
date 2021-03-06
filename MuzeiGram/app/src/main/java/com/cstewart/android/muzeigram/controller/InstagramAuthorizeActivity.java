package com.cstewart.android.muzeigram.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cstewart.android.muzeigram.R;
import com.cstewart.android.muzeigram.data.Config;
import com.cstewart.android.muzeigram.data.settings.Settings;

import javax.inject.Inject;

public class InstagramAuthorizeActivity extends MuzeiGramActivity {

    private static final String ACCESS_TOKEN_CONSTANT = "access_token=";

    @Inject Settings mSettings;

    private WebView mAuthorizeWebView;

    public static Intent newIntent(Context context) {
        return new Intent(context, InstagramAuthorizeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_authorize);

        mAuthorizeWebView = (WebView) findViewById(R.id.activity_instagram_authorize_webview);
        mAuthorizeWebView.setWebViewClient(mWebViewClient);

        mAuthorizeWebView.loadUrl(getAuthorizeUrl());
    }

    private String getAuthorizeUrl() {
        return String.format(Config.AUTHORIZE_URL_FORMAT, Config.CLIENT_ID, Config.REDIRECT_URI);
    }

    private void aquireToken(String url) {
        int index = url.indexOf(ACCESS_TOKEN_CONSTANT);
        index = index + ACCESS_TOKEN_CONSTANT.length();
        String accessToken = url.substring(index, url.length());

        mSettings.saveInstagramToken(accessToken);
        finish();
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(ACCESS_TOKEN_CONSTANT)) {
                aquireToken(url);
                return true;
            }

            return false;
        }
    };
}
