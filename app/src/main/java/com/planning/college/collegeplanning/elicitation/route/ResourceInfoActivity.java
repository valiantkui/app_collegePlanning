package com.planning.college.collegeplanning.elicitation.route;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.planning.college.collegeplanning.R;
import com.planning.college.tools.Globle;
import com.planning.college.tools.UnZipFiles;
import com.planning.college.tools.Utility;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KUIKUI on 2018-07-27.
 */

@EActivity(R.layout.resource_info)
public class ResourceInfoActivity extends Activity {




    @ViewById(R.id.web_view)
    WebView webView;

    @ViewById(R.id.label)
    TextView tv_title;

    @Extra("r_name")
    String nameStr;

    @Extra("update_date")
    String update_date;

    @Extra("type")
    String type;

    @Extra("r_no")
    String r_no;

    private String url;

    @Click(R.id.back)
    public void doBack(){
        finish();
    }

    @AfterViews
    public void afterViews(){

        Utility.setActionBar(this);

        tv_title.setText(nameStr);

        switch (type){
            case "1":
                url="/resource/1/";
                break;
            case "2":
                url="/resource/2/";
                break;
            case "3":
                url="/resource/3/";
                break;
        }

        getResourceIntro(r_no);


    }
    @UiThread
    public void updateWebView(final String indexPath){
        webView.loadUrl("file://"+indexPath);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.getSettings().setLoadWithOverviewMode(true);

    }
    @Background
    public void getResourceIntro(final String r_no){


        final File file = new File(getCacheDir().toString()+url+r_no+".zip");
        File fileParent = file.getParentFile();

        if(file.exists()){

            Date date_local = new Date(file.lastModified());
            Log.i("date",update_date);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date_remote = null;
            try {
                date_remote = sdf.parse(update_date);
                Log.i("date","date_local:"+date_local+"date_remote:"+date_remote);
                if(date_local.after(date_remote)){
                    String indexPath = null;

                    indexPath = UnZipFiles.unZipFiles(file,file.getParent()+"/");

                    updateWebView(indexPath);
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Log.i("return","return之后");
        if(! fileParent.exists())
        {
            fileParent.mkdirs();
        }
        if(! file.exists()){

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //下载简介
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Globle.HOST_PORT+"/cpServer/resource/downLoadResourceByR_no?r_no="+r_no).build();
        Call call = okHttpClient.newCall(request);
        //异步通信
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(ResourceInfoActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Response response) throws IOException {
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
                //然后进行文件的解压,并获取index.html的位置信息
                String indexPath = UnZipFiles.unZipFiles(file,file.getParent()+"/");
                updateWebView(indexPath);
            }
        });
    }
}
