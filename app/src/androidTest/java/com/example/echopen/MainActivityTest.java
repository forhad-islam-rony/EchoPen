package com.example.echopen;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before

    public void setUp() {
    Intents.init();
}

@After
public void tearDown() {
    Intents.release();
}

@Test
public void testSaveArticleButtonClick() {
    onView(withId(R.id.saveArticalButton)).perform(click());
    intended(hasComponent(SavedArticlesActivity.class.getName()));
}

@Test
public void testProfileImageClick() {
    onView(withId(R.id.profile_image)).perform(click());
    intended(hasComponent(ProfileActivity.class.getName()));
}

@Test
public void testCardViewClick() {
    onView(withId(R.id.cardView2)).perform(click());
    intended(hasComponent(ProfileActivity.class.getName()));
}

@Test
public void testAddArticleButtonClick() {
    onView(withId(R.id.floatingAddArticleButton)).perform(click());
    intended(hasComponent(AddArticleActivity.class.getName()));
}
}