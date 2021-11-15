package models;

import java.util.Date;


/**
 * <p>the subreddit model is devloped to store subreddit data.</p>
 * <p> the model uses getters to achive encapsulations.</p>
 *
 * @author Parthiv Akbari
 */
public class subreddit{
    String author;
    long created_utc;
    String title;
    String subreddit;

    public subreddit(String a,long b,String c,String d ){
        author = a;
        created_utc = b;
        title = c;
        subreddit = d;
    }

    public Date getCreated_utc() {
        Date d = new Date(created_utc);
        return d;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
}