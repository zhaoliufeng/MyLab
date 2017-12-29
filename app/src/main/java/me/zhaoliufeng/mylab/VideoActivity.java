package me.zhaoliufeng.mylab;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.VideoView;

/**
 * Created by zhaol on 2017/12/13.
 */

public class VideoActivity extends Activity{

    private VideoView videoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.video);
        Uri rawUri = Uri.parse(String.format("android.resource://%s/%s", getPackageName(), R.raw.step));
        videoView.setVideoURI(rawUri);
        videoView.start();
    }
}
