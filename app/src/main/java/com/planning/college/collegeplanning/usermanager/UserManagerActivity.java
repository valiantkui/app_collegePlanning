package com.planning.college.collegeplanning.usermanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.tools.Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by KUIKUI on 2018-10-05.
 */

@EActivity(R.layout.user_manager)
public class UserManagerActivity extends Activity {

    @ViewById(R.id.name)
    public TextView tv_name;

    @AfterViews
    public void afterViews(){
        Utility.setActionBar(this);
        SharedPreferences preference;
        preference = getSharedPreferences("user_info",getChangingConfigurations());
        String name = preference.getString("name","未登录");

        tv_name.setText(name);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.push_left_out);
    }


    @Click(R.id.logout)
    public void logout(){
        SharedPreferences preference;
        preference = getSharedPreferences("user_info",getChangingConfigurations());
        SharedPreferences.Editor editor = preference.edit();
        editor.remove("id");
        editor.remove("password");
        editor.commit();

        //返回到登陆页面

        Intent intent = new Intent(UserManagerActivity.this,LoginActivity_.class);
        startActivity(intent);
    }
}
