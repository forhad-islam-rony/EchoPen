package com.example.echopen;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class BlogAdapterTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Initialize intents
        Intents.init();
    }

    @Test
    public void testReadMoreButtonNavigatesToReadMoreActivity() {

        Espresso.onView(ViewMatchers.withId(R.id.blogRecyclerView))
                .perform((ViewActions.click()));


        Espresso.onView(ViewMatchers.withId(R.id.main))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}