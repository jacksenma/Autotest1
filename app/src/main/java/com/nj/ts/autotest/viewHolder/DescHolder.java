package com.nj.ts.autotest.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ts.autotest.R;

public class DescHolder extends RecyclerView.ViewHolder {
    public TextView descView;
    public TextView messageView;

    public DescHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        descView = (TextView) itemView.findViewById(R.id.tv_desc);
        messageView = (TextView) itemView.findViewById(R.id.tv_message);
    }
}
