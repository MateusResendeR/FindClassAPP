package com.findclass.ajvm.findclassapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.findclass.ajvm.findclassapp.SubjectActivities.AddSubjectActivity;
import com.findclass.ajvm.findclassapp.SubjectActivities.MySubjectsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AddSubjectActivityTest {

    @Rule
    public ActivityTestRule<AddSubjectActivity>
            mActivityRule = new ActivityTestRule<>(AddSubjectActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()));
    }

}
