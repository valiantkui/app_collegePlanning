package com.planning.college.fragment.elicitation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.planning.college.collegeplanning.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * Created by KUIKUI on 2018-05-23.
 */

@EFragment(R.layout.fragment_route)
public class RouteFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    private FragmentManager manager;
    private  SubjectFragment_ subject;
    private  MasterFragment_ master;
    private  CollegeFragment_ college;
    private  JobFragment job;
    private  ForeignFragment foreign;

    @ViewById(R.id.radioGroup)
    RadioGroup radioGroup;

    private FragmentTransaction transaction;

    @AfterViews
    public void afterView(){
        manager = getFragmentManager();
        radioGroup.setOnCheckedChangeListener(this);
        transaction = manager.beginTransaction();
        //transaction.add(R.id.route_content,new SubjectFragment_());
        subject = new SubjectFragment_();
        transaction.add(R.id.route_content, subject);
        transaction.commit();
    }

    private void remove(){
        if(subject!=null){
            transaction.remove(subject);
        }
        if(master!=null){
            transaction.remove(master);
        }
        if(college!=null){
            transaction.remove(college);
        }
        if(job != null){
            transaction.remove(job);
        }
        if(foreign!=null){
            transaction.remove(foreign);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
            transaction = manager.beginTransaction();
            remove();
            switch (id){
                case R.id.subject:
                    //if(transaction.isEmpty()) break;
                    subject = new SubjectFragment_();
                    transaction.add(R.id.route_content, subject);
                    break;
                case R.id.master:
                    master = new MasterFragment_();
                    transaction.add(R.id.route_content,master);
                    break;
                case R.id.college:
                    college = new CollegeFragment_();
                    transaction.add(R.id.route_content, college);
                    break;
                case R.id.job:
                    job = new JobFragment_();
                    transaction.add(R.id.route_content,job);
                    break;
                case R.id.foreign:
                    foreign = new ForeignFragment_();
                    transaction.add(R.id.route_content,foreign);
                    break;
            }
            transaction.commit();
    }
}
