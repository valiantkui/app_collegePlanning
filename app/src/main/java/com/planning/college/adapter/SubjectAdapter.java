package com.planning.college.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.model.Subject;

import java.util.List;

/**
 * Created by KUIKUI on 2018-07-27.
 */

public class SubjectAdapter extends BaseAdapter {

    private List<Subject> subjectList;
    private Context context;

    private LayoutInflater inflater;

    public SubjectAdapter(Context context, List<Subject> subjectList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.subjectList = subjectList;
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
            convertView = inflater.inflate(R.layout.item_subject,null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        Subject subject= subjectList.get(i);
        holder.s_no.setText(subject.getS_no());
        holder.s_name.setText(subject.getName());
        holder.intro.setText(subject.getIntro());
        holder.intro.setSingleLine();
        holder.intro.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.order_number.setText(String.valueOf(i+1));

        return convertView;
    }

    class Holder{


        public TextView order_number,s_name, s_no, intro;
        public View collect;
        

        public Holder(View view){
            order_number = view.findViewById(R.id.order_number);
            s_no = view.findViewById(R.id.s_no);
            s_name = view.findViewById(R.id.s_name);
            intro = view.findViewById(R.id.intro);
            collect = view.findViewById(R.id.collect);

        }
    }

}
