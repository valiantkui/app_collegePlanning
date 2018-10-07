package com.planning.college.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.model.Direction;

import java.util.List;

/**
 * Created by KUIKUI on 2018-05-19.
 * LayoutInflater:布局映射器。
 * 主要作用：将定义好的一个xmL布局文件转化为View对象
 * findViewById通过View对象来调用。
 */

public class DirectionAdapter extends BaseAdapter{

    private List<Direction> directionList;
    private Context context;

    private LayoutInflater inflater;

    public DirectionAdapter(Context context, List<Direction> directionList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.directionList = directionList;
    }

    @Override
    public int getCount() {
        return directionList.size();
    }

    @Override
    public Direction getItem(int i) {
        return directionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_direction,null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        Direction direction= directionList.get(i);
        holder.d_no.setText(direction.getD_no());
        holder.order_number.setText(String.valueOf(i+1));
        holder.d_name.setText(direction.getName());
        holder.update_date.setText(direction.getUpdate_date());
        holder.collect.setTag(direction.getD_no());
        return convertView;
    }

    class Holder{


        public TextView order_number,d_name,d_no,update_date;
        public View collect;

        public Holder(View view){
            d_no = view.findViewById(R.id.d_no);
            order_number = view.findViewById(R.id.order_number);
            update_date = view.findViewById(R.id.update_date);
            d_name = view.findViewById(R.id.d_name);
            collect = view.findViewById(R.id.collect);

        }
    }

}

