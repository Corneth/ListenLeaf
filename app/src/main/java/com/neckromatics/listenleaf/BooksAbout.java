package com.neckromatics.listenleaf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BooksAbout extends AppCompatActivity {


    String _author;
    String _summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_about);
        String uri = getIntent().getStringExtra("uri");
        String intent_title = getIntent().getStringExtra("title");
        TextView title = findViewById(R.id.title);
        title.setText(intent_title);
        TextView author = findViewById(R.id.author);
        author.setText(_author);
        TextView summary = findViewById(R.id.summary);
        summary.setText(_summary);
//        Toast toast = Toast.makeText(BooksAbout.this, intent_title, Toast.LENGTH_SHORT);
//        toast.show();
//        Toast toast2 = Toast.makeText(BooksAbout.this, title.getText(), Toast.LENGTH_SHORT);
//        toast2.show();

        Button backbtn = findViewById(R.id.backButton);
        Button readbtn = findViewById(R.id.readButton);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        readbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Book_Reader.class);
                intent.putExtra("uri", uri).putExtra("Title",intent_title);
                v.getContext().startActivity(intent);
            }
        });
    }
}