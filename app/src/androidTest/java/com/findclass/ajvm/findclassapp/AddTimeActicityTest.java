package com.findclass.ajvm.findclassapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.findclass.ajvm.findclassapp.CalendarActivities.AddTimeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AddTimeActicityTest {

    @Rule
    public ActivityTestRule<AddTimeActivity>
            mActivityRule = new ActivityTestRule<>(AddTimeActivity.class, false, true);

    @Test
    public void whenHorarioInicialIsEmpity_AndClickAdicionar_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        testEmptyFieldState(R.id.endTimeEditText,R.id.priceEditText);
    }

    @Test
    public void whenHorarioFinalIsEmpity_AndClickAdicionar_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        testEmptyFieldState(R.id.startTimeEditText,R.id.priceEditText);
    }

    /*@Test
    public void whenPrecoIsEmpity_AndClickAdicionar_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        testEmptyFieldState(R.id.startTimeEditText,R.id.endTimeEditText);
    }*/

    @Test
    public void whenThereIsNoEmptyField_andThereIsNoInvalidField_shouldRegisterSchedule() throws InterruptedException {
        Thread.sleep(1500);
        onView(withId(R.id.startTimeEditText)).perform(typeText("1200"));
        closeSoftKeyboard();
        onView(withId(R.id.endTimeEditText)).perform(typeText("1500"));
        closeSoftKeyboard();
        onView(withId(R.id.priceEditText)).perform(typeText("1500"));
        closeSoftKeyboard();
        onView(withId(R.id.addButton)).perform(click());

        onView(withId(R.id.myTimesRecyclerView)).check(matches(isDisplayed()));
    }

    private void testEmptyFieldState(int notEmptyField1, int notEmptyField2){
        onView(withId(notEmptyField1)).perform(typeText("1200"));
        closeSoftKeyboard();
        onView(withId(notEmptyField2)).perform(typeText("1500"));
        closeSoftKeyboard();
        onView(withId(R.id.addButton)).perform(click());
        onView(withText(R.string.TOAST_STRING)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

}
