package com.sgc.clock.util;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.sgc.clock.model.AlarmClock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AlarmManagerUtilTest extends AndroidTestCase {
    AlarmClock alarmClock;

    String testTime = "15 : 45";
    String testName = "testName";
    String dayOfWeek = "testBane";
    boolean active = false;

    Calendar currentCalendar = new GregorianCalendar();
    Calendar testCalendar = new GregorianCalendar();

    @Before
    public void setUp() {
        alarmClock = new AlarmClock(testName, testTime, dayOfWeek, active);
        currentCalendar.setTimeInMillis(System.currentTimeMillis());
        testCalendar.set(Calendar.DAY_OF_MONTH,currentCalendar.get(Calendar.DAY_OF_MONTH));
        testCalendar.set(Calendar.HOUR_OF_DAY,12);
        testCalendar.set(Calendar.MINUTE,12);
    }

    @Test
    public void testSetAlarm() {
        Date alarmTime = AlarmManagerUtil.setAlarm(alarmClock, getContext(),testCalendar.getTime());
        AlarmManagerUtil.cancel(alarmClock, getContext());

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(alarmTime.getTime());

        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 15);
        assertEquals(calendar.get(Calendar.MINUTE), 45);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testNextDaySetAlarm() {
        testCalendar.set(Calendar.HOUR_OF_DAY,16);

        Date alarmTime = AlarmManagerUtil.setAlarm(alarmClock, getContext(),testCalendar.getTime());
        AlarmManagerUtil.cancel(alarmClock, getContext());

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(alarmTime.getTime());

        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 15);
        assertEquals(calendar.get(Calendar.MINUTE), 45);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH) + 1);
    }

}
