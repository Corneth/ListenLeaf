package com.neckromatics.listenleaf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class signup_activity extends AppCompatActivity {
    VideoView videoBackground;
    Button signup_btn;
    TextInputEditText Username, Password, Email;


    // Firebase Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // Firebase Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        // View Finders
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.password);
        Email = findViewById(R.id.Email);
        signup_btn = findViewById(R.id.btn_signup);
        videoBackground = findViewById(R.id.t_background);




        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                // Check the user if logged in or not
                if(currentUser != null){
                    // user already logged in
                }else{
                    // user is signed out
                }
            }
        };
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(Email.getText().toString()) &&
                        !TextUtils.isEmpty(Username.getText().toString()) &&
                        !TextUtils.isEmpty(Password.getText().toString())
                ){
                    String email = Email.getText().toString().trim();
                    String pass = Password.getText().toString().trim();
                    String username = Username.getText().toString().trim();
                    CreateUserEmailAccount(email,pass,username);
                }
                else{
                    Toast.makeText(signup_activity.this
                            ,"Required fields not filled out",Toast.LENGTH_SHORT).show();
                }
            }
        });


        videoBackground.setMediaController(null);
        videoBackground.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.t_background);
        videoBackground.start();

        
    }
    private void CreateUserEmailAccount(String email, String pass, String username){
        if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(username)){
            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // The user is created successfully!
                        Toast.makeText(signup_activity.this,"Account is created successfully"
                                ,Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(signup_activity.this, Login_activity.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(signup_activity.this,"Error trying to create account"
                                ,Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseAuth","Error creating account",task.getException());
                    }
                }
            });
        }
    }
}