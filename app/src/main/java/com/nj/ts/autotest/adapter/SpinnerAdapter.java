package com.nj.ts.autotest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nj.ts.autotest.entity.Project;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private static final String TAG = SpinnerAdapter.class.getSimpleName();
    private Context mContext;
    private List<Project> mProjects;

    public SpinnerAdapter(Context context, List<Project> projects) {
        mContext = context;
        mProjects = projects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Project project = mProjects.get(position);
        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        text.setText(project.getProject());
        text.setTextSize(24f);
//        Typeface typeFace = FontCache.getTypeface("fonts/DS-DIGII.TTF", getContext());
//        text.setTypeface(typeFace);

        //text.setBackgroundColor(Color.GREEN);
        text.setTextColor(Color.WHITE);
        return convertView;
    }

    @Override
    public int getCount() {
        return mProjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mProjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
