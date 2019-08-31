package com.sgc.clock.ui.alarmClock;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sgc.clock.R;
import com.sgc.clock.db.AlarmClockDataBaseHelper;
import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.ui.createNewAlarmClock.createNewAlarmClockActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class alarmClockFragment extends Fragment {


    @BindView(R.id.alarmList)
    RecyclerView alarmList;
    Unbinder unbinder;
    @BindView(R.id.create)
    Button create;

    public alarmClockFragment() {

    }


    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_clock, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();
        AlarmClockDataBaseHelper.getInstance(getContext()).getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employees -> {
                    if(getActivity() != null) {
                        AlarmClockListAdapter alarmClockListAdapter = new AlarmClockListAdapter(getActivity(), (ArrayList<AlarmClock>) employees);
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

    @OnClick(R.id.create)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(),createNewAlarmClockActivity.class);
        startActivity(intent);
    }
}
