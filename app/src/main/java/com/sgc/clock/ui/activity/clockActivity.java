package com.sgc.clock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sgc.clock.R;
import com.sgc.clock.adapter.viewPagerAdapter;
import com.sgc.clock.db.AlarmClockDataBaseHelper;
import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.util.AlarmManagerUtil;
import com.sgc.clock.util.PowerSaverIntentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sgc.clock.util.AlarmClockConverter.convertByteArrayToAlarmClock;
import static com.sgc.clock.util.Constants.TAG_SEND_ALARM_CLOCK;

public class clockActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private viewPagerAdapter pagerAdapter;

    final int REQUEST_CHANGE_ALARM_CLOCK = 1;
    final int REQUEST_NEW_ALARM_CLOCK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        ButterKnife.bind(this);

        pagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        long alarmClockId = 0;
        AlarmClock alarmClock = null;

        if (data != null)
            alarmClock = data.getParcelableExtra(TAG_SEND_ALARM_CLOCK);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHANGE_ALARM_CLOCK:
                     AlarmClockDataBaseHelper.getInstance(getApplicationContext()).updateAlarmClockToDataBase(alarmClock);
                     alarmClockId = alarmClock.get_id();
                    break;
                case REQUEST_NEW_ALARM_CLOCK:
                    alarmClockId = AlarmClockDataBaseHelper.getInstance(getApplicationContext()).addAlarmClockToDataBase(alarmClock);
                    break;
            }
            AlarmManagerUtil.startAlarm(alarmClockId, getApplicationContext());
         }
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPager.post(() ->
                PowerSaverIntentUtil.startPowerSaverIntent(this));
    }
}
