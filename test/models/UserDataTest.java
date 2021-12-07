package models;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Date;

public class UserDataTest {
    long total_awards_received = 5;
    long created_utc = 1636401986301L;
    long score = 10;
    double upvote_ratio = 20;
    private UserData test = new UserData("Shubham Bhanderi",total_awards_received,"Shubham",created_utc,"Playfamework project","Alexander",score,upvote_ratio);

    @Test
    public void getCreatedUTCTest() {
        assertEquals(new Date(created_utc),test.getCreated_utc());
    }

    @Test
    public void getSubredditTest() {
        assertEquals("Alexander",test.getSubreddit());
    }

    @Test
    public void getAuthorTest() {
        assertEquals("Shubham",test.getAuthor());
    }

    @Test
    public void getTitleTest() {
        assertEquals("Playfamework project",test.getTitle());
    }

    @Test
    public void getTotal_awards_receivedTest() {
        assertEquals(total_awards_received,test.getTotal_awards_received());
    }

    @Test
    public void getAuthorFullnameTest() {
        assertEquals("Shubham Bhanderi",test.getAuthorFullname());
    }

    @Test
    public void getScoreTest() {
        assertEquals(score,test.getScore());
    }

    @Test
    public void getUpvoteRatioTest() {
        assertEquals(20,(int) test.getUpvoteRatio());
    }
}