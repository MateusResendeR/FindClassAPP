package com.findclass.ajvm.findclassapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.findclass.ajvm.findclassapp.menuActivities.MenuAlunoActivity;

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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MenuAlunoActivityTest {

    @Rule
    public ActivityTestRule<MenuAlunoActivity>
            mActivityRule = new ActivityTestRule<>(MenuAlunoActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState() throws InterruptedException {
        Thread.sleep(1500);
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.menuPesquisa)).check(matches(isDisplayed()));
    }

    /*@Test
    public void whenActivityIsLaunched_andClickAulasMarcadas_shouldShowClassesSchedule() throws InterruptedException {
        Thread.sleep(1500);
        ViewInteraction textView = onView(
                allOf(withText("Aulas Marcadas"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.viewpagertab),
                                        0),
                                0),
                        isDisplayed()));
        textView.perform(click());

        onView(withId(R.id.recyclerViewMySchedule)).check(matches(isDisplayed()));
    }*/

    @Test
    public void whenOpenMenuAluno_andClickMelhoresProfessores_shouldOpenProfessorRankingActivity() throws InterruptedException {
        Thread.sleep(1500);
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        onView(withId(R.id.recyclerViewProfessorRanking)).check(matches(isDisplayed()));

    }

    @Test
    public void whenOpenMenuAluno_andClickEditarCadastro_shouldOpenUpdateDataActivity() throws InterruptedException {
        Thread.sleep(1500);
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        9),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.surnameEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.phoneEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.cepEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.stateEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.cityEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.districtEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.addressEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.numberEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.complementEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.finishUpdateData)).check(matches(isDisplayed()));

    }

    @Test
    public void whenOpenMenu_andClickEnsinoMedioCategory_shouldOpenFundamentalSubjectActivity() throws InterruptedException {
        Thread.sleep(1500);
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        4),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        onView(withId(R.id.recyclerViewSubjectsOfLevel)).check(matches(isDisplayed()));

    }

    @Test
    public void whenOpenMenu_andClickEnsinoFundamentalCategory_shouldOpenFundamentalSubjectActivity() throws InterruptedException {
        Thread.sleep(1500);
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        5),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        onView(withId(R.id.recyclerViewSubjectsOfLevel)).check(matches(isDisplayed()));

    }

    @Test
    public void whenOpenMenu_andClickVariadosCategory_shouldOpenFundamentalSubjectActivity() throws InterruptedException {
        Thread.sleep(1500);
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        6),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        onView(withId(R.id.recyclerViewSubjectsOfLevel)).check(matches(isDisplayed()));

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
