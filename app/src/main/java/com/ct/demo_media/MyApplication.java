package com.ct.demo_media;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by koudai_nick on 2017/10/13.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59e08a10");
//        SpeechSynthesizer speechSynthesizer  =SpeechSynthesizer.getSynthesizer();
//
//
//        speechSynthesizer.setParameter( SpeechConstant.ENGINE_TYPE, engineType );
//        speechSynthesizer.setParameter( SpeechConstant.ENGINE_MODE, engineMode );
//
//        if( SpeechConstant.TYPE_LOCAL.equals(engineType)
//                &&SpeechConstant.MODE_MSC.equals(engineMode) ){
//            // 需下载使用对应的离线合成SDK
//            speechSynthesizer.setParameter( ResourceUtil.TTS_RES_PATH, ttsResPath );
//        }
//
//        mTts.setParameter( SpeechConstant.VOICE_NAME, voiceName );
//
//        final String strTextToSpeech = "科大讯飞，让世界聆听我们的声音";
//        mTts.startSpeaking( strTextToSpeech, mSynListener );
    }
}
