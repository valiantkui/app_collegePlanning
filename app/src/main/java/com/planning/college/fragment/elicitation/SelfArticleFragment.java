package com.planning.college.fragment.elicitation;

/**
 * Created by KUIKUI on 2018-05-22.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.planning.college.adapter.SelfArticleAdapter;
import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.elicitation.selfArticle.ArticalInfoActivity_;
import com.planning.college.collegeplanning.elicitation.selfArticle.EditArticalActivity_;
import com.planning.college.dao.A_s_uDao;
import com.planning.college.dao.ArticleDao;
import com.planning.college.model.A_s_u;
import com.planning.college.model.Article;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 心得笔记的fragment
 * 当用户点击心得体会按钮时，将该fragment加载到Elicitation的活动中
 */

@EFragment(R.layout.fragment_self_article)
public class SelfArticleFragment extends Fragment{

    @ViewById(R.id.menu)
     ImageView iv_menu;

    @ViewById(R.id.self_note_listView)
     ListView selfNoteListView;

    @Bean
    ArticleDao articleDao;

    @Bean
    A_s_uDao asuDao;

  //  private View view;
    private Activity activity;

    @AfterViews
    public void afterViews(){

        activity = getActivity();
        List<Article> articleList = articleDao.getAllArticle();

        //创建适配器，并向指定适配器添加资源

        Log.i("tag",articleList.toString());
        SelfArticleAdapter selfArticleAdapter = new SelfArticleAdapter(activity, articleList,"local" +
                "");

        //将适配器与视图进行绑定

        selfNoteListView.setAdapter(selfArticleAdapter);
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
                Article article = articleDao.findArticleByid(a_no);//获取对应Artical

                Log.i("tag","点击item时，所点击的文章为:"+article+"");

                Intent intent_articalInfo = new Intent(activity, ArticalInfoActivity_.class);

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

//舍弃的代码：遍历listView中的每一个item
/*        for(int i = 0;i<selfArticleAdapter.getCount();i++) {
            LinearLayout linearLayout = (LinearLayout) selfArticleAdapter.getView(i,null,null);
            TextView textView = linearLayout.findViewById(R.id.a_no);
            int a_no = Integer.parseInt(textView.getText().toString());

            final ImageView option = linearLayout.findViewById(a_no);

            int tag = Log.i("option",  "option"+i);

            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  PopupMenu popupMenu = new PopupMenu(activity,option);
                    Menu menu = popupMenu.getMenu();
                    menu.add(1,100,1,"删除");
                    menu.add(1,101,1,"上传");
                    menu.add(1,102,1,"测试");
                    popupMenu.show();
                }
            });
        }*/
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
                Intent intent = null;
                switch (menuItem.getItemId()){

                    case R.id.create_article:
                        intent = new Intent(activity,EditArticalActivity_.class);
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
                        FragmentManager manager = activity.getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        CloudArticleFragment_ cloudArticleFragment = new CloudArticleFragment_();
                        transaction.replace(R.id.e_content,cloudArticleFragment);
                        transaction.commit();


                        break;
                    case R.id.local_article:
                        toast.setText("点击了本地笔记");
                        toast.show();
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
}
