package com.neckromatics.listenleaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_activity extends AppCompatActivity {
    VideoView videoBackground;
    Button login_btn,signup_btn;
    TextInputLayout textInputLayoutUsername, textInputLayoutPassword;
    TextView Username, Password,Forgot_password;
    CheckBox saveLoginCheckbox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveLogin;

    // Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // firebase connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        // Creation of login saving information so we don't have to keep logging in every time we open
        // the app based off the remember me checkbox
        loginPreferences = getSharedPreferences("saveLogin",MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin",false);
        if (saveLogin) {
            Username.setText(loginPreferences.getString("username", ""));
            Password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckbox.setChecked(true);
        }

        // view finders
        saveLoginCheckbox = findViewById(R.id.Remember_Me);
        login_btn = findViewById(R.id.btn_login);
        signup_btn = findViewById(R.id.signup_btn);
        textInputLayoutUsername = findViewById(R.id.inputLayoutUsername);
        textInputLayoutPassword = findViewById(R.id.inputLayoutpassword);
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.password);
        videoBackground = findViewById(R.id.t_background);
        Forgot_password = findViewById(R.id.Forgot_password);



        videoBackground.setMediaController(null);
        videoBackground.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.t_background);
        videoBackground.start();


        Forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Forgot Password?" click event here
                // Add any functionality you need, such as navigating to a password recovery screen
                Toast.makeText(Login_activity.this, "Sending an email to verify new password.",
                        Toast.LENGTH_SHORT).show();

            }
        });
        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Listening for changes in the authentication state and responds accordingly
        // when the state changes
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                // Check if the user is logged in or not
                if (currentUser != null) {
                    // User already logged in
                    Intent i = new Intent(Login_activity.this, Homepage_activity.class);
                    startActivity(i);
                    finish();
                } else {
                    // The user signed out

                }
            }
        };

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logEmailPassUser(Username.getText().toString().trim(), Password.getText().toString().trim());
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_activity.this, signup_activity.class);
                startActivity(i);
                finish();
            }
        });

        videoBackground.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


    }

    private void logEmailPassUser(String username, String password) {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Intent i = new Intent(Login_activity.this, Homepage_activity.class);
                                startActivity(i);
                                finish(); // Optional: finish the current activity to prevent going back
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login_activity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Handle the case where either username or password is empty
            Toast.makeText(Login_activity.this, "Username and password are required.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video after coming from another activity
        videoBackground.start();
    }



}
