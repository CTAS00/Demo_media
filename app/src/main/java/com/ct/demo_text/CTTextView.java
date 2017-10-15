package com.ct.demo_text;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by koudai_nick on 2017/10/14.
 */

public class CTTextView extends TextView {
    // 获取到有多少个文字

    public CTTextView(Context context) {
        super(context);
    }

    public CTTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CTTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CTTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
