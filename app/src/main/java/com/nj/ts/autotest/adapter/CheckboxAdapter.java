package com.nj.ts.autotest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.Module;

import java.util.List;

/**
 * Created by ts on 17-9-28.
 */

public class CheckboxAdapter extends RecyclerView.Adapter<CheckboxAdapter.ViewHolder> implements View.OnClickListener{
    private List<Module> items;
    private OnItemClickListener mOnItemClickListener = null;



    public CheckboxAdapter(List<Module> items) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_item, parent, false);
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
        holder.mCheckBox.setText(items.get(position).getName());
        holder.itemView.setTag(position);
        holder.mCheckBox.setChecked(items.get(position).isChecked());
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = items.get(position).isChecked();

                if(isChecked){
                    items.get(position).setChecked(false);
                }else{
                    items.get(position).setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //这里初始化要展示的数据
        public CheckBox mCheckBox;
        public ViewHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_textView);
        }
    }

    public List<Module> getList(){
        return items;
    }





}
