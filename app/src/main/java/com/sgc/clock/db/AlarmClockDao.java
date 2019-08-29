package com.sgc.clock.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sgc.clock.model.AlarmClock;

import java.util.List;

@Dao
public interface AlarmClockDao {

    @Insert()
    long addAlarmClock(AlarmClock alarmClock);

    @Query("SELECT * FROM alarmclock WHERE _id = :id")
    AlarmClock getAlarmClock(int id);

    @Query("SELECT * FROM alarmclock")
    List<AlarmClock> getAllAlarmClock();

    @Query("SELECT * FROM alarmclock WHERE isActive = " + 1)
    List<AlarmClock> getAllActiveAlarmClock();

    @Query("SELECT COUNT() FROM alarmclock")
    int getAlarmClockCount();

    @Update()
    int updateAlarmClock(AlarmClock alarmClock);

    @Query("DELETE FROM alarmclock WHERE _id = :alarmClockId")
    int deleteAlarmClock(int alarmClockId);

    @Query("DELETE FROM alarmclock")
    void deleteAll();

}
