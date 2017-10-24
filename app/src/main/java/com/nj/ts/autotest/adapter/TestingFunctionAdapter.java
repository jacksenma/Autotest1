package com.nj.ts.autotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.entity.TestResult;

import java.util.ArrayList;

public class TestingFunctionAdapter extends RecyclerView.Adapter<TestingFunctionAdapter.TestingFunctionViewHolder>{
    private Context mContext;
    private ArrayList<TestResult> mTestResules;

    public TestingFunctionAdapter(ArrayList<TestResult> testResults, Context context) {
        this.mTestResules = testResults;
        this.mContext = context;
    }

    @Override
    public TestingFunctionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_testing_function, viewGroup, false);
        TestingFunctionViewHolder vh = new TestingFunctionViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(TestingFunctionViewHolder viewHolder, int position) {
        TestResult testResult = mTestResules.get(position);
        viewHolder.mFuctionTextView.setText(testResult.getMethod());
        viewHolder.mResultTextView.setText(testResult.getResultMessage());

        if (testResult.getResultCode() == 0) {
            viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.test_item_success_bg));
        } else {
            viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.test_item_fail_bg));
        }
    }

    @Override
    public int getItemCount() {
        return mTestResules.size();
    }

    public static class TestingFunctionViewHolder extends RecyclerView.ViewHolder {
        public TextView mFuctionTextView;
        public TextView mResultTextView;

        public TestingFunctionViewHolder(View view) {
            super(view);
            mFuctionTextView = (TextView) view.findViewById(R.id.tv_function_name);
            mResultTextView = (TextView) view.findViewById(R.id.tv_function_result);
        }
    }
}
