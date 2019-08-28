package com.sgc.clock.ui.clock;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sgc.clock.ui.alarmClock.alarmClockFragment;

public class viewPagerAdapter extends FragmentPagerAdapter {

    private Fragment fragments[] = new Fragment[]{
        new alarmClockFragment()
    };

    public viewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }


    @Override
    public int getCount() {
        return fragments.length;
    }
}
