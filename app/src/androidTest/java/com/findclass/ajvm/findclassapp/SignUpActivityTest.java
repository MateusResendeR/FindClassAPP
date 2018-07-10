package com.findclass.ajvm.findclassapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.findclass.ajvm.findclassapp.AccountActivities.SignInActivity;
import com.findclass.ajvm.findclassapp.AccountActivities.SignUpActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity>
            mActivityRule = new ActivityTestRule<>(SignUpActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState(){
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
    }

    @Test
    public void whenEmailIsEmpty_AndClickLoginButton_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        testEmptyFieldState(R.id.passwordEditText);
    }

    @Test
    public void whenPasswordIsEmpty_AndClickLoginButton_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        testEmptyFieldState(R.id.emailEditText);
    }

    @Test
    public void whenBothFieldAreFilled_butEmailIsNotValid_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        onView(withId(R.id.emailEditText)).perform(typeText("aaaa"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.continueButton)).perform(click());
        onView(withText(R.string.INVALID_EMAIL_TOAST)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void whenBothFieldAreFilled_butPasswordIsWeak_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        onView(withId(R.id.emailEditText)).perform(typeText("victor@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("12345"));
        closeSoftKeyboard();
        onView(withId(R.id.continueButton)).perform(click());
        onView(withText(R.string.WEAK_PASSWORD_TOAST)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

    private void testEmptyFieldState(int notEmptyField){
        onView(withId(notEmptyField)).perform(typeText("DefaultText"));
        closeSoftKeyboard();
        onView(withId(R.id.continueButton)).perform(click());
        onView(withText(R.string.TOAST_STRING)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

}
