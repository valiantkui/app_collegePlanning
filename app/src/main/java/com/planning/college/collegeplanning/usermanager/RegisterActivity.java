package com.planning.college.collegeplanning.usermanager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
 * Created by KUIKUI on 2018-09-29.
 */
@EActivity(R.layout.register)
public class RegisterActivity extends Activity {

    @ViewById(R.id.id)
    public EditText et_id;

    @ViewById(R.id.name)
    public EditText et_name;

    @ViewById(R.id.nan)
    public RadioButton nan;

    @ViewById(R.id.nv)
    public RadioButton nv;

    @ViewById(R.id.password1)
    public EditText et_pwd1;
    @ViewById(R.id.password2)
    public EditText et_pwd2;


    public boolean idOk = false;

    @ViewById(R.id.message)
    public TextView tv_message;

    @AfterViews
    public void afterViews(){


        //验证账号是否已经存在
        et_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    //获得焦点时的事件

                }else{
                    //失去焦点时的事件
                    //Toast.makeText(RegisterActivity.this,"失去焦点",Toast.LENGTH_SHORT).show();

                    checkId(et_id.getText().toString());

                }
            }
        });


    }




    @Click(R.id.register)
    public void submit(){
        //1.检验表单是否为空
        boolean isBlank = true;//默认为空，不可提交

        String id = et_id.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String password1 = et_pwd1.getText().toString().trim();
        String password2 = et_pwd2.getText().toString().trim();
        String gender = null;


        if("".equals(id)
                || "".equals(name)
                || "".equals(password1)
                || (!nan.isChecked() && !nv.isChecked())  ){

            Toast.makeText(RegisterActivity.this,"请输入完整",Toast.LENGTH_SHORT).show();

            return;
        }

        gender = nan.isChecked()?"男":"女";


        //Toast.makeText(RegisterActivity.this,"zhihou",Toast.LENGTH_SHORT).show();


        if(!et_pwd1.getText().toString().trim().equals(et_pwd2.getText().toString().trim())){
            Toast.makeText(RegisterActivity.this,"密码不统一",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!idOk){
            Toast.makeText(RegisterActivity.this,"账号不可使用",Toast.LENGTH_SHORT).show();
        }

        User user = new User();
        user.setU_id(id);
        user.setPassword(password1);
        user.setName(name);
        user.setGender(gender);

        user.setBirthday(null);
        user.setGrager(null);
        //将user传到服务器


        /**
         * 使用此方式创建gson对象，会将值为null的属性也转换成字符串
         *
         * */
       /* Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();*/

       Gson gson = new Gson();
        String userGson = gson.toJson(user);


        doRegister(userGson);


    }


    /**
     * Post请求
     * @param userGson
     */
    public void doRegister(String userGson){

        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        RequestBody requestBody = requestBodyBuilder.add("userGson",userGson).build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(Globle.HOST_PORT+"/cpServer/user/registerUser").post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                String result = response.body().string();

                if("ok".equals(result)){
                    //跳转到已登陆界面
                    //Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    //finish();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity_.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void checkId(String id){


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+"/cpServer/user/checkId"+"?id="+et_id.getText().toString().trim()).build();
        Call call = okHttpClient.newCall(request);


        //异步方式
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String result = response.body().string();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,result,Toast.LENGTH_SHORT).show();
                            //String s = tv_message.getText().toString() + result;
                            if("ok".equals(result)){

                                tv_message.setText("账号："+"可以使用");
                                idOk = true;
                            }else{
                                tv_message.setText("账号："+"账号已存在");
                            }
                        }
                    });
                }

        });

    }

}
