package com.example.myapplication;


import android.net.http.SslError;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class play extends Fragment {


    public play() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_play, container, false);

        WebView webView = (WebView) v.findViewById(R.id.webView1);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);



        webView.setWebViewClient(new WebViewClient() {

                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                         return true;
                                     }

                                     @Override
                                     public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                         handler.proceed();
                                     }

                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                        Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
            }
                                 });

        webView.loadUrl("https://taptap.gg");

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
