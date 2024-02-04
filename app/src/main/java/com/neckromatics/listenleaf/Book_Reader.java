package com.neckromatics.listenleaf;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.FirebaseApp;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Book_Reader extends AppCompatActivity {
    private static final String TAG = "Book_Reader";
    public static String FILE_NAME = "downloaded_file.pdf";
    private TextToSpeech textToSpeech;
    private int currentPageIndex = 0;
    private ViewPager pdfViewPager;
    private PdfPageAdapter pdfPageAdapter;
    private List<String> pageTexts;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_reader_activity);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Retrieve the URI from the intent
        String uri = getIntent().getStringExtra("uri");
        String title = getIntent().getStringExtra("Title");
        FILE_NAME = title +".pdf";
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing file...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Initialize the TextToSpeech engine
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Set the language for speech synthesis
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "TextToSpeech: Language not supported");
                }
            } else {
                Log.e(TAG, "TextToSpeech: Initialization failed");
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                // Go to the next page when the engine finishes speaking
                currentPageIndex++;
                if (currentPageIndex < pageTexts.size()) {
                    readPage(currentPageIndex);
                }
            }

            @Override
            public void onError(String utteranceId) {
            }
        });
        // Set up the Read Aloud button
        Button readButton = findViewById(R.id.readButton);
        readButton.setOnClickListener(view -> {
            if (textToSpeech.isSpeaking()) {
                // Stop speaking and jump to the last page that was being read
                textToSpeech.stop();
                pdfViewPager.setCurrentItem(currentPageIndex);
            } else {
                // Start reading from the current page index
                readPage(currentPageIndex);
            }
        });

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            InputStream inputStream = null;
            try {
                File file = new File(getFilesDir(),FILE_NAME);
                if (file.exists()) {
                    // The file is already downloaded, read it in this thread
                    readPdfFile(file);
                }
                else{
                URL url = new URL(uri);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.getInputStream();

                    // Save the downloaded file locally
                    File outputFile = new File(getFilesDir(), FILE_NAME);
                    try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }

                    // Load the PDF using iText
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        try {
                            PdfReader reader = new PdfReader(outputFile.getAbsolutePath());
                            int pageCount = reader.getNumberOfPages();
                            Log.e("pages:", "" + pageCount);
                            pageTexts = new ArrayList<>();

                            // Extract text from each page of the PDF
                            for (int i = 1; i <= pageCount; i++) {
                                String pageText = PdfTextExtractor.getTextFromPage(reader, i);
                                if(!pageText.trim().startsWith("Page | " ) || pageText.trim().isEmpty()){

                                pageTexts.add(pageText);}
                            }

                            // Close the PDF reader
                            reader.close();

                            // Set up the ViewPager
                            pdfViewPager = findViewById(R.id.pdfViewPager);
                            pdfPageAdapter = new PdfPageAdapter(getSupportFragmentManager(), pageTexts);
                            pdfViewPager.setAdapter(pdfPageAdapter);
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            pdfViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                }

                                @Override
                                public void onPageSelected(int position) {
                                    // Update the current page index when the page changes
                                    currentPageIndex = position;
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                }
                            });
                        } catch (IOException e) {
                            Log.e(TAG, "PDF Load Error: " + Objects.requireNonNull(e.getMessage()));
                            e.printStackTrace();
                        }
                    });
                } else {
                    // Log unsuccessful response
                    Log.i(TAG, "Retrieve PDF from URL - HTTP response code: " + urlConnection.getResponseCode());
                }}
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();
            }
        });
    }

    private void readPage(int pageIndex) {
        if (pageIndex >= 0 && pageIndex < pageTexts.size()) {
            String pageText = pageTexts.get(pageIndex);
            if (!TextUtils.isEmpty(pageText)) {
                // Speak the page text aloud
                if(textToSpeech.isSpeaking()){
                    textToSpeech.stop();
                }
                else {
                    textToSpeech.speak(pageText, TextToSpeech.QUEUE_ADD, null, null);
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the TextToSpeech engine
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void readPdfFile(File file) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            try {
                PdfReader reader = new PdfReader(file.getAbsolutePath());
                int pageCount = reader.getNumberOfPages();
                Log.e("pages:", "" + pageCount);
                pageTexts = new ArrayList<>();

                // Extract text from each page of the PDF
                for (int i = 1; i <= pageCount; i++) {
                    String pageText = PdfTextExtractor.getTextFromPage(reader, i);
                    if(!pageText.trim().startsWith("Page | " ) || pageText.trim().isEmpty()){
                        pageTexts.add(pageText);
                    }
                }

                // Close the PDF reader
                reader.close();

                // Set up the ViewPager
                pdfViewPager = findViewById(R.id.pdfViewPager);
                pdfPageAdapter = new PdfPageAdapter(getSupportFragmentManager(), pageTexts);
                pdfViewPager.setAdapter(pdfPageAdapter);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                pdfViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        // Update the current page index when the page changes
                        currentPageIndex = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "PDF Load Error: " + Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
        });
    }
}
