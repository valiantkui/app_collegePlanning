package com.planning.college.fragment.elicitation;

/**
 * Created by KUIKUI on 2018-05-22.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.adapter.SelfArticleAdapter;
import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.elicitation.selfArticle.CloudArticleInfoActivity_;
import com.planning.college.collegeplanning.elicitation.selfArticle.EditArticalActivity_;
import com.planning.college.dao.A_s_uDao;
import com.planning.college.dao.ArticleDao;
import com.planning.college.model.A_s_u;
import com.planning.college.model.Article;
import com.planning.college.tools.Globle;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

/**
 * 心得笔记的fragment
 * 当用户点击心得体会按钮时，将该fragment加载到Elicitation的活动中
 */

@EFragment(R.layout.fragment_cloud_article)
public class CloudArticleFragment extends Fragment{

    @ViewById(R.id.menu)
     ImageView iv_menu;

    @ViewById(R.id.self_note_listView)
     ListView selfNoteListView;

    @Bean
    ArticleDao articleDao;

    private List<Article> articleList;

    @Bean
    A_s_uDao asuDao;

  //  private View view;
    private Activity activity;






    @AfterViews
    public void afterViews(){

        activity = getActivity();

        getAllArticles();


        /**
         * 为ListView的item绑定监听事件
         * 1.当用户点击了某个item时，我们先获取该item的a_no
         * 2.然后查找数据库，从数据库中查找到对应资料。
         * 3.跳转到显示该文章的  Activity页面
         */
        selfNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView a_noTextView = view.findViewById(R.id.a_no);
                String a_no = a_noTextView.getText().toString();
                Article article = findArticleByid(a_no);//获取对应Artical

                Log.i("tag","点击item时，所点击的文章为:"+article+"");

                Intent intent_articalInfo = new Intent(activity, CloudArticleInfoActivity_.class);

                intent_articalInfo.putExtra("a_no",article.getA_no());
                intent_articalInfo.putExtra("title",article.getTitle());
                intent_articalInfo.putExtra("intro",article.getIntro());
                intent_articalInfo.putExtra("content_link",article.getContent_link());
                startActivity(intent_articalInfo);
                List<A_s_u> asuList = asuDao.findA_s_uByA_no(a_no);
                for(A_s_u asu: asuList){
                    Log.i("tag","item事件："+asu.getA_no()+"---"+asu.getS_no()+"---"+asu.getU_id());
                }
            }
        });


    }

    public Article findArticleByid(String a_no){

        if(a_no == null) return null;
        for(Article article:articleList){
            if(a_no.equals(article.getA_no())){
                return article;
            }
        }

        return null;
    }

    @Click(R.id.menu)
    public void doMenuClick(View v){

        Log.i("click","doMenuClick()");
        PopupMenu popupMenu = new PopupMenu(activity,iv_menu);

        popupMenu.getMenuInflater().inflate(R.menu.self_article_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
                Intent intent = new Intent(activity,EditArticalActivity_.class);
                switch (menuItem.getItemId()){

                    case R.id.create_article:
                        intent.putExtra("tag","article");
                        startActivity(intent);
                        break;
                    case R.id.create_diary:
                        toast.setText("点击了新建日记");
                        toast.show();
                        break;
                    case R.id.cloud_article:
                        toast.setText("点击了云端笔记");
                        toast.show();
                        break;
                    case R.id.local_article:
                        toast.setText("点击了本地笔记");
                        toast.show();
                        FragmentManager manager = activity.getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        SelfArticleFragment selfArticleFragment = new SelfArticleFragment_();
                        transaction.replace(R.id.e_content,selfArticleFragment);
                        transaction.commit();
                        break;
                }
                return false;
            }
        });

        popupMenu.show();

    }



    public void onClick(View view) {
        Intent intent = new Intent(activity,EditArticalActivity_.class);
        startActivity(intent);
        activity.overridePendingTransition(0,0);
    }


    public  void getAllArticles(){

        SharedPreferences preference;
        preference = activity.getSharedPreferences("user_info",activity.getChangingConfigurations());
        String u_id = preference.getString("id","");
        String url = "/cpServer/article/findArticleByU_id?u_id="+u_id;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+url).build();
        Call call = okHttpClient.newCall(request);

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
                articleList = gson.fromJson(resultJson, new TypeToken<List<Article>>() {
                }.getType());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SelfArticleAdapter selfArticleAdapter = new SelfArticleAdapter(activity, articleList,"cloud");
                        selfNoteListView.setAdapter(selfArticleAdapter);
                    }
                });






            }
        });
    }
}
