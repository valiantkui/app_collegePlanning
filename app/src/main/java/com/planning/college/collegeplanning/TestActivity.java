package com.planning.college.collegeplanning;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.model.Profession;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by KUIKUI on 2018-05-23.
 */

public class TestActivity extends Activity {

    private CountDownLatch c = new CountDownLatch(1);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllProfession();
        try {
            c.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Profession> getAllProfession(){



        List<Profession> professionList = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url("http://192.168.42.114:8080/cpServer/profession/findAll").build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("error","请求失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String resultJson = response.body().string();
                Log.i("info",resultJson);
                Gson gson = new Gson();
                List<Profession> professionList =gson.fromJson(resultJson, new TypeToken<List<Profession>>() {
                }.getType());

                c.countDown();

            }
        });



        return professionList;
    }
}
