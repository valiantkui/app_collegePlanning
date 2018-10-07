package com.planning.college.collegeplanning.guihua;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.planning.college.collegeplanning.R;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends SecondaryListAdapter<RecyclerAdapter.GroupItemViewHolder, RecyclerAdapter.SubItemViewHolder>{

    private TextView tvGroup;
    private ImageView add;
    private TextView tvSub;
    private Context context;
    private List<DataTree<String, String>> dts = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List datas) {
        dts = datas;
        notifyNewData(dts);
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_zi_item, parent, false);
        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.zi_item, parent, false);
        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(final RecyclerView.ViewHolder holder, int groupItemIndex) {
        tvGroup = (TextView) holder.itemView.findViewById(R.id.tv);
        add = (ImageView) holder.itemView.findViewById(R.id.add);
        tvGroup.setText(dts.get(groupItemIndex).getGroupItem());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }});
    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {
        tvSub = (TextView) holder.itemView.findViewById(R.id.tv);
        tvSub.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex));

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {
        Toast.makeText(context, "group item " + String.valueOf(groupItemIndex) + " is expand " +
                String.valueOf(isExpand), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {
        Toast.makeText(context, "sub item " + String.valueOf(subItemIndex) + " in group item " +
                String.valueOf(groupItemIndex), Toast.LENGTH_SHORT).show();
    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        public GroupItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {

        public SubItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}

