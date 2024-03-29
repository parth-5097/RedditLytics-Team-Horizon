package models;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Date;

public class subredditTest {
    long created_utc = 1636401986301L;
    private subreddit test = new subreddit("Parthiv",created_utc,"This is the sbreddit testing title", "subreddit-test");

    @Test
    public void getCreated_utcTest() {
        assertEquals(new Date(created_utc),test.getCreated_utc());
    }

    @Test
    public void getSubredditTest() {
        assertEquals("subreddit-test",test.getSubreddit());
    }

    @Test
    public void getAuthorTest() {
        assertEquals("Parthiv",test.getAuthor());
    }

    @Test
    public void getTitleTest() {
        assertEquals("This is the sbreddit testing title",test.getTitle());
    }
}