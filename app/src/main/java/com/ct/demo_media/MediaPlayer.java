package com.ct.demo_media;

import android.content.Context;

import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
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

public class MediaPlayer implements SurfaceHolder.Callback, MediaRecorder.OnInfoListener, android.media.MediaPlayer.OnCompletionListener, android.media.MediaPlayer.OnPreparedListener, android.media.MediaPlayer.OnInfoListener, android.media.MediaPlayer.OnErrorListener {
    private static final String TAG = "CTAS";
    public static MediaPlayer mMediaplayer = new MediaPlayer();
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private Surface mSurface;  //真正显示视屏数据的类
    private android.media.MediaPlayer mSysMediaPlayer ;
    public static MediaPlayer getInstance(){
        return mMediaplayer;
    }
    private MediaRecorder mMediaRecorder;
    private MediaPlayer (){
        mMediaRecorder = new MediaRecorder();
//        initMeidaRecorderSettings(mMediaRecorder);
        mSysMediaPlayer = new android.media.MediaPlayer();
        mSysMediaPlayer.setOnCompletionListener(this);
    }
    private Camera mCamera;
    private Context mContext;
    //  几个状态值设置 是否正在录制视频
    private boolean isRecord = false;

    private String path;
    private MediaRecorderListener mMediaRecorderListener ;




    // 视频播放时候的状态
    private static final  String MEDIA_IDLE = "IDLE"; // 初始状态
    private static final  String MEDIA_PLAYING = "PLAYING"; // 播放状态
    private static final  String MEDIA_PAUSED = "PAUSED"; // 暂停状态
    private static final  String MEDIA_STOPPED = "STOPPED"; // 停止状态
    private static final String MEDIA_ERROR = "ERROR"; //失败状态
    private String currentMediaPlayerState = MEDIA_IDLE;


    // 播放到的进度
    private int mProgress;



    // 会加载3次 要是3次还是加载不出来的话 说明有问题
    private int mCourentCount = 0;

    private static final  int TOTAL_COUNT=3;


