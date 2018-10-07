package com.planning.college.fragment.elicitation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.SearchActivity_;
import com.planning.college.collegeplanning.usermanager.UserManagerActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by KUIKUI on 2018-05-22.
 */

@EFragment(R.layout.nav_elicitation)
public class NavElicitationFragment extends Fragment{

    @ViewById(R.id.menu)
    protected ImageView tv_menu;

    @ViewById(R.id.self_article)
    protected TextView tv_self_article;

    @ViewById(R.id.route)
    protected TextView tv_route;

    @ViewById(R.id.search)
    protected ImageView tv_search;


    private View view;


    @Click({R.id.menu, R.id.self_article, R.id.route, R.id.search})
    public void onClick(View view) {

        Activity activity = getActivity();
        FragmentManager manager;
        FragmentTransaction transition;

        switch (view.getId()){
            case R.id.menu:
                //跳到一个新的activity

                Intent intent = new Intent(activity, UserManagerActivity_.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);


                break;

            case R.id.self_article:

                manager = getFragmentManager();
                transition = manager.beginTransaction();
                transition.replace(R.id.e_content,new SelfArticleFragment_());
                transition.commit();
                break;
            case R.id.route:
                //用户点击推荐路线的时候，修改内容的fragment
                manager = getFragmentManager();
                transition = manager.beginTransaction();
                transition.replace(R.id.e_content,new RouteFragment_());
                transition.commit();
                break;

            case R.id.search:

                Intent intent2 = new Intent(activity, SearchActivity_.class);
                startActivity(intent2);
                activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                break;



        }

    }

}
