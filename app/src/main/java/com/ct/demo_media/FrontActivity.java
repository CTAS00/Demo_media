package com.ct.demo_media;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.ct.demo_text.SecondaryActivity;

/**
 * Created by koudai_nick on 2017/10/13.
 */

public class FrontActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_activityview);
    }
    public void startCamera(View v){
        MainActivity.gotoMain(this);
    }
    public void startRecord(View v){
        SecondaryActivity.gotoSecondary(this);
    }




}
