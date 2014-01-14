package com.example.binewatchers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPageAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
    final int PAGE_COUNT = 3;
 
    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {
 
        // Open FragmentTab1.java
        case 0:
            FragmentTabCalculator fragmenttab1 = new FragmentTabCalculator();
            return fragmenttab1;
 
        // Open FragmentTab2.java
        case 1:
            FragmentTabCalculator fragmenttab2 = new FragmentTabCalculator();
            return fragmenttab2;
 
        // Open FragmentTab3.java
        case 2:
        	FragmentTabCalculator fragmenttab3 = new FragmentTabCalculator();
            return fragmenttab3;
        }
        return null;
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return PAGE_COUNT;
    }
 
}