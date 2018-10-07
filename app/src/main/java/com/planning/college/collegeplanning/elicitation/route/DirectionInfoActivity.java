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
 * Created by KUIKUI on 2018-07-25.
 */

@EActivity(R.layout.master_direction_info)
public class DirectionInfoActivity extends Activity {

    @ViewById(R.id.web_view)
    WebView webView;

    @Extra("r_no")
    String d_no;

    @Extra("title")
    String label;

    @Extra("d_name")
    String d_name;

    @Extra("update_date")
    String update_date;


    @ViewById(R.id.label)
    TextView title;

    @AfterViews
    public void afterViews(){
        Utility.setActionBar(this);
        title.setText(d_name);
        Log.i("web","before:"+d_no+"name:"+d_name+"   update_data:"+update_date);
        getMasterDirectionIntro(d_no);

        Log.i("web","after");
    }

    @UiThread
    public void updateWebView(final String indexPath){
                webView.loadUrl("file://"+indexPath);
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

                webView.getSettings().setLoadWithOverviewMode(true);

                /** WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setJavaScriptEnabled(true);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                webSettings.setUseWideViewPort(true);//关键点

                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

                webSettings.setDisplayZoomControls(false);
                webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
                webSettings.setAllowFileAccess(true); // 允许访问文件
                webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
                webSettings.setSupportZoom(true); // 支持缩放

                webSettings.setLoadWithOverviewMode(true);

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int mDensity = metrics.densityDpi;
                Log.d("maomao", "densityDpi = " + mDensity);
                if (mDensity == 240) {
                    webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
                } else if (mDensity == 160) {
                    webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
                } else if(mDensity == 120) {
                    webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
                }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
                    webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
                }else if (mDensity == DisplayMetrics.DENSITY_TV){
                    webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
                }else{
                    webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
                }

                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);*/
                /**
                 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
                 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
                 */
    }

    @Background
    public void getMasterDirectionIntro(final String d_no){

        String url = "/masterDirection/intro_link/";

        if("就业方向".equals(label)){
            url = "/jobDirection/intro_link/";
        }
        final File file = new File(getCacheDir().toString()+url+d_no+".zip");
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
        Request request = new Request.Builder().url(Globle.HOST_PORT+"/cpServer/master/downLoadDirecitonByD_no?d_no="+d_no).build();
        Call call = okHttpClient.newCall(request);
        //异步通信
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(DirectionInfoActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
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

    @Click(R.id.back)
    public void doBack(){
        finish();
    }

}
