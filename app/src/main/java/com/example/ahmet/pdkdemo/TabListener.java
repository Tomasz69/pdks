package com.example.ahmet.pdkdemo;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;


public class TabListener implements ActionBar.TabListener {

    private Fragment fragment;

    public TabListener(Fragment fragment) {
        this.fragment = fragment;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.frame_main, fragment);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
