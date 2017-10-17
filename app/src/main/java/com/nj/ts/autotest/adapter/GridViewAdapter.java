package com.nj.ts.autotest.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.Function;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Function> {

    private Context context;
    private int layoutResourceId;
    private List<Function> data = new ArrayList<Function>();

    public GridViewAdapter(Context context, int layoutResourceId, List<Function> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.itemTitle = (TextView) row.findViewById(R.id.text);
            holder.itemResult = (TextView) row.findViewById(R.id.result);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        Function item = data.get(position);
        updateItem(holder, item);
        return row;
    }

    public void updateItem(ViewHolder holder, Function item) {
        int state = item.getState();
        holder.itemTitle.setText(item.getName());
        switch (state) {
            case 0:
                holder.itemResult.setText("测试失败");
                break;
            case 1:
                holder.itemResult.setText("测试成功");
                break;
            case 2:
                holder.itemResult.setText("待测试");
                break;
            case 3:
                holder.itemResult.setText("正在测试");
                break;
            default:
        }
    }

    public class ViewHolder {
        TextView itemTitle;
        TextView itemResult;
    }
}
