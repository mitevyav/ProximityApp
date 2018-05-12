package com.example.yavor.proximityapp.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.yavor.proximityapp.R;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    public static final int LIST_FRAGMENT = 0;

    public static final int MAP_FRAGMENT = 1;

    private static final int FRAGMENTS_COUNT = 2;

    private Context context;

    public FragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case LIST_FRAGMENT:
                return new LocationListFragment();
            case MAP_FRAGMENT:
                return new MapFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case LIST_FRAGMENT:
                return context.getString(R.string.map_fragment_label);
            case MAP_FRAGMENT:
                return context.getString(R.string.list_fragment_label);
            default:
                return null;
        }
    }

}
