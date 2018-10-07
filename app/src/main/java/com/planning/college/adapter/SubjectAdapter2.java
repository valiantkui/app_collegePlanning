package com.planning.college.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.model.Subject;
import com.planning.college.tools.SubjectMap;

/**
 * Created by KUIKUI on 2018-07-27.
 */

public class SubjectAdapter2 extends BaseAdapter {

    private SubjectMap subjectMap;
    private Context context;

    private LayoutInflater inflater;

    public SubjectAdapter2(Context context, SubjectMap subjectMap){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.subjectMap = subjectMap;
    }

    @Override
    public int getCount() {
        return subjectMap.sNoArray.length;
    }

    @Override
    public Subject getItem(int i) {

            return subjectMap.map.get(subjectMap.sNoArray[i]);
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

        Subject subject=subjectMap.map.get(subjectMap.sNoArray[i]);
        holder.s_no.setText(subject.getS_no());
        holder.s_name.setText(subject.getName());
        holder.intro.setText(subject.getIntro());
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
