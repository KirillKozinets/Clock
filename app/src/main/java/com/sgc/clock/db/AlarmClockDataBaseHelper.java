package com.sgc.clock.db;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.sgc.clock.model.AlarmClock;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * class for working with the alarm clock database
 */
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
                    .allowMainThreadQueries()
                    .build();

            databaseHandler = dataBase.employeeDao();
        }
    }

    /**
     * @param alarmClock alarm clock to be added to data base
     * @return id alarm clock in data base
     */
    public long addAlarmClockToDataBase(AlarmClock alarmClock) {
        return databaseHandler.addAlarmClock(alarmClock);
    }

    /**
     * if id updated alarm clock is in the data base
     * is in the database otherwise add a new alarm clock
     * @param alarmClock alarm clock to be update in data base
     * @return if there is no alarm clock then returns added alarm clock id from the data base
     */
    public long updateAlarmClockToDataBase(AlarmClock alarmClock) {
        long id = -1;
        int updateAlarmClockCount = databaseHandler.updateAlarmClock(alarmClock);

        if (updateAlarmClockCount == 0)
            id = addAlarmClockToDataBase(alarmClock);

        return id;
    }

    /**
     * @param id id alarm clock which want to receive from data base
     * @return alarm clock by id
     */
    public AlarmClock getAlarmClock(long id) {
        return databaseHandler.getAlarmClock((int) id);
    }

    /**
     * delete all alarm clock
     */
    public void deleteAll() {
        databaseHandler.deleteAll();
    }

    public Flowable<List<AlarmClock>> getAll()
    {
        return databaseHandler.getAllAlarmClock();
    }

}
