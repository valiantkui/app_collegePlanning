package com.planning.college.collegeplanning.elicitation.route;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.adapter.DirectionAdapter;
import com.planning.college.collegeplanning.R;
import com.planning.college.model.Direction;
import com.planning.college.tools.Globle;
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
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

/**
 * Created by KUIKUI on 2018-07-24.
 */

@EActivity(R.layout.direction)
public class DirectionActivity extends Activity {



    @ViewById(R.id.label)
    TextView label;

    @Extra("title")
    String title;

    private String url;

    @ViewById(R.id.list_view)
    ListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.setActionBar(this);
    }

    @AfterViews
    public void afterViews(){


        label.setText(title);
        if("考研方向".equals(title)){
            url=Globle.MASTER_DIRECTIONS_URL;
        } else if("就业方向".equals(title)){

            url=Globle.JOB_DIRECTIONS_URL;
        }  else{
            Toast.makeText(DirectionActivity.this,"出错了！！",Toast.LENGTH_LONG).show();
        }

        Log.i("tag","main thread name = "+Thread.currentThread().getName());
        Log.i("tag","main thread id = "+Thread.currentThread().getId());
        getDirections();
        Toast.makeText(DirectionActivity.this,"getDirections方法之后",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Click(R.id.back)
    public void back(){
        finish();
    }

    @Background
    public void getDirections(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Globle.HOST_PORT+url).build();
        Call call = okHttpClient.newCall(request);
        //异步通信
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("tag","网络异常");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String resultJson = response.body().string();
                Log.i("tag","成功了"+resultJson);

                Gson gson = new Gson();
                List<Direction> directionList =gson.fromJson(resultJson, new TypeToken<List<Direction>>() {
                }.getType());

                final DirectionAdapter directionAdapter = new DirectionAdapter(DirectionActivity.this,directionList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        listView.setAdapter(directionAdapter);
                    }
                });


            }
        });
    }



    /**
     * 获得考研方向的简介
     * @param d_no 方向编号
     */

    public void doClick(View v){
        Toast.makeText(DirectionActivity.this,"测试",Toast.LENGTH_SHORT).show();
    }


    @ItemClick(R.id.list_view)
    public void itemClick(int position){

        View v = listView.getChildAt(position);
        TextView textView = v.findViewById(R.id.d_name);
        String d_name = textView.getText().toString();
      //  Toast.makeText(DirectionActivity.this,d_name,Toast.LENGTH_SHORT).show();

        String d_no = ((TextView)(v.findViewById(R.id.d_no))).getText().toString().trim();

        String update_date = ((TextView)(v.findViewById(R.id.update_date))).getText().toString().trim();
        update_date = update_date.substring(0,update_date.lastIndexOf("."));
        Toast.makeText(DirectionActivity.this,getCacheDir().toString(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DirectionActivity.this,DirectionInfoActivity_.class);
        intent.putExtra("r_no",d_no);
        intent.putExtra("d_name",d_name);
        intent.putExtra("update_date",update_date);
        intent.putExtra("title",title);
        startActivity(intent);
    }


}
