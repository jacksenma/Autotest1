package com.nj.ts.autotest.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by ts on 17-10-16.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView{
    public MyTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        AssetManager mgr=context.getAssets();//得到AssetManager
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/DS-DIGII.TTF");//根据路径得到Typeface
//        tv.setTypeface(tf);
//        Typeface customFont = FontCache.getTypeface("DS-DIGII.TTF", context);
        setTypeface(tf);
    }


}
