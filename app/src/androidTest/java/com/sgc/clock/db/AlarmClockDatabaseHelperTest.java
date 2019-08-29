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
import com.sgc.clock.db.AlarmClockDao;
import com.sgc.clock.db.AlarmClockDatabase;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AlarmClockDatabaseHelperTest extends AndroidTestCase {

    AlarmClockDataBaseHelper testHelper;

    @Before
    public void setUp() {
       testHelper = AlarmClockDataBaseHelper.getInstance(getContext());
    }

    @After
    public void tearDown() {
        testHelper.deleteAll();
    }

    @Test
    public void testUpdateNotExistingAlarmClockInDataBase() {
        AlarmClock resultAlarmClock;
        AlarmClock testAlarmClock = new AlarmClock("testAlarmClock", "18:00", "0", true);
        long id = testHelper.updateAlarmClockToDataBase(testAlarmClock);
        resultAlarmClock = testHelper.getAlarmClock((int) id);
        assertEquals(resultAlarmClock, testAlarmClock);
        assert(id != -1);
    }

    @Test
    public void testInsertAlarmClockInDataBase() {
        String testTime = "3333";
        AlarmClock testAlarmClock = new AlarmClock("testAlarmClock", "18:00", "0", true);

        testHelper.addAlarmClockToDataBase(testAlarmClock);
        testAlarmClock.setAlarmClockTime(testTime);
        long id = testHelper.updateAlarmClockToDataBase(testAlarmClock);
        testAlarmClock = testHelper.getAlarmClock(id);

       assertEquals(testAlarmClock.getAlarmClockTime(),testTime);
        assert(id == -1);
    }

}
