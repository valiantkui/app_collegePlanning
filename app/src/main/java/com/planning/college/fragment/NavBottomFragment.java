package com.planning.college.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.view.View;

import com.planning.college.collegeplanning.Elicitation_;
import com.planning.college.collegeplanning.Knowledge_;
import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.Think;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by KUIKUI on 2018-05-22.
 */

@EFragment(R.layout.nav_buttom)
public class NavBottomFragment extends Fragment{

    @ViewById(R.id.elicitation)
    protected View elicitation;

    @ViewById(R.id.think)
    protected View think;

    @ViewById(R.id.knowledge)
    protected View knowledge;

    @Click({R.id.elicitation, R.id.think, R.id.knowledge})
    public void onClick(View view) {

       Activity activity = getActivity();
        switch (view.getId()){
            case R.id.elicitation:
                //跳转到启发页面
                Intent intent = new Intent(activity,Elicitation_.class);
                startActivity(intent);
                activity.overridePendingTransition(0,0);
                break;

            case R.id.think:
                //跳转到思考页面
                Intent intent2 = new Intent(activity,Think.class);
                startActivity(intent2);
                activity.overridePendingTransition(0,0);

                break;
            case R.id.knowledge:
                //跳转到知识页面
                Intent intent3 = new Intent(activity,Knowledge_.class);
                startActivity(intent3);
                activity.overridePendingTransition(0,0);

                break;

        }
    }
}
