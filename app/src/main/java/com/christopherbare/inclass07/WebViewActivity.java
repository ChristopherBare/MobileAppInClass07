package com.christopherbare.inclass07;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        progressDialog = new ProgressDialog(WebViewActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (getIntent()!=null && getIntent().getExtras()!=null) {
            String url = getIntent().getStringExtra("Url");
            WebView webView = new WebView(WebViewActivity.this);
            webView.loadUrl(url);
            setContentView(webView);
        } else {
            Toast.makeText(WebViewActivity.this, "Error loading article", Toast.LENGTH_LONG).show();
            finish();
        }

        if (progressDialog.isShowing()) progressDialog.hide();
    }
}
