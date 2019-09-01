package com.sgc.clock.ui.createNewAlarmClock;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sgc.clock.ui.alarmClock.alarmClockFragment.TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE;
import static com.sgc.clock.ui.alarmClock.alarmClockFragment.TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK;

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
    @BindView(R.id.title)
    TextView mainTitle;

    private TextView hoursLastTextView;
    private TextView minutesLastTextView;

    private LinearSnapHelper hoursSnapHelper = new LinearSnapHelper();
    private LinearSnapHelper minutesSnapHelper = new LinearSnapHelper();

    private RecyclerView.LayoutManager hoursLayoutManager = new LinearLayoutManager(this);
    private RecyclerView.LayoutManager minutesLayoutManager = new LinearLayoutManager(this);

    private int startAlarmClockOffset = 8;
    private int hoursPosition = 2400 + startAlarmClockOffset;
    private int minutesPosition = 2400;

    private String alarmClockDescription = "Будильник";
    private boolean isChangeAlarmClock = false;
    private int alarmClockChangeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_alarm_clock);

        ButterKnife.bind(this);

        TimeSelectAdapter adapterHours = new TimeSelectAdapter(this, 24, 1, 1);
        TimeSelectAdapter adapterMinutes = new TimeSelectAdapter(this, 60, 1, 2);

        loadInstanceState(savedInstanceState);
        loadAlarmClockFromIntent();

        installationRecyclerView(
                hours, hoursSnapHelper, hoursLayoutManager,
                adapterHours, recyclerViewScrollListenerHours, hoursPosition);

        installationRecyclerView(
                minutes, minutesSnapHelper, minutesLayoutManager,
                adapterMinutes, recyclerViewScrollListenerMinutes, minutesPosition);
    }

    private void loadAlarmClockFromIntent() {
        Intent intent = getIntent();
        int alarmClockId = intent.getIntExtra(TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK, -1);
        String title = intent.getStringExtra(TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE);

        if (alarmClockId != -1) {
            showAlarmClockFromIntent(alarmClockId);
            alarmClockChangeId = alarmClockId;
            isChangeAlarmClock = true;
        }

        if (title != null)
            mainTitle.setText(title);

    }

    private void showAlarmClockFromIntent(int id) {
        AlarmClockDataBaseHelper alarmClockDataBaseHelper = AlarmClockDataBaseHelper.getInstance(getApplicationContext());
        AlarmClock alarmClock = alarmClockDataBaseHelper.getAlarmClock(id);

        showAlarmClockTimeFromIntent(alarmClock);
        showAlarmClockDescription(alarmClock);
    }

    private void showAlarmClockTimeFromIntent(AlarmClock alarmClock) {
        int alarmClockTimeHours;
        int alarmClockTimeMinutes;

        String[] alarmClockTime = alarmClock.getAlarmClockTime().split(" : ");
        if (alarmClockTime.length == 2) {
            alarmClockTimeHours = Integer.parseInt(alarmClockTime[0]);
            alarmClockTimeMinutes = Integer.parseInt(alarmClockTime[1]);
        } else
            throw new RuntimeException("alarm clock time wrong format. Incomprehensible format : " + Arrays.toString(alarmClockTime));

        hoursPosition = hoursPosition - startAlarmClockOffset + alarmClockTimeHours;
        minutesPosition = minutesPosition + alarmClockTimeMinutes;
    }

    private void showAlarmClockDescription(AlarmClock alarmClock) {
        String alarmClockName = alarmClock.getAlarmClockName();
        this.alarmClockName.setText(alarmClockName);
        alarmClockDescription = alarmClockName;
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
            alarmClockDescription = savedInstanceState.getString("description");
            alarmClockName.setText(alarmClockDescription);
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
        AlarmClockDataBaseHelper alarmClockDataBaseHelper = AlarmClockDataBaseHelper.getInstance(this);

        String hours = hoursLastTextView.getText().toString();
        String minutes = String.format("%02d", Integer.parseInt(minutesLastTextView.getText().toString()));
        String alarmTime = hours + " : " + minutes;

        if (!isChangeAlarmClock)
            alarmClockDataBaseHelper.addAlarmClockToDataBase(new AlarmClock(alarmClockDescription, alarmTime, "Пн - Пт", true));
        else
            alarmClockDataBaseHelper.updateAlarmClockToDataBase(new AlarmClock(alarmClockChangeId, alarmClockDescription, alarmTime, "Пн - Пт", true));

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
        description.setText(alarmClockDescription);
    }

    private void changeAlarmClockDescription(EditText description, AlertDialog alertDialog) {
        alarmClockDescription = description.getText().toString();
        alertDialog.cancel();
        alarmClockName.setText(alarmClockDescription);
    }

    private AlertDialog createChangeDescriptionAlertDialog(View promptsView) {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setView(promptsView);
        AlertDialog alertDialog = mDialogBuilder.create();
        return alertDialog;
    }

}
