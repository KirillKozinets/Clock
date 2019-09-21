package com.sgc.clock.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;
import android.os.Parcel;
import android.os.Parcelable;

import com.sgc.clock.db.AlarmClockDao;
import com.sgc.clock.db.AlarmClockDatabase;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Entity
public class AlarmClock implements Parcelable {

    public enum DaysOfWeek {
        ALLWEEK("Пн-Вс"),NOREPEAT("Без повторов"),NOWEEKEND("Пн - Пт");

        private static Map<Integer, DaysOfWeek> daysOfWeek = new HashMap<>();

        private static void init() {
            if (daysOfWeek.size() == 0) {
                daysOfWeek.put(0, DaysOfWeek.ALLWEEK);
                daysOfWeek.put(1, DaysOfWeek.NOREPEAT);
                daysOfWeek.put(2, DaysOfWeek.NOWEEKEND);
            }
        }

        public static DaysOfWeek get(int id) {
                init();
            return daysOfWeek.get(id);
        }

        public static ArrayList<DaysOfWeek> get() {
                init();
            return new ArrayList<>(daysOfWeek.values());
        }

        public static int indexOf(String code) {
                init();
            ArrayList<DaysOfWeek> days = get();
            ArrayList<String> codeDays = new ArrayList<>();

            for(int i = 0 ; i < days.size() ; i++)
                codeDays.add(days.get(i).code);

            return codeDays.indexOf(code);
        }

        private String code;

        DaysOfWeek(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }

    @PrimaryKey(autoGenerate = true)
    private int _id = 0;
    private String alarmClockName;
    private String alarmClockTime;
    private String alarmClockDaysOfWeek;

    // = true if alarm clock active
    // = false if alarm clock not active
    private boolean isActive;

    /**
     * copy constructor
     *
     * @param alarmClock alarm clock which you want to copy
     */
    @Ignore
    public AlarmClock(AlarmClock alarmClock) {
        this._id = alarmClock._id;
        this.alarmClockName = alarmClock.alarmClockName;
        this.alarmClockTime = alarmClock.alarmClockTime;
        this.alarmClockDaysOfWeek = alarmClock.alarmClockDaysOfWeek;
        this.isActive = alarmClock.isActive;
    }

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
     * @param in send alarm clock parcel
     */
    @Ignore
    public AlarmClock(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        this._id = Integer.parseInt(data[0]);
        this.alarmClockName = data[1];
        this.alarmClockTime = data[2];
        this.alarmClockDaysOfWeek = data[3];
        this.isActive = Boolean.parseBoolean(data[4]);
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

    /**
     * @return true if alarm clock active , false if alarm clock not active
     */
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

    public int getHours() {
        String hoursStr = alarmClockTime.split(" : ")[0];
        int hours = Integer.parseInt(hoursStr);
        return hours;
    }

    public int getMinutes() {
        String minutesStr = alarmClockTime.split(" : ")[1];
        int minutes = Integer.parseInt(minutesStr);
        return minutes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{Integer.toString(_id), alarmClockName, alarmClockTime, alarmClockDaysOfWeek, Boolean.toString(isActive)});
    }

    public static final Creator<AlarmClock> CREATOR = new Creator<AlarmClock>() {
        @Override
        public AlarmClock createFromParcel(Parcel in) {
            return new AlarmClock(in);
        }

        @Override
        public AlarmClock[] newArray(int size) {
            return new AlarmClock[size];
        }
    };

}

