package com.planning.college.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by KUIKUI on 2018-09-24.
 */

public class ArticleService extends Service{

    MyBinder mybinder;
    @Override
    public void onCreate() {
        Log.i("service","onCreate()");
        super.onCreate();

        mybinder = new MyBinder();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service","onBind()");
        return mybinder;
    }

    public class MyBinder extends Binder {
        public String name = "xiaoming";
        public int age = 21;

    }

}
