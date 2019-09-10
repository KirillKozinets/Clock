package com.sgc.clock.util;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.sgc.clock.model.AlarmClock;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AlarmClockConverterTest extends AndroidTestCase {

    @Test
    public void testConvertByteArrayToAlarmClock() {
        AlarmClock alarmClockTest = new AlarmClock("feefef", "gdfdfdf", "gffgfgfg", true);
        byte[] alarmClockTestByteArray = ParcelableUtil.marshall(alarmClockTest);
        AlarmClock alarmClockTest2 = AlarmClockConverter.convertByteArrayToAlarmClock(alarmClockTestByteArray);
        assertTrue(alarmClockTest.equals(alarmClockTest2));
    }
}
