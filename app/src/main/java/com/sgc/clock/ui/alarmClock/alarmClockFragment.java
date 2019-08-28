package com.sgc.clock.ui.alarmClock;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgc.clock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class alarmClockFragment extends Fragment {


    public alarmClockFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_clock, container, false);

        return view;
    }

}
