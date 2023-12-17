package com.neckromatics.listenleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

public class HomePage extends AppCompatActivity {
    VideoView videoBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        videoBackground = findViewById(R.id.homevideoView);
        videoBackground.setMediaController(null);
        videoBackground.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.forestmountainview);
        videoBackground.start();
        videoBackground.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        // to restart the video after coming from other activity
        videoBackground.start();
    }
}