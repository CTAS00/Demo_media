package com.ct.demo_media;

import android.content.Context;

import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by koudai_nick on 2017/10/13.
 * 管理类
 */

public class MediaPlayer implements SurfaceHolder.Callback, MediaRecorder.OnInfoListener {
    private static final String TAG = "CTAS";
    public static MediaPlayer mMediaplayer = new MediaPlayer();
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private android.media.MediaPlayer mSysMediaPlayer ;
    public static MediaPlayer getInstance(){
        return mMediaplayer;
    }
    private MediaRecorder mMediaRecorder;
    private MediaPlayer (){
        mMediaRecorder = new MediaRecorder();
//        initMeidaRecorderSettings(mMediaRecorder);
        mSysMediaPlayer = new android.media.MediaPlayer();
    }
    private Camera mCamera;
    private Context mContext;
    //  几个状态值设置 是否正在录制视频
    private boolean isRecord = false;

    private String path;

    public void initContext(Context context){
        this.mContext = context;
    }
    public void initSurfaceview( SurfaceView surfaceView){
        mSurfaceView = surfaceView;
        mSurfaceHolder = surfaceView.getHolder();
        // 设置参数
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mSurfaceHolder.addCallback(this);
        // 必须设置
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    // 设置参数
    private void initMeidaRecorderSettings(MediaRecorder mRecorder) {
        try {

            // 主要解决屏幕不是竖直状态的处理
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            if (mCamera != null) {
                mCamera.setDisplayOrientation(90);
                mCamera.unlock();
                mRecorder.setCamera(mCamera);
            }
            // 这两项需要放在setOutputFormat之前
            mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // Set output file format
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            // 这两项需要放在setOutputFormat之后
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

            mRecorder.setVideoSize(640, 480);
            mRecorder.setVideoFrameRate(30);
            mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
            mRecorder.setOrientationHint(90);
            //设置记录会话的最大持续时间（毫秒）
            mRecorder.setMaxDuration(30 * 1000);
//          mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
    }
    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒

        String date = "" + year + (month + 1) + day + hour + minute + second;
        Log.d(TAG, "date:" + date);

        return date;
    }
    public void startRecord(){
        if (!isRecord) {
            try {
                isRecord = true;
                initMeidaRecorderSettings(mMediaRecorder);
                mMediaRecorder.setOutputFile(Util.getFileByName("qiancheng",mContext) + "/" + getDate() + ".mp4");
                path = Util.getFileByName("qiancheng",mContext) + "/" + getDate() + ".mp4";
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                ToastUtils.s(mContext,"开始录制视频");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
                isRecord = false;
            }
        }
    }
    /**
     * 暂停录制
     */
    public void stopRecord(){
        // 在录制的时候的处理
        if (isRecord) {
            try {
                mMediaRecorder.stop();
//                mMediaRecorder.reset();
//                mMediaRecorder.release();
                isRecord = false;
                if (mCamera !=null) {
                    mCamera.release();
                    mCamera = null;
                }
                ToastUtils.s(mContext,"已经暂停处理");
            }catch (Exception e){
                Log.e(TAG,"stopRecord" + e.getMessage());
            }
        }
    }

    // 播放视频
    public void playRecord() {
        mSysMediaPlayer.reset();
        Uri uri = Uri.parse(path);
        mSysMediaPlayer = mSysMediaPlayer.create(mContext, uri);
        mSysMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mSysMediaPlayer.setDisplay(mSurfaceHolder);
        try{
            mSysMediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        mSysMediaPlayer.start();

    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = surfaceHolder;
    }
    /**
     * 类似于界面被销毁
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceView = null;
        mSurfaceHolder = null;
        if (mMediaRecorder !=null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
            Log.e(TAG,"surfaceDestroyed release mMediaRecorder");
        }
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }
}
