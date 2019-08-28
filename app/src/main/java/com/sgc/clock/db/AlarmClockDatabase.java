package com.sgc.clock.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sgc.clock.model.AlarmClock;

@Database(entities = {AlarmClock.class}, version = 8)
public abstract class AlarmClockDatabase extends RoomDatabase {
    public abstract AlarmClockDao employeeDao();
}