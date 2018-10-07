package com.planning.college.collegeplanning.elicitation.route;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.adapter.SubjectAdapter;
import com.planning.college.collegeplanning.R;
import com.planning.college.model.Subject;
import com.planning.college.tools.Globle;
import com.planning.college.tools.Utility;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;


/**
 * Created by KUIKUI on 2018-05-24.
 */

@EActivity(R.layout.profession)
public class ProfessionActivity extends Activity  implements AdapterView.OnItemClickListener{
    @ViewById(R.id.subject_listView)
    ListView listView;

    @ViewById(R.id.label)
    TextView profession_name;


    private  List<Subject> subjectList;

    @Extra("p_no")
    String p_no;
    @Extra("p_name")
    String p_name;

    @AfterViews
    protected void afterViews() {

        Utility.setActionBar(this);
        Log.i("info",p_no+"::::"+p_name);
        if(p_no != null){

            try{
                findSubjectJson(p_no);

            }catch(Exception e){
                Toast.makeText(this,"网络异常",Toast.LENGTH_SHORT).show();
            }
            //findSubjectByP_no(p_no);
        }

        //绑定事件
        listView.setOnItemClickListener(this);

    }

    @Click(R.id.back)
    public void doBack(){
        finish();
    }





    //获取指定专业的所有学生
    public void findSubjectJson(String p_no){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Globle.HOST_PORT+"/cpServer/subject/findSubjectByP_no?p_no="+p_no)
                .build();
        Log.i("info",request.urlString());
        Call call = okHttpClient.newCall(request);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String resultStr = response.body().string();
                Gson gson = new Gson();
              /*  subjectMap = gson.fromJson(resultStr, new TypeToken<SubjectMap>(){}.getType());

                String [] strArray = subjectMap.sNoArray;

                for(int i = 0;i<strArray.length;i++){
                    Log.i("json",strArray[i]);
                }

                int[][] matrix = subjectMap.matrixRelation;
                String row = "";
                for(int i = 0;i<matrix.length;i++){
                    row = "";
                    for(int j = 0;j<matrix[i].length;j++){
                        row += " "+matrix[i][j];
                    }
                    Log.i("json",row);
                }
*/

             //   final SubjectAdapter2 subjectAdapter2 = new SubjectAdapter2(ProfessionActivity.this,subjectMap);


                /**
                 * 1.先按行找到没有基础学科的学科，生成这些学科结点，并将这些学科信息索引到集合中
                 * 2.遍历集合，然后按列进行查询矩阵（并且本列不能索引该学科），找到该学科的后继学科
                 * 3.如果这些后继学科未被遍历过，则生成这些后继学科的结点，将这些后继学科添加到集合末尾（等待遍历）
                 * 4.重复步骤2，直到集合为空
                 */

              /*  List<Integer> tempList = new ArrayList<>();
                //第一步
                for( int i = 0;i<matrix.length;i++){
                    int j = 0;
                   second: for(;j<matrix[i].length;j++){
                        if(matrix[i][j] == 1){
                            break second;
                        }
                    }
                    if(j == matrix[i].length){

                       //生成该节点，并添加到集合中

                        tempList.add(i);
                    }
                }
                //第二步

                for(int j = 0;j<tempList.size();j++){

                    //倒找base_index列

                    for(int i = 0;i<matrix.length;i++){
                        if(i!=tempList.get(j) && matrix[i][tempList.get(j)] == 1){
                            //找到base学科的后继学科
                            //判断是否该后继学科是否被访问过了，如果访问过了，不再添加到集合当中，只生成学科结点
                            if(SubjectMap.isVisit(tempList,0,j,tempList.get(j))){


                                //TODO 建立这两个结点的连接线
                            }else{
                                //建立该结点，并将该节点保存到集合中

                            }
                            //

                        }
                    }
                }
*/
                Log.i("json",resultStr);
                subjectList = gson.fromJson(resultStr, new TypeToken<List<Subject>>(){}.getType());

                final SubjectAdapter subjectAdapter = new SubjectAdapter(ProfessionActivity.this,subjectList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        profession_name.setText(p_name);
                         listView.setAdapter(subjectAdapter);
                    }
                });
                Log.i("json",resultStr);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //1.获取学科编号
        String s_no = ((TextView)view.findViewById(R.id.s_no)).getText().toString();
        Toast.makeText(this,"点击的item："+i,Toast.LENGTH_SHORT).show();
        //2.根据s_no获取subject对象
        Subject subject = getSubjectFromSubjectList(s_no);
        //3.将subject对象转换成json字符串（因为intent不能直接传递一个对象）
        Gson gson = new Gson();
        String jsonSubject = gson.toJson(subject);
        Log.i("profession",jsonSubject);
        //4.加到intent对象里
        Intent intent2 = new Intent(ProfessionActivity.this,SubjectInfoActivity_.class);
        intent2.putExtra("jsonSubject",jsonSubject);
        startActivity(intent2);
    }

    /**
     * 根据从subjectList中获取Subject对象
     * @return  subject对象
     */
    public Subject getSubjectFromSubjectList(String s_no){

        //if(s_no == null) return null;

       for(Subject subject: subjectList){
           if(subject.getS_no().equals(s_no)) return subject;
       }
       // return subject;
        return null;
    }


}
