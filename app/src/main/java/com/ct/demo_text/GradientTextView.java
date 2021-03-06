package com.ct.demo_text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by koudai_nick on 2017/10/14.
 */

public class GradientTextView extends TextView {
    private Paint mPaint;

    private int delta = 15;

    private float mTextHeight;
    private float mTextWidth;

    private PorterDuffXfermode xformode;
    private String mText = "哈哈哈啊哈哈哈";

    public GradientTextView(Context ctx)
    {
        this(ctx,null);
    }


    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xformode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

        initViewAndDatas();

        setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                postIndex = 0;
                postInvalidate();
            }
        });

    }

    public void initViewAndDatas()
    {
        mPaint.setColor(Color.CYAN);
        mPaint.setTextSize(40.0f);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setXfermode(null);
        mPaint.setTextAlign(Paint.Align.LEFT);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom-fontMetrics.descent-fontMetrics.ascent;

//        mTextHeight = 50;
        mTextWidth  = mPaint.measureText(mText);
        //文字精确高度
        Log.e("CTAS","text 高度 =" +mTextHeight + "=====" + "宽度" + mTextWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth;
        int mHeight;
        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mWidth = specSize;
        }
        else
        {
            // 由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight()
                    + 380;
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mWidth = Math.min(desireByImg, specSize);
            } else
                mWidth = desireByImg;
        }
        /***
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSize;
        } else
        {
            int desire = getPaddingTop() + getPaddingBottom()
                    + 80;

            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mHeight = Math.min(desire, specSize);
            } else
            {
                mHeight = desire;
            }
        }

        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    private int postIndex;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        Bitmap srcBitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        Canvas srcCanvas = new Canvas(srcBitmap);
        srcCanvas.drawText(mText, 0, getPaddingTop(), mPaint);

        mPaint.setXfermode(xformode);
        mPaint.setColor(Color.RED);
        RectF rectF = new RectF(0,0,postIndex,mTextHeight);
        srcCanvas.drawRect(rectF, mPaint);
        canvas.drawBitmap(srcBitmap,getPaddingLeft(),getPaddingTop(), null);
        initViewAndDatas();
        if(postIndex<mTextWidth)
        {
            postIndex+=10;
            postInvalidateDelayed(30);
        }
    }

}
