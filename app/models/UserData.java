package models;

import java.util.Date;


/**
 * <p>The model is developed to store Author Data.</p>
 * <p>The model used getters methods to access Author data outside of class or in any other class.</p>
 *
 * @author Shubham Bhaderi
 */
public class UserData{
    String author;
    String author_fullname;
    long total_awards_received;
    long created_utc;
    String title;
    String subreddit;
    long score;
    double upvote_ratio;

    public UserData(String e, long f, String a,long b,String c,String d,long h,double i){
        author_fullname = e;
        total_awards_received = f;
        author = a;
        created_utc = b;
        title = c;
        subreddit = d;
        score = h;
        upvote_ratio = i;
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

    public long getScore() {
        return score;
    }

    public double getUpvoteRatio() {
        return upvote_ratio;
    }
}