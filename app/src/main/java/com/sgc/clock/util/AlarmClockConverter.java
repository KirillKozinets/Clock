package com.sgc.clock.util;

import android.os.Parcel;

import com.sgc.clock.model.AlarmClock;

public class AlarmClockConverter {

    public static AlarmClock convertByteArrayToAlarmClock(byte[] alarmClockByteArray)
    {
        Parcel alarmClockParcel = ParcelableUtil.unmarshall(alarmClockByteArray);
        AlarmClock alarmClock = new AlarmClock(alarmClockParcel);
        return alarmClock;
    }

}
