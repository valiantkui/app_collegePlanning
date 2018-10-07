package com.planning.college.fragment.elicitation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.elicitation.route.ProfessionActivity_;
import com.planning.college.model.Profession;
import com.planning.college.tools.Globle;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KUIKUI on 2018-05-23.
 */
@EFragment(R.layout.fragment_subject)
public class SubjectFragment extends Fragment implements AdapterView.OnItemClickListener{

   @ViewById(R.id.gv_profession)
   GridView gridView;

    private Activity activity;

    @AfterViews
    public void afterViews(){
        gridView.setOnItemClickListener(this);
        activity = getActivity();

        try {
            getAllProfession();
        } catch (Exception e) {
            Toast.makeText(activity,"网络异常",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从服务器获取所有的专业集合
     */
    public  void getAllProfession() throws Exception{
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()

                        .url(Globle.HOST_PORT+"/cpServer/profession/findAll").build();
        Call call = null;
        try{

        call = okHttpClient.newCall(request);
        }catch (Exception e){
            Toast.makeText(activity,"网络异常",Toast.LENGTH_SHORT).show();
        }

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("error","请求失败");
            }
            @Override
            public void onResponse(Response response) throws IOException {
                String resultJson = response.body().string();
                Log.i("info",resultJson);
                Gson gson = new Gson();
                final List<Profession> professionList = gson.fromJson(resultJson, new TypeToken<List<Profession>>() {
                }.getType());
                activity.runOnUiThread(new Runnable() {
                    List<Map<String, String>> list = new ArrayList<>();
                    @Override
                    public void run() {
                        for (Profession p: professionList) {
                            Map<String, String> map = new HashMap<>();
                            map.put("p_no", p.getP_no());
                            map.put("p_name",p.getName());
                            list.add(map);
                        }
                        String[] from = {"p_no", "p_name"};
                        int[] to = {R.id.p_no, R.id.p_name};
                        SimpleAdapter simpleAdapter = new SimpleAdapter
                                (activity, list, R.layout.item_profession, from, to);
                        gridView.setAdapter(simpleAdapter);
                    }
                });
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //1.获取专业编号
        String p_no =((TextView) view.findViewById(R.id.p_no)).getText().toString();
        String p_name =((TextView) view.findViewById(R.id.p_name)).getText().toString();

        //2.跳转到所点击专业的Activity中，与此同时将专业编号传递过去
        Intent intent = new Intent(activity, ProfessionActivity_.class);
        intent.putExtra("p_no",p_no);
        intent.putExtra("p_name",p_name);
        startActivity(intent);
    }
}