package com.planning.college.fragment.elicitation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.view.View;

import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.elicitation.route.DirectionActivity_;
import com.planning.college.collegeplanning.elicitation.route.ResourceActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * Created by KUIKUI on 2018-05-23.
 */

@EFragment(R.layout.fragment_job)
public class JobFragment extends Fragment {

    private Activity activity;


    @AfterViews
    public void afterViews(){

        activity = getActivity();

    }


    @Click({R.id.job_direction,R.id.company_info})
    public void doClick(View v){

        Intent intent;
        switch(v.getId()){

            case R.id.job_direction:
               // Toast.makeText(activity,"就业方向",Toast.LENGTH_SHORT).show();
                intent = new Intent(activity, DirectionActivity_.class);
                intent.putExtra("title","就业方向");
                startActivity(intent);

                break;
            case R.id.company_info:
                intent = new Intent(activity, ResourceActivity_.class);
                intent.putExtra("type","3");
                startActivity(intent);
                //Toast.makeText(activity,"公司招聘信息",Toast.LENGTH_SHORT).show();
                break;


        }
    }

}
