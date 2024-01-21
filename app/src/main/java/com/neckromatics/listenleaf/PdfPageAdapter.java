package com.neckromatics.listenleaf;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class PdfPageAdapter extends FragmentPagerAdapter {
    private List<String> pageTexts;

    public PdfPageAdapter(@NonNull FragmentManager fm, List<String> pageTexts) {
        super(fm);
        this.pageTexts = pageTexts;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PdfPageFragment.newInstance(pageTexts.get(position));
    }

    @Override
    public int getCount() {
        return pageTexts.size();
    }
}
