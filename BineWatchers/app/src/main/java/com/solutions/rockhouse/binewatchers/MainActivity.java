package com.solutions.rockhouse.binewatchers;

import android.app.ActionBar;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements IPointsConsumerListener{

    // Declare Variables
    ActionBar mActionBar;
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

        // add toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // create toolbar tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Calculator"));
        tabLayout.addTab(tabLayout.newTab().setText("DayCount"));
        tabLayout.addTab(tabLayout.newTab().setText("Weight"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Locate ViewPager in activity_main.xml
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(2);
        // Activate Fragment Manager
        FragmentManager fm = getSupportFragmentManager();

        // send page swipes to tab indicator
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set the View Pager Adapter into ViewPager
        mPager.setAdapter(new ViewPageAdapter(fm));

        // if tab clicked - do swipe
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

