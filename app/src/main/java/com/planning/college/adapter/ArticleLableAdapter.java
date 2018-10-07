package com.planning.college.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.model.A_s_u;
import com.planning.college.model.Subject;

import java.util.List;

/**
 * Created by KUIKUI on 2018-05-19.
 * LayoutInflater:布局映射器。
 * 主要作用：将定义好的一个xmL布局文件转化为View对象
 * findViewById通过View对象来调用。
 */

public class ArticleLableAdapter extends BaseAdapter{

    private List<Subject> subjectList;

    private Context context;

    private LayoutInflater inflater;

    private List<A_s_u> asuList;

    public ArticleLableAdapter(Context context, List<Subject> subjectList, List<A_s_u> asuList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.subjectList = subjectList;
        this.asuList = asuList;
    }

    @Override
    public int getCount() {
        return subjectList.size();
    }

    @Override
    public Subject getItem(int i) {
        return subjectList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_article_label,null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        Subject subject= subjectList.get(i);
        holder.article_box.setText(subject.getName());

        holder.s_no.setText(subject.getS_no());

        if(asuList != null){
            for(A_s_u a: asuList){
                if(a.getS_no().equals(subject.getS_no())){
                    holder.article_box.toggle();
                    break;
                }
            }
        }
        return convertView;
    }

    class Holder{
        public CheckBox article_box;
        public TextView s_no;

        public Holder(View view){
            article_box = view.findViewById(R.id.article_label);
            s_no = view.findViewById(R.id.s_no);
        }
    }

}

