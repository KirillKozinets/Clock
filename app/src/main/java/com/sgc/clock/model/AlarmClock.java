package com.sgc.clock.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;

import com.sgc.clock.db.AlarmClockDao;
import com.sgc.clock.db.AlarmClockDatabase;

@Entity
public class AlarmClock {
    @PrimaryKey(autoGenerate = true)
    private int _id = 0;
    private String alarmClockName;
    private String alarmClockTime;
    private String alarmClockDaysOfWeek;
    private boolean isActive;

    /**
     * @param alarmClockId         alarm clock id
     * @param alarmClockName       alarm clock name
     * @param alarmClockTime       alarm clock start time
     * @param alarmClockDaysOfWeek alarm clock start days of week
     * @param isActive             included alarm clock
     */
    @Ignore
    public AlarmClock(int alarmClockId, String alarmClockName, String alarmClockTime, String alarmClockDaysOfWeek, boolean isActive) {
        this._id = alarmClockId;
        this.alarmClockName = alarmClockName;
        this.alarmClockTime = alarmClockTime;
        this.alarmClockDaysOfWeek = alarmClockDaysOfWeek;
        this.isActive = isActive;
    }

    /**
     * @param alarmClockName       alarm clock name
     * @param alarmClockTime       alarm clock start time
     * @param alarmClockDaysOfWeek alarm clock start days of week
     * @param isActive             included alarm clock
     */
    public AlarmClock(String alarmClockName, String alarmClockTime, String alarmClockDaysOfWeek, boolean isActive) {
        this.alarmClockName = alarmClockName;
        this.alarmClockTime = alarmClockTime;
        this.alarmClockDaysOfWeek = alarmClockDaysOfWeek;
        this.isActive = isActive;
    }

    public int get_id() {
        return _id;
    }

    public String getAlarmClockName() {
        return alarmClockName;
    }

    public String getAlarmClockTime() {
        return alarmClockTime;
    }

    public String getAlarmClockDaysOfWeek() {
        return alarmClockDaysOfWeek;
    }

    public boolean getActive() {
        return isActive;
    }


    public void setActive(boolean active) {
        isActive = active;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setAlarmClockName(String alarmClockName) {
        this.alarmClockName = alarmClockName;
    }

    public void setAlarmClockTime(String alarmClockTime) {
        this.alarmClockTime = alarmClockTime;
    }

    public void setAlarmClockDaysOfWeek(String alarmClockDaysOfWeek) {
        this.alarmClockDaysOfWeek = alarmClockDaysOfWeek;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        AlarmClock alarmClock = (AlarmClock) obj;
        Boolean isEquals = true;

        if (!this.getAlarmClockName().equals(alarmClock.getAlarmClockName()))
            isEquals = false;

        if (!this.getAlarmClockDaysOfWeek().equals(alarmClock.getAlarmClockDaysOfWeek()))
            isEquals = false;

        if (!this.getAlarmClockTime().equals(alarmClock.getAlarmClockTime()))
            isEquals = false;

        if (this.isActive != alarmClock.isActive)
            isEquals = false;

        return isEquals;
    }
}

