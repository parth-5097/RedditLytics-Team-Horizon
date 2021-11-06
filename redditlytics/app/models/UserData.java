package models;

import java.util.Date;

public class UserData{
    String author;
    String author_fullname;
    long total_awards_received;
    long created_utc;
    String title;
    String subreddit;

    public UserData(String e, long f, String a,long b,String c,String d ){
        author_fullname = e;
        total_awards_received = f;
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

    public long getTotal_awards_received() {
        return total_awards_received;
    }

    public String getAuthorFullname() {
        return author_fullname;
    }
}