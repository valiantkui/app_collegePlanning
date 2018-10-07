package com.planning.college.collegeplanning.usermanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.collegeplanning.Elicitation_;
import com.planning.college.collegeplanning.R;
import com.planning.college.model.User;
import com.planning.college.tools.Globle;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Created by KUIKUI on 2018-10-05.
 */

@EActivity(R.layout.login)
public class LoginActivity extends Activity {
    @ViewById(R.id.id)
    public EditText et_id;

    @ViewById(R.id.password)
    public EditText et_pwd;

    @AfterViews
    public void afterViews(){


    }



    @Click(R.id.register)
    public void register(){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.login)
    public void login(){
       String id = et_id.getText().toString().trim();

       String password = et_pwd.getText().toString().trim();

        if("".equals(id) || "".equals(password)){
            Toast.makeText(LoginActivity.this,"请输入完整",Toast.LENGTH_SHORT).show();
            return;
        }

        //验证



        doLogin(id,password);






    }

    //post请求
    public void doLogin(final String id, final String password){
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        RequestBody requestBody = requestBodyBuilder.add("id",id).add("password",password).build();


        Request.Builder builder = new Request.Builder();
        Request request = builder.url(Globle.HOST_PORT+"/cpServer/user/loginUser").post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                String result = response.body().string().trim();

                Log.i("json",result+"");


                if(result != null && !"".equals(result)){
                    //登陆成功
                    //将id和password保存到 sharedPreferences

                    Gson gson = new Gson();
                    User user = gson.fromJson(result, new TypeToken<User>() {
                    }.getType());

                    Log.i("json",user+"");
                    SharedPreferences preference;
                    preference = getSharedPreferences("user_info",getChangingConfigurations());
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putString("id",id);
                    editor.putString("password",password);
                    editor.putString("name",user.getName());
                    editor.commit();//每次修改preference都要提交
                    //tiao转到主页
                    Intent intent = new Intent(LoginActivity.this, Elicitation_.class);
                    startActivity(intent);


                }else{

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

}
