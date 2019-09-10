package com.sgc.clock.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.sgc.clock.model.AlarmClock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ParcelableUtilTest extends AndroidTestCase {

    AlarmClock alarmClock;

    String testTime = "15 : 45";
    String testName = "testName";
    String dayOfWeek = "testBane";
    boolean active = false;

    @Before
    public void setUp() {
        alarmClock = new AlarmClock(testName, testTime, dayOfWeek, active);
    }

    @Test
    public void testParcelableUtil(){
        Parcelable alarmClockParcelable = alarmClock;
        byte[] alarmClockByteArray = ParcelableUtil.marshall(alarmClockParcelable);
        Parcel alarmClockParcel = ParcelableUtil.unmarshall(alarmClockByteArray);
        AlarmClock alarmClockTest = new AlarmClock(alarmClockParcel);
        assertTrue(alarmClock.equals(alarmClockTest));
    }

}
