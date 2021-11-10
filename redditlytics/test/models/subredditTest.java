package models;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;
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