package com.example.echopen;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TitleHandlerTest {

    private TitleHandler titleHandler;

    @Before
    public void setUp() {
        titleHandler = new TitleHandler();
    }

    @Test
    public void testSetTitle() {
        String testTitle = "Test Title";
        titleHandler.setTitle(testTitle);
        assertEquals("Title should be set correctly", testTitle, titleHandler.getTitle());
    }

    @Test
    public void testGetTitle() {
        String testTitle = "Another Test Title";
        titleHandler.setTitle(testTitle);
        String returnedTitle = titleHandler.getTitle();
        assertEquals("Returned title should match set title", testTitle, returnedTitle);
    }
}
