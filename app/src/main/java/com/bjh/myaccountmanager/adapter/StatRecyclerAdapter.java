package com.bjh.myaccountmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjh.myaccountmanager.R;
import com.bjh.myaccountmanager.viewHolder.RecyclerViewHolder;

import java.util.ArrayList;

public class StatRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ArrayList<String> mData;

    public StatRecyclerAdapter(ArrayList<String> list){
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
        String text = mData.get(position);
        holder.staticsList.setText(text);
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }
}
