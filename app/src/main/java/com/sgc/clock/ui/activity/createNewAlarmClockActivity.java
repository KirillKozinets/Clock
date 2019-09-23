package com.sgc.clock.ui.activity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sgc.clock.R;
import com.sgc.clock.adapter.TimeSelectAdapter;
import com.sgc.clock.db.AlarmClockDataBaseHelper;
import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.util.AlarmManagerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sgc.clock.util.Constants.TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE;
import static com.sgc.clock.util.Constants.TAG_SEND_ALARM_CLOCK;
import static com.sgc.clock.util.Constants.TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK;
import static com.sgc.clock.util.RecyclerViewUtil.installationRecyclerView;
import static com.sgc.clock.util.RecyclerViewUtil.setSelectColor;

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
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.dayOfWeek)
    TextView dayOfWeek;

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
    private String alarmClockDaysOfWeek = "Без повторов";
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
            delete.setVisibility(View.VISIBLE);
        }

        if (title != null)
            mainTitle.setText(title);

    }

    private void showAlarmClockFromIntent(int id) {
        AlarmClockDataBaseHelper alarmClockDataBaseHelper = AlarmClockDataBaseHelper.getInstance(getApplicationContext());
        AlarmClock alarmClock = alarmClockDataBaseHelper.getAlarmClock(id);
        if (alarmClock != null) {
            showAlarmClockTimeFromIntent(alarmClock);
            showAlarmClockDescription(alarmClock);
            showAlarmClockDaysOfWeek(alarmClock);
        }
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

    private void showAlarmClockDaysOfWeek(AlarmClock alarmClock) {
        String alarmClockDayOfWeek = alarmClock.getAlarmClockDaysOfWeek();
        this.dayOfWeek.setText(alarmClockDayOfWeek);
        this.alarmClockDaysOfWeek = alarmClockDayOfWeek;
    }


    private void loadInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            hoursPosition = savedInstanceState.getInt("hours");
            minutesPosition = savedInstanceState.getInt("minutes");
            alarmClockDescription = savedInstanceState.getString("description");
            alarmClockDaysOfWeek = savedInstanceState.getString("daysOfWeek");
            alarmClockName.setText(alarmClockDescription);
            dayOfWeek.setText(alarmClockDaysOfWeek);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("daysOfWeek", dayOfWeek.getText().toString());
        outState.putString("description", alarmClockName.getText().toString());
        outState.putInt("hours", hoursLayoutManager.getPosition(hoursLastTextView));
        outState.putInt("minutes", minutesLayoutManager.getPosition(minutesLastTextView));
        super.onSaveInstanceState(outState);
    }

    RecyclerView.OnScrollListener recyclerViewScrollListenerHours = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            hoursLastTextView = setSelectColor(hoursLastTextView, hoursSnapHelper, hoursLayoutManager,getApplicationContext());
        }
    };

    RecyclerView.OnScrollListener recyclerViewScrollListenerMinutes = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            minutesLastTextView = setSelectColor(minutesLastTextView, minutesSnapHelper, minutesLayoutManager,getApplicationContext());
        }
    };

    @SuppressLint("DefaultLocale")
    @OnClick(R.id.addAlarmClock)
    public void addNewAlarmClock() {
        String hours = hoursLastTextView.getText().toString();
        String minutes = convertNumberToTwoNumbersFormat(minutesLastTextView.getText().toString());
        String alarmTime = hours + " : " + minutes;

        AlarmClock alarmClock = new AlarmClock(alarmClockDescription, alarmTime, alarmClockDaysOfWeek, true);
        if (isChangeAlarmClock)
            alarmClock.set_id(alarmClockChangeId);

        Intent intent = new Intent(this, clockActivity.class);
        intent.putExtra(TAG_SEND_ALARM_CLOCK, alarmClock);
        setResult(RESULT_OK, intent);

        onBackPressed();
    }

    private String convertNumberToTwoNumbersFormat(String number) {
        return String.format(Locale.getDefault(), "%02d", Integer.parseInt(number));
    }

    @OnClick(R.id.cancel)
    public void backToClockActivity() {
        setResult(RESULT_CANCELED);
        onBackPressed();
    }

    @OnClick(R.id.alarmClockName)
    public void setAlarmClockName() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.get_alarm_clock_name_alert_dialog, null);

        EditText description = promptsView.findViewById(R.id.description);
        Button ok = promptsView.findViewById(R.id.OK);
        Button cancel = promptsView.findViewById(R.id.cancel);

        AlertDialog descriptionAlertDialog = createAlertDialog(promptsView);
        descriptionAlertDialog.getWindow().setBackgroundDrawableResource(
                R.drawable.oval_button);
        descriptionAlertDialog.show();

        ok.setOnClickListener(view -> changeAlarmClockDescription(description, descriptionAlertDialog));
        cancel.setOnClickListener(view -> descriptionAlertDialog.cancel());
        description.setText(alarmClockDescription);
    }

    @OnClick(R.id.alarmClockDaysOfWeek)
    public void setAlarmClockDaysOfWeek() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.get_days_of_the_week_alert_dialog, null);

        Button cancel = promptsView.findViewById(R.id.cancel);
        RadioGroup radioGroupDayOfWeek = promptsView.findViewById(R.id.radioGroupDayOfWeek);

        AlertDialog descriptionAlertDialog = createAlertDialog(promptsView);
        descriptionAlertDialog.getWindow().setBackgroundDrawableResource(
                R.drawable.oval_button);
        descriptionAlertDialog.show();

        cancel.setOnClickListener(view -> descriptionAlertDialog.cancel());
        RadioButton defaultSelectRadioButton =
                (RadioButton)radioGroupDayOfWeek.getChildAt(AlarmClock.DaysOfWeek.indexOf(alarmClockDaysOfWeek));
        defaultSelectRadioButton.setChecked(true);
        radioGroupDayOfWeek.setOnCheckedChangeListener((radioGroup, i) -> {
            View radioButton = radioGroup.findViewById(i);
            int index = radioGroup.indexOfChild(radioButton);
            changeAlarmClockDaysOfWeek(index,descriptionAlertDialog);
        });
    }

    private void changeAlarmClockDaysOfWeek(int daysOfWeek, AlertDialog alertDialog) {
        alarmClockDaysOfWeek = AlarmClock.DaysOfWeek.get(daysOfWeek).getCode();
        alertDialog.cancel();
        dayOfWeek.setText(alarmClockDaysOfWeek);
    }

    private void changeAlarmClockDescription(EditText description, AlertDialog alertDialog) {
        alarmClockDescription = description.getText().toString();
        alertDialog.cancel();
        alarmClockName.setText(alarmClockDescription);
    }

    private AlertDialog createAlertDialog(View promptsView) {
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setView(promptsView);
        AlertDialog alertDialog = mDialogBuilder.create();
        return alertDialog;
    }

    @OnClick(R.id.delete)
    public void deleteAlarmClock() {
        AlarmManagerUtil.cancel(AlarmClockDataBaseHelper.getInstance(getApplicationContext()).getAlarmClock(alarmClockChangeId), getApplicationContext());
        AlarmClockDataBaseHelper.getInstance(getApplicationContext()).deleteAlarmClockById(alarmClockChangeId);
        setResult(RESULT_CANCELED);
        onBackPressed();
    }
}
