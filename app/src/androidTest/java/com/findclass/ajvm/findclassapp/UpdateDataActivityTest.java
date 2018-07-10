package com.findclass.ajvm.findclassapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.findclass.ajvm.findclassapp.AccountActivities.UpdateDataActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class UpdateDataActivityTest {

    @Rule
    public ActivityTestRule<UpdateDataActivity>
            mActivityRule = new ActivityTestRule<>(UpdateDataActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState() throws InterruptedException {
        Thread.sleep(1500);
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.surnameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.phoneEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.finishUpdateData)).check(matches(isDisplayed()));
    }

    @Test
    public void whenNameIsEmpty_AndClickFinishUpdateButton_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        clearFields();
        testEmptyFieldState(R.id.surnameEditText, R.id.phoneEditText);
    }

    @Test
    public void whenSurnameIsEmpty_AndClickFisnishUpdateButton_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        clearFields();
        testEmptyFieldState(R.id.nameEditText, R.id.phoneEditText);
    }

    @Test
    public void whenPhoneIsEmpty_AndClickFisnishUpdateButton_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        clearFields();
        testEmptyFieldState(R.id.nameEditText, R.id.surnameEditText);
    }

    private void clearFields(){
        onView(withId(R.id.nameEditText)).perform(clearText());
        onView(withId(R.id.surnameEditText)).perform(clearText());
        onView(withId(R.id.phoneEditText)).perform(clearText());
    }

    private void testEmptyFieldState(int notEmptyField1, int notEmptyField2) {
        onView(withId(notEmptyField1)).perform(typeText("DefaultText"));
        closeSoftKeyboard();
        onView(withId(notEmptyField2)).perform(typeText("DefaultText"));
        closeSoftKeyboard();
        onView(withId(R.id.finishUpdateData)).perform(click());
        onView(withText(R.string.TOAST_STRING)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));


    }

}