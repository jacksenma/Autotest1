package com.nj.ts.autotest.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.RuanModule;
import com.nj.ts.autotest.entity.TestResult;

import java.util.ArrayList;

public class TestResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = TestResultAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;

    private Context mContext;
    private ArrayList<Object> mResults;
    private ArrayList<RuanModule> mModuleArrayList;

    public TestResultAdapter(ArrayList<RuanModule> moduleArrayList, ArrayList<Object> results, Context context) {
        this.mResults = results;
        this.mContext = context;
        this.mModuleArrayList = moduleArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_header, parent, false);
            return new TestResultHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_content, parent, false);
            return new TestResultContentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TestResultHeaderViewHolder) {
            ((TestResultHeaderViewHolder) holder).mProjectTextView.setText(String.valueOf(mResults.get(position)));
            for (int i = 0; i < mModuleArrayList.size(); i++) {
                RuanModule module = mModuleArrayList.get(i);
                if (module.getName().equals(mResults.get(position))) {
                    if (module.isAllSuccess()) {
                        ((TestResultHeaderViewHolder) holder).mProjectTextView.setTextColor(Color.GREEN);
                    } else {
                        ((TestResultHeaderViewHolder) holder).mProjectTextView.setTextColor(Color.RED);
                    }
                }
            }

        } else if (holder instanceof TestResultContentViewHolder) {
            TestResult result = (TestResult) mResults.get(position);

            ((TestResultContentViewHolder) holder).mFunctionTextView.setText(result.getMethod());
            ((TestResultContentViewHolder) holder).mResultTextView.setText(result.getResultMessage());
            if (result.getResultCode() == 0) {
                ((TestResultContentViewHolder) holder).mFunctionTextView.setTextColor(Color.GREEN);
                ((TestResultContentViewHolder) holder).mResultTextView.setTextColor(Color.GREEN);
            } else {
                ((TestResultContentViewHolder) holder).mFunctionTextView.setTextColor(Color.RED);
                ((TestResultContentViewHolder) holder).mResultTextView.setTextColor(Color.RED);
            }

        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "ruan getItemCount and results size is " + mResults.size());
        return mResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        Object object = mResults.get(position);
        if (object instanceof String) {
            type = 0;
        } else {
            type = 1;
        }
        return type;
    }

    public class TestResultHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView mProjectTextView;

        public TestResultHeaderViewHolder(View itemView) {
            super(itemView);
            mProjectTextView = (TextView) itemView.findViewById(R.id.tv_project_name);
        }
    }

    public class TestResultContentViewHolder extends RecyclerView.ViewHolder {
        public TextView mFunctionTextView;
        public TextView mResultTextView;

        public TestResultContentViewHolder(View itemView) {
            super(itemView);
            mFunctionTextView = (TextView) itemView.findViewById(R.id.tv_function_name);
            mResultTextView = (TextView) itemView.findViewById(R.id.tv_function_result);
        }
    }
}
