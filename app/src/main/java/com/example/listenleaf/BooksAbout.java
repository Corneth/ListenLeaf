package com.example.listenleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BooksAbout extends AppCompatActivity {

    String _title;
    String _author;
    String _summary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_about);

        TextView title = findViewById(R.id.title);
        title.setText(_title);
        TextView author = findViewById(R.id.author);
        author.setText(_author);
        TextView summary = findViewById(R.id.summary);
        summary.setText(_summary);

        Button backbtn = findViewById(R.id.button2);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}