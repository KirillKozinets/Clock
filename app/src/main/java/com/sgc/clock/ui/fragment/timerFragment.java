package com.sgc.clock.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sgc.clock.R;
import com.sgc.clock.adapter.TimeSelectAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sgc.clock.util.RecyclerViewUtil.installationRecyclerView;
import static com.sgc.clock.util.RecyclerViewUtil.setSelectColor;
import static com.sgc.clock.util.RecyclerViewUtil.toTargetPosition;

public class timerFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.seconds)
    RecyclerView seconds;
    @BindView(R.id.minutes)
    RecyclerView minutes;
    @BindView(R.id.hours)
    RecyclerView hours;
    Unbinder unbinder;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    private TextView hoursLastTextView;
    private TextView minutesLastTextView;
    private TextView secondsLastTextView;

    private LinearSnapHelper hoursSnapHelper = new LinearSnapHelper();
    private LinearSnapHelper minutesSnapHelper = new LinearSnapHelper();
    private LinearSnapHelper secondsSnapHelper = new LinearSnapHelper();

    private RecyclerView.LayoutManager hoursLayoutManager;
    private RecyclerView.LayoutManager minutesLayoutManager;
    private RecyclerView.LayoutManager secondsLayoutManager;

    private int startHoursPosition = 200;
    private int startMinutesPosition = 240;
    private int startSecondsPosition = 240;

    private int saveHoursPosition = 200;
    private int savMinutesPosition = 240;
    private int savSecondsPosition = 240;


    private boolean activeInstrumentsMenu = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        hoursLayoutManager = new LinearLayoutManager(getContext());
        minutesLayoutManager = new LinearLayoutManager(getContext());
        secondsLayoutManager = new LinearLayoutManager(getContext());

        TimeSelectAdapter adapterHours = new TimeSelectAdapter(getContext(), 100, 1, 2);
        TimeSelectAdapter adapterMinutes = new TimeSelectAdapter(getContext(), 60, 1, 2);
        TimeSelectAdapter adapterSeconds = new TimeSelectAdapter(getContext(), 60, 1, 2);

        adapterHours.setTextColor(Color.DKGRAY);
        adapterMinutes.setTextColor(Color.DKGRAY);
        adapterSeconds.setTextColor(Color.DKGRAY);

        adapterHours.setTextSize(40);
        adapterMinutes.setTextSize(40);
        adapterSeconds.setTextSize(40);

        loadInstanceState(savedInstanceState);

        installationRecyclerView(
                hours, hoursSnapHelper, hoursLayoutManager,
                adapterHours, recyclerViewScrollListenerHours, saveHoursPosition);

        installationRecyclerView(
                minutes, minutesSnapHelper, minutesLayoutManager,
                adapterMinutes, recyclerViewScrollListenerMinutes, savMinutesPosition);

        installationRecyclerView(
                seconds, secondsSnapHelper, secondsLayoutManager,
                adapterSeconds, recyclerViewScrollListenerSeconds, savSecondsPosition);

        checkZeroTime();

        return view;
    }

    RecyclerView.OnScrollListener recyclerViewScrollListenerHours = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            hoursLastTextView = setSelectColor(hoursLastTextView, hoursSnapHelper,
                    hoursLayoutManager, getActivity().getApplicationContext());
            checkZeroTime();
        }
    };

    RecyclerView.OnScrollListener recyclerViewScrollListenerMinutes = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            minutesLastTextView = setSelectColor(minutesLastTextView, minutesSnapHelper,
                    minutesLayoutManager, getActivity().getApplicationContext());
            checkZeroTime();
        }
    };

    RecyclerView.OnScrollListener recyclerViewScrollListenerSeconds = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            secondsLastTextView = setSelectColor(secondsLastTextView, secondsSnapHelper,
                    secondsLayoutManager, getActivity().getApplicationContext());
            checkZeroTime();
        }
    };

    private void checkZeroTime() {
        if (hoursLastTextView != null && minutesLastTextView != null && secondsLastTextView != null) {
            boolean isNeedChangeMenu = false;

            if (hoursLastTextView.getText().equals("00") &&
                    minutesLastTextView.getText().equals("00") &&
                    secondsLastTextView.getText().equals("00")) {
                if (activeInstrumentsMenu) {
                    activeInstrumentsMenu = false;
                    isNeedChangeMenu = true;
                }
            } else if (!activeInstrumentsMenu) {
                activeInstrumentsMenu = true;
                isNeedChangeMenu = true;
            }

            if (isNeedChangeMenu)
                setActiveInstrumentsMenu(activeInstrumentsMenu);
        }
    }

    private void setActiveInstrumentsMenu(boolean active) {
        bottomNavigation.getMenu().setGroupVisible(R.id.active_group, active);
        bottomNavigation.getMenu().setGroupVisible(R.id.no_active_group, !active);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.stop:
                toTargetPosition(secondsLayoutManager, secondsSnapHelper,seconds, startSecondsPosition);
                toTargetPosition(minutesLayoutManager,minutesSnapHelper,minutes, startMinutesPosition);
                toTargetPosition(hoursLayoutManager,hoursSnapHelper,hours, startHoursPosition);
                break;
            case R.id.start:

                break;
        }
        return true;
    }

    private void loadInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            saveHoursPosition = savedInstanceState.getInt("hours");
            savMinutesPosition = savedInstanceState.getInt("minutes");
            savSecondsPosition = savedInstanceState.getInt("seconds");
            activeInstrumentsMenu = savedInstanceState.getBoolean("activeInstrumentsMenu");
            setActiveInstrumentsMenu(activeInstrumentsMenu);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("seconds", secondsLayoutManager.getPosition(secondsLastTextView));
        outState.putInt("hours", hoursLayoutManager.getPosition(hoursLastTextView));
        outState.putInt("minutes", minutesLayoutManager.getPosition(minutesLastTextView));
        outState.putBoolean("activeInstrumentsMenu", activeInstrumentsMenu);
        super.onSaveInstanceState(outState);
    }
}
