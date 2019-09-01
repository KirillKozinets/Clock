package com.sgc.clock.ui.alarmClock;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sgc.clock.R;
import com.sgc.clock.db.AlarmClockDataBaseHelper;
import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.ui.createNewAlarmClock.createNewAlarmClockActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class alarmClockFragment extends Fragment implements AlarmClockListAdapter.AlarmClockClickListener {


    @BindView(R.id.alarmList)
    RecyclerView alarmList;
    Unbinder unbinder;

    public static final String TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK = "changeId";
    public static final String TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE = "createAlarmClockActivityTitle";
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    public alarmClockFragment() {

    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_clock, container, false);
        unbinder = ButterKnife.bind(this, view);

        bottomNavigation.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.add_alarm_clock:
                            startCreateAlarmClockActivity();
                    }
                    return true;
                });

        return view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();
        AlarmClockDataBaseHelper.getInstance(getContext()).getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employees -> {
                    if (getActivity() != null) {
                        AlarmClockListAdapter alarmClockListAdapter = new AlarmClockListAdapter(getActivity(), (ArrayList<AlarmClock>) employees, this);
                        alarmList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        alarmList.setAdapter(alarmClockListAdapter);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void startCreateAlarmClockActivity() {
        Intent intent = new Intent(getActivity(), createNewAlarmClockActivity.class);
        startActivity(intent);
    }

    @Override
    public void alarmClockItemClick(int alarmClockId) {
        Intent intent = new Intent(getActivity(), createNewAlarmClockActivity.class);
        intent.putExtra(TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK, alarmClockId);
        intent.putExtra(TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE, "Изменить");
        startActivity(intent);
    }
}
