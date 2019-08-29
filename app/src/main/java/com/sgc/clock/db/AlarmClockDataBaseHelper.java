package com.sgc.clock.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.sgc.clock.model.AlarmClock;

public class AlarmClockDataBaseHelper {

    private static AlarmClockDataBaseHelper instance;

    public static AlarmClockDataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AlarmClockDataBaseHelper(context);
        }

        return instance;
    }

    private static AlarmClockDatabase dataBase;
    private static AlarmClockDao databaseHandler;

    private AlarmClockDataBaseHelper(Context context) {
        if (dataBase == null || databaseHandler == null) {
            dataBase = Room.databaseBuilder(context,
                    AlarmClockDatabase.class, "alarmClock")
                    .fallbackToDestructiveMigration()
                    .build();

            databaseHandler = dataBase.employeeDao();
        }
    }

    public void addAlarmClockToDataBase(AlarmClock alarmClock) {
        databaseHandler.addAlarmClock(alarmClock);
    }

    public void insertAlarmClockToDataBase(AlarmClock alarmClock) {
        int updateAlarmClockCount = databaseHandler.updateAlarmClock(alarmClock);

        if (updateAlarmClockCount == 0)
            addAlarmClockToDataBase(alarmClock);
    }


    public void deleteAll() {
        databaseHandler.deleteAll();
    }

}
