package com.sgc.clock.ui.createNewAlarmClock;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sgc.clock.R;
import com.sgc.clock.db.AlarmClockDataBaseHelper;
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
    @BindView(R.id.alarmClockName)
    ConstraintLayout alarmClockNameChange;
    @BindView(R.id.description)
    TextView alarmClockName;

    private TextView hoursLastTextView;
    private TextView minutesLastTextView;

    private LinearSnapHelper hoursSnapHelper = new LinearSnapHelper();
    private LinearSnapHelper minutesSnapHelper = new LinearSnapHelper();

    private RecyclerView.LayoutManager hoursLayoutManager = new LinearLayoutManager(this);
    private RecyclerView.LayoutManager minutesLayoutManager = new LinearLayoutManager(this);

    private int hoursPosition = 2408;
    private int minutesPosition = 2400;

    String description = "Будильник";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_alarm_clock);

        ButterKnife.bind(this);

        TimeSelectAdapter adapterHours = new TimeSelectAdapter(this, 24, 1, 1);
        TimeSelectAdapter adapterMinutes = new TimeSelectAdapter(this, 60, 1, 2);

        loadInstanceState(savedInstanceState);

        installationRecyclerView(
                hours, hoursSnapHelper, hoursLayoutManager,
                adapterHours, recyclerViewScrollListenerHours, hoursPosition);

        installationRecyclerView(
                minutes, minutesSnapHelper, minutesLayoutManager,
                adapterMinutes, recyclerViewScrollListenerMinutes, minutesPosition);
    }


    private void installationRecyclerView(RecyclerView recyclerView, LinearSnapHelper snapHelper,
                                          RecyclerView.LayoutManager layoutManager, TimeSelectAdapter adapter,
                                          RecyclerView.OnScrollListener scrollListener, int position) {

        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.scrollToPosition(position);
        recyclerView.post(() -> toTargetPosition(layoutManager, snapHelper, recyclerView, position));
    }

    private void loadInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            hoursPosition = savedInstanceState.getInt("hours");
            minutesPosition = savedInstanceState.getInt("minutes");
            description = savedInstanceState.getString("description");
            alarmClockName.setText(description);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("description", alarmClockName.getText().toString());
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

    @SuppressLint("DefaultLocale")
    @OnClick(R.id.addAlarmClock)
    public void addNewAlarmClock() {
        String hours = hoursLastTextView.getText().toString();
        String minutes = String.format("%02d", Integer.parseInt(minutesLastTextView.getText().toString()));
        String alarmTime = hours + " : " + minutes;

        AlarmClockDataBaseHelper.getInstance(this).addAlarmClockToDataBase(new AlarmClock(description, alarmTime, "Пн - Пт", true));
        onBackPressed();
    }

    @OnClick(R.id.cancel)
    public void backToClockActivity() {
        onBackPressed();
    }

    @OnClick(R.id.alarmClockName)
    public void setAlarmClockName() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.getalarmclockname, null);

        EditText description = promptsView.findViewById(R.id.description);
        Button ok = promptsView.findViewById(R.id.OK);
        Button cancel = promptsView.findViewById(R.id.cancel);

        AlertDialog alertDialog = createChangeDescriptionAlertDialog(promptsView);
        alertDialog.show();

        ok.setOnClickListener(view -> changeAlarmClockDescription(description, alertDialog));
        cancel.setOnClickListener(view -> alertDialog.cancel());
    }

    private void changeAlarmClockDescription(EditText description, AlertDialog alertDialog) {
        this.description = description.getText().toString();
        alertDialog.cancel();
        alarmClockName.setText(this.description);
    }

    private AlertDialog createChangeDescriptionAlertDialog(View promptsView) {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setView(promptsView);
        AlertDialog alertDialog = mDialogBuilder.create();
        return alertDialog;
    }

}
