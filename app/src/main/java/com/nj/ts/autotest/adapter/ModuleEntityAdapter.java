package com.nj.ts.autotest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.Module;
import com.nj.ts.autotest.util.Util;
import com.nj.ts.autotest.viewHolder.DescHolder;
import com.nj.ts.autotest.viewHolder.HeaderHolder;

import java.util.List;

public class ModuleEntityAdapter extends SectionedRecyclerViewAdapter<HeaderHolder, DescHolder, RecyclerView.ViewHolder> {


    public List<Module> mAllModules;
    private Context mContext;
    private LayoutInflater mInflater;

    private SparseBooleanArray mBooleanMap;

    public ModuleEntityAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBooleanMap = new SparseBooleanArray();
    }

    public void setData(List<Module> allModules) {
        mAllModules = allModules;
        notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        return Util.isEmpty(mAllModules) ? 0 : mAllModules.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int count = mAllModules.get(section).getFunctions().size();
//        if (count >= 8 && !mBooleanMap.get(section)) {
//            count = 8;
//        }

        return Util.isEmpty(mAllModules.get(section).getFunctions()) ? 0 : count;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(mInflater.inflate(R.layout.module_title_item, parent, false));
    }


    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected DescHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new DescHolder(mInflater.inflate(R.layout.module_desc_item, parent, false));
    }


    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderHolder holder, final int section) {
//        holder.openView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isOpen = mBooleanMap.get(section);
//                String text = isOpen ? "展开" : "关闭";
//                mBooleanMap.put(section, !isOpen);
//                holder.openView.setText(text);
//                notifyDataSetChanged();
//            }
//        });

        Module module = mAllModules.get(section);
        holder.titleView.setText(module.getName());
//        if (Util.isEmpty(module.getFunctions()) || module.getFunctions().size() < 9) {
//            holder.openView.setVisibility(View.GONE);
//        } else {
//            holder.openView.setVisibility(View.VISIBLE);
//            holder.openView.setText(mBooleanMap.get(section) ? "关闭" : "展开");
//        }
        updateTextColor(module.getState(), holder.titleView);
    }


    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(DescHolder holder, int section, int position) {
        holder.descView.setText(mAllModules.get(section).getFunctions().get(position).getName());
        holder.messageView.setText(mAllModules.get(section).getFunctions().get(position).getTag());

        updateTextColor(mAllModules.get(section).getFunctions().get(position).getState(), holder.descView);

    }

    private void updateTextColor(int state, TextView textView) {
        switch (state) {
            case 0:
                textView.setTextColor(Color.RED);
                break;
            case 1:
                textView.setTextColor(Color.GREEN);
                break;
            default:
                textView.setTextColor(Color.BLACK);
                break;
        }
    }

}
