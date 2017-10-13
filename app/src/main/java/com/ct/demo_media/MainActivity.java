package com.ct.demo_media;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button2;
//    private MediaPlayer mMediaplayer;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button2 = (Button) findViewById(R.id.button2);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        MediaPlayer.getInstance().initContext(this);
        MediaPlayer.getInstance().initSurfaceview(surfaceView);
    }
    public void startRecord(View v){
        // 开始录制的功能
        MediaPlayer.getInstance().startRecord();
    }
    public void stopRecord(View v){
        MediaPlayer.getInstance().stopRecord();
    }

    public void playRecord(View v){
        MediaPlayer.getInstance().playRecord();
    }
}
