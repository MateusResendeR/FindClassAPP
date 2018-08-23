package com.findclass.ajvm.findclassapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.findclass.ajvm.findclassapp.CalendarActivities.MyCalendarProfessorActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MyCalendarProfessorActivityTest {

    @Rule
    public ActivityTestRule<MyCalendarProfessorActivity>
            mActivityRule = new ActivityTestRule<>(MyCalendarProfessorActivity.class, false, true);

    @Test
    public void whenOpenMyCalendarActivity_andClickMeusHorarios_shoulOpenInfoScheduleTeacherActivity() throws InterruptedException {
        Thread.sleep(1500);
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.addButton), withText("Meus Horários"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        onView(withId(R.id.myTimesRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void whenOpenMyCalendarActivity_andClickAdicionarDatas_shoulOpenAddTimeActivity() throws InterruptedException {
        Thread.sleep(1500);
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.updateButton), withText("Adicionar Datas"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton3.perform(click());

        onView(withId(R.id.startTimeEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.endTimeEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.daySpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.priceEditText)).check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}
