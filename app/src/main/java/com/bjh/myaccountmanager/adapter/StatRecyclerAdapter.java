package com.bjh.myaccountmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjh.myaccountmanager.R;
import com.bjh.myaccountmanager.viewHolder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class StatRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ArrayList<HashMap<String, String>> mData;

    public StatRecyclerAdapter(ArrayList<HashMap<String, String>> list){
        mData = list;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_statlist, parent, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position){
        HashMap<String, String> hashMap = mData.get(position);

        holder.staticsDate.setText(hashMap.get("staticsDate"));
        holder.staticsTime.setText(hashMap.get("staticsTime"));
        holder.staticsAmount.setText(hashMap.get("staticsAmount"));
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }
}
