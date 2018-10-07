package com.planning.college.collegeplanning.elicitation.selfArticle;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.tools.Globle;
import com.planning.college.tools.UnZipFiles;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by KUIKUI on 2018-05-21.
 */
@EActivity(R.layout.article_cloud_info)
public class CloudArticleInfoActivity extends Activity{
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
        downloadContent(content_link);
    }


    public void downloadContent(String content_link){

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+"/cpServer/article/downloadContent?content_link="+content_link).build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                File file = new File(getCacheDir().toString()+"/cloudArticle/"+a_no+".zip");
                if(!file.exists() && !file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }

                InputStream in = response.body().byteStream();
                FileOutputStream fos= new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(in);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int b = -1;
                while((b=bis.read()) != -1){
                    bos.write(b);
                }
                bos.flush();
                bis.close();
                bos.close();


                indexPath = UnZipFiles.unZipFiles(file,file.getParent()+"/");//一定要加上斜杠

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        doUpdateWebView(indexPath);
                    }
                });
            }
        });

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



    @Click({R.id.back})
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.back:

                finish();
                break;



        }
    }




}
