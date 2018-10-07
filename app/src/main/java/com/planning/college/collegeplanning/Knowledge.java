package com.planning.college.collegeplanning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.planning.college.collegeplanning.knowledge.Knowledge1Activity_;
import com.planning.college.collegeplanning.knowledge.Knowledge2Activity_;
import com.planning.college.collegeplanning.knowledge.Knowledge3Activity_;
import com.planning.college.collegeplanning.knowledge.Knowledge4Activity_;
import com.planning.college.collegeplanning.knowledge.Knowledge5Activity_;
import com.planning.college.collegeplanning.knowledge.Knowledge6Activity_;
import com.planning.college.collegeplanning.knowledge.Knowledge7Activity_;
import com.planning.college.collegeplanning.knowledge.Knowledge8Activity_;
import com.planning.college.collegeplanning.knowledge.Knowledge9Activity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;


/**
 * 1.给底部导航的三个按钮添加事件
 * 2.实现每个事件的跳转功能
 */

@EActivity(R.layout.knowledge)
public class Knowledge extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click({R.id.k1,R.id.k2,R.id.k3,R.id.k4,R.id.k5,R.id.k6,R.id.k7,R.id.k8,R.id.k9})
    public void clickAction(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.k1:
                intent = new Intent(Knowledge.this, Knowledge1Activity_.class);
                startActivity(intent);
                break;
            case R.id.k2:
                intent = new Intent(Knowledge.this, Knowledge2Activity_.class);
                startActivity(intent);
                break;
            case R.id.k3:
                intent = new Intent(Knowledge.this, Knowledge3Activity_.class);
                startActivity(intent);
                break;
            case R.id.k4:
                intent = new Intent(Knowledge.this, Knowledge4Activity_.class);
                startActivity(intent);
                break;
            case R.id.k5:
                intent = new Intent(Knowledge.this, Knowledge5Activity_.class);
                startActivity(intent);
                break;
            case R.id.k6:
                intent = new Intent(Knowledge.this, Knowledge6Activity_.class);
                startActivity(intent);
                break;
            case R.id.k7:
                intent = new Intent(Knowledge.this, Knowledge7Activity_.class);
                startActivity(intent);
                break;
            case R.id.k8:
                intent = new Intent(Knowledge.this, Knowledge8Activity_.class);
                startActivity(intent);
                break;
            case R.id.k9:
                intent = new Intent(Knowledge.this, Knowledge9Activity_.class);
                startActivity(intent);
                break;
        }
    }
}