    public void setMediaRecorderListener(MediaRecorderListener mediaRecorderListener){
        this.mMediaRecorderListener = mediaRecorderListener;
    }

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
        // 在开始录制的状态下进行操作
        if (!isRecord) {
            try {
                isRecord = true;
                initMeidaRecorderSettings(mMediaRecorder);
                mMediaRecorder.setOutputFile(Util.getFileByName("qiancheng",mContext) + "/" + getDate() + ".mp4");
                path = Util.getFileByName("qiancheng",mContext) + "/" + getDate() + ".mp4";
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                ToastUtils.s(mContext,"开始录制视频");
                if (mMediaRecorderListener!=null){
                    mMediaRecorderListener.start();
                }
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
                if (mMediaRecorderListener!=null){
                    File file = new File(path);
                    mMediaRecorderListener.end(file);
                }
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

    // 判断视频是否播放完成
    public boolean isMediaPlayerCompletion(){
        boolean isCompletion = false;
        if (MEDIA_STOPPED.equals(currentMediaPlayerState) || mProgress <= mSysMediaPlayer.getDuration()) {
            isCompletion = true;
        }
        return isCompletion;

    }

    // 暂停视频的播放
    public void pauseMediaPlayer(){
        if (mSysMediaPlayer !=null && MEDIA_PLAYING.equals(currentMediaPlayerState) && mSysMediaPlayer.isPlaying()) {
            currentMediaPlayerState = MEDIA_PAUSED;
            mSysMediaPlayer.stop();
            Log.e("CTAS", "pauseMediaPlayer");
        }
    }
    // 恢复视频的播放
    public void resumeMediaPlayer(){
        if (MEDIA_PAUSED.equals(currentMediaPlayerState)&& mSysMediaPlayer !=null ) {
            currentMediaPlayerState = MEDIA_PLAYING;
            mSysMediaPlayer.start();
            Log.e("CTAS", "resumeMediaPlayer");
            if (!isMediaPlayerCompletion()) {
                Log.e("CTAS", "resumeMediaPlayer   seektoResume");
                playRecord(mProgress);
//                mSysMediaPlayer.seekTo(mProgress);
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
        currentMediaPlayerState = MEDIA_PLAYING;
    }


    // 带有position的播放视频
    public void playRecord(int position) {
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
        mSysMediaPlayer.seekTo(position);
        currentMediaPlayerState = MEDIA_PLAYING;
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        mSurfaceHolder = surfaceHolder;
        mSurface = mSurfaceHolder.getSurface();
        Log.e(TAG,"surfaceCreated surface重新创建");
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
        // 界面回到后台时候的处理
        mProgress = mSysMediaPlayer.getCurrentPosition();
        Log.e(TAG,"当前的播放进度"+ mProgress);
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {

    }

    @Override
    public void onCompletion(android.media.MediaPlayer mediaPlayer) {
        // 视频播放完成状态
        currentMediaPlayerState = MEDIA_STOPPED;
    }


    // 加载mediaplayer的资源
    public void load(){
        if(!MEDIA_IDLE.equals(currentMediaPlayerState)){
            return ;
        }
        try{
            setCurrentMediaPlayerState(MEDIA_IDLE);
            checkMediaPlayer();


        }catch(Exception e){

        }



    }
    //检查mediaplayer是否已经被创建了
    private synchronized void checkMediaPlayer() {
        if(mSysMediaPlayer==null){
            mSysMediaPlayer = createMediaPlayer();
        }
    }

    public android.media.MediaPlayer createMediaPlayer(){
        mSysMediaPlayer = new android.media.MediaPlayer();
        mSysMediaPlayer.reset();
        mSysMediaPlayer.setOnPreparedListener(this);
        mSysMediaPlayer.setOnCompletionListener(this);
        mSysMediaPlayer.setOnInfoListener(this);
        mSysMediaPlayer.setOnErrorListener(this);
        mSysMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (mSurface != null && mSurface.isValid()) {
            mSysMediaPlayer.setSurface(mSurface);
        } else {
            stop();
        }
        return mSysMediaPlayer;



    }

    public void setCurrentMediaPlayerState(String state){
        this.currentMediaPlayerState = state;
    }

    @Override
    public void onPrepared(android.media.MediaPlayer mp) {
        mSysMediaPlayer = mp;
        if(mSysMediaPlayer!=null){
            mCourentCount = 0;
            // 准备好以后就是暂停的状态
            setCurrentMediaPlayerState(MEDIA_PAUSED);
            
            // 去播放视频
            decideCanPlay();
            
            
        }
        

    }
    //是否能够播放视频   可以考虑在wifi环境下去播放视频等等
    private void decideCanPlay() {
        resume();
    }
    // 播放视频的状态
    private void resume() {
        if (!MEDIA_PAUSED.equals(currentMediaPlayerState)){
            return ;
        }
        if(!isPlaying()){
            setCurrentMediaPlayerState(MEDIA_PLAYING);
            mSysMediaPlayer.start();
            // 在播放的状态中有可能出现error
        }
    }


    // 判断当前的mediaplayer是否在播放状态中
    public boolean isPlaying(){
        if(mSysMediaPlayer!=null && mSysMediaPlayer.isPlaying()){
            return true;
        }
        return false;
    }

    @Override
    public boolean onInfo(android.media.MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onError(android.media.MediaPlayer mp, int what, int extra) {
        // 返回true 由自己去处理
        setCurrentMediaPlayerState(MEDIA_ERROR);
        if(mCourentCount>=TOTAL_COUNT){
            // 告诫外界知道
            // 显示视屏加载不了的界面
//            if(listener!=null){
//                listener.onVideoLoadFailed();
//            }
//            showPauseOrPlayView();
        }
        stop();// 释放资源
        return true;
    }

    /**
     * 清空状态 释放资源
     */
    public void stop() {
        // 清空掉我们当前的mediaplayer
        Log.e("CTAS", "do stop");
        if (this.mSysMediaPlayer!=null){
            this.mSysMediaPlayer.reset();
            this.mSysMediaPlayer.setOnSeekCompleteListener(null);
            this.mSysMediaPlayer.stop();
            this.mSysMediaPlayer.release();
            this.mSysMediaPlayer = null;
        }
        // 回到最原始的状态
        setCurrentMediaPlayerState(MEDIA_IDLE);
        // 去重新load
        if(mCourentCount < TOTAL_COUNT){
            mCourentCount+=1;
            load();
        } else{
            // 显示暂停中的界面  并且提示当前的视频加载不了
        }

    }

    public void pause(){
        if(!MEDIA_PLAYING.equals(currentMediaPlayerState)){
            return ;
        }
        setCurrentMediaPlayerState(MEDIA_PLAYING);
        if(isPlaying()){
            mSysMediaPlayer.pause();
        }
        // 展示暂停中的页面



    }

}
