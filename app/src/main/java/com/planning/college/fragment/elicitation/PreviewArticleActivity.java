package com.planning.college.fragment.elicitation;

import android.app.Activity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.planning.college.collegeplanning.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by KUIKUI on 2018-08-06.
 */

@EActivity(R.layout.preview_article)
public class PreviewArticleActivity extends Activity {

    @ViewById(R.id.web_view)
    WebView webView;




    Activity activity;

    @AfterViews
    public void afterViews() {

        activity = this;








        update();


    }

    @Click(R.id.back)
    public void back(){
        finish();
    }

    @Background
    public void update(){


        updateWebView();

    }


    @UiThread
    public void updateWebView(){


        webView.loadUrl("file://"+activity.getCacheDir()+"/index.html");
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.getSettings().setLoadWithOverviewMode(true);
    }




    public void onClick(View v) {
        switch (v.getId()) {


        }
    }




}
