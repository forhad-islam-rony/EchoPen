package com.example.echopen.Model;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BlogItemModelTest {

    private BlogItemModel blogItemModel;

    @Before
    public void setUp() {
        blogItemModel = new BlogItemModel(
                "Sample Heading",
                "Sample User",
                "2024-05-25",
                "This is a sample post content.",
                "user123",
                42,
                "https://example.com/profile.jpg"
        );
        blogItemModel.setSaved(true);
        blogItemModel.setPostId("post123");
        blogItemModel.setLikedBy(Arrays.asList("user1", "user2"));
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("Sample Heading", blogItemModel.getHeading());
        assertEquals("Sample User", blogItemModel.getUserName());
        assertEquals("2024-05-25", blogItemModel.getDate());
        assertEquals("user123", blogItemModel.getUserId());
        assertEquals("This is a sample post content.", blogItemModel.getPost());
        assertEquals(42, blogItemModel.getLikeCount());
        assertEquals("https://example.com/profile.jpg", blogItemModel.getProfileImage());
        assertTrue(blogItemModel.isSaved());
        assertEquals("post123", blogItemModel.getPostId());
        assertEquals(Arrays.asList("user1", "user2"), blogItemModel.getLikedBy());
    }

    @Test
    public void testModelPropertyUpdate() {
        blogItemModel.setHeading("Updated Heading");
        assertEquals("Updated Heading", blogItemModel.getHeading());

        blogItemModel.setUserName("Updated User");
        assertEquals("Updated User", blogItemModel.getUserName());

        blogItemModel.setDate("2024-06-01");
        assertEquals("2024-06-01", blogItemModel.getDate());

        blogItemModel.setUserId("user456");
        assertEquals("user456", blogItemModel.getUserId());

        blogItemModel.setPost("Updated post content.");
        assertEquals("Updated post content.", blogItemModel.getPost());

        blogItemModel.setLikeCount(100);
        assertEquals(100, blogItemModel.getLikeCount());

        blogItemModel.setProfileImage("https://example.com/updated_profile.jpg");
        assertEquals("https://example.com/updated_profile.jpg", blogItemModel.getProfileImage());

        blogItemModel.setSaved(false);
        assertFalse(blogItemModel.isSaved());

        blogItemModel.setPostId("post456");
        assertEquals("post456", blogItemModel.getPostId());

        blogItemModel.setLikedBy(Arrays.asList("user3", "user4"));
        assertEquals(Arrays.asList("user3", "user4"), blogItemModel.getLikedBy());
    }

    @Test
    public void testNullAndEmptyValues() {
        blogItemModel.setHeading(null);
        blogItemModel.setUserName("");
        blogItemModel.setDate(null);
        blogItemModel.setUserId("");
        blogItemModel.setPost(null);
        blogItemModel.setProfileImage("");
        blogItemModel.setPostId(null);
        blogItemModel.setLikedBy(null);

        assertNull(blogItemModel.getHeading());
        assertEquals("", blogItemModel.getUserName());
        assertNull(blogItemModel.getDate());
        assertEquals("", blogItemModel.getUserId());
        assertNull(blogItemModel.getPost());
        assertEquals("", blogItemModel.getProfileImage());
        assertNull(blogItemModel.getPostId());
        assertNull(blogItemModel.getLikedBy());
    }

    @Test
    public void testExtremeValues() {
        blogItemModel.setLikeCount(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, blogItemModel.getLikeCount());

        blogItemModel.setLikeCount(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, blogItemModel.getLikeCount());
    }

    @Test
    public void testBoundaryValues() {
        blogItemModel.setLikeCount(0);
        assertEquals(0, blogItemModel.getLikeCount());

        blogItemModel.setLikeCount(-1);
        assertEquals(-1, blogItemModel.getLikeCount());
    }
}