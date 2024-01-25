package com.neckromatics.listenleaf;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class Account_Page extends AppCompatActivity {
    VideoView videoBackground;
    Button logIn;


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

        logIn = findViewById(R.id.loginButtonAP);

        ImageButton goToSignUpV = findViewById(R.id.addNewAccountImgButt);
        goToSignUpV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToSignUp();
            };
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account_Page.this,Login_activity.class));

            }
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
        Intent intent = new Intent(Account_Page.this, signup_activity.class);
        startActivity(intent);
    }
}