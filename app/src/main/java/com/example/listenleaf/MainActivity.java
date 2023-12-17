//package com.example.listenleaf;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.widget.VideoView;
//
//public class MainActivity extends AppCompatActivity {
//    VideoView videoBackground;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        videoBackground = findViewById(R.id.video_background);
//        videoBackground.setMediaController(null);
//        videoBackground.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.video_background);
//        videoBackground.start();
//        videoBackground.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
//
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        // to restart the video after coming from other activity
//        videoBackground.start();
//    }
//
//}