package com.example.echopen;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserProfileHelperTest {

    private UserProfileHelper userProfileHelper;

    @Before
    public void setUp() {
        userProfileHelper = new UserProfileHelper();
    }

    @Test
    public void testSetAndGetProfileImageUrl() {
        String expectedImageUrl = "app/src/main/res/drawable/heart.png";

        userProfileHelper.setProfileImageUrl(expectedImageUrl);
        String actualImageUrl = userProfileHelper.getProfileImageUrl();

        assertEquals(expectedImageUrl, actualImageUrl);
    }

    @Test
    public void testGetProfileImageUrlWhenNotSet() {
        String actualImageUrl = userProfileHelper.getProfileImageUrl();
        //No image set here

        assertNull(actualImageUrl);
    }
    @Test
    public void testUserName() {
        String expect="anything";
        userProfileHelper.set_name(expect);

        //No image set here
        assertEquals(userProfileHelper.getUsername(),expect);
    }

    public void testEmail() {
        String expect="anything@gmail.com";
        userProfileHelper.set_email("anything@gmail.com");


        //No image set here
        assertEquals(userProfileHelper.getUsername(),expect);
    }
}
