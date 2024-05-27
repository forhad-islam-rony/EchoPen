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
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.example.echopen.register.WelcomeActivity;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Rule
    public ActivityScenarioRule<ProfileActivity> activityScenarioRule = new ActivityScenarioRule<>(ProfileActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testAddNewBlogButtonClick() {
        onView(withId(R.id.AddNewBlogButton)).perform(click());
        intended(hasComponent(SavedArticlesActivity.class.getName()));
    }

    @Test
    public void testArticleButtonClick() {
        onView(withId(R.id.articleButton)).perform(click());
        intended(hasComponent(ArticleActivity.class.getName()));
    }

    @Test
    public void testLogOutButtonClick() {
        onView(withId(R.id.LogOutButton)).perform(click());
        intended(hasComponent(WelcomeActivity.class.getName()));
    }
}