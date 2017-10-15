//package com.ct.demo_media;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.SurfaceTexture;
//import android.media.*;
//import android.media.MediaPlayer;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.Surface;
//import android.view.TextureView;
//import android.view.View;
//
///**
// * Created by CTAS on 2017/10/15.
// */
//public class TextMedia extends View implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, TextureView.SurfaceTextureListener {
//
//    // ===================================================================
//    private static final int STATE_ERROR = -1;//加载失败的状态
//    private static final int STATE_IDLE = 0;//初始状态
//    private static final int STATE_PLAYING = 1;//播放中的状态
//    private static final int STATE_PAUSING = 2;//暂停中的状态
//    //加载会存在失败的情况下
//    private static final int LOAD_TOTAL_COUNT = 3;
//
//    private int mCurrentCount = 0;
//
//    private int playerState = STATE_IDLE;//当前处于原始的状态
//
//
//    private android.media.MediaPlayer mediaPlayer;
//
//
//    private TextureView mTextureView;
//
//
//    private Surface videoSurface;//真正显示视频数据的类
//
//    public TextMedia(Context context) {
//        super(context);
//    }
//
//    public TextMedia(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public TextMedia(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public TextMedia(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
//
//    // 只有在load的时候才会去加载一个播放器
//    public void load(String url){
//        if (this.playerState != STATE_IDLE) {
//            return ;
//        }
//        try {
//            //1.展示加载时候的状态 2.改变状态
//            setCurrentPlayState(STATE_IDLE);
//            // 第一次去加载
//            checkMediaPlayer();
//            mediaPlayer.setDataSource(url);
//            mediaPlayer.prepareAsync();
//        }catch (Exception e){
//            stop();
//        }
//    }
//
//
//    public void text(){
//        mTextureView.setSurfaceTextureListener(this);
//    }
//
//
//    public void setCurrentPlayState(int state){
//        playerState = state;
//    }
//
//
//    /**
//     * 避免多线程创建多个类
//     */
//    private synchronized void checkMediaPlayer() {
//        if (mediaPlayer == null) {
//            mediaPlayer = createMediaPlayer(); //每次都重新创建一个新的播放器
//        }
//    }
//
//    private android.media.MediaPlayer createMediaPlayer() {
//        mediaPlayer = new android.media.MediaPlayer();
//        mediaPlayer.reset();
//        mediaPlayer.setOnPreparedListener(this);
//        mediaPlayer.setOnCompletionListener(this);
//        mediaPlayer.setOnInfoListener(this);
//        mediaPlayer.setOnErrorListener(this);
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        if (videoSurface != null && videoSurface.isValid()) {
//            mediaPlayer.setSurface(videoSurface);
//        } else {
//            stop();
//        }
//        return mediaPlayer;
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        mediaPlayer = mp;
//        if (mediaPlayer!=null){
//            // 加载成功以后可以监听缓存进度
//            mediaPlayer.setOnBufferingUpdateListener(this);
//            // 前面的状态边为空
//            mCurrentCount = 0;
//            // 可以通知外部  我们的视频已经加载成功了
////            if(litener!=null){
////                listener.onVideoLoadSuccess();
////            }
//
//            //  准备好以后是进度播放还是暂停状态
//            decideCanPlay();
//
//
//
//        }
//
//    }
//
//    /**
//     * 是否可以去播放视频
//     */
//    private void decideCanPlay() {
//
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        // 播放完成的功能
//        if(listener!=null){
//            listener.onVideoPlayerComplete();
//        }
//        // 回到初始状态
//        playBack();
//
//    }
//
//    private void playBack() {
//        setCurrentPlayState(STATE_PAUSING);
//        // 讲一些handler的处理要全部去掉
//        if(mediaPlayer!=null){
//            mediaPlayer.setOnSeekCompleteListener(null);
//            mediaPlayer.seekTo(0);
//            mediaPlayer.pause();
//        }
//        // 一些ui层面的修改
//        showPauseOrPlayView();
//    }
//
//    private void showPauseOrPlayView() {
//    }
//
//    @Override
//    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//        return false;
//    }
//
//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        // 返回true 由自己去处理
//        setCurrentPlayState(STATE_ERROR);
//        if(mCurrentCount>=LOAD_TOTAL_COUNT){
//            if(listener!=null){
//                listener.onVideoLoadFailed();
//            }
//            showPauseOrPlayView();
//        }
//        stop();// 释放资源
//        return true;
//    }
//
//    /**
//     * 清空状态 释放资源
//     */
//    public void stop() {
//        // 清空掉我们当前的mediaplayer
//        Log.e("CTAS", "do stop");
//        if (this.mediaPlayer!=null){
//            this.mediaPlayer.reset();
//            this.mediaPlayer.setOnSeekCompleteListener(null);
//            this.mediaPlayer.stop();
//            this.mediaPlayer.release();
//            this.mediaPlayer = null;
//        }
//        // 回到最原始的状态
//        setCurrentPlayState(STATE_IDLE);
//        // 去重新load
//        if(mCurrentCount < LOAD_TOTAL_COUNT){
//            mCurrentCount+=1;
//            load("url");
//        } else{
//            // 显示暂停中的界面  并且提示当前的视频加载不了
//        }
//
//    }
//
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//
//    }
//
//
//    /**
//     * 恢复视频的播放  统一而言 就是播放视频的状态
//     */
//    public void resume(){
//        if(playerState!=STATE_PAUSING){
//            return ;
//        }
//        if(!isPlaying()){
//            //1. 首先是要对界面进行处理  变为播放时候的状态值
//            //2.
//            mediaPlayer.start();
//        }
//
//    }
//
//
//    public boolean isPlaying(){
//        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
//            return true;
//        }
//        return false;
//    }
//
//    public void onpause(){
//        if (this.playerState != STATE_PLAYING){
//            return ;
//        }
//        setCurrentPlayState(STATE_PLAYING);
//        if(isPlaying()){
//            // 真正的完成暂停
//            mediaPlayer.pause();
//
//        }
//        showPauseOrPlayView();
//        handler.removecallbackandmessage(null);
//
//    }
//
//    /**
//     * 只有在textureview准备好以后才会去触发这个视频的加载
//     * @param surface
//     * @param width
//     * @param height
//     */
//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//      videoSurface = new Surface(surface);
//        load("url");
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        return false;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//    }
//
//
//    //  对于锁屏的处理
//
//    private class ScreenEventRecever extends BroadcastReceiver{
//
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            switch (intent.getAction()){
//                case Intent.ACTION_SCREEN_OFF:
//                    if(playerState == STATE_PLAYING){
//                        onpause();
//                    }
//
//                        break;
//
//                case Intent.ACTION_USER_PRESENT:
//                    if(playerState === STATE_PAUSING) {
//                        if("已经播放结束了"){
//                            onpause();
//
//                        }
//                    } else{
//                        decideCanPlay();
//
//                    }
//
//            }       }
//    }
//
//    // 界面状态改变的时候
//    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//
//        if(visibility==VISIBLE && playerState == STATE_PAUSING){
//            // 决定是否播放
//            if('已经播放完成以后'){
//                onpause();
//            }else{
//                decideCanPlay();
//            }
//        }else{
//            onpause();
//        }
//    }
//
//
//    public void seekAndPause(int position){
//        if(this.playerState != STATE_PLAYING){
//            return ;
//        }
//
//        showPauseOrPlayView();
//        setCurrentPlayState(STATE_PAUSING);
//        if(isPlaying()){
//            mediaPlayer.seekTo(position);
//            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//                @Override
//                public void onSeekComplete(MediaPlayer mp) {
//                    mediaPlayer.pause();
//                    handler.removemessage(null);
//                }
//            });
//
//        }
//
//
//    }
//}
