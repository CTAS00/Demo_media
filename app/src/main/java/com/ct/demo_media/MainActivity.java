package com.ct.demo_media;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements MediaRecorderListener {
    private Button button2;
//    private MediaPlayer mMediaplayer;
    private SurfaceView surfaceView;

    private Button mStartButton;

    private Button mUpButton;


    private File mFile;

    private ImageView pauseIv;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            // 进过一段时间  停止录制
            MediaPlayer.getInstance().stopRecord();
        }
    };

    public static void gotoMain(Activity activity){
        Intent intent = new Intent(activity,MainActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartButton = (Button) findViewById(R.id.button);
        mUpButton = (Button) findViewById(R.id.upButton);
        button2 = (Button) findViewById(R.id.button2);
        pauseIv = (ImageView) findViewById(R.id.pauseIv);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        MediaPlayer.getInstance().initContext(this);
        MediaPlayer.getInstance().setMediaRecorderListener(this);
        MediaPlayer.getInstance().initSurfaceview(surfaceView);
        // 显示一个弹窗
        showSelfDialog();
    }

    private void showSelfDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("显示弹窗")
                .setMessage("文本内容")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing - it will close on its own
                    }
                })
                .show();

//        new AlertDialog.Builder(TestMsg.this)
//                .setTitle("Alerting Message")
//                .setMessage("Ha Ha!")
//                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //do nothing - it will close on its own
//                    }
//                })
//                .show();
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

    // 上传信息
    public void upRecoder(View v){
        ToastUtils.s(MainActivity.this,"上传视频信息");
    }
    /*8
       1. 将老视频删除
     */
    public void resetRecoder(View v){
        if (mFile!=null){
            // 删除视频
            Log.e("CTAS","删除文件");
            mFile.delete();
        }
    }
    @Override
    public void start() {
        // 录音开始的时候的处理
        mStartButton.setText("录制完成");
        mStartButton.setEnabled(false);
        // 进过一段时间处理以后
        mHandler.sendEmptyMessageDelayed(110,3000);
    }

    @Override
    public void end(File file) {
        mFile = file;
        Log.e("CTAS","end" + "生成了一个视频文件" + mFile.getAbsolutePath());
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setMessage("监测您录制的视频是否可以")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing - it will close on its own
                        // 播放视频
                        // 视频播放完成以后 要有提示上传或者重新录制
                        MediaPlayer.getInstance().playRecord();
                    }
                })
                .show();
    }
    // 播放完成的回调
    @Override
    public void playcomplete() {
        // 显示出来
        mUpButton.setVisibility(View.VISIBLE);
    }
    // 视频切入到后台时候的处理 要停止视频的播放


    @Override
    protected void onResume() {
        super.onResume();
        // 恢复视频的播放
        MediaPlayer.getInstance().resumeMediaPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 暂停视频的播放
        pauseIv.setVisibility(View.VISIBLE);
        MediaPlayer.getInstance().pauseMediaPlayer();
    }
}
