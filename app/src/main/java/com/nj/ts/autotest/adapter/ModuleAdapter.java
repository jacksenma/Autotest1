package com.nj.ts.autotest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.RuanModule;
import com.nj.ts.autotest.entity.RuanProject;

import java.util.List;

public class ModuleAdapter extends BaseAdapter {
    private static final String TAG = SpinnerAdapter.class.getSimpleName();
    private Context mContext;
    private List<RuanModule> mModuleList;

    public ModuleAdapter(Context context, List<RuanModule> moduleList) {
        mContext = context;
        mModuleList = moduleList;
    }

    @Override
    public int getCount() {
        return mModuleList.size();
    }

    @Override
    public Object getItem(int position) {
        return mModuleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.listitem, null);
            viewHolder = new ViewHolder();
            viewHolder.itemTextView = (TextView) convertView.findViewById(R.id.X_item_text);
            viewHolder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.X_checkbox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RuanModule module = mModuleList.get(position);
        viewHolder.itemTextView.setText(module.getName());
        viewHolder.itemCheckBox.setChecked(module.isSelect());
        return convertView;
    }

    private class ViewHolder {
        private TextView itemTextView;
        private CheckBox itemCheckBox;
    }
}
