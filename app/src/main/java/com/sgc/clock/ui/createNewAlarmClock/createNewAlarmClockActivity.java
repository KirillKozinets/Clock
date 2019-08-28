package com.sgc.clock.ui.createNewAlarmClock;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sgc.clock.R;
import com.sgc.clock.model.AlarmClock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class createNewAlarmClockActivity extends AppCompatActivity {

    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.addAlarmClock)
    Button addAlarmClock;
    @BindView(R.id.hours)
    RecyclerView hours;
    @BindView(R.id.minutes)
    RecyclerView minutes;

    private TextView hoursLastTextView;
    private TextView minutesLastTextView;

    private LinearSnapHelper hoursSnapHelper = new LinearSnapHelper();
    private LinearSnapHelper minutesSnapHelper = new LinearSnapHelper();

    private RecyclerView.LayoutManager hoursLayoutManager = new LinearLayoutManager(this);
    private RecyclerView.LayoutManager minutesLayoutManager = new LinearLayoutManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_alarm_clock);

        ButterKnife.bind(this);

        TimeSelectAdapter adapterHours = new TimeSelectAdapter(this, 24, 1);
        TimeSelectAdapter adapterMinutes = new TimeSelectAdapter(this, 60, 1);

        hoursSnapHelper.attachToRecyclerView(hours);
        minutesSnapHelper.attachToRecyclerView(minutes);

        hours.setLayoutManager(hoursLayoutManager);
        minutes.setLayoutManager(minutesLayoutManager);

        hours.setAdapter(adapterHours);
        minutes.setAdapter(adapterMinutes);

        hours.addOnScrollListener(recyclerViewScrollListenerHours);
        minutes.addOnScrollListener(recyclerViewScrollListenerMinutes);

        if (savedInstanceState != null) {
            int hoursPosition =  savedInstanceState.getInt("hours");
            int minutesPosition = savedInstanceState.getInt("minutes");
            hours.scrollToPosition(hoursPosition);
            minutes.scrollToPosition(minutesPosition);
            hours.post(() -> toTargetPosition(hoursLayoutManager, hoursSnapHelper, hours, hoursPosition));
            minutes.post(() -> toTargetPosition(minutesLayoutManager, minutesSnapHelper, minutes, minutesPosition));
        } else {
            hours.scrollToPosition(2408);
            minutes.scrollToPosition(2400);
            hours.post(() -> toTargetPosition(hoursLayoutManager, hoursSnapHelper, hours, 2408));
            minutes.post(() -> toTargetPosition(minutesLayoutManager, minutesSnapHelper, minutes, 2400));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("hours", hoursLayoutManager.getPosition(hoursLastTextView));
        outState.putInt("minutes", minutesLayoutManager.getPosition(minutesLastTextView));
        super.onSaveInstanceState(outState);
    }

    private void toTargetPosition(RecyclerView.LayoutManager LayoutManager, LinearSnapHelper snap, RecyclerView recyclerView, int position) {
        View view = LayoutManager.findViewByPosition(position);
        if (view != null) {

            int[] snapDistance = snap.calculateDistanceToFinalSnap(LayoutManager, view);

            if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                recyclerView.scrollBy(snapDistance[0], snapDistance[1]);
            }

        }
    }

    RecyclerView.OnScrollListener recyclerViewScrollListenerHours = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            hoursLastTextView = setSelectColor(hoursLastTextView, hoursSnapHelper, hoursLayoutManager);
        }
    };

    RecyclerView.OnScrollListener recyclerViewScrollListenerMinutes = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            minutesLastTextView = setSelectColor(minutesLastTextView, minutesSnapHelper, minutesLayoutManager);
        }
    };

    @SuppressLint("ResourceType")
    private TextView setSelectColor(TextView lastText, LinearSnapHelper snapHelper, RecyclerView.LayoutManager manager) {
        TextView centerView = (TextView) snapHelper.findSnapView(manager);
        if (lastText != null)
            lastText.setTextColor(Color.BLACK);
        int blueColor = Color.parseColor(getResources().getString(R.color.blue));
        (centerView).setTextColor(blueColor);
        (centerView).setTextColor(Color.parseColor("#2853e0"));
        return centerView;
    }

    @OnClick(R.id.addAlarmClock)
    public void addNewAlarmClock() {

    }

    @OnClick(R.id.cancel)
    public void backToClockActivity() {
        onBackPressed();
    }
}
