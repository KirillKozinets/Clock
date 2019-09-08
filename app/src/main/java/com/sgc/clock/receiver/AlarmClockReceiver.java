package com.sgc.clock.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.AppLaunchChecker;

import com.sgc.clock.service.OrderReminderNotificationService;
import com.sgc.clock.util.AlarmClockConverter;
import com.sgc.clock.util.AlarmManagerUtil;

public class AlarmClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent i = new Intent(context, OrderReminderNotificationService.class);
        byte[] alarmClockByteArray = intent.getByteArrayExtra("alarmClock");
        i.putExtra("alarmClock", alarmClockByteArray);

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AlarmManagerUtil.setAlarm(AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray).get_id(), context);
        }
        if (Intent.ACTION_TIME_CHANGED.equals(i.getAction()) || Intent.ACTION_TIMEZONE_CHANGED.equals(i.getAction())) {
            AlarmManagerUtil.cancel(AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray), context);
            AlarmManagerUtil.setAlarm(AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray).get_id(), context);
        }

        context.startService(i);
        setResultCode(Activity.RESULT_OK);
    }
}
