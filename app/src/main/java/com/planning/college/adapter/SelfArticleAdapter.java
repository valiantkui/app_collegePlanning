package com.planning.college.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.planning.college.collegeplanning.R;
import com.planning.college.dao.A_s_uDao;
import com.planning.college.dao.ArticleDao;
import com.planning.college.model.A_s_u;
import com.planning.college.model.Article;
import com.planning.college.tools.Globle;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by KUIKUI on 2018-05-19.
 * LayoutInflater:布局映射器。
 * 主要作用：将定义好的一个xmL布局文件转化为View对象
 * findViewById通过View对象来调用。
 *
 * 注意：Adapter中不能用@EBean注解来标识，也不能注入其他bean
 */

public class SelfArticleAdapter extends BaseAdapter{



    private List<Article> articleList;
    private Activity context;

    private ArticleDao articleDao;
    A_s_uDao asuDao;

    private LayoutInflater inflater;

    private String flag;

    public SelfArticleAdapter(Activity activity,List<Article> articleList,String flag){
        this.context = activity;
        inflater = LayoutInflater.from(activity);
        this.articleList = articleList;
        this.flag = flag;

        articleDao = new ArticleDao();
        articleDao.setContext(context);

        asuDao = new A_s_uDao();
        asuDao.setContext(context);
    }


    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Article getItem(int i) {
        return articleList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_article,null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        final Article article = articleList.get(i);

        String type = "";
        switch (article.getType()){

            case "note":
                type = "笔记";
                break;
            case "technology":
                type = "技术";
                break;
        }

        final String a_no = article.getA_no();
        holder.a_no.setText(a_no);
        holder.title.setText(article.getTitle());
        holder.intro.setText(article.getIntro());
        holder.type.setText(type);
        holder.status.setText(article.getStatus());
        holder.option.setId(Integer.parseInt(a_no));

        final ImageView option = holder.option;
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,option);
                Menu menu = popupMenu.getMenu();

                if("local".equals(flag)){

                    menu.add(1,100,1,"删除");
                    menu.add(1,101,1,"上传");
                } else if("cloud".equals(flag)){
                    menu.add(1,102,1,"从云端删除");
                   // menu.add(1,103,1,"上传");
                }
                //menu.add(1,103,1,"");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

                        switch (item.getItemId()){
                            case 100:
                                if(deleteContent(a_no)
                                        && articleDao.deleteArticleByA_no(a_no)
                                        && asuDao.deleteA_s_uByA_no(a_no)){
                                    toast.setText("删除成功");
                                }else{
                                    toast.setText("删除失败");
                                }
                                toast.show();
                                break;
                            case 101:
                                uploadArticle(a_no);
                                //做上传的操作，将文件上传，同时将article对象和a_s_uList对象也传到服务器

                                break;
                            case 102:
                               /* toast.setText("弹出3");
                                toast.show();*/
                                deleteArticleFromCloud(a_no);
                                //从云端删除该文章

                                break;
                        }
                        return false;
                    }
                });
            }
        });
        return convertView;
    }

    private void deleteArticleFromCloud(String a_no){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+"/cpServer/article/deleteArticleByA_no?a_no="+a_no).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();


                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("ok".equals(result)) {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });



            }
        });
    }

    private boolean deleteContent(String a_no){
        Log.i("tag","传来的文章编号参数为："+a_no);
        ArticleDao articleDao = new ArticleDao();
        articleDao.setContext(context);
        Article article = articleDao.findArticleByid(a_no);
        String content_link = article.getContent_link();
        Log.i("tag","content_link:"+content_link+",article:"+article);

        if( !   (content_link== null)){

            File file = new File(context.getFilesDir()+"/article/"+content_link);
            File file2 = new File(context.getFilesDir()+"/article/"+a_no+".cp");
            if(file.exists() && file2.exists()){
               return  file.delete()&& file2.delete();
            }
        }
        return true;
    }

    /**
     * 上传的时候还需要将article和a_s_u的数据表的信息上传
     * @param a_no
     */
    private void uploadArticle(String a_no){


        Article article = articleDao.findArticleByid(a_no);
        List<A_s_u> asuList = asuDao.findA_s_uByA_no(a_no);
        String content_link = article.getContent_link();

        Gson gson = new Gson();
        String articleGson = gson.toJson(article);
        String asuGson = gson.toJson(asuList);

        File zipFile = new File(context.getFilesDir()+"/article/"+content_link);

        File cpFile = new File(context.getFilesDir()+"/article/"+article.getA_no()+".cp");



       // doPost(article);
        if(zipFile.exists() && cpFile.exists()){
                doPostFiles(zipFile,cpFile,article,articleGson,asuGson);

        }

    }

    private void doPost(Article article){

        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        RequestBody requestBody = requestBodyBuilder.add("username","yuankui").add("password","123456").build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(Globle.HOST_PORT+"/cpServer/uploadArticle").post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();

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


    private void doPostString(){

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),"{username:yuankui,password:123456}");
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("").post(requestBody).build();


    }
    private void doPostFile(File file){

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(Globle.HOST_PORT+"/cpServer/uploadArticle").post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();

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

    private void doPostFiles(File zipFile, File cpFile, final Article article,String articleGson,String asuGson){

        SharedPreferences preference;
        preference = context.getSharedPreferences("user_info",context.getChangingConfigurations());
        SharedPreferences.Editor editor = preference.edit();

        String id = preference.getString("id", "");
        String password = preference.getString("password","");
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        RequestBody requestBody = multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("username",id)
                .addFormDataPart("password",password)
                .addFormDataPart("articleGson",articleGson)
                .addFormDataPart("asuGson",asuGson)
                .addFormDataPart("zipFileName",zipFile.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),zipFile))
                .addFormDataPart("cpFileName",cpFile.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),cpFile))
                .build();


        Request.Builder builder = new Request.Builder();
        Request request = builder.url(Globle.HOST_PORT+"/cpServer/article/uploadArticle").post(requestBody).build();

        OkHttpClient okHttpClient = new OkHttpClient();
        final Call call = okHttpClient.newCall(request);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                        Toast.makeText(context,"上传异常",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
               // Toast.makeText(context, response.body().string(), Toast.LENGTH_SHORT).show();


                final String string = response.body().string();

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       Toast.makeText(context, string,Toast.LENGTH_SHORT).show();

                    }
                });
                if("上传成功".equals(string)){

                    article.setStatus("cloud");
                    articleDao.updateArticle(article);
                }
            }
        });
    }
}




class Holder{


    public TextView a_no,title,intro,type,status;
    public ImageView option;

    public Holder(View view){
        a_no = view.findViewById(R.id.a_no);
        title = view.findViewById(R.id.title);
        intro = view.findViewById(R.id.intro);
        type = view.findViewById(R.id.type);
        status = view.findViewById(R.id.status);
        option = view.findViewById(R.id.option);

    }
}
