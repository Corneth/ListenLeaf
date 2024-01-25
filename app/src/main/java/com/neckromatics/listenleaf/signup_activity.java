package com.neckromatics.listenleaf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class signup_activity extends AppCompatActivity {
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passwordEditText;

    Button signUp;
    Button logIn;
    ImageButton backButt;




    // Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currUser;

    // Firebase Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);


        signUp = findViewById(R.id.signUpButt);
        logIn = findViewById(R.id.loginButt);

        emailEditText = findViewById(R.id.emailBox);
        passwordEditText = findViewById(R.id.passwordBox);
        firstNameEditText = findViewById(R.id.firstNameBox);
        lastNameEditText = findViewById((R.id.lastNameBox));

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //We need to listen for changes in authentication whether the user is signed in and then signed out or
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currUser = firebaseAuth.getCurrentUser();

                //check if user is logged in or not
                if (currUser != null) {
                    //already logged in
                }else{
                    //user is signed out

                }
            }
        };

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailEditText.getText().toString())
                        && !TextUtils.isEmpty(passwordEditText.getText().toString())
                        && !TextUtils.isEmpty(firstNameEditText.getText().toString())
                        && !TextUtils.isEmpty(emailEditText.getText().toString())
                )
                {
                    String email =  emailEditText.getText().toString().trim();
                    String password =  passwordEditText.getText().toString().trim();
                    String firstName =  firstNameEditText.getText().toString().trim();
                    String lastName =  lastNameEditText.getText().toString().trim();

                    CreateUser(email,password,firstName,lastName);

                }
                else
                {
                    Toast.makeText(signup_activity.this," Error No Fields Can Be Empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup_activity.this,Login_activity.class));
            }
        });

        backButt = findViewById(R.id.backButt);

        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup_activity.this,Account_Page.class));
            }
        });



    }

    public void CreateUser(String email, String password, String firstName,String lastName)
    {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName))
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(signup_activity.this,"Reader is Created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(signup_activity.this,Login_activity.class));
                    }
                    else
                    {
                        ///
                    }
                }
            });
        }
    }



}