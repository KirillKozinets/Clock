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
        //create intent for start service
        Intent i = new Intent(context, OrderReminderNotificationService.class);

        //get alarm clock from intent
        byte[] alarmClockByteArray = intent.getByteArrayExtra("alarmClock");

        //put alarm clock to intent
        i.putExtra("alarmClock", alarmClockByteArray);

        //convert alarm clock in the format bite array
        // to alarm clock because you cant send Parcelable to alarmManager
        //using intent
        AlarmClock alarmClock = AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray);

        //when rebooting the device alarm cancel
        //so need to set the alarm again
        if (checkReboot(intent))
            AlarmManagerUtil.setAlarm(alarmClock, context);

        //when changing time need reset alarm
        if (checkTimeChanged(intent)) {
            AlarmManagerUtil.cancel(alarmClock, context);
            AlarmManagerUtil.setAlarm(alarmClock, context);
        }

        // start service for work the alarm
        context.startService(i);

        setResultCode(Activity.RESULT_OK);
    }

    //checks if the device has been reboot
    private boolean checkReboot(Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return true;
        }
        return false;
    }

    //checks if the time has been changed
    private boolean checkTimeChanged(Intent intent) {
        String intentAction = intent.getAction();

        if (Intent.ACTION_TIME_CHANGED.equals(intentAction)
                || Intent.ACTION_TIMEZONE_CHANGED.equals(intentAction)) {

            return true;
        }

        return false;
    }
}
