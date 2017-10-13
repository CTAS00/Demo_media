package com.ct.demo_media;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by koudai_nick on 2017/10/13.
 */

public class Util {

    public static File getFileByName(String fileName, Context context){
        File parent;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            parent =  Environment.getExternalStorageDirectory();

        }else {
            parent = context.getCacheDir();
        }
//        String fileName = Md5Utils.generateCode(url+"5555");
        File file = new File(parent,fileName);
        if(!file.exists()){
            try {
                //这个是创建文件  并不是创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                file = null;
            }
        }
//        Log.i(TAG,file.getAbsolutePath());
        return file;
    }
}
