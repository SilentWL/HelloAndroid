package com.example.administrator.mediaplayer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentContainer;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class MainActivity extends AppCompatActivity {
    private TabHost tabHost = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            android.support.v7.app.ActionBar actionBar = getSupportActionBar();

            // Use ViewPager in the support library where possible.
            // At this time, the support library for L is not ready so using the deprecated method
            // to create tabs.
            actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);
            android.support.v7.app.ActionBar.Tab localMp3ListTab = actionBar.newTab().setText("本地mp3列表").setIcon(android.R.drawable.stat_sys_download_done);
            android.support.v7.app.ActionBar.Tab RemoteMp3ListTab = actionBar.newTab().setText("网络mp3列表").setIcon(android.R.drawable.stat_sys_upload_done);

            localMp3ListTab.setTabListener(new FragmentTabListener(LocalMp3ListActivity
                    .newInstance()));
            RemoteMp3ListTab.setTabListener(new FragmentTabListener(Mp3ListActivity
                    .newInstance()));

            actionBar.addTab(localMp3ListTab, 0);
            actionBar.addTab(RemoteMp3ListTab, 1);

//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction transaction = fm.beginTransaction();
//            LocalMp3ListActivity firstFragment = new LocalMp3ListActivity();
//            Mp3ListActivity mp3ListActivity = new Mp3ListActivity();
//
//            transaction.add(R.id.container, mp3ListActivity).commit();


            // Add the fragment to the 'fragment_container' FrameLayout
            //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }
    private static class FragmentTabListener implements android.support.v7.app.ActionBar.TabListener {
        public android.support.v4.app.Fragment fragment;

        public FragmentTabListener(android.support.v4.app.Fragment fragment) {
            this.fragment = fragment;
        }


        @Override
        public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            ft.replace(R.id.container, this.fragment);
        }

        @Override
        public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            ft.remove(fragment);
        }

        @Override
        public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

        }
    }
}

