package com.sgc.clock.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;

import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.service.OrderReminderNotificationService;
import com.sgc.clock.util.AlarmClockConverter;
import com.sgc.clock.util.AlarmManagerUtil;

import java.util.Date;
import java.util.GregorianCalendar;

import static com.sgc.clock.util.Constants.TAG_SEND_ALARM_CLOCK;

public class AlarmClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent i = new Intent(context, OrderReminderNotificationService.class);

        byte[] alarmClockByteArray = intent.getByteArrayExtra(TAG_SEND_ALARM_CLOCK);

        i.putExtra(TAG_SEND_ALARM_CLOCK, alarmClockByteArray);

        AlarmClock alarmClock = AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray);

        if (checkReboot(intent))
            AlarmManagerUtil.startAlarm(alarmClock, context);

        if (checkTimeChanged(intent)) {
            AlarmManagerUtil.cancel(alarmClock, context);
            AlarmManagerUtil.startAlarm(alarmClock, context);
        }

        context.startService(i);
        transferAlarmToNextDay(alarmClock, context);
        setResultCode(Activity.RESULT_OK);
    }

    private void transferAlarmToNextDay(AlarmClock alarmClock, Context context) {
        if (!alarmClock.getAlarmClockDaysOfWeek().equals(AlarmClock.DaysOfWeek.NOREPEAT.getCode())) {
            GregorianCalendar currentDate = new GregorianCalendar();
            currentDate.setTimeInMillis(System.currentTimeMillis() + AlarmManager.INTERVAL_HOUR);
            AlarmManagerUtil.startAlarm(alarmClock, context,currentDate.getTime());
        }
    }

    private boolean checkReboot(Intent intent) {
        return Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction());
    }

    private boolean checkTimeChanged(Intent intent) {
        String intentAction = intent.getAction();

        return Intent.ACTION_TIME_CHANGED.equals(intentAction)
                || Intent.ACTION_TIMEZONE_CHANGED.equals(intentAction);
    }
}
