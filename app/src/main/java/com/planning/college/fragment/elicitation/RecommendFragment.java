package com.planning.college.fragment.elicitation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.adapter.DirectionAdapter;
import com.planning.college.adapter.SelfArticleAdapter;
import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.elicitation.route.DirectionInfoActivity_;
import com.planning.college.model.Article;
import com.planning.college.model.Direction;
import com.planning.college.model.Subject;
import com.planning.college.tools.Globle;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

/**
 * Created by KUIKUI on 2018-10-05.
 */

@EFragment(R.layout.recommed)
public class RecommendFragment extends Fragment {


    @ViewById(R.id.lv_article)
    public ListView lv_article;

    @ViewById(R.id.lv_direction)
    public ListView lv_direction;

    private Activity activity;



    @AfterViews
    public void afterViews(){

        activity = getActivity();
        Bundle bundle = getArguments();

        String jsonSubject = bundle.getString("jsonSubject");

        Gson gson = new Gson();

        Subject subject = gson.fromJson(jsonSubject, new TypeToken<Subject>() {
        }.getType());



        String s_no = subject.getS_no();
        findArticleByS_no(s_no);
        findDirectionByS_no(s_no);

    }




    @ItemClick(R.id.lv_direction)
    public void itemClick(int position){

        View v = lv_direction.getChildAt(position);
        TextView textView = v.findViewById(R.id.d_name);
        String d_name = textView.getText().toString();
        //  Toast.makeText(DirectionActivity.this,d_name,Toast.LENGTH_SHORT).show();

        String d_no = ((TextView)(v.findViewById(R.id.d_no))).getText().toString().trim();

        String update_date = ((TextView)(v.findViewById(R.id.update_date))).getText().toString().trim();
        update_date = update_date.substring(0,update_date.lastIndexOf("."));
        //Toast.makeText(DirectionActivity.this,getCacheDir().toString(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity,DirectionInfoActivity_.class);
        intent.putExtra("r_no",d_no);
        intent.putExtra("d_name",d_name);
        intent.putExtra("update_date",update_date);
        intent.putExtra("title","方向");
        startActivity(intent);
    }

    public void findArticleByS_no(String s_no){
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        RequestBody requestBody = requestBodyBuilder
                .add("s_no",s_no)
                .build();


        Request.Builder builder = new Request.Builder();
        Request request = builder.url(Globle.HOST_PORT+"/cpServer/article/findArticleByS_no").post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                String result = response.body().string();
                Log.i("result",result);

                Gson gson = new Gson();

                List<Article> articleList = gson.fromJson(result,new TypeToken<List<Article>>(){}.getType());
                final SelfArticleAdapter selfArticleAdapter = new SelfArticleAdapter(activity,articleList,"recommend");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv_article.setAdapter(selfArticleAdapter);
                    }
                });



            }
        });
    }

    public void findDirectionByS_no(String s_no){
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        RequestBody requestBody = requestBodyBuilder
                .add("s_no",s_no)
                .build();


        Request.Builder builder = new Request.Builder();
        Request request = builder.url(Globle.HOST_PORT+"/cpServer/direction/findDirectionByS_no").post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();

                Log.i("result",result);
                Gson gson = new Gson();

                List<Direction> directionList = gson.fromJson(result, new TypeToken<List<Direction>>() {
                }.getType());

               final DirectionAdapter directionAdapter = new DirectionAdapter(activity,directionList);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv_direction.setAdapter(directionAdapter);
                    }
                });
            }
        });
    }





}
