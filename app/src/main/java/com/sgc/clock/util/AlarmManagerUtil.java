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

public class AlarmManagerUtil {

    public static void cancel(AlarmClock alarmClock, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmClockReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmClock.get_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public static Date setAlarm(AlarmClock alarm, Context context) {
        Calendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHorse());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());

        toNextDay(calendar, currentCalendar, alarm);

        Intent myIntent = new Intent(context, AlarmClockReceiver.class);
        myIntent.putExtra("alarmClock", ParcelableUtil.marshall(alarm));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.get_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Date startAlarmClockTime = calendar.getTime();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        setAlarm(alarmManager, pendingIntent, startAlarmClockTime);
        return startAlarmClockTime;
    }

    public static Date setAlarm(AlarmClock alarm, Context context,Date currentDate) {
        Calendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTime(currentDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHorse());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());

        toNextDay(calendar, currentCalendar, alarm);

        Intent myIntent = new Intent(context, AlarmClockReceiver.class);
        myIntent.putExtra("alarmClock", ParcelableUtil.marshall(alarm));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.get_id(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Date startAlarmClockTime = calendar.getTime();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        setAlarm(alarmManager, pendingIntent, startAlarmClockTime);
        return startAlarmClockTime;
    }


    public static Date setAlarm(long alarmId, Context context) {
        AlarmClock alarmClock = AlarmClockDataBaseHelper.getInstance(context).getAlarmClock(alarmId);
        return  setAlarm(alarmClock,context);
    }

    private static void toNextDay(Calendar calendar, Calendar currentCalendar, AlarmClock alarm) {
        if (currentCalendar.get(Calendar.HOUR_OF_DAY) >= alarm.getHorse()) {
            if (currentCalendar.get(Calendar.HOUR_OF_DAY) == alarm.getHorse() && currentCalendar.get(Calendar.MINUTE) > alarm.getMinutes()) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            } else if (currentCalendar.get(Calendar.HOUR_OF_DAY) > alarm.getHorse()) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            }
        }
    }

    private static void setAlarm(AlarmManager alarmManager, PendingIntent pendingIntent, Date currentDate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, currentDate.getTime(), pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentDate.getTime(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentDate.getTime(), pendingIntent);
    }

}
