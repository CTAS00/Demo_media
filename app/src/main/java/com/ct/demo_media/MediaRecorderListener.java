package com.ct.demo_media;

import java.io.File;

/**
 * Created by koudai_nick on 2017/10/14.
 */

public interface MediaRecorderListener {
    void start();
    void end(File file);
    void playcomplete();
}
