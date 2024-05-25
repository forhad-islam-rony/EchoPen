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
        String expectedImageUrl = "http://example.com/image.jpg";

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
}
