package com.planning.college.collegeplanning;

import android.app.Activity;
import android.widget.EditText;

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
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Created by KUIKUI on 2018-10-06.
 */

@EActivity(R.layout.search)
public class SearchActivity extends Activity {


    @ViewById(R.id.search_content)
    public EditText et_content;


    @AfterViews
    public void afterViews(){
        Utility.setActionBar(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }


    @Click(R.id.back)
    public void doBack(){
        finish();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }


    /**
     * 分四次进行查询
     * 1.查学科
     * 2.查云端笔记
     * 3.查考研方向
     * 4.查资源信息
     */
    @Click(R.id.search)
    public void doSearch(){
        String searchContent = et_content.getText().toString().trim();




    }

    /**
     * 根据关键字查询学科信息
     * @param searchContent
     */
    public void searchSubject(String searchContent){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+"/cpServer/search/searchSubject?searchContent="+searchContent).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {


            }
        });
    }

    /**
     * 根据关键字进行云端文章查询
     * @param searchContent
     */
    public void searchArticle(String searchContent){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+"/cpServer/search/searchArticle?searchContent="+searchContent).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    /**
     * 根据关键字进行方向查询
     * @param searchContent
     */
    public void searchDirection(String searchContent){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+"/cpServer/search/searchDirection?searchContent="+searchContent).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    /**
     * 根据关键字进行资源查询
     * @param searchContent
     */
    public void searchResource(String searchContent){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+"/cpServer/search/searchResource?searchContent="+searchContent).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

}
