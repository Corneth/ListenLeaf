package com.neckromatics.listenleaf;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PdfPageFragment extends Fragment {
    private static final String ARG_PAGE_TEXT = "page_text";

    private String pageText;

    public PdfPageFragment() {
        // Required empty public constructor
    }

    public static PdfPageFragment newInstance(String pageText) {
        PdfPageFragment fragment = new PdfPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAGE_TEXT, pageText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageText = getArguments().getString(ARG_PAGE_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pdf_page_item, container, false);
        TextView textView = view.findViewById(R.id.pageTextView);
        textView.setText(formatAndRemoveText(pageText));
        textView.setMovementMethod(new ScrollingMovementMethod()); // Enable scrolling
        return view;
    }

    private SpannableString formatAndRemoveText(String text) {
        // Remove specific text patterns
        String cleanedText = text.replaceAll("\\b\\w+\\.jpg \\(\\d+K\\)\\b", "");

        // Remove empty pages
        cleanedText = cleanedText.replaceAll("\\b\\s*\\(\\d+K\\)\\b", "");

        String[] sentences = cleanedText.split("\\.\\s*"); // Split text into sentences after periods
        SpannableString spannableString = new SpannableString(cleanedText);
        int start = 0;
        for (String sentence : sentences) {
            int end = start + sentence.length();
            if (end <= spannableString.length()) { // Check if the end index is within the text length
                spannableString.setSpan(new RelativeSizeSpan(1.25f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            start = end + 2; // Move start to the beginning of the next sentence (including the period and space)
        }
        return spannableString;
    }


}
