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
        //create intent for start service
        Intent i = new Intent(context, OrderReminderNotificationService.class);

        //get alarm clock from intent
        byte[] alarmClockByteArray = intent.getByteArrayExtra(TAG_SEND_ALARM_CLOCK);

        //put alarm clock to intent
        i.putExtra(TAG_SEND_ALARM_CLOCK, alarmClockByteArray);

        //convert alarm clock in the format bite array
        // to alarm clock because you cant send Parcelable to alarmManager
        //using intent
        AlarmClock alarmClock = AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockByteArray);

        //when rebooting the device alarm cancel
        //so need to set the alarm again
        if (checkReboot(intent))
            AlarmManagerUtil.startAlarm(alarmClock, context);

        //when changing time need reset alarm
        if (checkTimeChanged(intent)) {
            AlarmManagerUtil.cancel(alarmClock, context);
            AlarmManagerUtil.startAlarm(alarmClock, context);
        }

        // start service for work the alarm
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


    //checks if the device has been reboot
    private boolean checkReboot(Intent intent) {
        return Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction());
    }

    //checks if the time has been changed
    private boolean checkTimeChanged(Intent intent) {
        String intentAction = intent.getAction();

        return Intent.ACTION_TIME_CHANGED.equals(intentAction)
                || Intent.ACTION_TIMEZONE_CHANGED.equals(intentAction);
    }
}
