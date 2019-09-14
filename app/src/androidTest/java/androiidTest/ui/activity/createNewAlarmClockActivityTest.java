package androiidTest.ui.activity;


import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.sgc.clock.R;
import com.sgc.clock.model.AlarmClock;
import com.sgc.clock.ui.activity.createNewAlarmClockActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androiidTest.testUtil.TestUtils.withRecyclerView;
import static com.sgc.clock.util.Constants.TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE;
import static com.sgc.clock.util.Constants.TAG_SEND_ALARM_CLOCK;
import static com.sgc.clock.util.Constants.TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class createNewAlarmClockActivityTest {

    @Rule
    public ActivityTestRule<createNewAlarmClockActivity> mActivityRule =
            new ActivityTestRule<>(createNewAlarmClockActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testCurrentTitle() {
        String testTitle = getSaltString(6);

        Intent testIntent = new Intent();
        testIntent.putExtra(TAG_ACTIVITY_CREATE_ALARM_CLOCK_TITLE, testTitle);
        mActivityRule.launchActivity(testIntent);

        onView(withId(R.id.title)).check(matches(withText(testTitle)));
    }

    @Test
    public void testCurrentStartTime() {
        onView(withId(R.id.addAlarmClock)).perform(click());

        Intent resultData = mActivityRule.getActivityResult().getResultData();
        AlarmClock testAlarmClock = resultData.getParcelableExtra(TAG_SEND_ALARM_CLOCK);

        assertNotNull(testAlarmClock);
        assertEquals(testAlarmClock.getHours(), 8);
        assertEquals(testAlarmClock.getMinutes(), 0);
    }

    @Test
    public void testVisibleVisibilityDeleteButton() {
        Intent testIntent = new Intent();
        testIntent.putExtra(TAG_SEND_ID_TO_CHANGE_ALARM_CLOCK, 0);
        mActivityRule.launchActivity(testIntent);

        onView(withId(R.id.delete)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void testGoneVisibilityDeleteButton() {
        onView(withId(R.id.delete)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void testChangeAlarmClockDescription() {
        String testDescription = getSaltString(6);

        onView(withId(R.id.alarmClockName)).perform(click());
        onView(withId(R.id.description)).perform(setTextInTextView(testDescription));
        onView(withId(R.id.OK)).perform(click());
        onView(withId(R.id.addAlarmClock)).perform(click());

        Intent resultData = mActivityRule.getActivityResult().getResultData();
        AlarmClock testAlarmClock = resultData.getParcelableExtra(TAG_SEND_ALARM_CLOCK);

        assertEquals(testAlarmClock.getAlarmClockName(), testDescription);
    }

    public static ViewAction setTextInTextView(final String value) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TextView.class));
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((TextView) view).setText(value);
            }

            @Override
            public String getDescription() {
                return "replace text";
            }
        };
    }

    private String getSaltString(int lenght) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < lenght) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Test
    public void testCloseActivity() {
        onView(withId(R.id.cancel)).perform(click());
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void testCorrectShowListMinutes() {
        for (int i = 0; i < 75; i++) {
            int number = i % 60;
            String padded = String.format("%02d", number);
            onView(withId(R.id.minutes)).perform(RecyclerViewActions.scrollToPosition(number));
            onView(withRecyclerView(R.id.minutes)
                    .atPositionOnView(number, R.id.textTime))
                    .check(matches(withText(padded)));
        }
    }
    @Test
    public void testCorrectShowListHours() {
        for (int position = 0; position < 30; position++) {
            int hours = position % 24;
            onView(withId(R.id.hours)).perform(RecyclerViewActions.scrollToPosition(hours));
            onView(withRecyclerView(R.id.hours)
                    .atPositionOnView(hours, R.id.textTime))
                    .check(matches(withText(Integer.toString(hours))));
        }
    }

    @Test
    public void testCorrectSetTime() {
        int toCenterOffset = 2;
        int hours = 2 + (int)(Math.random() * 21);
        int minutes = 2 + (int)(Math.random() * 57);

        onView(withId(R.id.hours)).perform(RecyclerViewActions.scrollToPosition(hours - toCenterOffset));
        onView(withId(R.id.minutes)).perform(RecyclerViewActions.scrollToPosition(minutes - toCenterOffset));

        onView(withId(R.id.addAlarmClock)).perform(click());
        Intent resultData = mActivityRule.getActivityResult().getResultData();
        AlarmClock testAlarmClock = resultData.getParcelableExtra(TAG_SEND_ALARM_CLOCK);

        assertEquals(testAlarmClock.getMinutes(), minutes);
        assertEquals(testAlarmClock.getHours(), hours);
    }
}
