package com.planning.college.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;
import com.planning.college.model.Resource;

import java.util.List;

/**
 * Created by KUIKUI on 2018-07-27.
 */

public class ResourceAdapter extends BaseAdapter {

    private List<Resource> resourceList;
    private Context context;

    private LayoutInflater inflater;

    public ResourceAdapter(Context context, List<Resource> resourceList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.resourceList = resourceList;
    }

    @Override
    public int getCount() {
        return resourceList.size();
    }

    @Override
    public Resource getItem(int i) {
        return resourceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_resource,null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        Resource resource= resourceList.get(i);
        holder.r_no.setText(resource.getR_no());
        holder.r_name.setText(resource.getTitle());
        holder.update_date.setText(resource.getUpdate_date());
        holder.order_number.setText(String.valueOf(i+1));

        return convertView;
    }

    class Holder{


        public TextView order_number,r_name, r_no,update_date;
        public View collect;
        

        public Holder(View view){
            r_no = view.findViewById(R.id.r_no);
            order_number = view.findViewById(R.id.order_number);
            update_date = view.findViewById(R.id.update_date);
            r_name = view.findViewById(R.id.r_name);
            collect = view.findViewById(R.id.collect);

        }
    }

}
