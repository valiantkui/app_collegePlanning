package com.planning.college.collegeplanning;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.planning.college.collegeplanning.usermanager.LoginActivity_;
import com.planning.college.fragment.elicitation.RouteFragment_;
import com.planning.college.tools.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * 1.给底部导航的三个按钮添加事件
 * 2.实现每个事件的跳转功能
 */
@EActivity(R.layout.elicitation)
public class Elicitation extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.elicitation);

        FragmentManager manager;
        FragmentTransaction transition;
        manager = getFragmentManager();
        transition = manager.beginTransaction();
        transition.replace(R.id.e_content,new RouteFragment_());
        transition.commit();

    }

    /**
     * 判断用户是否登陆
     */
    @AfterViews
    public void afterViews(){
        Utility.setActionBar(this);
        SharedPreferences preference;
        preference = getSharedPreferences("user_info",getChangingConfigurations());
        if(!preference.contains("id")){
            //如果不包含，则跳到登陆页面
            Intent  intent = new Intent(Elicitation.this, LoginActivity_.class);

            startActivity(intent);

            return;
        }







    }
}
