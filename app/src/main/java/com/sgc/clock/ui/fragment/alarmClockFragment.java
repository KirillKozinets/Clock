package com.sgc.clock.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgc.clock.R;
import com.sgc.clock.adapter.AlarmClockListAdapter;
import com.sgc.clock.db.AlarmClockDataBaseHelper;
import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.ui.activity.createNewAlarmClockActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.sgc.clock.util.Constants.TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE;
import static com.sgc.clock.util.Constants.TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK;

public class alarmClockFragment extends Fragment implements AlarmClockListAdapter.AlarmClockClickListener {


    @BindView(R.id.alarmList)
    RecyclerView alarmList;
    Unbinder unbinder;



    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    AlarmClockListAdapter alarmClockListAdapter;

    final int REQUEST_CHANGE_ALARM_CLOCK = 1;
    final int REQUEST_NEW_ALARM_CLOCK = 2;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();
        AlarmClockDataBaseHelper.getInstance(getContext()).getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employees -> {
                    if (getActivity() != null) {
                        if (alarmClockListAdapter == null) {
                            alarmClockListAdapter = new AlarmClockListAdapter(getActivity(), (ArrayList<AlarmClock>) employees, this);
                            alarmList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            alarmList.setAdapter(alarmClockListAdapter);
                        }
                        alarmClockListAdapter.setData((ArrayList<AlarmClock>) employees);
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
        getActivity().startActivityForResult(intent, REQUEST_NEW_ALARM_CLOCK);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }

    @Override
    public void alarmClockItemClick(int alarmClockId) {
        Intent intent = new Intent(getActivity(), createNewAlarmClockActivity.class);
        intent.putExtra(TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK, alarmClockId);
        intent.putExtra(TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE, "Изменить");
        getActivity().startActivityForResult(intent, REQUEST_CHANGE_ALARM_CLOCK);
    }


}
