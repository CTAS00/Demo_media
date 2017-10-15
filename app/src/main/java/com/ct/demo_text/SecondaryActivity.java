package com.ct.demo_text;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.ct.demo_media.MainActivity;
import com.ct.demo_media.R;

/**
 * Created by koudai_nick on 2017/10/14.
 *
 */

public class SecondaryActivity extends AppCompatActivity {
    private CTTextView mCtTextView;

    private GradientTextView mGrandientTextView;


    private int textPosition = 1;
    private int lastTextposition = 0;
    private int yushu = 0;
    private int totalTime = 10;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 10秒完成
            int total = textPosition + lastTextposition;
            lastTextposition = total;
            Log.e("CTAS",textPosition + "==");
            changecolor(total);

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondary_activityview);
        mCtTextView = (CTTextView) findViewById(R.id.cttextview);
        mGrandientTextView = (GradientTextView) findViewById(R.id.gradienttv);
//        mGrandientTextView.setText("哈哈哈哈哈啊哈哈哈");
        mCtTextView.setText("本人自愿向中银消费金融申请贷款，提供真实资料，遵守合约，统一上报征信。");
        Log.e("CTAS","字的个数 = "+ mCtTextView.getText().length());
        // 然后每隔一秒中给字设置一个颜色
        mHandler.sendEmptyMessageDelayed(110,1000);
        mathTextCount();
    }

    /**
     * 如
     */
    private void mathTextCount() {
        int textLength = mCtTextView.getText().length();
        yushu = textLength % totalTime;
        textPosition =  textLength /totalTime;
    }

    // 改变颜色  10秒完成
    public void changecolor(int position){
        if (position >mCtTextView.getText().length()){
            Log.e("CTAS","到底啦！！");
            mHandler.removeCallbacksAndMessages(null);
            return ;
        }
        SpannableString spannableString = new SpannableString(mCtTextView.getText());
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0,position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCtTextView.setText(spannableString);
        mHandler.sendEmptyMessageDelayed(110,1000);


    }
    public static void gotoSecondary(Activity activity){
        Intent intent = new Intent(activity,SecondaryActivity.class);
        activity.startActivity(intent);
    }
}
