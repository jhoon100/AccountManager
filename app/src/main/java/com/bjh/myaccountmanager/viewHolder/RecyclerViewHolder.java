package com.bjh.myaccountmanager.viewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bjh.myaccountmanager.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView staticsDate;
    public TextView staticsTime;
    public TextView staticsAmount;

    public RecyclerViewHolder(View itemView){
        super(itemView);

        staticsDate = itemView.findViewById(R.id.staticsDate);
        staticsTime = itemView.findViewById(R.id.staticsTime);
        staticsAmount = itemView.findViewById(R.id.staticsAmount);
    }
}
