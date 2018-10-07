package com.planning.college.fragment.elicitation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.elicitation.route.DirectionActivity_;
import com.planning.college.collegeplanning.elicitation.route.ResourceActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by KUIKUI on 2018-05-23.
 */

@EFragment(R.layout.fragment_master)
public class MasterFragment extends Fragment {

    public final static String EXTRA_TAG = "type";

    @ViewById(R.id.master_direction)
    LinearLayout masterDirection;

    private Activity activity;

    @AfterViews
    public void afterViews(){

        activity = getActivity();
    }

    @Click({R.id.master_direction,R.id.tutor_info,R.id.college_enroll})
    public void masterDirectionAction(View view){
        Intent intent;

        switch (view.getId()){
            case R.id.master_direction:
              //  Toast.makeText(activity,"考研方向",Toast.LENGTH_SHORT).show();

                //跳转到考研方向的页面
                intent = new Intent(activity, DirectionActivity_.class);
                intent.putExtra("title","考研方向");
                startActivity(intent);
                break;
            case R.id.tutor_info:
                //Toast.makeText(activity,"导师院校信息",Toast.LENGTH_SHORT).show();
                intent = new Intent(activity, ResourceActivity_.class);
                intent.putExtra("type","1");
                startActivity(intent);



                break;
            case R.id.college_enroll:
               // Toast.makeText(activity,"历年院校录取信息",Toast.LENGTH_SHORT).show();

                intent = new Intent(activity, ResourceActivity_.class);
                intent.putExtra(EXTRA_TAG,"2");
                startActivity(intent);
                break;
        }
    }


}
