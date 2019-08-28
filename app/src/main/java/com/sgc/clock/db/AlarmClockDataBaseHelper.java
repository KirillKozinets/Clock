package com.sgc.clock.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.sgc.clock.model.AlarmClock;

public class AlarmClockDataBaseHelper {

    private static AlarmClockDatabase dataBase;
    private static AlarmClockDao databaseHandler;

    public static void loadDataBase(Context context) {
        if (dataBase == null || databaseHandler == null) {
            dataBase = Room.databaseBuilder(context,
                    AlarmClockDatabase.class, "alarmClock")
                    .fallbackToDestructiveMigration()
                    .build();

            databaseHandler = dataBase.employeeDao();
        }
    }

    public static void addAlarmClockToDataBase(AlarmClock alarmClock) {
        isDataBaseLoad();
        databaseHandler.addAlarmClock(alarmClock);
    }

    public static void гзвфеуAlarmClockToDataBase(AlarmClock alarmClock) {
        isDataBaseLoad();
        int updateAlarmClockCount = databaseHandler.updateAlarmClock(alarmClock);

        if(updateAlarmClockCount == 0)
            addAlarmClockToDataBase(alarmClock);
    }

    private static void isDataBaseLoad() {
        if (dataBase == null || databaseHandler == null)
            throw new RuntimeException("Database was not loaded. Check if the loadDataBase() method was called");
    }


}
