package com.nj.ts.autotest.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nj.ts.autotest.util.FontCache;

import java.util.List;

/**
 * Created by ts on 17-9-30.
 */

public class SpinnerAdapter extends ArrayAdapter<String>{

    private Context mContext;
    private List<String> mStr;
    public SpinnerAdapter(Context context, List<String> str){
        super(context, android.R.layout.simple_spinner_dropdown_item,str);
        mContext = context;
        mStr = str;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        text.setText(mStr.get(position));
        text.setTextSize(26f);
        Typeface typeFace = FontCache.getTypeface("fonts/DS-DIGII.TTF", getContext());
        text.setTypeface(typeFace);

        //text.setBackgroundColor(Color.GREEN);
        //text.setTextColor(Color.WHITE);
        return convertView;
    }
}
