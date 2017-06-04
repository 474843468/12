package com.example.taojin.qq12.adapter;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

/**
 * Created by taojin on 2016/9/9.11:31
 */
public abstract  class TabSelectedListenerAdapter implements BottomNavigationBar.OnTabSelectedListener {
    @Override
    public abstract void onTabSelected(int position) ;

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
