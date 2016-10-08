package com.example.qi.teacheronline.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/3/6.
 */
public class Noscrollview extends ListView {
    public Noscrollview(Context context) {
        super(context);
    }
    public Noscrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Noscrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
