package com.sgc.clock.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.persistence.room.Room;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.sgc.clock.model.AlarmClock;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;

@RunWith(AndroidJUnit4.class)
public class AlarmClockDatabaseTest extends AndroidTestCase {

    private AlarmClockDatabase dataBase;
    private AlarmClockDao databaseHandler;

    @Before
    public void setUp() {
        dataBase = Room.databaseBuilder(mContext,
                AlarmClockDatabase.class, "alarmClock")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        databaseHandler = dataBase.employeeDao();

        assertNotNull(databaseHandler);
    }

    @After
    public void tearDown() {
        databaseHandler.deleteAll();
    }

    @Test
    public void testAddAlarmClockInDataBase() {
        AlarmClock resultAlarmClock;
        AlarmClock testAlarmClock = new AlarmClock("testAlarmClock", "18:00", "0", true);
        long id = databaseHandler.addAlarmClock(testAlarmClock);
        databaseHandler.addAlarmClock(testAlarmClock);
        resultAlarmClock = databaseHandler.getAlarmClock((int) id);
        assertEquals(resultAlarmClock, testAlarmClock);
    }

    @Test
    public void testDeleteAlarmClockInDataBase() {
        AlarmClock testAlarmClock = new AlarmClock("testAlarmClock", "18:00", "0", true);

        long id = databaseHandler.addAlarmClock(testAlarmClock);
        databaseHandler.addAlarmClock(testAlarmClock);

        int startCount = databaseHandler.getAlarmClockCount();
        databaseHandler.deleteAlarmClock((int) id);
        int postDeleteCount = databaseHandler.getAlarmClockCount();

        assertEquals(startCount, postDeleteCount + 1);
    }

    @Test
    public void testDeleteAllInDataBase() {
        AlarmClock testAlarmClock1 = new AlarmClock("testAlarmClock", "18:00", "0", true);
        AlarmClock testAlarmClock2 = new AlarmClock("testAlarmClock", "18:00", "0", true);
        AlarmClock testAlarmClock3 = new AlarmClock("testAlarmClock", "18:00", "0", true);

        databaseHandler.addAlarmClock(testAlarmClock1);
        databaseHandler.addAlarmClock(testAlarmClock2);
        databaseHandler.addAlarmClock(testAlarmClock3);

        databaseHandler.deleteAll();

        assertEquals(databaseHandler.getAlarmClockCount(), 0);
    }

    @Test
    public void testGetAllAlarmClock() {
        AlarmClock testAlarmClock1 = new AlarmClock("testAlarmClock", "18:00", "0", true);
        AlarmClock testAlarmClock2 = new AlarmClock("testAlarmClock2", "18:00", "0", true);
        AlarmClock testAlarmClock3 = new AlarmClock("testAlarmClock3", "18:00", "0", true);

        databaseHandler.addAlarmClock(testAlarmClock1);
        databaseHandler.addAlarmClock(testAlarmClock2);
        databaseHandler.addAlarmClock(testAlarmClock3);

        databaseHandler.getAllAlarmClock().observeOn(AndroidSchedulers.mainThread())
                .subscribe(employees -> {
                    assertEquals(employees.size(), 3);
                    assertEquals(testAlarmClock1, employees.get(0));
                    assertEquals(testAlarmClock2, employees.get(1));
                    assertEquals(testAlarmClock3, employees.get(2));
                }).dispose();

    }

    @Test
    public void testGetAllActiveAlarmClock() {
        AlarmClock testAlarmClock1 = new AlarmClock("testAlarmClock", "18:00", "0", true);
        AlarmClock testAlarmClock2 = new AlarmClock("testAlarmClock2", "18:00", "0", true);
        AlarmClock testAlarmClock3 = new AlarmClock("testAlarmClock3", "18:00", "0", false);

        databaseHandler.addAlarmClock(testAlarmClock1);
        databaseHandler.addAlarmClock(testAlarmClock2);
        databaseHandler.addAlarmClock(testAlarmClock3);

        ArrayList<AlarmClock> alarmClocks = (ArrayList<AlarmClock>) databaseHandler.getAllActiveAlarmClock();

        assertEquals(alarmClocks.size(), 2);
        assertEquals(databaseHandler.getAlarmClockCount(), 3);
        assertEquals(testAlarmClock1, alarmClocks.get(0));
        assertEquals(testAlarmClock2, alarmClocks.get(1));
    }

    @Test
    public void testUpdateAllAlarmClock() {
        AlarmClock testAlarmClock1 = new AlarmClock("testAlarmClock", "18:00", "0", true);

        long idAddAlarmClock = databaseHandler.addAlarmClock(testAlarmClock1);

        AlarmClock testAlarmClock2 = databaseHandler.getAlarmClock((int) idAddAlarmClock);

        testAlarmClock2.setAlarmClockTime("34433345");

        int countUpdate = databaseHandler.updateAlarmClock(testAlarmClock2);

        assertEquals(countUpdate, 1);
    }
}
