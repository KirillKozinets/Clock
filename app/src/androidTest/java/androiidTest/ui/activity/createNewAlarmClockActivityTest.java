package androiidTest.ui.activity;


import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sgc.clock.R;
import com.sgc.clock.ui.activity.createNewAlarmClockActivity;
import com.sgc.clock.ui.fragment.alarmClockFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class createNewAlarmClockActivityTest {

    @Rule
    public ActivityTestRule <createNewAlarmClockActivity> mActivityRule =
            new ActivityTestRule <>(createNewAlarmClockActivity.class);

    @Before
    public void setUp(){
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testCurrentTitle() {
        String testTitle = getSaltString();

        Intent testIntent = new Intent();
        testIntent.putExtra(alarmClockFragment.TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE, testTitle);
        mActivityRule.launchActivity(testIntent);

        onView(withId(R.id.title)).check(matches(withText(testTitle)));
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
