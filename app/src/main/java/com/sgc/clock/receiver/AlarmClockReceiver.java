package com.sgc.clock.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.AppLaunchChecker;

import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.service.OrderReminderNotificationService;
import com.sgc.clock.util.AlarmClockConverter;
import com.sgc.clock.util.AlarmManagerUtil;

public class AlarmClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent i = new Intent(context, OrderReminderNotificationService.class);
        byte[] alarmClockByteArray = intent.getByteArrayExtra("alarmClock");
        i.putExtra("alarmClock", alarmClockByteArray);

        if (checkReboot(intent))
            AlarmManagerUtil.setAlarm(AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray), context);

        if (checkTimeChanged(intent)) {
            AlarmManagerUtil.cancel(AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray), context);
            AlarmManagerUtil.setAlarm(AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray), context);
        }

        context.startService(i);

        setResultCode(Activity.RESULT_OK);

    }

    private boolean checkReboot(Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return true;
        }
        return false;
    }

    private boolean checkTimeChanged(Intent intent) {
        String intentAction = intent.getAction();

        if (Intent.ACTION_TIME_CHANGED.equals(intentAction)
                || Intent.ACTION_TIMEZONE_CHANGED.equals(intentAction)) {

            return true;
        }

        return false;
    }
}
