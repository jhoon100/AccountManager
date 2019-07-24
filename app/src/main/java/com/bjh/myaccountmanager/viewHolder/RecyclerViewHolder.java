package com.bjh.myaccountmanager.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bjh.myaccountmanager.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView staticsList;

    public RecyclerViewHolder(View itemView){
        super(itemView);

        staticsList = itemView.findViewById(R.id.staticsList);
    }
}
