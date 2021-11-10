package controllers;

import play.mvc.*;
import models.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    public String data;
    KeyResults results = new KeyResults();
    Word word = new Word();
    UserProfile profile = new UserProfile();

    public HomeController() {
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result getSearchResult(String key) {
        data = results.getData(key);
        return ok(data);
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result getWordStats(String a) {
        if(data != null){
            return ok(views.html.word_stats.render(word.bodyData(data)));
        } else {
            data = results.getData(a);
            return ok(views.html.word_stats.render(word.bodyData(data)));
        }

    }

    //****************
    public Result getUserProfile(String username) {
        return ok(profile.getData(username));
    }

    public Result getSubreddit(String word){
        return ok(views.html.subreddit.render(results.getSubredditData(word)));
    }
}