package com.miniature.dynamiciceland.lock.utils;

import android.media.projection.MediaProjection;
import com.miniature.dynamiciceland.lock.activites.ScreenshotCaptureActivity;

public class screenShotRunable implements Runnable {
    public void run() {
        MediaProjection mediaProjection = ScreenshotCaptureActivity.mediaProjection;
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
    }
}
