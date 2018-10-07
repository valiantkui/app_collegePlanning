package com.planning.college.collegeplanning.elicitation.selfArticle;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.tools.UnZipFiles;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;


/**
 * Created by KUIKUI on 2018-05-21.
 */
@EActivity(R.layout.article_local_info)
public class ArticalInfoActivity extends Activity{
   @ViewById(R.id.title)
    TextView titleView;

    @ViewById(R.id.intro)
    TextView introView;

    @ViewById(R.id.content)
    WebView contentView;

    @ViewById(R.id.back)
    ImageView iv_back;

    @Extra("a_no")
    String a_no;

    @Extra("title")
    String title;

    @Extra("intro")
    String intro;

    @Extra("content_link")
    String content_link;

    private String indexPath;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    public void afterViews(){
        titleView.setText(title);
        introView.setText(intro);
      //  contentView.setText(loadFromFile(content_link));

        Log.i("tag","内容链接："+content_link+"");
        updateWebView();
    }


    @Background
    public void updateWebView(){
        //先解指定压缩文件
        String path = getFilesDir()+"/article/";
        File zipFile = new File(path+content_link);

        try {
            //目标地址最后一定要加斜杠
            indexPath = UnZipFiles.unZipFiles(zipFile, getCacheDir()+"/article/");//解压后的文件放在 cache目录的article中
        } catch (IOException e) {
            e.printStackTrace();
        }


        doUpdateWebView(indexPath);

    }
    @UiThread
    public void doUpdateWebView(String indexPath){
        contentView.loadUrl("file://"+indexPath);
        contentView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        contentView.getSettings().setLoadWithOverviewMode(true);
    }

   /* private String loadFromFile(String content_link) {

        String result = "";
        try {
            FileInputStream fis = openFileInput(content_link);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffers = new byte[1024*10];
            int len = 0;
            while((len = fis.read(buffers)) != -1){
                baos.write(buffers,0,len);
            }

            result = baos.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }*/

    @Click({R.id.back})
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.back:

                finish();
                break;



        }
    }

    @Click(R.id.update)
    public void doUpdate(){
        finish();
        Intent intent = new Intent(ArticalInfoActivity.this,EditArticalActivity_.class);
        intent.putExtra("tag","article");
        intent.putExtra("a_no",a_no);
        intent.putExtra("indexPath",indexPath);
        startActivity(intent);

    }



}
