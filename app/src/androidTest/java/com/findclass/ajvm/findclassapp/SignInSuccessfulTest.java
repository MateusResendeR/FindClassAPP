package com.findclass.ajvm.findclassapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.findclass.ajvm.findclassapp.AccountActivities.SignInActivity;

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
public class SignInSuccessfulTest {

    @Rule
    public ActivityTestRule<SignInActivity>
            mActivityRule = new ActivityTestRule<>(SignInActivity.class, false, true);

    @Test
    public void withBothFieldsFilled_andItAuthenticate_shouldOpenUserActivity(){
        onView(withId(R.id.emailEditText)).perform(typeText("victor_s.s.m._15@hotmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.signinButton)).perform(click());
        onView(withText(R.string.TOAST_BEM_VINDO)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }
}
