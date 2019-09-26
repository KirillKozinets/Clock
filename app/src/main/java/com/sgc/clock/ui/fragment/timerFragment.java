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

public class timerFragment extends Fragment {
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
    private TextView secundsLastTextView;

    private LinearSnapHelper hoursSnapHelper = new LinearSnapHelper();
    private LinearSnapHelper minutesSnapHelper = new LinearSnapHelper();
    private LinearSnapHelper secundsSnapHelper = new LinearSnapHelper();

    private RecyclerView.LayoutManager hoursLayoutManager;
    private RecyclerView.LayoutManager minutesLayoutManager;
    private RecyclerView.LayoutManager secundsLayoutManager;

    public static timerFragment newInstance() {
        return new timerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        hoursLayoutManager = new LinearLayoutManager(getContext());
        minutesLayoutManager = new LinearLayoutManager(getContext());
        secundsLayoutManager = new LinearLayoutManager(getContext());

        TimeSelectAdapter adapterHours = new TimeSelectAdapter(getContext(), 100, 1, 2);
        TimeSelectAdapter adapterMinutes = new TimeSelectAdapter(getContext(), 60, 1, 2);
        TimeSelectAdapter adapterSeconds = new TimeSelectAdapter(getContext(), 60, 1, 2);

        adapterHours.setTextColor(Color.DKGRAY);
        adapterMinutes.setTextColor(Color.DKGRAY);
        adapterSeconds.setTextColor(Color.DKGRAY);

        adapterHours.setTextSize(40);
        adapterMinutes.setTextSize(40);
        adapterSeconds.setTextSize(40);

        installationRecyclerView(
                hours, hoursSnapHelper, hoursLayoutManager,
                adapterHours, recyclerViewScrollListenerHours, 200);

        installationRecyclerView(
                minutes, minutesSnapHelper, minutesLayoutManager,
                adapterMinutes, recyclerViewScrollListenerMinutes, 240);

        installationRecyclerView(
                seconds, secundsSnapHelper, secundsLayoutManager,
                adapterSeconds, recyclerViewScrollListenerSeconds, 240);

        bottomNavigation.setItemIconTintList(null);

        return view;
    }

    RecyclerView.OnScrollListener recyclerViewScrollListenerHours = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            hoursLastTextView = setSelectColor(hoursLastTextView, hoursSnapHelper,
                    hoursLayoutManager, getActivity().getApplicationContext());
        }
    };

    RecyclerView.OnScrollListener recyclerViewScrollListenerMinutes = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            minutesLastTextView = setSelectColor(minutesLastTextView, minutesSnapHelper,
                    minutesLayoutManager, getActivity().getApplicationContext());
        }
    };

    RecyclerView.OnScrollListener recyclerViewScrollListenerSeconds = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            secundsLastTextView = setSelectColor(secundsLastTextView, secundsSnapHelper,
                    secundsLayoutManager, getActivity().getApplicationContext());
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
