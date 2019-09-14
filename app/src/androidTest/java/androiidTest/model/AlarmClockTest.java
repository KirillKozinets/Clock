package androiidTest.model;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.sgc.clock.model.AlarmClock;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AlarmClockTest extends AndroidTestCase {
    AlarmClock alarmClock;

    String testTime = "15 : 45";
    String testName = "testName";
    String dayOfWeek = "testBane";
    boolean active = false;

    @Before
    public void setUp() {
        alarmClock = new AlarmClock(testName, testTime, dayOfWeek, active);
    }

    @Test
    public void testParseTime() {
        int testHours;
        int testMinutes;
        int trueHours = 15;
        int trueMinutes = 45;

        testHours = alarmClock.getHours();
        testMinutes = alarmClock.getMinutes();

        assertEquals(testHours, trueHours);
        assertEquals(testMinutes, trueMinutes);
    }

    @Test
    public void testCorrectFillField() {
        assertEquals(testTime, alarmClock.getAlarmClockTime());
        assertEquals(testName, alarmClock.getAlarmClockName());
        assertEquals(dayOfWeek, alarmClock.getAlarmClockDaysOfWeek());
        assertEquals(active, alarmClock.getActive());
    }

    @Test
    public void testTrueEquals() {
        AlarmClock alarmClock1 = new AlarmClock(alarmClock);
        AlarmClock alarmClock2 = new AlarmClock(alarmClock);
        assertTrue(alarmClock1.equals(alarmClock2));
    }

    @Test
    public void testFalseEquals1() {
        AlarmClock alarmClock1 = new AlarmClock(alarmClock);
        AlarmClock alarmClock2 = new AlarmClock(alarmClock);

        alarmClock2.setAlarmClockTime("44444");

        assertFalse(alarmClock1.equals(alarmClock2));
    }

    @Test
    public void testFalseEquals2() {
        AlarmClock alarmClock1 = new AlarmClock(alarmClock);
        AlarmClock alarmClock2 = new AlarmClock(alarmClock);

        alarmClock2.setAlarmClockName("44444");

        assertFalse(alarmClock1.equals(alarmClock2));
    }

    @Test
    public void testFalseEquals3() {
        AlarmClock alarmClock1 = new AlarmClock(alarmClock);
        AlarmClock alarmClock2 = new AlarmClock(alarmClock);

        alarmClock2.setActive(true);

        assertFalse(alarmClock1.equals(alarmClock2));
    }

}
