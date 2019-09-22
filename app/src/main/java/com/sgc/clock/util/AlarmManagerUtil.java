package com.sgc.clock.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.sgc.clock.db.AlarmClockDataBaseHelper;
import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.receiver.AlarmClockReceiver;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;
import static com.sgc.clock.util.Constants.TAG_SEND_ALARM_CLOCK;

public class AlarmManagerUtil {

    public static void cancel(AlarmClock alarmClock, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmClockReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmClock.get_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public static Date startAlarm(AlarmClock alarm, Context context) {
        Date startTimeAlarm = getStartTimeAlarm(alarm, new Date(System.currentTimeMillis()));

        PendingIntent alarmBroadcastPendingIntent = getAlarmBroadcastPendingIntent(context, alarm);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        startAlarm(alarm, alarmManager, alarmBroadcastPendingIntent, startTimeAlarm);
        return startTimeAlarm;
    }

    public static Date startAlarm(AlarmClock alarm, Context context, Date currentDate) {
        Date startTimeAlarm = getStartTimeAlarm(alarm, currentDate);

        PendingIntent alarmBroadcastPendingIntent = getAlarmBroadcastPendingIntent(context, alarm);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        startAlarm(alarm, alarmManager, alarmBroadcastPendingIntent, startTimeAlarm);
        return startTimeAlarm;
    }

    private static Date getStartTimeAlarm(AlarmClock alarm, Date currentDate) {
        Calendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTime(currentDate);

        Calendar startAlarmCalendar = Calendar.getInstance();
        startAlarmCalendar.setTimeInMillis(System.currentTimeMillis());
        startAlarmCalendar.set(Calendar.HOUR_OF_DAY, alarm.getHours());
        startAlarmCalendar.set(Calendar.MINUTE, alarm.getMinutes());
        startAlarmCalendar.set(Calendar.SECOND, 0);

        toNextDay(startAlarmCalendar, currentCalendar);

        return startAlarmCalendar.getTime();
    }

    private static PendingIntent getAlarmBroadcastPendingIntent(Context context, AlarmClock alarm) {
        Intent alarmClockReceiver = getAlarmClockReceiverIntent(context, alarm);
        return PendingIntent.getBroadcast(context, alarm.get_id(), alarmClockReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Intent getAlarmClockReceiverIntent(Context context, AlarmClock alarm) {
        Intent alarmClockReceiver = new Intent(context, AlarmClockReceiver.class);
        alarmClockReceiver.putExtra(TAG_SEND_ALARM_CLOCK, ParcelableUtil.marshall(alarm));
        return alarmClockReceiver;
    }

    public static Date startAlarm(long alarmId, Context context) {
        AlarmClock alarmClock = AlarmClockDataBaseHelper.getInstance(context).getAlarmClock(alarmId);
        return startAlarm(alarmClock, context);
    }

    private static void toNextDay(Calendar startAlarmCalendar, Calendar currentCalendar) {
        if (startAlarmCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis())
            startAlarmCalendar.set(Calendar.DAY_OF_YEAR, startAlarmCalendar.get(Calendar.DAY_OF_YEAR) + 1);
    }

    private static void startAlarm(AlarmClock alarmClock, AlarmManager alarmManager, PendingIntent pendingIntent, Date currentDate) {
            starSingleAlarm(alarmManager, pendingIntent, currentDate);
    }

    private static void starSingleAlarm(AlarmManager alarmManager, PendingIntent pendingIntent, Date currentDate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, currentDate.getTime(), pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentDate.getTime(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentDate.getTime(), pendingIntent);
    }

}
