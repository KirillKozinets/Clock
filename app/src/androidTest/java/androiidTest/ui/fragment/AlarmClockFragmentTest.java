package androiidTest.ui.fragment;


import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.sgc.clock.ui.activity.clockActivity;
import com.sgc.clock.ui.activity.createNewAlarmClockActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withText;;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class AlarmClockFragmentTest {

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(clockActivity.class);

    @Before
    public void setup() {
        activityRule.launchActivity(new Intent());
        Intents.init();
    }

    @Test
    public void testClickAddAlarmClock(){
            onView(allOf(
                    withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                    withText("добавить")))
                    .perform(click());

        intended(hasComponent(createNewAlarmClockActivity.class.getName()));
    }

}
