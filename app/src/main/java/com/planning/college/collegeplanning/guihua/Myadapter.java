package com.planning.college.collegeplanning.guihua;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planning.college.collegeplanning.R;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter{

    private ArrayList<String> mData;
    private Context mContext;
    private TextView mMyTextView;
    public static CardView mycard;

    public Myadapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        mycard = (CardView) holder.itemView.findViewById(R.id.bianqiancardview);
        mMyTextView = (TextView) holder.itemView.findViewById(R.id.tv_my_title);
        NotesDB notesDB = new NotesDB(this.mContext);
        SQLiteDatabase dbReader = notesDB.getReadableDatabase();
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAMEO,null,null,null,null,null,null);
        cursor.moveToPosition(holder.getAdapterPosition());
        mMyTextView.setText(cursor.getString(cursor.getColumnIndex("content")));
        mMyTextView.setSingleLine();
        mMyTextView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
