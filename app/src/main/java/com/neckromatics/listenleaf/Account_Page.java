package com.neckromatics.listenleaf;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class Account_Page extends AppCompatActivity {
    VideoView videoBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
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


        ImageButton goToSignUpV = findViewById(R.id.addNewAccountImgButt);
        goToSignUpV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToSignUp();
            };
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        // to restart the video after coming from other activity
        videoBackground.start();
    }

    public void GoToSignUp()
    {
        Intent intent = new Intent(this, signup_activity.class);
        startActivity(intent);
    }
}