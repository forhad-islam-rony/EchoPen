package com.example.echopen;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntegrationTest {

    private TitleHandler titleHandler;
    private UserProfileHelper userProfileHelper;

    @Before
    public void setUp() {
        titleHandler = new TitleHandler();
        userProfileHelper = new UserProfileHelper();
    }

    @Test
    public void testIntegrationTitleAndUserProfile() {
        String testTitle = "User Profile Page";
        String testUsername = "JohnDoe";
        String testEmail = "john.doe@example.com";

        // Setting values
        titleHandler.setTitle(testTitle);
        userProfileHelper.set_name(testUsername);
        userProfileHelper.set_email(testEmail);

        // Retrieving and asserting values
        assertEquals("Title should be correctly set", testTitle, titleHandler.getTitle());
        assertEquals("Username should be correctly set", testUsername, userProfileHelper.getUsername());
        assertEquals("Email should be correctly set", testEmail, userProfileHelper.getEmail());
    }

    @Test
    public void testTitleReflectsUserProfileChanges() {
        String initialTitle = "User Profile: ";
        String testUsername = "JaneDoe";

        // Setting initial title
        titleHandler.setTitle(initialTitle);

        // Simulating a change in user profile that should reflect in the title
        userProfileHelper.set_name(testUsername);
        titleHandler.setTitle(initialTitle + userProfileHelper.getUsername());

        assertEquals("Title should reflect the updated username", "User Profile: JaneDoe", titleHandler.getTitle());
    }
}
