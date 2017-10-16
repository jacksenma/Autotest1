package com.nj.ts.autotest.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.Module;

import java.util.List;

/**
 * Created by ts on 17-9-28.
 */

public class TestModuleAdapter extends RecyclerView.Adapter<TestModuleAdapter.ViewHolder> implements View.OnClickListener{
    private List<Module> items;
    private OnItemClickListener mOnItemClickListener = null;



    public TestModuleAdapter(List<Module> items) {
        this.items = items;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 创建ViewHolder的布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }


    /**
     * 通过ViewHolder将数据绑定到界面上进行显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(items.get(position).getName());
        Module m = items.get(position);
        switch(m.getState()){
            case 0:
                holder.mTextView.setBackgroundColor(Color.parseColor("#e57373"));
                break;
            case 1:
                holder.mTextView.setBackgroundColor(Color.parseColor("#81c784"));
                break;
            case 2:
                holder.mTextView.setBackgroundColor(Color.parseColor("#e6e6e6"));
                break;
            case 3:
                holder.mTextView.setBackgroundColor(Color.parseColor("#64b5f6"));
                break;

        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //这里初始化要展示的数据
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.module_textView);
        }
    }






}
