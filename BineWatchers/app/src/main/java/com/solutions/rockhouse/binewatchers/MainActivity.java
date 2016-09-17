package com.solutions.rockhouse.binewatchers;

import android.app.ActionBar;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends FragmentActivity implements IPointsConsumerListener{

    // Declare Variables
//    ActionBar mActionBar;
    ViewPager mPager;
  //  ActionBar.Tab tabCalculator;
  //  ActionBar.Tab tabDayCount;
  //  ActionBar.Tab tabWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init preference provider
        PreferenceProvider.getInstance().setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(this));

        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);

        // Activate Navigation Mode Tabs
    //    mActionBar = getActionBar();
        //mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Locate ViewPager in activity_main.xml
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(2);
        // Activate Fragment Manager
        FragmentManager fm = getSupportFragmentManager();

        // Capture ViewPager page swipes
        ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Find the ViewPager Position
      //          mActionBar.setSelectedNavigationItem(position);
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        };

        mPager.setOnPageChangeListener(ViewPagerListener);
        // Locate the adapter class called ViewPagerAdapter.java
        ViewPageAdapter viewpageradapter = new ViewPageAdapter(fm);
        // Set the View Pager Adapter into ViewPager
        mPager.setAdapter(viewpageradapter);

        // Capture tab button clicks
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabReselected(ActionBar.Tab tab,
                                        android.app.FragmentTransaction arg1) {
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab,
                                      android.app.FragmentTransaction arg1) {
                // Pass the position on tab click to ViewPager
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab arg0,
                                        android.app.FragmentTransaction arg1) {
                // TODO Auto-generated method stub

            }
        };

        // Create first Tab
        /*tabCalculator = mActionBar.newTab().setText("Calculator").setTabListener(tabListener);
        mActionBar.addTab(tabCalculator);

        // Create second Tab
        tabDayCount = mActionBar.newTab().setText("DayCount").setTabListener(tabListener);
        mActionBar.addTab(tabDayCount);

        // Create third Tab
        tabWeight = mActionBar.newTab().setText("Weight").setTabListener(tabListener);
        mActionBar.addTab(tabWeight);*/
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
    }


    @Override
    public void consumePoints(double amount) {
        //FragmentTabDayCount daycountFrag = (FragmentTabDayCount)
        //      getSupportFragmentManager().findFragmentByTag("DayCount");

        FragmentTabDayCount daycountFrag = (FragmentTabDayCount) getSupportFragmentManager().findFragmentByTag(
                "android:switcher:"+R.id.pager+":1");


        if (daycountFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            daycountFrag.consumePoints(amount);
         //   mActionBar.setSelectedNavigationItem(tabDayCount.getPosition());
        }
    }


}

