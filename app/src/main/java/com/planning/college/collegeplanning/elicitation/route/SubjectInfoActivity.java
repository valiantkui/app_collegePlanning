package com.planning.college.collegeplanning.elicitation.route;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planning.college.collegeplanning.R;
import com.planning.college.fragment.elicitation.RecommendFragment_;
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
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KUIKUI on 2018-05-24.
 */

@EActivity(R.layout.subject_info)
public class SubjectInfoActivity extends Activity {

    @ViewById(R.id.s_name)
    TextView tv_sName;
    @ViewById(R.id.s_intro)
    TextView tv_sIntro;
    @ViewById(R.id.s_image)
    ImageView iv_sImage;




    @AfterViews
    public void afterViews() {

        Utility.setActionBar(this);
        Intent intent = getIntent();
        //1.获取ProfessionActivity传来的subject对象
        String jsonSubject = intent.getStringExtra("jsonSubject");

        Gson gson = new Gson();
        final Subject subject = gson.fromJson(jsonSubject, Subject.class);
        //3.更新ui
        runOnUiThread(new Runnable() {

            public void run() {
                tv_sName.setText(subject.getName());
                tv_sIntro.setText(subject.getIntro());

                //不在此处加载图片，在网络请求函数中去更新图片ui
               // tv_sName.setText(subject.getName());
            }
        });

        loadImageByS_no(subject.getS_no());



        //启动fragment
        Bundle bundle = new Bundle();
        bundle.putString("jsonSubject",jsonSubject);
        Fragment recommendFragment = new RecommendFragment_();
        recommendFragment.setArguments(bundle);

        FragmentManager manager;
        FragmentTransaction transition;
        manager = getFragmentManager();
        transition = manager.beginTransaction();
        transition.add(R.id.recommend,recommendFragment);
        transition.commit();


    }

    @Click(R.id.back)
    public void doBack(){
        finish();
    }



    /**
     * 如果图片已经存在本地了，则直接加载，否则：
     * 根据学科编号从网上下载图片，保存到系统目录的cache中，并更新指定ui
     * @param s_no
     */
    private void loadImageByS_no(final String s_no){
        File file = new File(getCacheDir().toString()+"/subject/"+s_no+".jpg");

        if(file.exists() && file.length()>1000){
            try {
              final  Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       iv_sImage.setImageBitmap(bitmap);
                   }
               });


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {

            }
            return;
        }

        //get方式请求数据
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Globle.HOST_PORT+Globle.SUBJECT_INFO_URL+"?s_no="+s_no)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(SubjectInfoActivity.this,"向服务器请求图片失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final InputStream in =
                        response.body().byteStream();

                File file = new File(getCacheDir().toString()+"/subject/"+s_no+".jpg");


                Log.i("info","文件路径："+file.toString());
                    File fileParent = file.getParentFile();
                if(!fileParent.exists()){
                    fileParent.mkdirs();
                }
                if(! file.exists())
                {
                    file.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(file);

                byte[] bytes = new byte[1024*10];
                int len = -1;
                while((len = in.read(bytes)) != -1){
                    fos.write(bytes,0,len);
                }

                fos.flush();
                fos.close();
                in.close();


                //此时file已经存在了，则将图片流加载到imageView里
                //final  InputStream is = new FileInputStream(file);
                final  Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_sImage.setImageBitmap(bitmap);
                    }
                });

            }
        });

    }
}
