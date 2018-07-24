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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SignInActivityTest {

    @Rule
    public ActivityTestRule<SignInActivity>
        mActivityRule = new ActivityTestRule<>(SignInActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState() throws InterruptedException {
        Thread.sleep(1500);
        onView(withId(R.id.googleButton)).check(matches(isDisplayed()));
        onView(withId(R.id.facebookButton)).check(matches(isDisplayed()));
        onView(withId(R.id.orTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.signinButton)).check(matches(isDisplayed()));
        onView(withId(R.id.notUserYetTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.signUpButton)).check(matches(isDisplayed()));

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
    public void whenBothFieldsAreFilled_butDoesntAuthenticate_shouldDisplayToast() throws InterruptedException {
        Thread.sleep(1500);
        onView(withId(R.id.emailEditText)).perform(typeText("aaaa@aaa.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordEditText)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.signinButton)).perform(click());
        onView(withText(R.string.TOAST_STRING_EMAIL_NAO_CADASTRADO)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

    private void testEmptyFieldState(int notEmptyField){
        onView(withId(notEmptyField)).perform(typeText("DefaultText"));
        closeSoftKeyboard();
        onView(withId(R.id.signinButton)).perform(click());
        onView(withText(R.string.TOAST_STRING)).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));


    }
}
