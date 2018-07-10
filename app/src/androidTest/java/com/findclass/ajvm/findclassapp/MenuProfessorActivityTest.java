package com.findclass.ajvm.findclassapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.findclass.ajvm.findclassapp.menuActivities.MenuProfessorActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MenuProfessorActivityTest {

    @Rule
    public ActivityTestRule<MenuProfessorActivity>
            mActivityRule = new ActivityTestRule<>(MenuProfessorActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

}
