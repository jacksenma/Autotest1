package com.nj.ts.autotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.RuanModule;

import java.util.ArrayList;

public class TestingModuleAdapter extends RecyclerView.Adapter<TestingModuleAdapter.TestingModuleViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<RuanModule> mModules;
    private OnItemClickListener mOnItemClickListener = null;

    public TestingModuleAdapter(ArrayList<RuanModule> modules, Context context) {
        this.mModules = modules;
        this.mContext = context;
    }

    @Override
    public TestingModuleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_testing_module, viewGroup, false);
        TestingModuleViewHolder vh = new TestingModuleViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(TestingModuleViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(position);
        RuanModule module = mModules.get(position);
        viewHolder.mTextView.setText(module.getName());
        if (module.isSelect()) {
//            viewHolder.mTextView.setBackgroundColor(mContext.getResources().getColor(R.color.lightgray));
            viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.tab_item_sel_bg));
        } else {
//            viewHolder.mTextView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.tab_item_bg));
        }
    }

    @Override
    public int getItemCount() {
        return mModules.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public static class TestingModuleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public TestingModuleViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_module_name);
        }
    }
}
