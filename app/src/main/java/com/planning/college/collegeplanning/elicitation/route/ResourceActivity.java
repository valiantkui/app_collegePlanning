package com.planning.college.collegeplanning.elicitation.route;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.adapter.ResourceAdapter;
import com.planning.college.collegeplanning.R;
import com.planning.college.fragment.elicitation.MasterFragment;
import com.planning.college.model.Resource;
import com.planning.college.tools.Globle;
import com.planning.college.tools.Utility;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

/**
 * Created by KUIKUI on 2018-07-27.
 */

@EActivity(R.layout.resource)
public class ResourceActivity extends Activity{

    @ViewById(R.id.label)
    TextView label;

    @ViewById(R.id.list_view)
    ListView listView;

    @Extra(MasterFragment.EXTRA_TAG)
    String type;

    private String url;

    @AfterViews
    public void afterViews(){
        Utility.setActionBar(this);

        switch (type){
            case "1":
                label.setText("导师院校信息");
                url = Globle.GET_RESOURCES+"?type=1";

                break;
            case "2":
                label.setText("历年院校录取信息");
                url = Globle.GET_RESOURCES+"?type=2";
                break;
            case "3":
                label.setText("公司招聘信息");
                url = Globle.GET_RESOURCES+"?type=3";
                break;
        }

        getResourceList();

    }

    @Click(R.id.back)
    public void doBack(){
        finish();
    }


    /**
     * 根据类型获得资源列表
     * 如果type = '1' ,则获得导师院校信息的资源列表
     * 如果type = '2' ,则获得历年院校录取信息的资源列表
     * 如果type = '3' ,则获得公司招聘信息的资源列表
     */
    @Background
    public void getResourceList(){

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Globle.HOST_PORT+url).build();
        Call call = okHttpClient.newCall(request);
        //异步通信
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("tag","网络异常");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String resultJson = response.body().string();
                Log.i("tag","成功了"+resultJson);

                Gson gson = new Gson();
                List<Resource> resourceList = gson.fromJson(resultJson, new TypeToken<List<Resource>>() {
                }.getType());

                final ResourceAdapter resourceAdapter = new ResourceAdapter(ResourceActivity.this,resourceList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        listView.setAdapter(resourceAdapter);
                    }
                });


            }
        });

    }

    @ItemClick(R.id.list_view)
    public void itemClick(int position) {
        View v = listView.getChildAt(position);
        TextView textView = v.findViewById(R.id.r_name);
        String r_name = textView.getText().toString();
        //  Toast.makeText(DirectionActivity.this,r_name,Toast.LENGTH_SHORT).show();

        String r_no = ((TextView)(v.findViewById(R.id.r_no))).getText().toString().trim();


        String update_date = ((TextView)(v.findViewById(R.id.update_date))).getText().toString().trim();
        update_date = update_date.substring(0,update_date.lastIndexOf("."));
        Toast.makeText(ResourceActivity.this,getCacheDir().toString(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ResourceActivity.this,ResourceInfoActivity_.class);
        intent.putExtra("r_no",r_no);
        intent.putExtra("r_name",r_name);
        intent.putExtra("update_date",update_date);
        intent.putExtra("type",type);
        startActivity(intent);



    }
}



